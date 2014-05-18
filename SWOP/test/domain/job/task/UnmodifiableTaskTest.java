package domain.job.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.job.task.UnmodifiableTask;


public class UnmodifiableTaskTest {
	ITask task;
	ITask immutable;
	@Before
	public void initialize(){
		task = new Task("Test");
		immutable = new UnmodifiableTask(task);
		
	}
	
	@Test
	public void test() throws UnmodifiableException{
		assertEquals("Test", immutable.getTaskDescription());
		Action action = new Action("Paint");
		task.addAction(action);
		assertEquals(action, immutable.getActions().get(0));
		assertFalse(immutable.isCompleted());
		assertEquals(task.toString(), immutable.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void illegalConstructorTest(){
		new UnmodifiableTask(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable1() throws UnmodifiableException{
		immutable.setActions(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable2() throws UnmodifiableException{
		immutable.addAction(null);
	}
	
	@Test(expected=UnmodifiableException.class)
	public void testImmutable3() throws UnmodifiableException{
		immutable.setTaskDescription(null);
	}
}
