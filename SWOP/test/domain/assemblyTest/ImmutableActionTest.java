package domain.assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.exception.UnmodifiableException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.UnmodifiableAction;


public class ImmutableActionTest {


	@Test
	public void test() {
		Action action = new Action("Test");
		IAction immutable = new UnmodifiableAction(action);
		
		assertEquals("Test", immutable.getDescription());
		assertFalse(immutable.isCompleted());
		action.setCompleted(true);
		assertTrue(immutable.isCompleted());
		assertEquals(action.toString(), immutable.toString());
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new UnmodifiableAction(null);
	}
	
	@Test (expected= UnmodifiableException.class)
	public void testImmutableException() throws UnmodifiableException{
		Action action = new Action("Test");
		IAction immutable = new UnmodifiableAction(action);
		immutable.setCompleted(true);
	}
	
	@Test (expected= UnmodifiableException.class)
	public void testImmutableException2() throws UnmodifiableException{
		Action action = new Action("Test");
		IAction immutable = new UnmodifiableAction(action);
		immutable.setDescription("blabla");
	}
}
