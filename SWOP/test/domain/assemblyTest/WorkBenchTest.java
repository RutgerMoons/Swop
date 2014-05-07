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

import domain.assembly.WorkBench;
import domain.car.Vehicle;
import domain.car.VehicleSpecification;
import domain.car.VehicleOption;
import domain.car.VehicleOptionCategory;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.job.Action;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.Job;
import domain.job.Task;
import domain.order.StandardOrder;


public class WorkBenchTest {

	private WorkBench workBench;
	private Vehicle model;
	private ImmutableClock clock;
	@Before
	public void initialize() throws AlreadyInMapException{
		workBench = new WorkBench(new HashSet<String>(), "name");
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		VehicleSpecification template = new VehicleSpecification("model", parts, 60);
		model = new Vehicle(template);
		model.addCarPart(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addCarPart(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addCarPart(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addCarPart(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addCarPart(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addCarPart(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addCarPart(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
		clock = new ImmutableClock(0,240);
	}
	
	@Test
	public void TestContstructor(){
		assertNotNull(workBench);
		assertNotNull(workBench.getResponsibilities());
		assertEquals("name", workBench.getWorkbenchName());
	}

	@Test
	public void TestSetCurrentJob(){
		workBench.isCompleted();
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		workBench.setCurrentJob(Optional.fromNullable(job));
		assertNotNull(workBench.getCurrentJob());
		assertEquals(workBench.getCurrentJob().get(), job);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetCurrentJobInvalid(){
		workBench.setCurrentJob(null);
	}
	
	@Test
	public void TestSetType(){
		Set<String> list = new HashSet<>();
		workBench.setResponsibilities(list);
		assertEquals(list, workBench.getResponsibilities());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestSetInvalidType(){
		workBench.setResponsibilities(null);
		assertEquals(null, workBench.getResponsibilities());
	}
	
	@Test
	public void TestAddOneResponsibility(){
		workBench.addResponsibility("Montage");
		assertEquals(1, workBench.getResponsibilities().size());
		assertTrue(workBench.getResponsibilities().contains("Montage"));
	}
	
	@Test
	public void TestAddOneResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility("Chassis");
		assertEquals(2, workBench.getResponsibilities().size());
		assertTrue(workBench.getResponsibilities().contains("Montage"));
		assertTrue(workBench.getResponsibilities().contains("Chassis"));
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneInvalidResponsibility(){
		workBench.addResponsibility("");
		assertEquals(1, workBench.getResponsibilities().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneNullResponsibility(){
		workBench.addResponsibility(null);
		assertEquals(1, workBench.getResponsibilities().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneValidOneInvalidResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility("");
		assertEquals(2, workBench.getResponsibilities().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneValidOneNullResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility(null);
		assertEquals(2, workBench.getResponsibilities().size());
	}
	
	@Test
	public void TestSetCurrentTasks(){
		List<ITask> tasks = new ArrayList<ITask>();
		workBench.setCurrentTasks(tasks);
		assertEquals(tasks, workBench.getCurrentTasks());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestSetInvalidCurrentTasks(){
		workBench.setCurrentTasks(null);
		assertEquals(null, workBench.getCurrentTasks());
	}

	@Test
	public void TestChooseNextTasks(){
		workBench.addResponsibility("Paint");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		Task task = new Task("Paint");
		task.addAction(new Action("Spray Colour"));
		((Job) job).addTask(task);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();;
		assertEquals(1, workBench.getCurrentTasks().size());
		assertEquals(task, workBench.getCurrentTasks().get(0));
	}
	
	@Test
	public void TestChooseNextTasksTwoCompatibleTasks(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		ITask task1 = new Task("Paint");
		
		((Task) task1).addAction(new Action("Spray Colour"));
		ITask task2 = new Task("Body");
		((Task) task2).addAction(new Action("Spray Colour"));
		
		((Job) job).addTask(task1);
		((Job) job).addTask(task2);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();
		assertEquals(2, workBench.getCurrentTasks().size());
		assertEquals(task1, workBench.getCurrentTasks().get(0));
		assertEquals(task2, workBench.getCurrentTasks().get(1));
	}
	
	@Test
	public void TestChooseNextTasksOneCompatibleOneInCompatibleTask(){
		workBench.addResponsibility("Paint");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		Task task1 = new Task("Paint");
		task1.addAction(new Action("Spray Colour"));
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		((Job) job).addTask(task1);
		((Job) job).addTask(task2);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();
		assertEquals(1, workBench.getCurrentTasks().size());
		assertEquals(task1, workBench.getCurrentTasks().get(0));
	}
	
	@Test
	public void TestChooseTasksInvalid(){
		workBench.chooseTasksOutOfJob();
		assertEquals(0,workBench.getCurrentTasks().size());
	}
	@Test
	public void TestNotCompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		Task task1 = new Task("Paint");
		task1.addAction(new Action("Spray Colour"));
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		((Job) job).addTask(task1);
		((Job) job).addTask(task2);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();
		assertFalse(workBench.isCompleted());
	}
	
	@Test
	public void TestOneCompletedOneIncompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		Task task1 = new Task("Paint");
		Action action1 = new Action("Spray Colour");
		task1.addAction(action1);
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		((Job) job).addTask(task1);
		((Job) job).addTask(task2);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();
		action1.setCompleted(true);
		assertFalse(workBench.isCompleted());
	}
	
	@Test
	public void TestTwoCompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		IJob job = new Job(new StandardOrder("Jef", model, 1, clock));
		Task task1 = new Task("Paint");
		Action action1 = new Action("Spray Colour");
		task1.addAction(action1);
		Task task2 = new Task("Body");
		Action action2 = new Action("Spray Colour");
		task2.addAction(action2);
		
		((Job) job).addTask(task1);
		((Job) job).addTask(task2);
		workBench.setCurrentJob(Optional.fromNullable(job));
		workBench.chooseTasksOutOfJob();
		action1.setCompleted(true);
		action2.setCompleted(true);
		assertTrue(workBench.isCompleted());
	}
	
	@Test
	public void TestToString(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		assertEquals("name", workBench.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new WorkBench(new HashSet<String>(), null);
	}
}
