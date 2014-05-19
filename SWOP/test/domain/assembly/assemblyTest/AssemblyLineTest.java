package domain.assembly.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.AssemblyLineStateObserver;
import domain.observer.observers.ClockObserver;
import domain.observer.observers.OrderBookObserver;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.scheduling.WorkloadDivider;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;


public class AssemblyLineTest{

	private AssemblyLine line;
	private Vehicle model;

	@Before
	public void initialize() {


		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		Map<WorkBenchType, Integer> map = new HashMap<WorkBenchType, Integer>();
		map.put(WorkBenchType.ACCESSORIES, 20);
		map.put(WorkBenchType.BODY, 20);
		map.put(WorkBenchType.DRIVETRAIN, 20);
		VehicleSpecification template = new VehicleSpecification("model", parts, map, new HashSet<VehicleOption>());
		VehicleSpecification template2 = new VehicleSpecification("model B", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		VehicleSpecification template3 = new VehicleSpecification("model C", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		Set<VehicleSpecification> specifications = new HashSet<VehicleSpecification>();
		specifications.add(template);
		specifications.add(template2);
		specifications.add(template3);
		model = new Vehicle(template);

		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0,240), AssemblyLineState.OPERATIONAL, specifications);

		WorkBench body1 = new WorkBench(WorkBenchType.BODY);

		WorkBench drivetrain1 = new WorkBench(WorkBenchType.DRIVETRAIN);

		WorkBench accessories1 = new WorkBench(WorkBenchType.ACCESSORIES);

		line.addWorkBench(body1);
		line.addWorkBench(drivetrain1);
		line.addWorkBench(accessories1);

		line.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getWorkBenches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new AssemblyLine(null, new ImmutableClock(0, 240), null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor2(){
		new AssemblyLine(new ClockObserver(), null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor3(){
		new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 240), null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor4(){
		new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 240), AssemblyLineState.BROKEN	, null);
	}

	@Test
	public void addWorkBenchTest(){
		IWorkBench workbench1 = new WorkBench(WorkBenchType.BODY);
		line.addWorkBench(workbench1);
		assertEquals(4, line.getWorkBenches().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void addWorkBenchTestInvalid(){
		line.addWorkBench(null);
	}

	@Test
	public void canAdvanceTestTrue(){
		Action action1 = new Action("paint");
		action1.setCompleted(true);
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);

		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkBenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkBenches().get(0).chooseTasksOutOfJob();
		for(IWorkBench bench : line.getWorkBenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}
		assertTrue(line.canAdvance());
	}

	@Test
	public void canAdvanceTestFalse(){
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkBenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkBenches().get(0).chooseTasksOutOfJob();
		assertFalse(line.canAdvance());
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
		AssemblyLineObserver obs = null;
		line.attachObserver(obs);
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
		AssemblyLineObserver obs = null;
		line.detachObserver(obs);
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
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkBenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkBenches().get(0).chooseTasksOutOfJob();
		assertEquals(1, line.getBlockingWorkBenches().size());
	}

	@Test
	public void advanceTest() {
		AssemblyLineObserver observer = new AssemblyLineObserver();
		line.attachObserver(observer);
		ClockObserver clockObserver = new ClockObserver();
		Logger logger = new Logger(2);
		clockObserver.attachLogger(logger);
		observer.attachLogger(logger);
		ImmutableClock clock = new ImmutableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 3, clock);

		for(int i=0; i<3; i++){
			IJob job = new Job(order);
			for (VehicleOption part : order.getVehicleOptions()) {
				ITask task = new Task(part.getTaskDescription());
				IAction action = new Action(part.getActionDescription());
				task.addAction(action);
				job.addTask(task);
			}
			line.schedule(job);
		}
		line.advance();
		for(IWorkBench bench : line.getWorkBenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}

		line.advance();

		for(IWorkBench bench : line.getWorkBenches()){
			for(ITask task : bench.getCurrentTasks()){
				for(IAction action: task.getActions()){
					action.setCompleted(true);
				}
			}
		}
		clockObserver.advanceTime(clock);
		line.advance();

		for(IWorkBench bench : line.getWorkBenches()){
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
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkBenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkBenches().get(0).chooseTasksOutOfJob();
		line.advance();
	}
	@Test
	public void TestToString(){
		WorkBench bench1 = new WorkBench(WorkBenchType.BODY);
		line.addWorkBench(bench1);
		assertTrue(line.toString().contains("Responsibilities"));
		assertTrue(line.toString().contains("model C"));
		assertTrue(line.toString().contains("model B"));
		assertTrue(line.toString().contains("model"));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testInvalidSchedule(){
		line.schedule(null);
	}

	@Test
	public void testGetMinimalIndex(){
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		line.schedule(job);
		assertEquals(0, job.getMinimalIndex());
		AssemblyLine assemblyLine = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.OPERATIONAL, line.getResponsibilities());
		assemblyLine.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		assemblyLine.schedule(job);
		assertEquals(-1, job.getMinimalIndex());
	}

	@Test
	public void testGetCurrentSchedulingAlgorithm(){
		assertEquals("Fifo", line.getCurrentSchedulingAlgorithm());
	}

	@Test
	public void testGetAllVehicleOptionsInPendingOrders(){
		assertEquals(0, line.getAllVehicleOptionsInPendingOrders().size());
	}

	@Test
	public void testGetAndSetState(){
		assertEquals(AssemblyLineState.OPERATIONAL, line.getState());
		line.setState(AssemblyLineState.BROKEN);
		assertEquals(AssemblyLineState.BROKEN, line.getState());

	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalState(){
		line.setState(null);
	}

	@Test
	public void testCompleteChosenTaskAtWorkbench(){
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		List<ITask> tasks = new ArrayList<>();
		ITask task = new Task(VehicleOptionCategory.COLOR.toString());
		tasks.add(task);
		IWorkBench bench = line.getWorkBenches().get(0);
		job.setTasks(tasks);

		bench.setCurrentJob(Optional.fromNullable(job));
		bench.chooseTasksOutOfJob();

		line.completeChosenTaskAtChosenWorkBench(bench, task, new ImmutableClock(0, 0));
		assertTrue(task.isCompleted());
	}

	@Test (expected = IllegalStateException.class)
	public void testCompleteChosenTaskAtIllegalWorkbench(){
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		List<ITask> tasks = new ArrayList<>();
		ITask task = new Task(VehicleOptionCategory.COLOR.toString());
		tasks.add(task);
		WorkBench bench = new WorkBench(WorkBenchType.BODY);
		job.setTasks(tasks);

		bench.setCurrentJob(Optional.fromNullable(job));
		bench.chooseTasksOutOfJob();

		line.completeChosenTaskAtChosenWorkBench(bench, task, new ImmutableClock(0, 0));
		assertTrue(task.isCompleted());
	}

	@Test
	public void testGetStandardJobs(){
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		List<ITask> tasks = new ArrayList<>();
		ITask task = new Task("Paint");
		tasks.add(task);
		WorkBench bench = new WorkBench(WorkBenchType.BODY);
		line.addWorkBench(bench);
		job.setTasks(tasks);


		line.schedule(job);
		assertTrue(line.getStandardJobs().contains(job));
	}

	@Test
	public void testRemoveUnscheduledJobs(){
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		List<ITask> tasks = new ArrayList<>();
		ITask task = new Task("Paint");
		tasks.add(task);
		job.setTasks(tasks);
		line.schedule(job);

		assertTrue(line.removeUnscheduledJobs().contains(job));
		assertFalse(line.getStandardJobs().contains(job));

	}


	@Test
	public void observerTest(){
		AssemblyLineStateObserver observer = new AssemblyLineStateObserver();
		WorkloadDivider divider = new WorkloadDivider(new ArrayList<AssemblyLine>(), new OrderBookObserver(), new AssemblyLineObserver());
		observer.attachLogger(divider);
		line.attachObserver(observer);

		line.setState(AssemblyLineState.BROKEN);
		line.detachObserver(observer);

	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalAttachObserver(){
		AssemblyLineStateObserver observer = null;
		line.attachObserver(observer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalDetachObserver(){
		AssemblyLineStateObserver observer = null;
		line.detachObserver(observer);
	}

	@Test
	public void testEqualsAndHashcode(){
		assertEquals(line, line);
		assertEquals(line.hashCode(), line.hashCode());
		assertNotEquals(line, null);
		assertNotEquals(line, AssemblyLineState.BROKEN);

		AssemblyLine line2 = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.BROKEN, new HashSet<VehicleSpecification>());
		line2.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		assertNotEquals(line, line2);
		assertNotEquals(line.hashCode(), line2.hashCode());

		line2.setState(AssemblyLineState.OPERATIONAL);
		assertNotEquals(line, line2);
		assertNotEquals(line.hashCode(), line2.hashCode());
		line2 = new AssemblyLine(new ClockObserver(), new ImmutableClock(0, 0), AssemblyLineState.OPERATIONAL, line.getResponsibilities());
		assertNotEquals(line, line2);
		assertNotEquals(line.hashCode(), line2.hashCode());

		for(IWorkBench bench: line.getWorkBenches()){
			line2.addWorkBench(bench);
		}

		assertEquals(line, line2);
		assertEquals(line.hashCode(), line2.hashCode());

		
	}
}