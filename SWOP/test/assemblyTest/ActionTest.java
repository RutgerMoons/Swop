package assemblyTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;

public class ActionTest {

	private Action action;
	@Before
	public void initialize(){
		action = new Action();
	}
	@Test
	public void TestCompleted(){
		action.setCompleted(true);
		assertTrue(action.isCompleted());
		action.setCompleted(false);
		assertFalse(action.isCompleted());
	}

}