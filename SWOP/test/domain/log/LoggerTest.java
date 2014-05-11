package domain.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;

public class LoggerTest {

	private Logger logger;
	
	@Before
	public void testConstructor() {
		int amount = 5;
		logger = new Logger(amount, new ClockObserver(), new AssemblyLineObserver());
		assertNotNull(logger);
		assertEquals(0,logger.getDetailedDays().size());
		assertEquals(0,logger.getDetailedDelays().size());
		int days = 4;
		int minutes = 850;
		ImmutableClock newCurrentTime = new ImmutableClock(days, minutes);
		logger.advanceTime(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest2(){
		logger = new Logger(5, null , new AssemblyLineObserver());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest3(){
		logger = new Logger(5, new ClockObserver(), null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest4(){
		logger = new Logger(5, null , null);
	}
	
	@Test
	public void testAdvanceClock(){
		int days = 4;
		int minutes = 500;
		ImmutableClock newCurrentTime = new ImmutableClock(days, minutes);
		logger.advanceTime(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAdvanceClockThrows(){
		logger.advanceTime(null);
	}
	
	@Test
	public void testStartNewDay(){
		logger.startNewDay(new ImmutableClock(1, 0));
		assertEquals(1, logger.getCurrentTime().getDays());
		assertEquals(0, logger.getCurrentTime().getMinutes());
	}
	
	@Test
	public void testUpdateCompletedOrder(){
		ImmutableClock estimatedTimeOfCompletion = new ImmutableClock(4,500);
		logger.updateCompletedOrder(estimatedTimeOfCompletion);
		logger.advanceTime(new ImmutableClock(4,450));
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUpdateCompletedOrderThrows(){
		logger.updateCompletedOrder(null);
	}
	
	@Test 
	public void testMedian(){
		ImmutableClock estimatedTimeOfCompletion1 = new ImmutableClock(4,550);
		ImmutableClock estimatedTimeOfCompletion2 = new ImmutableClock(4,650);
		ImmutableClock estimatedTimeOfCompletion3 = new ImmutableClock(4,750);
		logger.updateCompletedOrder(estimatedTimeOfCompletion1);
		logger.updateCompletedOrder(estimatedTimeOfCompletion2);
		logger.updateCompletedOrder(estimatedTimeOfCompletion3);
		logger.startNewDay(new ImmutableClock(5,600));
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.startNewDay(new ImmutableClock(6,300));
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,150);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay(new ImmutableClock(7, 0));
		assertEquals(2,logger.medianDays());
		assertEquals(225,logger.medianDelays());
	}
	
	@Test 
	public void testAverage(){
		ImmutableClock estimatedTimeOfCompletion1 = new ImmutableClock(4,550);
		ImmutableClock estimatedTimeOfCompletion2 = new ImmutableClock(4,650);
		ImmutableClock estimatedTimeOfCompletion3 = new ImmutableClock(4,750);
		logger.updateCompletedOrder(estimatedTimeOfCompletion1);
		logger.updateCompletedOrder(estimatedTimeOfCompletion2);
		logger.updateCompletedOrder(estimatedTimeOfCompletion3);
		logger.startNewDay(new ImmutableClock(5,600));
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.startNewDay(new ImmutableClock(6,300));
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,150);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay(new ImmutableClock(7, 0));
		assertEquals(2,logger.averageDays());
		assertEquals(225,logger.averageDelays());
	}

}
