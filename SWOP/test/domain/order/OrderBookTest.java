package domain.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.OrderBookObserver;
import domain.order.OrderBook;
import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.scheduling.WorkloadDivider;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {
	OrderBook orderBook;
	Vehicle model1;
	Vehicle model2;
	@Before
	public void setUp() throws AlreadyInMapException{
		orderBook = new OrderBook();
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		
		VehicleSpecification specification = new VehicleSpecification("test", parts, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		model1 = new Vehicle(specification);
		
		Set<VehicleOption> parts2 = new HashSet<>();
		parts2.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		parts2.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts2.add(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));
		VehicleSpecification specification2 = new VehicleSpecification("test", parts2, new HashMap<WorkBenchType, Integer>(), new HashSet<VehicleOption>());
		model2 = new Vehicle(specification2);
}

	@Test
	public void test1() throws UnmodifiableException, NotImplementedException {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		StandardOrder order = new StandardOrder("Mario", model1,1, new ImmutableClock(0,0));
		orderBook.addOrder(order);
		assertFalse(orderBook.getPendingOrders().isEmpty());
		StandardOrder order2 = new StandardOrder("Mario",model2,2, new ImmutableClock(0,0));
		orderBook.addOrder(order2);
		order2.completeCar();
		order2.completeCar();
		
		assertEquals(1,orderBook.getPendingOrders().keySet().size());
		assertEquals(2,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order2);
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		order.completeCar();
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test2a(){
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		CustomVehicle customModel = new CustomVehicle();
		CustomOrder order = new CustomOrder("Mario", customModel,1, new ImmutableClock(0,20), new ImmutableClock(1, 420));
		orderBook.updateOrderBook(order);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test2b(){
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		orderBook.updateOrderBook(null);
	}
	
	@Test
	public void testAddCustomOrder() {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		CustomVehicle customModel = new CustomVehicle();
		CustomOrder order = new CustomOrder("Mario", customModel,1, new ImmutableClock(0,20), new ImmutableClock(1, 420));
		orderBook.addOrder(order);
		
		assertFalse(orderBook.getPendingOrders().isEmpty());
		assertEquals(1,orderBook.getPendingOrders().keySet().size());
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		order.completeCar();
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		
		assertEquals(1, order.getDeadline().getDays());
		assertEquals(420, order.getDeadline().getMinutes());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		StandardOrder order = new StandardOrder("mario", model1, 1, new ImmutableClock(0, 0));
		order.completeCar();
		orderBook.updateOrderBook(order);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test5(){
		
		orderBook.updateOrderBook(null);
	}
	
	@Test
	public void testObservers(){
		OrderBookObserver o1 = new OrderBookObserver();
		AssemblyLineObserver o2 = new AssemblyLineObserver();
		new WorkloadDivider(new ArrayList<AssemblyLine>(), o1, o2);
		orderBook.attachObserver(o1);
		StandardOrder order = new StandardOrder("mario", model1, 1, new ImmutableClock(0, 0));
		orderBook.addOrder(order);
		order.completeCar();
		orderBook.updateCompletedOrder(order);
		
		orderBook.detachObserver(o1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testObservers2(){
		orderBook.attachObserver(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testObservers3(){
		orderBook.detachObserver(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testObservers4(){
		orderBook.updatePlacedOrder(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testObservers5(){
		orderBook.updateCompletedOrder(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddIllegalOrder(){
		StandardOrder order = new StandardOrder("mario", model1, 1, new ImmutableClock(0, 0));
		order.completeCar();
		orderBook.addOrder(order);
	}
}
