import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class Test_Integer_Stack {
	Integer_Stack stack;

	@Before
	public void initialise() {
		stack = new Integer_Stack();
	}

	@Test
	public void TestInitialise() {
		assertEquals(stack.count(), 0);
	}

	@Test
	public void TestPopEmpty() {
		assertEquals(stack.pop(), null);
	}

	@Test
	public void TestPop() {
		stack.push(5);
		assertTrue(stack.pop() == 5);
		assertEquals(stack.count(), 0);
	}

	@Test
	public void TestPush() {
		stack.push(6);
		assertEquals(stack.count(), 1);
		assertTrue(stack.pop() == 6);
	}

	@Test
	public void pushMultiple() {
		int[] nb = { 1, 2, 3 };
		stack.pushMultiple(nb);
		assertTrue(stack.peek() == 3);
		assertTrue(stack.count() == 3);
	}

	@Test
	public void pushMultipleEmptyArray() {
		int[] empty = new int[0];
		stack.pushMultiple(empty);
		assertTrue(stack.count() == 0);
	}

	@Test
	public void TestPushPopMultiple() {
		int[] derp = { 1, 2 };
		stack.pushMultiple(derp);
		assertTrue(stack.count() == 2);
	}

	@Test
	public void TestPopMultiple() {
		stack.push(1);
		stack.push(2);
		assertTrue(stack.pop() == 2);
		assertTrue(stack.pop() == 1);
		assertEquals(stack.count(), 0);
	}

	@Test
	public void TestNullPush() {
		stack.push(1);
		stack.push(null);
		assertTrue(stack.pop() == 1);
	}

	@Test
	public void TestPeek() {
		stack.push(1);
		assertTrue(stack.peek() == 1);
		assertTrue(stack.count() == 1);
	}

	@Test
	public void TestPeakNull() {
		assertTrue(stack.peek() == null);
	}
}
