package domain.job.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.job.action.Action;


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
	
	@Test
	public void testEquals(){
		assertEquals(action, action);
		assertEquals(action.hashCode(), action.hashCode());
		
		assertNotEquals(action, null);
		assertNotEquals(action, AssemblyLineState.BROKEN);
		
		Action action2 = new Action("Put on body");
		assertNotEquals(action, action2);
		assertNotEquals(action.hashCode(), action2.hashCode());
		
		action2.setDescription("Spray Colour");
		action2.setCompleted(true);
		
		assertNotEquals(action, action2);
		assertNotEquals(action.hashCode(), action2.hashCode());
		
		action.setCompleted(true);
		
		assertEquals(action, action2);
		assertEquals(action.hashCode(), action2.hashCode());
	}
}
