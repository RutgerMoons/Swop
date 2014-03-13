package uiTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import order.Order;
import order.OrderBook;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.OrderHandler;
import ui.UIFacade;
import ui.UserInterface;
import users.GarageHolder;
import users.Worker;
import assembly.AssemblyLine;
import car.Airco;
import car.Body;
import car.CarModel;
import car.CarModelCatalogue;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import clock.Clock;

@RunWith(Parameterized.class)
public class OrderHandlerTest {

	private UIFacade ui;
	private OrderHandler handler;
	private OrderBook book;

	public OrderHandlerTest(UIFacade ui) {
		this.ui = ui;
	}

	@Before
	public void initialize() {
		AssemblyLine line = new AssemblyLine(new Clock());
		book = new OrderBook(line);
		CarModelCatalogue catalogue = new CarModelCatalogue();
		handler = new OrderHandler(ui, book, catalogue);
	}

	@Test
	public void TestConstructor() {
		AssemblyLine line = new AssemblyLine(new Clock());
		OrderBook book = new OrderBook(line);
		CarModelCatalogue catalogue = new CarModelCatalogue();
		OrderHandler handler = new OrderHandler(ui, book, catalogue);
		assertNotNull(handler);
	}

	@Test
	public void TestMayUseHandler() {
		GarageHolder holder = new GarageHolder("Stef");
		Worker worker = new Worker("Karen");
		assertTrue(handler.mayUseThisHandler(holder));
		assertFalse(handler.mayUseThisHandler(worker));
	}

	@Test
	public void TestShowOrders() {
		try {

			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			GarageHolder holder = new GarageHolder("Stef");
			handler.showOrders(holder);
			String output = myout.toString();
			myout.flush();
			assertEquals(
					"You have no pending Orders\r\n\r\nYou have no completed Orders\r\n\r\n",
					output);
			CarModel model = new CarModel("Volvo", new Airco("manual"),
					new Body("sedan"), new Color("blue"),
					new Engine("standard"), new Gearbox("manual"), new Seat(
							"black leather"), new Wheel("black"));
			Order order = new Order("Stef", model, 1);
			book.addOrder(order);

			myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			handler.showOrders(holder);
			output = myout.toString();
			assertEquals(
					"Your pending orders:\r\n1 Volvo Estimated completion time: 0 days and 4 hours and 0 minutes\r\n\r\nYou have no completed Orders\r\n\r\n",
					output);

			order.completeCar();
			book.updateOrderBook(order);
			myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			handler.showOrders(holder);
			output = myout.toString();
			assertEquals(
					"You have no pending Orders\r\n\r\nYour completed orders:\r\n1 Volvo\r\n\r\n",
					output);

		} catch (Throwable t) {	}
	}


	@Test
	public void TestPlaceNewOrder(){
		try{
			String s = System.lineSeparator();
			GarageHolder holder = new GarageHolder("Stef");
			
			String input = "N" + s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			AssemblyLine line = new AssemblyLine(new Clock());
			book = new OrderBook(line);
			CarModelCatalogue catalogue = new CarModelCatalogue();
			ui = new UserInterface();
			handler = new OrderHandler(ui, book, catalogue);
			
			Order order = handler.placeNewOrder(holder);
			String output = myout.toString();
			assertEquals("Do you want to continue? Y/N\r\n", output);
			assertNull(order);
			int x = in.read(input.getBytes());
			input = "Y";
			myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			order = handler.placeNewOrder(holder);
			output = myout.toString();
			assertEquals("Do you want to continue? Y/N\r\n", output);
		}catch(Throwable t){}
	}
	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new UserInterface() } });
	}

}