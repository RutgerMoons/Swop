package scenarioTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import order.Order;
import order.OrderBook;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.OrderHandler;
import ui.UIFacade;
import ui.UserInterface;
import users.GarageHolder;
import clock.Clock;
import car.Airco;
import car.Body;
import car.CarModel;
import car.CarModelCatalogue;
import car.CarModelCatalogueFiller;
import car.CarPartCatalogue;
import car.CarPartCatalogueFiller;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import assembly.AssemblyLine;

/**
 * Assumption : the garageholder is succesfully logged in.
 * 
 */
@RunWith(Parameterized.class)
public class OrderNewCarScenario {

	private UIFacade ui;
	private OrderHandler handler;
	private OrderBook book;
	private GarageHolder holder;
	private Order order;
	private CarModelCatalogue catalogue;

	public OrderNewCarScenario(UIFacade ui) {
		this.ui = ui;
		holder = new GarageHolder("Stef");
		AssemblyLine line = new AssemblyLine(new Clock());
		book = new OrderBook(line);
		
		CarPartCatalogue catalogueCarPart = new CarPartCatalogue();
		CarPartCatalogueFiller partFiller = new CarPartCatalogueFiller(catalogueCarPart);
		partFiller.initializeCarParts();
		catalogue = new CarModelCatalogue(catalogueCarPart);
		CarModelCatalogueFiller filler = new CarModelCatalogueFiller();
		catalogue.initializeCatalogue(filler.getInitialModels());
		
		
		CarModel model = new CarModel("Volvo", new Airco("manual"), new Body(
				"sedan"), new Color("blue"), new Engine("standard"),
				new Gearbox("manual"), new Seat("black leather"), new Wheel(
						"black"));
		order = new Order("Stef", model, 1);
	}

	@Test
	public void TestUseCaseDeclineOrder() {
		String s = System.lineSeparator();
		String input = "Y" + s // step 2, user wants to place an order
						+ "Lada" + s // step 4, user chooses a car model
						+ "1" + s // step 6 completing of ordering form
						+ "N" + s // // canceling of order, step 6a
						+ "N" + s; // end of flow
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new UserInterface();
		handler = new OrderHandler(ui, book, catalogue);
		book.addOrder(order);

		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		handler.executeUseCase(holder);
		String output = myout.toString();
		assertEquals(
				"Your pending orders:" // System presents an overview of the orders placed, step 1
						+ s
						+ "1 Volvo Estimated completion time: 0 days and 4 hours and 0 minutes"
						+ s
						+ s
						+ "You have no completed Orders"
						+ s
						+ s
						+ "Do you want to continue? Y/N" // end step 1
						+ s
						+ "Possible models:"// System shows a list of available car models, step 3
						+ s
						+ "Lada"
						+ s
						+ "Polo"
						+ s
						+ s
						+ "Which model do you want to order?" // step 4, system asks wich carmodel the garageholder wants to order
						+ s
						+ "How many cars do you want to order?" // Step 5, beginning over ordering form
						+ s
						+ "Your order:"
						+ s
						+ "1 Lada"
						+ s
						+ s
						+ "Do you want to continue? Y/N" //step 7, stores the new order
						+ s
						+ "Your pending orders:" // step 8
						+ s
						+ "1 Volvo Estimated completion time: 0 days and 4 hours and 0 minutes"
						+ s + s + "You have no completed Orders" + s + s
						+ "Do you want to continue? Y/N" + s + "", output); 
		//end of flow
	}

	@Test
	public void TestUseCase() {
		String s = System.lineSeparator();
		String input = "Y" + s // step 2, user wants to place an order
					+ "Lada" + s // step 4, user chooses a car model
					+ "1" + s // starting to complete an new order, step 6
					+ "Y" + s // user confirms the order
					+ "N" + s; // canceling of order, step 6a
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new UserInterface();
		handler = new OrderHandler(ui, book, catalogue);

		CarModel model = new CarModel("Volvo", new Airco("manual"), new Body(
				"sedan"), new Color("blue"), new Engine("standard"),
				new Gearbox("manual"), new Seat("black leather"), new Wheel(
						"black"));
		Order order = new Order("Stef", model, 1);
		book.addOrder(order);

		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		handler.executeUseCase(holder);
		String output = myout.toString();
		assertEquals(
				"Your pending orders:" // System presents an overview of the orders placed, step 1
						+ s
						+ "1 Volvo Estimated completion time: 0 days and 4 hours and 0 minutes"
						+ s + s + "You have no completed Orders" + s + s
						+ "Do you want to continue? Y/N" + s // end step 1
						+ "Possible models:" + s + "Lada" + s + "Polo" + s + s // System shows a list of available car models, step 3
						+ "Which model do you want to order?" + s // step 4, system asks wich carmodel the garageholder wants to order
						+ "How many cars do you want to order?" + s
						+ "Your order:" + s + "1 Lada" + s + s // Step 5, beginning over ordering form
						+ "Do you want to continue? Y/N" + s + "Your order:"
						+ s + "1 Lada Estimated completion time: day 0 5h0" + s
						+ s, output);
		// end of flow
	}

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new UserInterface() } });
	}

}
