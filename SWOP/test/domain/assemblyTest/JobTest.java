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

import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.job.Action;
import domain.job.ITask;
import domain.job.Job;
import domain.job.Task;
import domain.order.StandardOrder;

public class JobTest {
	
	private CarModel model;
	@Before
	public void initializeModel() throws AlreadyInMapException{
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);
		model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
		model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
		model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
		model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
		model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
		model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
		model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
	}

	@Test
	public void TestConstructor(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		assertNotNull(job.getTasks());
		assertEquals(order, job.getOrder());
	}

	@Test
	public void TestSetOrder(){
		StandardOrder order1 = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order1);
		assertEquals(order1, job.getOrder());
		StandardOrder order2 = new StandardOrder("Mark", model, 1, new UnmodifiableClock(0,240));
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
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		job.setTasks(tasks);
		assertEquals(tasks, job.getTasks());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setInvalidTasks(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		job.setTasks(null);
		assertEquals(null, job.getTasks());
	}
	
	@Test
	public void TestAdd(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		Task task = new Task("Paint");
		assertEquals(0, job.getTasks().size());
		job.addTask(task);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestTwoAdd(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
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
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		assertEquals(0, job.getTasks().size());
		job.addTask(null);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestCompletedOneTask(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
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
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
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
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		assertFalse(job.equals(null));
		assertFalse(job.equals(new Action("Test")));
		Job job2 = new Job(new StandardOrder("Jan", model, 1, new UnmodifiableClock(0,240)));
		assertFalse(job.equals(job2));
		job2 = new Job(order);
		
		List<ITask> tasks = new ArrayList<>();
		job.setTasks(tasks);
		job2.setTasks(tasks);
		assertTrue(job.equals(job2));
		assertEquals(job2.hashCode(), job.hashCode());
		
		assertEquals(job, job);
	}
	
	@Test
	public void testIndex(){
		StandardOrder order = new StandardOrder("Jef", model, 1, new UnmodifiableClock(0,240));
		Job job = new Job(order);
		job.setMinimalIndex(5);
		assertEquals(5, job.getMinimalIndex());
	}
}
