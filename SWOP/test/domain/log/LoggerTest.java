package domain.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.UnmodifiableClock;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;

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
		UnmodifiableClock newCurrentTime = new UnmodifiableClock(days, minutes);
		logger.advanceTime(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test
	public void testAdvanceClock(){
		int days = 4;
		int minutes = 500;
		UnmodifiableClock newCurrentTime = new UnmodifiableClock(days, minutes);
		logger.advanceTime(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAdvanceClockThrows(){
		logger.advanceTime(null);
	}
	
	@Test
	public void testStartNewDay(){
		logger.startNewDay(new UnmodifiableClock(1, 0));
		assertEquals(1, logger.getCurrentTime().getDays());
		assertEquals(0, logger.getCurrentTime().getMinutes());
	}
	
	@Test
	public void testUpdateCompletedOrder(){
		UnmodifiableClock estimatedTimeOfCompletion = new UnmodifiableClock(4,500);
		logger.updateCompletedOrder(estimatedTimeOfCompletion);
		logger.advanceTime(new UnmodifiableClock(4,450));
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testUpdateCompletedOrderThrows(){
		logger.updateCompletedOrder(null);
	}
	
	@Test 
	public void testMedian(){
		UnmodifiableClock estimatedTimeOfCompletion1 = new UnmodifiableClock(4,550);
		UnmodifiableClock estimatedTimeOfCompletion2 = new UnmodifiableClock(4,650);
		UnmodifiableClock estimatedTimeOfCompletion3 = new UnmodifiableClock(4,750);
		logger.updateCompletedOrder(estimatedTimeOfCompletion1);
		logger.updateCompletedOrder(estimatedTimeOfCompletion2);
		logger.updateCompletedOrder(estimatedTimeOfCompletion3);
		logger.startNewDay(new UnmodifiableClock(5,600));
		UnmodifiableClock estimatedTimeOfCompletion4 = new UnmodifiableClock(5,250);
		UnmodifiableClock estimatedTimeOfCompletion5 = new UnmodifiableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.startNewDay(new UnmodifiableClock(6,300));
		UnmodifiableClock estimatedTimeOfCompletion6 = new UnmodifiableClock(6,150);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay(new UnmodifiableClock(7, 0));
		assertEquals(2,logger.medianDays());
		assertEquals(225,logger.medianDelays());
	}
	
	@Test 
	public void testAverage(){
		UnmodifiableClock estimatedTimeOfCompletion1 = new UnmodifiableClock(4,550);
		UnmodifiableClock estimatedTimeOfCompletion2 = new UnmodifiableClock(4,650);
		UnmodifiableClock estimatedTimeOfCompletion3 = new UnmodifiableClock(4,750);
		logger.updateCompletedOrder(estimatedTimeOfCompletion1);
		logger.updateCompletedOrder(estimatedTimeOfCompletion2);
		logger.updateCompletedOrder(estimatedTimeOfCompletion3);
		logger.startNewDay(new UnmodifiableClock(5,600));
		UnmodifiableClock estimatedTimeOfCompletion4 = new UnmodifiableClock(5,250);
		UnmodifiableClock estimatedTimeOfCompletion5 = new UnmodifiableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.startNewDay(new UnmodifiableClock(6,300));
		UnmodifiableClock estimatedTimeOfCompletion6 = new UnmodifiableClock(6,150);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay(new UnmodifiableClock(7, 0));
		assertEquals(2,logger.averageDays());
		assertEquals(225,logger.averageDelays());
	}

}
