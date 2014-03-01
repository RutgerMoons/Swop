package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import Order.Order;
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
}
