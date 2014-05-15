package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkbenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.order.StandardOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
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
		VehicleSpecification template = new VehicleSpecification("model", parts, new HashMap<WorkbenchType, Integer>());
		VehicleSpecification template2 = new VehicleSpecification("model B", parts, new HashMap<WorkbenchType, Integer>());
		VehicleSpecification template3 = new VehicleSpecification("model C", parts, new HashMap<WorkbenchType, Integer>());
		Set<VehicleSpecification> specifications = new HashSet<VehicleSpecification>();
		specifications.add(template);
		specifications.add(template2);
		specifications.add(template3);
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
		line = new AssemblyLine(new ClockObserver(), new ImmutableClock(0,240), AssemblyLineState.OPERATIONAL, specifications);

		Set<String> responsibilities = new HashSet<>();
		responsibilities.add("Body");
		responsibilities.add("Color");
		WorkBench body1 = new WorkBench(responsibilities, WorkbenchType.BODY);

		responsibilities = new HashSet<>();
		responsibilities.add("Engine");
		responsibilities.add("Gearbox");
		WorkBench drivetrain1 = new WorkBench(responsibilities, WorkbenchType.DRIVETRAIN);

		responsibilities = new HashSet<>();
		responsibilities.add("Seat");
		responsibilities.add("Airco");
		responsibilities.add("Spoiler");
		responsibilities.add("Wheel");
		WorkBench accessories1 = new WorkBench(responsibilities, WorkbenchType.ACCESSORIES);

		line.addWorkBench(body1);
		line.addWorkBench(drivetrain1);
		line.addWorkBench(accessories1);
		
		line.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getWorkbenches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new AssemblyLine(null, new ImmutableClock(0, 240), null, null);
	}

	@Test
	public void addWorkBenchTest(){
		Set<String> resp1 = new HashSet<String>();
		resp1.add("bla");
		resp1.add("bleu");
		IWorkBench workbench1 = new WorkBench(resp1, WorkbenchType.BODY);
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
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);

		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkbenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkbenches().get(0).chooseTasksOutOfJob();
		for(IWorkBench bench : line.getWorkbenches()){
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
		line.getWorkbenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkbenches().get(0).chooseTasksOutOfJob();
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
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkbenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkbenches().get(0).chooseTasksOutOfJob();
		assertEquals(1, line.getBlockingWorkBenches().size());
	}

	@Test
	public void advanceTest() throws UnmodifiableException, NoSuitableJobFoundException, NotImplementedException{
		AssemblyLineObserver observer = new AssemblyLineObserver();
		line.attachObserver(observer);
		ClockObserver clockObserver = new ClockObserver();
		Logger logger = new Logger(2);
		clockObserver.attachLogger(logger);
		observer.attachLogger(logger);
		ImmutableClock clock = new ImmutableClock(0,240);
		StandardOrder order = new StandardOrder("Luigi", this.model, 3, clock);
		StandardOrder order1 = new StandardOrder("Mario", this.model, 10, clock);
		
		for(int i=0; i<3; i++){
			IJob job = new Job(order);
			for (VehicleOption part : order.getVehicleOptions()) {
				ITask task = new Task(part.getTaskDescription());
				IAction action = new Action(part.getActionDescription());
				task.addAction(action);
				job.addTask(task);
			}
			
			line.getCurrentScheduler().addJobToAlgorithm(job);
		}
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
		Task task1 = new Task("Color");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		IOrder order = new StandardOrder("jos", model, 1, new ImmutableClock(0, 0));
		IJob job = new Job(order);
		job.setTasks(list);
		line.getWorkbenches().get(0).setCurrentJob(Optional.fromNullable(job));
		line.getWorkbenches().get(0).chooseTasksOutOfJob();
		line.advance();
	}
	@Test
	public void TestToString(){
		WorkBench bench1 = new WorkBench(new HashSet<String>(), WorkbenchType.ACCESSORIES);
		bench1.addResponsibility("Paint");
		line.addWorkBench(bench1);
		assertEquals("Responsibilities: model C, model B, model", line.toString());
	}
}
