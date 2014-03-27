package ui;

import java.util.ArrayList;

import order.OrderBook;
import users.UserBook;
import clock.Clock;
import car.CarModelCatalogue;
import car.CarModelCatalogueFiller;
import car.CarPartCatalogue;
import car.CarPartCatalogueFiller;
import assembly.AssemblyLine;

/**
 * This is the class where the program starts running.
 * 
 */
public class runProgram {
	private static UserInterface userInterface;
	private static UserHandler userHandler;
	private static OrderHandler orderHandler;
	private static AssembleHandler assembleHandler;
	private static AdvanceAssemblyLineHandler advanceAssemblyLineHandler;

	public static void main(String[] args) {
		initializeData();
		startProgram();
	}

	/**
	 * Initializes all datastructures that the program needs from the start.
	 */
	public static void initializeData() {

		userInterface = new UserInterface();
		orderHandler = new OrderHandler(userInterface);
		assembleHandler = new AssembleHandler(userInterface);
		advanceAssemblyLineHandler = new AdvanceAssemblyLineHandler(userInterface);

		ArrayList<UseCaseHandler> handlers = new ArrayList<UseCaseHandler>();
		handlers.add(orderHandler);
		handlers.add(assembleHandler);
		handlers.add(advanceAssemblyLineHandler);

		userHandler = new UserHandler(userInterface);
	}

	/**
	 * Starts the program.
	 */
	public static void startProgram() {
		do {
			userHandler.login();

			userHandler.performDuties();

			userHandler.logOut();
		} while (true);

	}

}
