package domain.job.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.action.UnmodifiableAction;


public class UnmodifiableActionTest {


	@Test
	public void test() {
		Action action = new Action("Test");
		IAction immutable = new UnmodifiableAction(action);
		
		assertEquals("Test", immutable.getDescription());
		assertFalse(immutable.isCompleted());
		action.setCompleted(true);
		assertTrue(immutable.isCompleted());
		assertEquals(action.toString(), immutable.toString());
		
		assertEquals(action, immutable);
		assertEquals(immutable, action);
		assertEquals(action.hashCode(), immutable.hashCode());
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
