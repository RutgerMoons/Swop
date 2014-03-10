package assemblyTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import assembly.Action;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;

public class WorkBenchTest {

	private WorkBench workBench;
	private CarModel model;
	@Before
	public void initialize(){
		workBench = new WorkBench(new ArrayList<String>(), "name");
		model = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));
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
		assertNull(workBench.getCurrentJob());
		Job job = new Job(new Order("Jef", model, 1));
		workBench.setCurrentJob(job);
		assertNotNull(workBench.getCurrentJob());
		assertEquals(job, workBench.getCurrentJob());
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
		Job job = new Job(new Order("Jef", model, 1));
		Task task = new Task("Paint");
		task.addAction(new Action("Spray Colour"));
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
		Job job = new Job(new Order("Jef", model, 1));
		Task task1 = new Task("Paint");
		task1.addAction(new Action("Spray Colour"));
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
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
		Job job = new Job(new Order("Jef", model, 1));
		Task task1 = new Task("Paint");
		task1.addAction(new Action("Spray Colour"));
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		assertEquals(1, workBench.getCurrentTasks().size());
		assertEquals(task1, workBench.getCurrentTasks().get(0));
	}
	
	
	@Test
	public void TestNotCompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		Job job = new Job(new Order("Jef", model, 1));
		Task task1 = new Task("Paint");
		task1.addAction(new Action("Spray Colour"));
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		assertFalse(workBench.isCompleted());
	}
	
	@Test
	public void TestOneCompletedOneIncompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		Job job = new Job(new Order("Jef", model, 1));
		Task task1 = new Task("Paint");
		Action action1 = new Action("Spray Colour");
		task1.addAction(action1);
		Task task2 = new Task("Body");
		task2.addAction(new Action("Spray Colour"));
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		action1.setCompleted(true);
		assertFalse(workBench.isCompleted());
	}
	
	@Test
	public void TestTwoCompleted(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		Job job = new Job(new Order("Jef", model, 1));
		Task task1 = new Task("Paint");
		Action action1 = new Action("Spray Colour");
		task1.addAction(action1);
		Task task2 = new Task("Body");
		Action action2 = new Action("Spray Colour");
		task2.addAction(action2);
		
		job.addTask(task1);
		job.addTask(task2);
		workBench.setCurrentJob(job);
		workBench.chooseTasksOutOfJob();
		action1.setCompleted(true);
		action2.setCompleted(true);
		assertTrue(workBench.isCompleted());
	}
	
	@Test
	public void TestToString(){
		workBench.addResponsibility("Paint");
		workBench.addResponsibility("Body");
		assertEquals("Paint, Body, ", workBench.toString());
	}
}
