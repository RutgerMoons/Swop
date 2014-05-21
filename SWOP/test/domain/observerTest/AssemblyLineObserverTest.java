package domain.observerTest;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.order.OrderBook;
import domain.order.order.StandardOrder;
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
		Logger logger = new Logger(3, new ImmutableClock(0, 0));
		observer.attachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void attachLoggerTest2(){
		observer.attachLogger(null);
	}
	
	@Test 
	public void detachLoggerTest1(){
		Logger logger = new Logger(3, new ImmutableClock(0, 0));
		observer.attachLogger(logger);
		observer.detachLogger(logger);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void detachLoggerTest2(){
		observer.detachLogger(null);
	}
	
	@Test
	public void updateCompletedOrderTest(){
		Logger logger = new Logger(3, new ImmutableClock(0, 0));
		logger.advanceTime(new ImmutableClock(1,200));
		StandardOrder order = new StandardOrder("garageholder", new Vehicle(new VehicleSpecification("jos", new HashSet<VehicleOption>(), new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>())), 1, new ImmutableClock(0, 0));
		book.addOrder(order);
		order.setEstimatedTime(new ImmutableClock(1, 1));
		order.completeCar();
		observer.updateCompletedOrder(order);
		assertNotNull(logger.getDetailedDelays());	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegal(){
		observer.updateCompletedOrder(null);
	}
}
