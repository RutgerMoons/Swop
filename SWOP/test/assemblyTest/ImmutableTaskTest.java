package assemblyTest;

import static org.junit.Assert.*;

import org.junit.Test;

import assembly.Action;
import assembly.ITask;
import assembly.ImmutableTask;
import assembly.Task;

public class ImmutableTaskTest {

	@Test
	public void test(){
		Task task = new Task("Test");
		ITask immutable = new ImmutableTask(task);
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
}
