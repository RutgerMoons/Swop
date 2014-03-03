package usersTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import assembly.WorkBench;
import users.Worker;

public class WorkerTest {

	private Worker worker;
	@Before
	public void initialize(){
		worker = new Worker("Jos");
	}
	
	@Test
	public void TestConstructor(){
		assertEquals("Jos", worker.getName());
	}
	
	@Test
	public void TestWorkbench() {
		assertNull(worker.getCurrentWorkBench());
		WorkBench workBench = new WorkBench(new ArrayList<String>());
		worker.setCurrentWorkBench(workBench);
		assertEquals(workBench, worker.getCurrentWorkBench());
	}

}
