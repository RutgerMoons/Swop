package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import domain.assembly.Action;
import domain.assembly.ITask;
import domain.assembly.Job;
import domain.assembly.Task;
import domain.car.CarModel;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.exception.AlreadyInMapException;
import domain.order.Order;

public class JobTest {
	
	private CarModel model;
	@Before
	public void initializeModel() throws AlreadyInMapException{
		model = new CarModel("Volkswagen");
		model.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
	}

	@Test
	public void TestConstructor(){
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		assertNotNull(job.getTasks());
		assertEquals(order, job.getOrder());
	}

	@Test
	public void TestSetOrder(){
		Order order1 = new Order("Jef", model, 1);
		Job job = new Job(order1);
		assertEquals(order1, job.getOrder());
		Order order2 = new Order("Mark", model, 1);
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
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		job.setTasks(tasks);
		assertEquals(tasks, job.getTasks());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setInvalidTasks(){
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		job.setTasks(null);
		assertEquals(null, job.getTasks());
	}
	
	@Test
	public void TestAdd(){
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		Task task = new Task("Paint");
		assertEquals(0, job.getTasks().size());
		job.addTask(task);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestTwoAdd(){
		Order order = new Order("Jef", model, 1);
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
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		assertEquals(0, job.getTasks().size());
		job.addTask(null);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestCompletedOneTask(){
		Order order = new Order("Jef", model, 1);
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
		Order order = new Order("Jef", model, 1);
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
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		assertFalse(job.equals(null));
		assertFalse(job.equals(new Action("Test")));
		Job job2 = new Job(new Order("Jan", model, 1));
		assertFalse(job.equals(job2));
		job2 = new Job(order);
		
		List<ITask> tasks = new ArrayList<>();
		job.setTasks(tasks);
		job2.setTasks(tasks);
		assertTrue(job.equals(job2));
		assertEquals(job2.hashCode(), job.hashCode());
	}
}
