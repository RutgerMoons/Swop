package domain.observerTest;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;

public class ClockObserverTest {

	private ClockObserver observer;

	@Before
	public void constructorTest() {
		observer = new ClockObserver();
		assertNotNull(observer);
	}

	@Test 
	public void attachLoggerTest1(){
		AssemblyLineObserver observer2 = new AssemblyLineObserver();
		Logger logger = new Logger(3, observer, observer2);
		observer.attachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void attachLoggerTest2(){
		observer.attachLogger(null);
	}
	
	@Test 
	public void detachLoggerTest1(){
		AssemblyLineObserver observer2 = new AssemblyLineObserver();
		Logger logger = new Logger(3, observer, observer2);
		observer.attachLogger(logger);
		observer.detachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void detachLoggerTest2(){
		observer.detachLogger(null);
	}	
	@Test
	public void advanceTimeTest(){
		AssemblyLineObserver observer2 = new AssemblyLineObserver();
		Logger logger = new Logger(3, observer, observer2);
		observer.attachLogger(logger);
		observer.advanceTime(new ImmutableClock(1,100));
	}
	
	@Test
	public void startNewDayTest(){
		AssemblyLineObserver observer2 = new AssemblyLineObserver();
		Logger logger = new Logger(3, observer, observer2);
		observer.attachLogger(logger);
		observer.startNewDay(new ImmutableClock(1,0));
	}

}
