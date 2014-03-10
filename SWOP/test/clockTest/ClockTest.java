package clockTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import car.Airco;
import clock.Clock;

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
		clock.reset();
		assertTrue(clock.getMinutes() == 0);
		clock.advanceTime(1450);
		assertTrue(clock.getMinutes() == 10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAdvanceException() {
		clock.advanceTime(-10);
	}
}
