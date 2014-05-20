package domain.clockTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import domain.clock.Clock;
import domain.observer.observers.ClockObserver;

public class ClockTest {

	private Clock clock;

	@Before
	public void setup() {
		this.clock = new Clock(360);
	}

	@Test
	public void testConstructor() {
		assertNotNull(clock);
		assertTrue(clock.getMinutes() == 0);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalConstructor1(){
		new Clock(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalConstructor2(){
		new Clock(2000);
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
		assertEquals(0, clock.getDays());
		clock.startNewDay();
		assertEquals(1, clock.getDays());
	}
	
	@Test
	public void TestAttachObserver() {
		ClockObserver obs = new ClockObserver();
		assertNotNull(obs);
		clock.attachObserver(obs);
		assertNotNull(clock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestAttachObserverNull() {
		clock.attachObserver(null);
	}
	
	@Test
	public void TestDetachObserver() {
		ClockObserver obs = new ClockObserver();
		assertNotNull(obs);
		clock.attachObserver(obs);
		assertNotNull(clock);
		clock.detachObserver(obs);
		assertNotNull(clock);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void TestDetachObserverNull() {
		clock.detachObserver(null);
	}
	
	@Test
	public void TestDetachObserverNull2() {
		ClockObserver obs = new ClockObserver();
		assertNotNull(obs);
		assertNotNull(clock);
		clock.detachObserver(obs);
		assertNotNull(clock);
	}
	
	@Test
	public void TestNotifyAdvance() {
		ClockObserver obs = new ClockObserver();
		assertNotNull(obs);
		clock.attachObserver(obs);
		assertNotNull(clock);
		clock.notifyObserversAdvanceTime();
		assertNotNull(obs);
		assertNotNull(clock);
	}
	
	@Test
	public void TestNotifyNewDay() {
		ClockObserver obs = new ClockObserver();
		assertNotNull(obs);
		clock.attachObserver(obs);
		assertNotNull(clock);
		clock.notifyObserversStartNewDay();
		assertNotNull(obs);
		assertNotNull(clock);
	}
}
