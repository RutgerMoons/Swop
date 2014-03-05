package ui;

import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import order.OrderBook;
import users.UserBook;
import assembly.AssemblyLine;
import car.Catalogue;
import clock.Clock;


public class runProgram {
	private static Clock clock;
	private static Catalogue catalogue;
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
	
	public static void initializeData(){
		clock = new Clock();
		//de constructor van Assemblyline moet de initial workbenches aanmaken en erinstoppen
		assemblyLine = new AssemblyLine(clock);
		catalogue = new Catalogue();
		
		orderBook = new OrderBook();
		userBook = new UserBook();
		userInterface = new UserInterface();
		orderHandler = new OrderHandler(userInterface,orderBook,catalogue);
		assembleHandler = new AssembleHandler();
		advanceAssemblyLineHandler = new AdvanceAssemblyLineHandler();
		
		ArrayList<UseCaseHandler> handlers = new ArrayList<UseCaseHandler>();
		handlers.add(orderHandler);
		handlers.add(assembleHandler);
		handlers.add(advanceAssemblyLineHandler);
		
		userHandler = new UserHandler(userInterface, clock, catalogue, orderBook, assemblyLine, userBook, handlers);
	}
	
	public static void startProgram(){
		do{userHandler.login();
		userHandler.performDuties();
		userHandler.logOut();
		}while(true);
		
	}
	
	
}
