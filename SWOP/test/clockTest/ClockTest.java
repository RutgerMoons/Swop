package clockTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.clock.Clock;

public class ClockTest {

	private Clock clock;

	@Before
	public void setup() {
		this.clock = new Clock();
	}

	@Test
	public void testConstructor() {
		assertNotNull(clock);
		assertTrue(clock.getMinutes() == 0);
	}

	@Test
	public void testMethods() {
		clock.advanceTime(360);
		assertTrue(clock.getMinutes() == 360);
		clock.advanceTime(1450);
		assertTrue(clock.getMinutes() == 370);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAdvanceException() {
		clock.advanceTime(-10);
	}

	@Test
	public void TestStartNewDay() {
		assertEquals(0, clock.getDay());
		clock.startNewDay();
		assertEquals(1, clock.getDay());
	}
	
}
