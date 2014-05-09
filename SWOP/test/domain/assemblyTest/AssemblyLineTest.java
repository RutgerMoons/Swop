package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.assembly.WorkBench;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.Job;
import domain.job.Task;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.StandardOrder;
import domain.vehicle.CustomVehicle;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;


public class AssemblyLineTest{

	private AssemblyLine line;
	private Vehicle model;

	@Before
	public void initialize() {
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0,240));

		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);

		try {
			model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
			model.addCarPart(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
			model.addCarPart(new VehicleOption("red",  VehicleOptionCategory.COLOR));
			model.addCarPart(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
			model.addCarPart(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
			model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
			model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		} catch (AlreadyInMapException e) {}
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getWorkbenches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new AssemblyLine(null, new ImmutableClock(0, 240));
	}

	@Test
	public void addWorkBenchTest(){
		Set<String> resp1 = new HashSet<String>();
		resp1.add("bla");
		resp1.add("bleu");
		IWorkBench workbench1 = new WorkBench(resp1, "workbench1");
		line.addWorkBench(workbench1);
		assertEquals(4, line.getWorkbenches().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void addWorkBenchTestInvalid(){
		line.addWorkBench(null);
	}

	@Test
	public void canAdvanceTestTrue(){
		Action action1 = new Action("paint");
		action1.setCompleted(true);
		Task task1 = new Task("task pait");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			line.getWorkbenches().get(0).setCurrentTasks(list);
			for(IWorkBench bench : line.getWorkbenches()){
				for(ITask task : bench.getCurrentTasks()){
					for(IAction action: task.getActions()){
						action.setCompleted(true);
					}
				}
			}
			assertTrue(line.canAdvance());
		} catch (UnmodifiableException e) {}
	}

	@Test
	public void canAdvanceTestFalse(){
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("task pait");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			line.getWorkbenches().get(0).setCurrentTasks(list);
		} catch (UnmodifiableException e) {		}
		assertFalse(line.canAdvance());
	}

	@Test
	public void testConvertStandardOrderToJob(){
		ImmutableClock clock = new ImmutableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 5, clock);
		try {
			int minutes = line.convertStandardOrderToJob(order);
			assertEquals(420,minutes);
		} catch (UnmodifiableException e) {}

	}

	@Test (expected = IllegalArgumentException.class)
	public void testConvertStandardOrderToJobError(){
		try {
			line.convertStandardOrderToJob(null);
		} catch (UnmodifiableException e) {}
	}

	@Test
	public void testConvertCustoOrderToJob(){
		ImmutableClock clock = new ImmutableClock(0,240);
		ImmutableClock deadline = new ImmutableClock(5, 800);
		CustomVehicle model = new CustomVehicle();
		CustomOrder order = new CustomOrder("Luigi in da house", model, 5, clock, deadline);
		try {
			int minutes = line.convertCustomOrderToJob(order);
			assertEquals(deadline.minus(clock),minutes);
		} catch (UnmodifiableException e) {}

	}

	@Test (expected = IllegalArgumentException.class)
	public void testConvertCustomOrderToJobError(){
		try {
			line.convertCustomOrderToJob(null);
		} catch (UnmodifiableException e) {}
	}

	@Test
	public void TestAttachObserver() {
		AssemblyLineObserver obs = new AssemblyLineObserver();
		assertNotNull(obs);
		line.attachObserver(obs);
		assertNotNull(line);
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestAttachObserverNull() {
		line.attachObserver(null);
	}

	@Test
	public void TestDetachObserver() {
		AssemblyLineObserver obs = new AssemblyLineObserver();
		assertNotNull(obs);
		line.attachObserver(obs);
		assertNotNull(line);
		line.detachObserver(obs);
		assertNotNull(line);
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestDetachObserverNull() {
		line.detachObserver(null);
	}

	@Test
	public void TestDetachObserverNull2() {
		AssemblyLineObserver obs = new AssemblyLineObserver();
		assertNotNull(obs);
		assertNotNull(line);
		line.detachObserver(obs);
		assertNotNull(line);
	}

	@Test
	public void getBlockingWorkenchesTest(){
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("task pait");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			line.getWorkbenches().get(0).setCurrentTasks(list);
		} catch (UnmodifiableException e) {		}
		assertTrue(line.getBlockingWorkBenches().contains(1));
		assertTrue(line.getBlockingWorkBenches().size()==1);
	}

	@Test
	public void advanceTest() throws UnmodifiableException, NoSuitableJobFoundException, NotImplementedException{
		AssemblyLineObserver observer = new AssemblyLineObserver();
		line.attachObserver(observer);
		ClockObserver clockObserver = new ClockObserver();
		Logger logger = new Logger(2, clockObserver, observer);
		ImmutableClock clock = new ImmutableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 10, clock);
		try {
			int minutes = line.convertStandardOrderToJob(order);
			order.setEstimatedTime(clock.getImmutableClockPlusExtraMinutes(minutes));
		} catch (UnmodifiableException e) {}
		line.advance();

		for(IWorkBench bench : line.getWorkbenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}

		line.advance();

		for(IWorkBench bench : line.getWorkbenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}
		clockObserver.advanceTime(clock);
		line.advance();

		for(IWorkBench bench : line.getWorkbenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}

		line.advance();

		clockObserver.startNewDay(new ImmutableClock(2, 360));
		assertEquals(1, logger.averageDays());
	}


	@Test (expected = IllegalStateException.class)
	public void advanceTestFail(){
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("task paint");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			line.getWorkbenches().get(0).setCurrentTasks(list);
			line.advance();
		} catch (UnmodifiableException e) {}
		catch (NoSuitableJobFoundException e) {}
	}
	
	@Test
	public void switchToFifoTest() throws NotImplementedException{
		line.switchToFifo();
	}
	
	@Test
	public void switchToBatchTest() throws NotImplementedException{
		List<VehicleOption> list = new ArrayList<VehicleOption>();
		list.add(new VehicleOption("break", VehicleOptionCategory.BODY));
		line.switchToBatch(list);
	}

	@Test
	public void TestToString(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		assertEquals("-workbench 1: car body,-workbench 2: drivetrain,-workbench 3: accessories,-workbench 4: test", line.toString());
		StandardOrder order = new StandardOrder("Stef", model, 1, new ImmutableClock(0,240));
		IJob job = new Job(order);
		ITask task = new Task("Paint");
		IAction action = new Action("Paint car blue");
		((Task) task).addAction(action);
		((Job) job).addTask(task);
		bench1.setCurrentJob(Optional.fromNullable(job));
		bench1.chooseTasksOutOfJob();
		assertEquals("-workbench 1: car body,-workbench 2: drivetrain,-workbench 3: accessories,-workbench 4: test,  *Paint: not completed", line.toString());
		((Action) action).setCompleted(true);
		assertEquals("-workbench 1: car body,-workbench 2: drivetrain,-workbench 3: accessories,-workbench 4: test,  *Paint: completed", line.toString());
	}
}
