package orderTest;

import static org.junit.Assert.*;

import order.Order;
import order.OrderBook;

import org.junit.Test;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {

	@Test
	public void test1() {
		OrderBook.initializeBook();
		assertNotNull(OrderBook.getCompletedOrders());
		assertNotNull(OrderBook.getPendingOrders());
		Order order = new Order("Mario", "Volkswagen",3);
		OrderBook.addOrder(order);
		assertFalse(OrderBook.getPendingOrders().isEmpty());
		Order order2 = new Order("Mario","BMW",2);
		OrderBook.addOrder(order2);
		assertEquals(1,OrderBook.getPendingOrders().size());
		assertEquals(2,OrderBook.getPendingOrders().get(order.getGarageHolder()).size());
		OrderBook.updateOrderBook(order2);
		assertEquals(1,OrderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,OrderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		OrderBook.updateOrderBook(order);
		assertEquals(0,OrderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,OrderBook.getCompletedOrders().get(order.getGarageHolder()).size());
	}

	@Test
	public void test2(){
		OrderBook.initializeBook();
		assertNotNull(OrderBook.getCompletedOrders());
		assertNotNull(OrderBook.getPendingOrders());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void test3(){
		OrderBook.initializeBook();
		Order order = new Order(null,null,0);
		OrderBook.updateOrderBook(order);
		
	}
}
