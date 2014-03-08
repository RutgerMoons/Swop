package orderTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import order.Order;
import order.OrderBook;

import org.junit.Before;
import org.junit.Test;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {
	OrderBook orderBook;
	@Before
	public void setUp(){
		orderBook = new OrderBook();
	}

	@Test
	public void test1() {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		Order order = new Order("Mario", "Volkswagen",3);
		orderBook.addOrder(order);
		assertFalse(orderBook.getPendingOrders().isEmpty());
		Order order2 = new Order("Mario","BMW",2);
		orderBook.addOrder(order2);
		assertEquals(1,orderBook.getPendingOrders().size());
		assertEquals(2,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order2);
		assertEquals(1,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(1,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		orderBook.updateOrderBook(order);
		assertEquals(0,orderBook.getPendingOrders().get(order.getGarageHolder()).size());
		assertEquals(2,orderBook.getCompletedOrders().get(order.getGarageHolder()).size());
		HashMap<String,ArrayList<Order>> test = orderBook.getCompletedOrders();	 
		test.get(order.getGarageHolder()).remove(0);
		assertEquals(1,test.get(order.getGarageHolder()).size());
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
		Order order = new Order(null,null,0);
		orderBook.updateOrderBook(order);
		
	}
}
