package domain.assemblyTest;

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

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.job.action.Action;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

public class JobTest {
	
	private Vehicle model;
	@Before
	public void initializeModel() throws AlreadyInMapException{
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		Map<WorkBenchType, Integer> time = new HashMap<WorkBenchType, Integer>();
		time.put(WorkBenchType.ACCESSORIES, 10);
		VehicleSpecification template = new VehicleSpecification("model", parts, time);
		model = new Vehicle(template);
		model.addVehicleOption(new VehicleOption("manual", VehicleOptionCategory.AIRCO));
		model.addVehicleOption(new VehicleOption("sedan",  VehicleOptionCategory.BODY));
		model.addVehicleOption(new VehicleOption("red",  VehicleOptionCategory.COLOR));
		model.addVehicleOption(new VehicleOption("standard 2l 4 cilinders",  VehicleOptionCategory.ENGINE));
		model.addVehicleOption(new VehicleOption("6 speed manual",  VehicleOptionCategory.GEARBOX));
		model.addVehicleOption(new VehicleOption("leather black", VehicleOptionCategory.SEATS));
		model.addVehicleOption(new VehicleOption("comfort", VehicleOptionCategory.WHEEL));
	}

	@Test
	public void TestConstructor(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertNotNull(job.getTasks());
		assertEquals(order, job.getOrder());
	}

	@Test
	public void TestSetOrder(){
		StandardOrder order1 = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order1);
		assertEquals(order1, job.getOrder());
		StandardOrder order2 = new StandardOrder("Mark", model, 1, new ImmutableClock(0,240));
		job.setOrder(order2);
		assertEquals(order2, job.getOrder());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestSetInvalidOrder(){
		Job job = new Job(null);
		assertEquals(null, job.getOrder());
	}
	
	@Test
	public void setTasks(){
		List<ITask> tasks = new ArrayList<>();
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		job.setTasks(tasks);
		assertEquals(tasks, job.getTasks());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setInvalidTasks(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		job.setTasks(null);
		assertEquals(null, job.getTasks());
	}
	
	@Test
	public void TestAdd(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		Task task = new Task("Paint");
		assertEquals(0, job.getTasks().size());
		job.addTask(task);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestTwoAdd(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		Task task = new Task("Paint");
		assertEquals(0, job.getTasks().size());
		job.addTask(task);
		assertEquals(1, job.getTasks().size());
		Task task2 = new Task("Paint");
		job.addTask(task2);
		assertEquals(2, job.getTasks().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidAdd(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertEquals(0, job.getTasks().size());
		job.addTask(null);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestCompletedOneTask(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		Task task = new Task("Paint");
		assertTrue(job.isCompleted());
		Action action = new Action("Spray Colour");
		task.addAction(action);
		job.addTask(task);
		assertFalse(job.isCompleted());
		action.setCompleted(true);
		assertTrue(job.isCompleted());
	}
	
	@Test
	public void TestCompletedTwoTasks(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		Task task1 = new Task("Paint");
		Task task2 = new Task("Paint");
		assertTrue(job.isCompleted());
		
		Action action1 = new Action("Spray Colour");
		task1.addAction(action1);
		job.addTask(task1);
		assertFalse(job.isCompleted());
		action1.setCompleted(true);
		
		Action action2 = new Action("Spray Colour");
		task2.addAction(action2);
		job.addTask(task2);
		
		assertFalse(job.isCompleted());
		action2.setCompleted(true);
		assertTrue(job.isCompleted());
	}
	
	@Test
	public void TestEqualsAndHashCode(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertFalse(job.equals(null));
		assertFalse(job.equals(new Action("Test")));
		Job job2 = new Job(new StandardOrder("Jan", model, 1, new ImmutableClock(0,240)));
		assertFalse(job.equals(job2));
		job2 = new Job(order);
		
		List<ITask> tasks = new ArrayList<>();
		tasks.add(new Task("blabla"));
		job2.setTasks(tasks);
		assertFalse(job.equals(job2));
		assertNotEquals(job2.hashCode(), job.hashCode());
		
		job.setTasks(tasks);
		assertTrue(job.equals(job2));
		assertEquals(job2.hashCode(), job.hashCode());
		
		assertEquals(job, job);
	}
	
	@Test
	public void testIndex(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		job.setMinimalIndex(5);
		assertEquals(5, job.getMinimalIndex());
	}
	
	@Test
	public void testGetVehicleOptions(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertEquals(order.getDescription().getVehicleOptions().size(), job.getVehicleOptions().size());
	}
	
	@Test
	public void testGetVehicleSpecification(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertEquals(order.getDescription().getVehicleSpecification(), job.getVehicleSpecification());
	}
	
	@Test
	public void testGetProductionTime(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		Integer expected = order.getDescription().getTimeAtWorkBench().get(WorkBenchType.ACCESSORIES);
		Integer actual = job.getProductionTime(WorkBenchType.ACCESSORIES);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetTotalProductionTime(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		assertEquals(order.getProductionTime(), job.getTotalProductionTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAcceptVisit(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new ImmutableClock(0,240));
		Job job = new Job(order);
		job.acceptVisit(null);
	}
}
