package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import assembly.AssemblyLine;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;
import clock.Clock;

public class AssemblyLineTest{

	private AssemblyLine line;
	@Before
	public void initialize(){
		line = new AssemblyLine(new Clock());
		line.setWorkbenches(new ArrayList<WorkBench>()); //DIT MOET GEBEUREN OMDAT ER ANDERS AL 3 WORKBENCHES AANWEZIG ZIJN!!
	}
	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getClock());
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getOvertime());
		assertNotNull(line.getWorkbenches());
	}

	@Test
	public void TestSetValidCurrentJobs(){
		List<Job> jobs = new ArrayList<>();
		Order order1 = new Order("Jef", "Car", 1);
		jobs.add(new Job(order1));
		jobs.add(new Job(order1));
		line.setCurrentJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(jobs, line.getCurrentJobs());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void TestSetInvalidCurrentJobs(){
		List<Job> jobs = null;
		line.setCurrentJobs(jobs);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(jobs, line.getCurrentJobs());
	}
	
	@Test
	public void TestSetValidOvertime(){
		line.setOvertime(1);
		assertEquals(1, line.getOvertime());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void TestSetInvaldOvertime(){
		line.setOvertime(-1);
		assertEquals(-1, line.getOvertime());
	}
	
	@Test
	public void TestSetValidWorkBenches(){
		List<WorkBench> workbenches = new ArrayList<WorkBench>();
		workbenches.add(new WorkBench(new ArrayList<String>()));
		workbenches.add(new WorkBench(new ArrayList<String>()));
		line.setWorkbenches(workbenches);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetInvalidWorkBenches(){
		List<WorkBench> workbenches = null;
		line.setWorkbenches(workbenches);
		assertEquals(0, line.getWorkbenches().size());
		assertEquals(workbenches, line.getWorkbenches());
	}
	
	@Test
	public void TestAddOneValidJob(){
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		line.addJob(job);
		assertEquals(1, line.getCurrentJobs().size());
		assertEquals(job, line.getCurrentJobs().get(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneInvalidJob(){
		line.addJob(null);
		assertEquals(1, line.getCurrentJobs().size());
	}
	
	@Test
	public void TestAddTwoValidJobs(){
		Order order1 = new Order("Jef", "Car", 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		assertEquals(2, line.getCurrentJobs().size());
		assertEquals(job1, line.getCurrentJobs().get(0));
		assertEquals(job2, line.getCurrentJobs().get(1));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidJobOneInvalidJob(){
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		line.addJob(job);
		assertEquals(1, line.getCurrentJobs().size());
		assertEquals(job, line.getCurrentJobs().get(0));
		line.addJob(null);
		assertEquals(2, line.getCurrentJobs().size());
	}
	
	@Test
	public void TestAddOneValidWorkBench(){
		WorkBench workBench = new WorkBench(new ArrayList<String>());
		line.addWorkBench(workBench);
		assertEquals(1, line.getWorkbenches().size());
		assertEquals(workBench, line.getWorkbenches().get(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneInvalidWorkBench(){
		line.addWorkBench(null);
		assertEquals(1, line.getCurrentJobs().size());
	}
	
	@Test
	public void TestAddTwoValidWorkBenchs(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		assertEquals(2, line.getWorkbenches().size());
		assertEquals(bench1, line.getWorkbenches().get(0));
		assertEquals(bench2, line.getWorkbenches().get(1));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestAddOneValidWorkBenchOneInvalidWorkBench(){
		WorkBench bench = new WorkBench(new ArrayList<String>());
		line.addWorkBench(bench);
		assertEquals(1, line.getWorkbenches().size());
		assertEquals(bench, line.getWorkbenches().get(0));
		line.addWorkBench(null);
		assertEquals(2, line.getCurrentJobs().size());
	}
	
	
	
	
	@Test
	public void TestAdvanceOneWorkbench(){
		WorkBench bench = new WorkBench(new ArrayList<String>());
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench);
		line.advance();
		assertEquals(job, bench.getCurrentJob());
	}
	
	@Test
	public void TestAdvanceTwoWorkbenches(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		line.advance();
		assertEquals(job, bench2.getCurrentJob());
	}
	
	
	@Test
	public void TestAdvanceTwoWorkbenchesTwoJobs(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		Order order1 = new Order("Jef", "Car", 1);
		Job job1 = new Job(order1);
		Job job2 = new Job(order1);
		line.addJob(job1);
		line.addJob(job2);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job1, bench1.getCurrentJob());
		line.advance();
		assertEquals(job2, bench1.getCurrentJob());
		assertEquals(job1, bench2.getCurrentJob());
	}
	
	
	
	@Test
	public void TestAdvanceTwoWorkbenchesIncompleteJob(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		bench1.addResponsibility("Body");
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		Task task = new Task("Body");
		task.addAction(new Action("Spray Colour"));
		job.addTask(task);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		assertEquals(null, bench2.getCurrentJob());
	}
	
	/**
	 * Check if the job is completed it will be removed from currentjobs. 
	 */
	@Test
	public void TestAdvanceTwoWorkbenchesCompleteFullJob(){
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		bench2.addResponsibility("Body");
		Order order1 = new Order("Jef", "Car", 1);
		Job job = new Job(order1);
		Task task = new Task("Body");
		Action action = new Action("Spray Colour");
		task.addAction(action);
		job.addTask(task);
		line.addJob(job);
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.advance();
		assertEquals(job, bench1.getCurrentJob());
		line.advance();
		assertEquals(job, bench2.getCurrentJob());
		action.setCompleted(true);
		line.advance();
		assertEquals(0, line.getCurrentJobs().size());
	}
	
	
	@Test
	public void TestConvertOrderToJobOneCar(){
		Order order = new Order("Stef", "auto", 1);
		List<Job> jobs = line.convertOrderToJob(order);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestConvertIllegalOrderToJobOneCar(){
		List<Job> jobs = line.convertOrderToJob(null);
		assertEquals(1, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}
	
	@Test
	public void TestConvertOrderToJobTwoCars(){
		Order order = new Order("Stef", "auto", 2);
		List<Job> jobs = line.convertOrderToJob(order);
		assertEquals(2, jobs.size());
		assertEquals(7, jobs.get(0).getTasks().size());
	}
	
	@Test
	public void TestEstimatedTime1(){
		Order order = new Order("Stef", "auto", 2);
		line.getCurrentJobs().addAll(line.convertOrderToJob(order));
		WorkBench bench1 = new WorkBench(new ArrayList<String>());
		WorkBench bench2 = new WorkBench(new ArrayList<String>());
		line.addWorkBench(bench1);
		line.addWorkBench(bench2);
		line.calculateEstimatedTime(order);
		assertEquals(0, order.getEstimatedTime()[0]);
		assertEquals(240, order.getEstimatedTime()[1]);
	}
}
