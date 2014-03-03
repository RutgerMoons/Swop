package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import order.Order;

import org.junit.Before;
import org.junit.Test;

import assembly.AssemblyLine;
import assembly.Job;
import assembly.WorkBench;
import clock.Clock;

public class AssemblyLineTest{

	private AssemblyLine line;
	@Before
	public void initialize(){
		line = new AssemblyLine(new Clock());
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
		assertEquals(2, line.getWorkbenches().size());
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
}
