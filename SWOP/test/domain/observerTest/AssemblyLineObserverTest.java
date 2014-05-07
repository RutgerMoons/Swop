package domain.observerTest;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;

public class AssemblyLineObserverTest {

	private AssemblyLineObserver observer;
	
	@Before
	public void constructorTest() {
		observer = new AssemblyLineObserver();
		assertNotNull(observer);
	}

	@Test 
	public void attachLoggerTest1(){
		ClockObserver observer2 = new ClockObserver();
		Logger logger = new Logger(3, observer2, observer);
		observer.attachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void attachLoggerTest2(){
		observer.attachLogger(null);
	}
	
	@Test 
	public void detachLoggerTest1(){
		ClockObserver observer2 = new ClockObserver();
		Logger logger = new Logger(3, observer2, observer);
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
		Logger logger = new Logger(3,observer2, observer);
		logger.advanceTime(new ImmutableClock(1,200));
		observer.updateCompletedOrder(new ImmutableClock(1,100));
	}
	
}
