package assemblyTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assembly.Job;
import assembly.WorkBench;

public class WorkBenchTest {

	private WorkBench workBench;
	@Before
	public void initialize(){
		workBench = new WorkBench();
	}
	
	@Test
	public void TestContstructor(){
		assertNotNull(workBench);
		assertNotNull(workBench.getType());
	}

	@Test
	public void TestCurrentJob(){
		assertNull(workBench.getCurrentJob());
		Job job = new Job(null);
		workBench.setCurrentJob(job);
		assertNotNull(workBench.getCurrentJob());
		assertEquals(job, workBench.getCurrentJob());
	}
	
	@Test
	public void TestType(){
		workBench.setType(...);
		assertEquals(..., workBench.getType());
	}
}
