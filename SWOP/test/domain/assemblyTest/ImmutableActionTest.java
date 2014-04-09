package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.exception.ImmutableException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.ImmutableAction;


public class ImmutableActionTest {


	@Test
	public void test() {
		Action action = new Action("Test");
		IAction immutable = new ImmutableAction(action);
		
		assertEquals("Test", immutable.getDescription());
		assertFalse(immutable.isCompleted());
		action.setCompleted(true);
		assertTrue(immutable.isCompleted());
		assertEquals(action.toString(), immutable.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableAction(null);
	}
	
	@Test (expected= ImmutableException.class)
	public void testImmutableException() throws ImmutableException{
		Action action = new Action("Test");
		IAction immutable = new ImmutableAction(action);
		immutable.setCompleted(true);
	}
	
	@Test (expected= ImmutableException.class)
	public void testImmutableException2() throws ImmutableException{
		Action action = new Action("Test");
		IAction immutable = new ImmutableAction(action);
		immutable.setDescription("blabla");
	}
}