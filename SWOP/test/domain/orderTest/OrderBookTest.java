package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.AssemblyLine;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.OrderBook;
import domain.order.StandardOrder;
import domain.vehicle.CustomVehicle;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {
	OrderBook orderBook;
	Vehicle model1;
	Vehicle model2;
	@Before
	public void setUp() throws AlreadyInMapException{
		orderBook = new OrderBook(new AssemblyLine(new ClockObserver(), new ImmutableClock(0,0)));
		Set<VehicleOption> parts = new HashSet<>();
		parts.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		parts.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		
		VehicleSpecification specification = new VehicleSpecification("test", parts, 60);
		model1 = new Vehicle(specification);
		
		Set<VehicleOption> parts2 = new HashSet<>();
		parts2.add(new VehicleOption("sport", VehicleOptionCategory.BODY));
		parts2.add(new VehicleOption("black", VehicleOptionCategory.COLOR));
		parts2.add(new VehicleOption("6 Speed Manual", VehicleOptionCategory.GEARBOX));
		VehicleSpecification specification2 = new VehicleSpecification("test", parts2, 60);
		model2 = new Vehicle(specification2);
}

	@Test
	public void test1() throws ImmutableException, NotImplementedException {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		StandardOrder order = new StandardOrder("Mario", model1,3, new ImmutableClock(0,0));
		orderBook.addOrder(order, new ImmutableClock(0, 0));
		assertFalse(orderBook.getPendingOrders().isEmpty());
		StandardOrder order2 = new StandardOrder("Mario",model2,2, new ImmutableClock(0,0));
		orderBook.addOrder(order2, new ImmutableClock(0, 0));
		assertEquals(1,orderBook.getPendingOrders().keySet().size());
		assertEquals(2,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order2);
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		
		assertEquals(0, order.getEstimatedTime().getDays());
		assertEquals(300, order.getEstimatedTime().getMinutes());
	}
	
	@Test
	public void testAddCustomOrder() throws ImmutableException, NotImplementedException {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		CustomVehicle customModel = new CustomVehicle();
		CustomOrder order = new CustomOrder("Mario", customModel,3, new ImmutableClock(0,20), new ImmutableClock(1, 420));
		orderBook.addOrder(order, new ImmutableClock(0, 0));
		
		assertFalse(orderBook.getPendingOrders().isEmpty());
		assertEquals(1,orderBook.getPendingOrders().keySet().size());
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		
		assertEquals(1, order.getDeadline().getDays());
		assertEquals(420, order.getDeadline().getMinutes());
		assertEquals(1, order.getEstimatedTime().getDays());
		assertEquals(420, order.getEstimatedTime().getMinutes());
	}

	@Test
	public void test2(){
		orderBook.initializeBook();
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void test3(){
		orderBook.initializeBook();
		StandardOrder order = new StandardOrder(null,null,0, new ImmutableClock(0,20));
		orderBook.updateOrderBook(order);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		orderBook.updateOrderBook(null);
	}
}
