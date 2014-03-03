package orderTest;

import static org.junit.Assert.*;

import org.junit.Test;

import Order.Order;
import Order.OrderBook;

public class OrderBookTest {

	@Test
	public void test1() {
		assertNull(OrderBook.getCompletedOrders());
		assertNull(OrderBook.getPendingOrders());
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
		OrderBook.updateOrderBook(order);
		assertEquals(0,OrderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,OrderBook.getCompletedOrders().get(order.getGarageHolder()).size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void test2UpdateOrdrerIfCondition(){
		OrderBook.initializeBook();
		Order order = new Order(null,null,1);
		OrderBook.updateOrderBook(order);
		
	}
}
