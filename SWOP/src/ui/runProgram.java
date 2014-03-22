package ui;

import java.util.ArrayList;

import order.OrderBook;
import users.UserBook;
import clock.Clock;
import car.CarModelCatalogue;
import assembly.AssemblyLine;

/**
 * This is the class where the program starts running.
 * 
 */
public class runProgram {
	private static Clock clock;
	private static CarModelCatalogue catalogue;
	private static OrderBook orderBook;
	private static AssemblyLine assemblyLine;
	private static UserInterface userInterface;
	private static UIFacade UIFacade;
	private static UserBook userBook;
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
		clock = new Clock();
		assemblyLine = new AssemblyLine(clock);
		catalogue = new CarModelCatalogue();

		orderBook = new OrderBook(assemblyLine);
		userBook = new UserBook();
		userInterface = new UserInterface();
		orderHandler = new OrderHandler(userInterface, orderBook, catalogue);
		assembleHandler = new AssembleHandler(userInterface, assemblyLine);
		advanceAssemblyLineHandler = new AdvanceAssemblyLineHandler(
				userInterface, assemblyLine, clock);

		ArrayList<UseCaseHandler> handlers = new ArrayList<UseCaseHandler>();
		handlers.add(orderHandler);
		handlers.add(assembleHandler);
		handlers.add(advanceAssemblyLineHandler);

		userHandler = new UserHandler(userInterface, userBook, handlers);
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
