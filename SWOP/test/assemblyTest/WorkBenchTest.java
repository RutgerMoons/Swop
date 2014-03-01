package assemblyTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	}

}
