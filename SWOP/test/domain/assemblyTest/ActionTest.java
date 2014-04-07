package domain.assemblyTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.Action;


public class ActionTest {

	private Action action;
	@Before
	public void initialize(){
		action = new Action("Spray Colour");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidDescription(){
		new Action("");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidDescription2(){
		new Action(null);
	}
	
	@Test
	public void TestCompleted(){
		action.setCompleted(true);
		assertTrue(action.isCompleted());
		action.setCompleted(false);
		assertFalse(action.isCompleted());
	}

	@Test
	public void TestSetDescription(){
		action.setDescription("Mix Colours");
		assertEquals("Mix Colours", action.getDescription());
	}
	
	@Test
	public void TestToString(){
		assertEquals("Spray Colour", action.toString());
	}
}
