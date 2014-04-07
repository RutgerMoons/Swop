package domain.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.clock.ImmutableClock;

public class LoggerTest {

	private Logger logger;
	private ImmutableClock currentTime;
	
	@Before
	public void testConstructor() {
		int amount = 5;
		logger = new Logger(amount);
		assertNotNull(logger);
		assertEquals(0,logger.getDetailedDays().size());
		assertEquals(0,logger.getDetailedDelays().size());
		int days = 4;
		int minutes = 850;
		ImmutableClock newCurrentTime = new ImmutableClock(days, minutes);
		logger.advanceClock(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test
	public void testAdvanceClock(){
		int days = 4;
		int minutes = 500;
		ImmutableClock newCurrentTime = new ImmutableClock(days, minutes);
		logger.advanceClock(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAdvanceClockThrows(){
		logger.advanceClock(null);
	}
	
	@Test
	public void testStartNewDay(){
		logger.startNewDay();
	}
	
	@Test
	public void testUpdateCompletedOrder(){
		ImmutableClock estimatedTimeOfCompletion = new ImmutableClock(4,500);
		logger.updateCompletedOrder(estimatedTimeOfCompletion);
		logger.advanceClock(new ImmutableClock(4,450));
		
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
		logger.advanceClock(new ImmutableClock(5,600));
		logger.startNewDay();
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.advanceClock(new ImmutableClock(6,300));
		logger.startNewDay();
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,150);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay();
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
		logger.advanceClock(new ImmutableClock(5,600));
		logger.startNewDay();
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		logger.updateCompletedOrder(estimatedTimeOfCompletion4);
		logger.updateCompletedOrder(estimatedTimeOfCompletion5);
		logger.advanceClock(new ImmutableClock(6,300));
		logger.startNewDay();
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,250);
		logger.updateCompletedOrder(estimatedTimeOfCompletion6);
		logger.startNewDay();
		assertEquals(2,logger.averageDays());
		assertEquals(208,logger.averageDelays());
	}

}
