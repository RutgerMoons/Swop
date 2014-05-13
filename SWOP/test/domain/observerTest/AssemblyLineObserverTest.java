package domain.observerTest;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;

public class AssemblyLineObserverTest {

	private AssemblyLineObserver observer;
	
	@Before
	public void constructorTest() {
		observer = new AssemblyLineObserver();
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
	public void updateCompletedOrderTest(){
		ClockObserver observer2 = new ClockObserver();
		Logger logger = new Logger(3);
		logger.advanceTime(new ImmutableClock(1,200));
		observer.updateCompletedOrder(new ImmutableClock(1,100));
	}
	
}
