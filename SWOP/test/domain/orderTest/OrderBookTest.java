package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.observer.ClockObserver;
import domain.order.StandardOrder;
import domain.order.OrderBook;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {
	OrderBook orderBook;
	CarModel model1;
	CarModel model2;
	@Before
	public void setUp() throws AlreadyInMapException{
		orderBook = new OrderBook(new AssemblyLine(new ClockObserver(), new UnmodifiableClock(0)));
		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		parts.add(new CarOption("black", CarOptionCategory.COLOR));
		
		CarModelSpecification specification = new CarModelSpecification("test", parts, 60);
		model1 = new CarModel(specification);
		
		Set<CarOption> parts2 = new HashSet<>();
		parts2.add(new CarOption("sport", CarOptionCategory.BODY));
		parts2.add(new CarOption("black", CarOptionCategory.COLOR));
		parts2.add(new CarOption("6 Speed Manual", CarOptionCategory.GEARBOX));
		CarModelSpecification specification2 = new CarModelSpecification("test", parts2, 60);
		model2 = new CarModel(specification2);
}

	@Test
	public void test1() throws ImmutableException, NotImplementedException {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		StandardOrder order = new StandardOrder("Mario", model1,3, new UnmodifiableClock(20));
		orderBook.addOrder(order);
		assertFalse(orderBook.getPendingOrders().isEmpty());
		StandardOrder order2 = new StandardOrder("Mario",model2,2, new UnmodifiableClock(20));
		orderBook.addOrder(order2);
		assertEquals(1,orderBook.getPendingOrders().keySet().size());
		assertEquals(2,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order2);
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
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
		StandardOrder order = new StandardOrder(null,null,0, new UnmodifiableClock(20));
		orderBook.updateOrderBook(order);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		orderBook.updateOrderBook(null);
	}
}
