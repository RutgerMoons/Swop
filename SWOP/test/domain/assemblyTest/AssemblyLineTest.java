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
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
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


public class AssemblyLineTest{

	private AssemblyLine line;
	private CarModel model;

	@Before
	public void initialize() {
		line = new AssemblyLine(new ClockObserver(), new UnmodifiableClock(0,240));

		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);

		try {
			model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
			model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
			model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
			model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
			model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
			model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
			model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
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
		new AssemblyLine(null, new UnmodifiableClock(0, 240));
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
		} catch (ImmutableException e) {}
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
		} catch (ImmutableException e) {		}
		assertFalse(line.canAdvance());
	}

	@Test
	public void testConvertStandardOrderToJob(){
		UnmodifiableClock clock = new UnmodifiableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 5, clock);
		try {
			int minutes = line.convertStandardOrderToJob(order);
			assertEquals(420,minutes);
		} catch (ImmutableException | NotImplementedException e) {}

	}

	@Test (expected = IllegalArgumentException.class)
	public void testConvertStandardOrderToJobError(){
		try {
			line.convertStandardOrderToJob(null);
		} catch (ImmutableException | NotImplementedException e) {}
	}

	@Test
	public void testConvertCustoOrderToJob(){
		UnmodifiableClock clock = new UnmodifiableClock(0,240);
		UnmodifiableClock deadline = new UnmodifiableClock(5, 800);
		CustomCarModel model = new CustomCarModel();
		CustomOrder order = new CustomOrder("Luigi in da house", model, 5, clock, deadline);
		try {
			int minutes = line.convertCustomOrderToJob(order);
			assertEquals(deadline.minus(clock),minutes);
		} catch (ImmutableException | NotImplementedException e) {}

	}

	@Test (expected = IllegalArgumentException.class)
	public void testConvertCustomOrderToJobError(){
		try {
			line.convertCustomOrderToJob(null);
		} catch (ImmutableException | NotImplementedException e) {}
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
		} catch (ImmutableException e) {		}
		assertTrue(line.getBlockingWorkBenches().contains(1));
		assertTrue(line.getBlockingWorkBenches().size()==1);
	}

	@Test
	public void advanceTest() throws ImmutableException, NoSuitableJobFoundException, NotImplementedException{
		AssemblyLineObserver observer = new AssemblyLineObserver();
		line.attachObserver(observer);
		ClockObserver clockObserver = new ClockObserver();
		Logger logger = new Logger(2, clockObserver, observer);
		UnmodifiableClock clock = new UnmodifiableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 10, clock);
		try {
			int minutes = line.convertStandardOrderToJob(order);
			order.setEstimatedTime(clock.getUnmodifiableClockPlusExtraMinutes(minutes));
		} catch (ImmutableException | NotImplementedException e) {}
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

		clockObserver.startNewDay(new UnmodifiableClock(2, 360));
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
		} catch (ImmutableException e) {}
		catch (NoSuitableJobFoundException | NotImplementedException e) {}
	}
	
	@Test
	public void switchToFifoTest(){
		line.switchToFifo();
	}
	
	@Test
	public void switchToBatchTest(){
		List<CarOption> list = new ArrayList<CarOption>();
		list.add(new CarOption("break", CarOptionCategory.BODY));
		line.switchToBatch(list);
	}

	@Test
	public void TestToString(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), "test");
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		assertEquals("-workbench 1: car body,-workbench 2: drivetrain,-workbench 3: accessories,-workbench 4: test", line.toString());
		StandardOrder order = new StandardOrder("Stef", model, 1, new UnmodifiableClock(0,240));
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
