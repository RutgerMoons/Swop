package orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarPart;
import domain.car.CarPartType;
import domain.clock.Clock;
import domain.exception.AlreadyInMapException;
import domain.order.Order;
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
		orderBook = new OrderBook(new AssemblyLine(new Clock()));
		model1 = new CarModel("Volkswagen");
		model1.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model1.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model1.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model1.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model1.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model1.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model1.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
		
		model2 = new CarModel("BMW");
		model2.addCarPart(new CarPart("manual", true, CarPartType.AIRCO));
		model2.addCarPart(new CarPart("sedan", false, CarPartType.BODY));
		model2.addCarPart(new CarPart("red", false, CarPartType.COLOR));
		model2.addCarPart(new CarPart("standard 2l 4 cilinders", false, CarPartType.ENGINE));
		model2.addCarPart(new CarPart("6 speed manual", false, CarPartType.GEARBOX));
		model2.addCarPart(new CarPart("leather black", false, CarPartType.SEATS));
		model2.addCarPart(new CarPart("comfort", false, CarPartType.WHEEL));
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
		Order order = new Order(null,null,0);
		orderBook.updateOrderBook(order);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		orderBook.updateOrderBook(null);
	}
}
