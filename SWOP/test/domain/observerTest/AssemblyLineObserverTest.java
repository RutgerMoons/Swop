package domain.observerTest;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkbenchType;
import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.order.OrderBook;
import domain.order.StandardOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

public class AssemblyLineObserverTest {

	private AssemblyLineObserver observer;
	private OrderBook book;
	@Before
	public void constructorTest() {
		observer = new AssemblyLineObserver();
		book = new OrderBook();
		observer.attachLogger(book);
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
		Logger logger = new Logger(3);
		logger.advanceTime(new ImmutableClock(1,200));
		StandardOrder order = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("null", new HashSet<VehicleOption>(), new HashMap<WorkbenchType, Integer>())), 5, new ImmutableClock(0, 0));
		order.setEstimatedTime(new ImmutableClock(1, 1));
		observer.updateCompletedOrder(order);
		assertNotNull(logger.getDetailedDelays());	
	}
	
}
