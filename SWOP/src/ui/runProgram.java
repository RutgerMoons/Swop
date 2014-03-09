package ui;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import order.OrderBook;
import users.UserBook;
import assembly.AssemblyLine;
import car.CarModelCatalogue;
import clock.Clock;

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

		userHandler = new UserHandler(userInterface, clock, catalogue,
				orderBook, assemblyLine, userBook, handlers);
	}

	public static void startProgram() {
		do {
			userHandler.login();
			try {
				userHandler.performDuties();
			} catch (InvalidObjectException i) {
				UIFacade.invalidUserPrompt();
			}
			userHandler.logOut();
		} while (true);

	}

}
