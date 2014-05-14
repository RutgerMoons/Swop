package domain.observerTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.observers.ClockObserver;

public class ClockObserverTest {

	private ClockObserver observer;

	@Before
	public void constructorTest() {
		observer = new ClockObserver();
		assertNotNull(observer);
	}

	@Test 
	public void attachLoggerTest1(){
		Logger logger = new Logger(3);
		observer.attachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void attachLoggerTest2(){
		observer.attachLogger(null);
	}
	
	@Test 
	public void detachLoggerTest1(){
		Logger logger = new Logger(3);
		observer.attachLogger(logger);
		observer.detachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void detachLoggerTest2(){
		observer.detachLogger(null);
	}	
	@Test
	public void advanceTimeTest(){
		Logger logger = new Logger(3);
		observer.attachLogger(logger);
		observer.advanceTime(new ImmutableClock(1,100));
		assertEquals(new ImmutableClock(1, 100), logger.getCurrentTime());
	}
	
	@Test
	public void startNewDayTest(){
		Logger logger = new Logger(3);
		observer.attachLogger(logger);
		observer.startNewDay(new ImmutableClock(1,0));
		assertEquals(new ImmutableClock(1, 0), logger.getCurrentTime());
		assertNotNull(logger.getDetailedDays());
	}

}
