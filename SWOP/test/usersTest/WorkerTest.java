package usersTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ui.AssembleFlowController;
import ui.OrderFlowController;
import ui.UseCaseFlowController;
import users.Manager;
import users.Worker;

public class WorkerTest {

	private Worker worker;
	@Before
	public void initialize(){
		worker = new Worker("Jos", new ArrayList<String>(Arrays.asList(new String[] {"Complete assembly tasks"})));
	}
	
	@Test
	public void TestConstructor(){
		assertEquals("Jos", worker.getName());
		assertEquals("Complete assembly tasks", worker.getAccessRights().get(0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName1(){
		new Worker("", new ArrayList<String>(Arrays.asList(new String[] {"Complete assembly tasks"})));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void TestSetIllegalName2(){
		new Worker(null, new ArrayList<String>(Arrays.asList(new String[] {"Complete assembly tasks"})));
	}
}
