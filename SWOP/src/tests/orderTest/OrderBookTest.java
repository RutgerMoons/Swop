package tests.orderTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import code.assembly.AssemblyLine;
import code.car.Airco;
import code.car.Body;
import code.car.CarModel;
import code.car.Color;
import code.car.Engine;
import code.car.Gearbox;
import code.car.Seat;
import code.car.Wheel;
import code.clock.Clock;
import code.order.Order;
import code.order.OrderBook;


/*
 * Geen 100% code coverage bij OrderBook maar vind niet hoe ik het kan fixen.
 */
public class OrderBookTest {
	OrderBook orderBook;
	CarModel model1;
	CarModel model2;
	@Before
	public void setUp(){
		orderBook = new OrderBook(new AssemblyLine(new Clock()));
		model1 = new CarModel("Volkswagen", new Airco("manual"), new Body("sedan"), new Color("blue"), 
				new Engine("standard 2l 4 cilinders"), new Gearbox("6 speed manual"), new Seat("leather black"), new Wheel("comfort"));
		
		model2 = new CarModel("BMW", new Airco("automatic climate control"), new Body("break"), new Color("red"), 
				new Engine("performance 2.5l 6 cilinders"), new Gearbox("5 speed automatic"), new Seat("leather white"), new Wheel("sports (low profile)"));
	}

	@Test
	public void test1() {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		Order order = new Order("Mario", model1,3);
		orderBook.addOrder(order);
		assertFalse(orderBook.getPendingOrders().isEmpty());
		Order order2 = new Order("Mario",model2,2);
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
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		orderBook.updateOrderBook(null);
	}
}
