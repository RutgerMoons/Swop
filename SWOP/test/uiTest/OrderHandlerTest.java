package uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import order.Order;
import order.OrderBook;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
import assembly.AssemblyLine;
import ui.OrderHandler;
import ui.UIFacade;
import ui.UserInterface;
import users.GarageHolder;
import users.Worker;

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
		String s = System.lineSeparator();
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		GarageHolder holder = new GarageHolder("Stef");
		handler.showOrders(holder);
		String output = myout.toString();
		assertEquals(
				"You have no pending Orders" + s + s + "You have no completed Orders" + s + s,
				output);
		CarModel model = new CarModel("Volvo", new Airco("manual"), new Body(
				"sedan"), new Color("blue"), new Engine("standard"),
				new Gearbox("manual"), new Seat("black leather"), new Wheel(
						"black"));
		Order order = new Order("Stef", model, 1);
		book.addOrder(order);

		myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		handler.showOrders(holder);
		output = myout.toString();
		assertEquals(
				"Your pending orders:" + s + "1 Volvo Estimated completion time: 0 days and 4 hours and 0 minutes" + s + s + "You have no completed Orders" + s + s,
				output);

		order.completeCar();
		book.updateOrderBook(order);
		myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		handler.showOrders(holder);
		output = myout.toString();
		assertEquals(
				"You have no pending Orders" + s +s + "Your completed orders:" + s + "1 Volvo" + s + s,
				output);

	}

	@Test
	public void TestPlaceNewOrder() {
		String s = System.lineSeparator();
		GarageHolder holder = new GarageHolder("Stef");
		String input = "N" + s + "Y" + s + "Lada" + s + "1" + s + "Y" + s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		AssemblyLine line = new AssemblyLine(new Clock());
		book = new OrderBook(line);
		CarModelCatalogue catalogue = new CarModelCatalogue();
		ui = new UserInterface();
		handler = new OrderHandler(ui, book, catalogue);

		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		Order order = handler.placeNewOrder(holder);
		String output = myout.toString();
		assertEquals("Do you want to continue? Y/N" + s, output);
		assertNull(order);

		input = "Y";
		myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		order = handler.placeNewOrder(holder);
		output = myout.toString();
		assertNotNull(order);

		assertEquals(
				"Do you want to continue? Y/N" + s + "Possible models:" + s + "Lada" + s + "Polo" + s + s + "Which model do you want to order?" + s + "How many cars do you want to order?" + s + "Your order:" + s + "1 Lada" + s + s + "Do you want to continue? Y/N" + s,
				output);

		myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		handler.showNewOrder(order);
		output = myout.toString();
		assertEquals(
				"Your order:" + s + "1 Lada Estimated completion time: day 0 4h0" + s + s,
				output);
	}

	@Test
	public void TestPlaceNewOrderCancel() {
		String s = System.lineSeparator();
		GarageHolder holder = new GarageHolder("Stef");
		String input = "Y" + s + "Lada" + s + "1" + s + "N" + s + "N" + s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		AssemblyLine line = new AssemblyLine(new Clock());
		book = new OrderBook(line);
		CarModelCatalogue catalogue = new CarModelCatalogue();
		ui = new UserInterface();
		handler = new OrderHandler(ui, book, catalogue);

		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		assertNull(handler.placeNewOrder(holder));
		String output = myout.toString();
		assertEquals(
				"Do you want to continue? Y/N" + s + "Possible models:" + s + "Lada" + s + "Polo" + s + s + "Which model do you want to order?" + s + "How many cars do you want to order?" + s + "Your order:" + s + "1 Lada" + s + s + "Do you want to continue? Y/N" + s + "You have no pending Orders" + s + s + "You have no completed Orders" + s + s + "Do you want to continue? Y/N" + s + "",
				output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestExecuteInvalidUser(){
		handler.executeUseCase(null);
	}
	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new UserInterface() } });
	}

}
