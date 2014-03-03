package assemblyTest;

import static org.junit.Assert.*;

import order.Order;

import org.junit.Test;

import assembly.Action;
import assembly.Job;
import assembly.Task;

public class JobTest {

	@Test
	public void TestConstructor(){
		Order order = new Order("Jef", "Car", 1);
		Job job = new Job(order);
		assertNotNull(job.getTasks());
		assertEquals(order, job.getOrder());
	}

	@Test
	public void TestSetOrder(){
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		assertEquals(order1, job.getOrder());
		Order order2 = new Order("Mark", "Car", 1);
		job.setOrder(order2);
		assertEquals(order2, job.getOrder());
	}
	
	@Test
	public void TestAdd(){
		Order order = new Order("Jef", "Car", 1);
		Job job = new Job(order);
		Task task = new Task();
		assertEquals(0, job.getTasks().size());
		job.addTask(task);
		assertEquals(1, job.getTasks().size());
	}
	
	@Test
	public void TestCompletedOneTask(){
		Order order = new Order("Jef", "Car", 1);
		Job job = new Job(order);
		Task task = new Task();
		assertTrue(job.isCompleted());
		Action action = new Action();
		task.addAction(action);
		job.addTask(task);
		assertFalse(job.isCompleted());
		action.setCompleted(true);
		assertTrue(job.isCompleted());
	}
	
	@Test
	public void TestCompletedTwoTasks(){
		Order order = new Order("Jef", "Car", 1);
		Job job = new Job(order);
		Task task1 = new Task();
		Task task2 = new Task();
		assertTrue(job.isCompleted());
		
		Action action1 = new Action();
		task1.addAction(action1);
		job.addTask(task1);
		assertFalse(job.isCompleted());
		action1.setCompleted(true);
		
		Action action2 = new Action();
		task2.addAction(action2);
		job.addTask(task2);
		
		assertFalse(job.isCompleted());
		action2.setCompleted(true);
		assertTrue(job.isCompleted());
	}
}
