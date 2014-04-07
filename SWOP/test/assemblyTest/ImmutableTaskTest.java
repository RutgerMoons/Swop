package assemblyTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.Action;
import domain.assembly.ITask;
import domain.assembly.ImmutableTask;
import domain.assembly.Task;
import domain.exception.ImmutableException;


public class ImmutableTaskTest {
	ITask task;
	ITask immutable;
	@Before
	public void initialize(){
		task = new Task("Test");
		immutable = new ImmutableTask(task);
		
	}
	
	@Test
	public void test() throws ImmutableException{
		assertEquals("Test", immutable.getTaskDescription());
		Action action = new Action("Paint");
		task.addAction(action);
		assertEquals(action, immutable.getActions().get(0));
		assertFalse(immutable.isCompleted());
		assertEquals(task.toString(), immutable.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new ImmutableTask(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable1() throws ImmutableException{
		immutable.setActions(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable2() throws ImmutableException{
		immutable.addAction(null);
	}
	
	@Test(expected=ImmutableException.class)
	public void testImmutable3() throws ImmutableException{
		immutable.setTaskDescription(null);
	}
}
