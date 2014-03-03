package assemblyTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import assembly.Action;
import assembly.Task;

public class TaskTest {

	private Task task;
	@Before
	public void initialize(){
		task = new Task("Paint");
	}
	@Test
	public void TestConstructor(){
		assertNotNull(task.getActions());
		assertNotNull(task.getTaskDescription());
	}

	@Test
	public void setActions(){
		List<Action> actions = new ArrayList<>();
		task.setActions(actions);
		assertEquals(actions, task.getActions());
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void setInvalidActions(){
		task.setActions(null);
		assertEquals(null, task.getActions());
	}
	
	@Test
	public void TestAdd(){
		assertEquals(0, task.getActions().size());
		task.addAction(new Action());
		assertEquals(1, task.getActions().size());
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void TestInvalidAdd(){
		assertEquals(0, task.getActions().size());
		task.addAction(null);
		assertEquals(1, task.getActions().size());
	}
	
	@Test
	public void TestSetDescription(){
		task.setTaskDescription("Body");
		assertEquals("Body", task.getTaskDescription());
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void TestSetEmptyDescription(){
		task.setTaskDescription("");
		assertEquals("", task.getTaskDescription());
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void TestSetInvalidDescription(){
		task.setTaskDescription(null);
		assertEquals(null, task.getTaskDescription());
	}
	
	@Test
	public void TestCompletedOneAction(){
		Action action = new Action();
		task.addAction(action);
		assertFalse(task.isCompleted());
		action.setCompleted(true);
		assertTrue(task.isCompleted());
		action.setCompleted(false);
		assertFalse(task.isCompleted());
	}
	
	@Test
	public void TestCompletedTwoActions(){
		Action action1 = new Action();
		Action action2 = new Action();
		task.addAction(action1);
		task.addAction(action2);
		assertFalse(task.isCompleted());
		action1.setCompleted(true);
		assertFalse(task.isCompleted());
		action2.setCompleted(true);
		assertTrue(task.isCompleted());
		action1.setCompleted(false);
		assertFalse(task.isCompleted());
	}
}
