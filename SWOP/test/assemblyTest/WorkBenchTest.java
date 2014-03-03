package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;

public class WorkBenchTest {

	private WorkBench workBench;
	@Before
	public void initialize(){
		workBench = new WorkBench(new ArrayList<String>());
	}
	
	@Test
	public void TestContstructor(){
		assertNotNull(workBench);
		assertNotNull(workBench.getResponsibilities());
	}

	@Test
	public void TestSetCurrentJob(){
		assertNull(workBench.getCurrentJob());
		Job job = new Job(new Order("Jef", "Car", 1));
		workBench.setCurrentJob(job);
		assertNotNull(workBench.getCurrentJob());
		assertEquals(job, workBench.getCurrentJob());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestSetInvalidCurrentJob(){
		assertNull(workBench.getCurrentJob());
		workBench.setCurrentJob(null);
		assertNotNull(workBench.getCurrentJob());
	}
	
	@Test
	public void TestSetType(){
		List<String> list = new ArrayList<>();
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
		assertEquals("Montage", workBench.getResponsibilities().get(0));
	}
	
	@Test
	public void TestAddOneResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility("Chassis");
		assertEquals(2, workBench.getResponsibilities().size());
		assertEquals("Montage", workBench.getResponsibilities().get(0));
		assertEquals("Chassis", workBench.getResponsibilities().get(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneInvalidResponsibility(){
		workBench.addResponsibility("");
		assertEquals(1, workBench.getResponsibilities().size());
		assertEquals("", workBench.getResponsibilities().get(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneNullResponsibility(){
		workBench.addResponsibility(null);
		assertEquals(1, workBench.getResponsibilities().size());
		assertEquals(null, workBench.getResponsibilities().get(0));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneValidOneInvalidResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility("");
		assertEquals(2, workBench.getResponsibilities().size());
		assertEquals("Montage", workBench.getResponsibilities().get(0));
		assertEquals("", workBench.getResponsibilities().get(1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestAddOneValidOneNullResponsibilities(){
		workBench.addResponsibility("Montage");
		workBench.addResponsibility(null);
		assertEquals(2, workBench.getResponsibilities().size());
		assertEquals("Montage", workBench.getResponsibilities().get(0));
		assertEquals(null, workBench.getResponsibilities().get(1));
	}
	
	@Test
	public void TestSetCurrentTasks(){
		List<Task> tasks = new ArrayList<Task>();
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
		Job job = new Job(new Order("Jef", "Car", 1));
		Task task = new Task("Paint");
		task.addAction(new Action());
		job.addTask(task);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();;
		assertEquals(1, workBench.getCurrentTasks().size());
		assertEquals(task, workBench.getCurrentTasks().get(0));
	}
	
	@Test
	public void TestChooseNextTasksTwoCompatibleTasks(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		Job job = new Job(new Order("Jef", "Car", 1));
		Task task1 = new Task("Paint");
		task1.addAction(new Action());
		Task task2 = new Task("Body");
		task2.addAction(new Action());
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		assertEquals(2, workBench.getCurrentTasks().size());
		assertEquals(task1, workBench.getCurrentTasks().get(0));
		assertEquals(task2, workBench.getCurrentTasks().get(1));
	}
	
	@Test
	public void TestChooseNextTasksOneCompatibleOneInCompatibleTask(){
		workBench.addResponsibility("Paint");
		Job job = new Job(new Order("Jef", "Car", 1));
		Task task1 = new Task("Paint");
		task1.addAction(new Action());
		Task task2 = new Task("Body");
		task2.addAction(new Action());
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		assertEquals(1, workBench.getCurrentTasks().size());
		assertEquals(task1, workBench.getCurrentTasks().get(0));
	}
}
