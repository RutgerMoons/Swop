package domain.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.order.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class LoggerTest {

	private Logger logger;
	
	@Before
	public void testConstructor() {
		int amount = 5;
		logger = new Logger(amount, new ImmutableClock(0, 0));
		assertNotNull(logger);
		assertEquals(0,logger.getDetailedDays().size());
		assertEquals(0,logger.getDetailedDelays().size());
		assertEquals(0, logger.averageDelays());
		assertEquals(0, logger.medianDelays());
		int days = 4;
		int minutes = 850;
		ImmutableClock newCurrentTime = new ImmutableClock(days, minutes);
		logger.advanceTime(newCurrentTime);
		assertEquals(newCurrentTime,logger.getCurrentTime());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest2(){
		logger = new Logger(0, new ImmutableClock(0, 0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructorTest3(){
		logger = new Logger(1, null);
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
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testUpdateCompletedOrderThrows(){
		logger.updateCompletedOrder(null);
	}
	
	@Test 
	public void testMedian(){
		ImmutableClock estimatedTimeOfCompletion1 = new ImmutableClock(4,550);
		ImmutableClock estimatedTimeOfCompletion2 = new ImmutableClock(4,650);
		ImmutableClock estimatedTimeOfCompletion3 = new ImmutableClock(4,750);
		StandardOrder order1 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order2 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order3 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		
		order1.setEstimatedTime(estimatedTimeOfCompletion1);
		order2.setEstimatedTime(estimatedTimeOfCompletion2);
		order3.setEstimatedTime(estimatedTimeOfCompletion3);
		logger.updateCompletedOrder(order1);
		logger.updateCompletedOrder(order2);
		logger.updateCompletedOrder(order3);
		logger.startNewDay(new ImmutableClock(5,600));
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		StandardOrder order4 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order5 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		order4.setEstimatedTime(estimatedTimeOfCompletion4);
		order5.setEstimatedTime(estimatedTimeOfCompletion5);
		
		logger.updateCompletedOrder(order4);
		logger.updateCompletedOrder(order5);
		logger.startNewDay(new ImmutableClock(6,300));
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,150);
		StandardOrder order6 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		order6.setEstimatedTime(estimatedTimeOfCompletion6);
		logger.updateCompletedOrder(order6);
		logger.startNewDay(new ImmutableClock(7, 0));
		assertEquals(2,logger.medianDays());
		assertEquals(225,logger.medianDelays());
	}
	
	@Test 
	public void testAverage(){
		ImmutableClock estimatedTimeOfCompletion1 = new ImmutableClock(4,550);
		ImmutableClock estimatedTimeOfCompletion2 = new ImmutableClock(4,650);
		ImmutableClock estimatedTimeOfCompletion3 = new ImmutableClock(4,750);
		StandardOrder order1 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order2 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order3 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		
		order1.setEstimatedTime(estimatedTimeOfCompletion1);
		order2.setEstimatedTime(estimatedTimeOfCompletion2);
		order3.setEstimatedTime(estimatedTimeOfCompletion3);
		logger.updateCompletedOrder(order1);
		logger.updateCompletedOrder(order2);
		logger.updateCompletedOrder(order3);
		logger.startNewDay(new ImmutableClock(5,600));
		ImmutableClock estimatedTimeOfCompletion4 = new ImmutableClock(5,250);
		ImmutableClock estimatedTimeOfCompletion5 = new ImmutableClock(5,350);
		StandardOrder order4 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		StandardOrder order5 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		order4.setEstimatedTime(estimatedTimeOfCompletion4);
		order5.setEstimatedTime(estimatedTimeOfCompletion5);
		
		logger.updateCompletedOrder(order4);
		logger.updateCompletedOrder(order5);
		logger.startNewDay(new ImmutableClock(6,300));
		ImmutableClock estimatedTimeOfCompletion6 = new ImmutableClock(6,150);
		StandardOrder order6 = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 5, new ImmutableClock(0, 0));
		order6.setEstimatedTime(estimatedTimeOfCompletion6);
		logger.updateCompletedOrder(order6);
		logger.startNewDay(new ImmutableClock(7, 0));
		assertEquals(2,logger.averageDays());
		assertEquals(225,logger.averageDelays());
	}
}
