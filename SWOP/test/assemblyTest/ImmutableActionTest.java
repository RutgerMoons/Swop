package assemblyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import assembly.Action;
import assembly.IAction;
import assembly.ImmutableAction;

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
}
