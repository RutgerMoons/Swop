package domain.orderTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.AssemblyLine;
import domain.car.CarModel;
import domain.car.CarOption;
import domain.car.CarOptionCategogry;
import domain.clock.Clock;
import domain.exception.AlreadyInMapException;
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
		orderBook = new OrderBook(new AssemblyLine(new Clock()));
		model1 = new CarModel("Volkswagen");
		model1.addCarPart(new CarOption("manual", true, CarOptionCategogry.AIRCO));
		model1.addCarPart(new CarOption("sedan", false, CarOptionCategogry.BODY));
		model1.addCarPart(new CarOption("red", false, CarOptionCategogry.COLOR));
		model1.addCarPart(new CarOption("standard 2l 4 cilinders", false, CarOptionCategogry.ENGINE));
		model1.addCarPart(new CarOption("6 speed manual", false, CarOptionCategogry.GEARBOX));
		model1.addCarPart(new CarOption("leather black", false, CarOptionCategogry.SEATS));
		model1.addCarPart(new CarOption("comfort", false, CarOptionCategogry.WHEEL));
		
		model2 = new CarModel("BMW");
		model2.addCarPart(new CarOption("manual", true, CarOptionCategogry.AIRCO));
		model2.addCarPart(new CarOption("sedan", false, CarOptionCategogry.BODY));
		model2.addCarPart(new CarOption("red", false, CarOptionCategogry.COLOR));
		model2.addCarPart(new CarOption("standard 2l 4 cilinders", false, CarOptionCategogry.ENGINE));
		model2.addCarPart(new CarOption("6 speed manual", false, CarOptionCategogry.GEARBOX));
		model2.addCarPart(new CarOption("leather black", false, CarOptionCategogry.SEATS));
		model2.addCarPart(new CarOption("comfort", false, CarOptionCategogry.WHEEL));
}

	@Test
	public void test1() {
		assertNotNull(orderBook.getCompletedOrders());
		assertNotNull(orderBook.getPendingOrders());
		StandardOrder order = new StandardOrder("Mario", model1,3);
		orderBook.addOrder(order);
		assertFalse(orderBook.getPendingOrders().isEmpty());
		StandardOrder order2 = new StandardOrder("Mario",model2,2);
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
		StandardOrder order = new StandardOrder(null,null,0);
		orderBook.updateOrderBook(order);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void test4(){
		orderBook.updateOrderBook(null);
	}
}
