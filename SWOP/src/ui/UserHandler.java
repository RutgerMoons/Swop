package ui;

import java.util.ArrayList;

import order.OrderBook;
import users.GarageHolder;
import users.Manager;
import users.User;
import users.UserBook;
import users.Worker;
import util.Ensure;
import assembly.AssemblyLine;
import car.Catalogue;
import clock.Clock;

public class UserHandler {
	
	private Clock clock;
	private Catalogue catalogue;
	private OrderBook orderBook;
	private AssemblyLine assemblyLine;
	private UserBook userBook; 
	private UIFacade UIFacade;
	private ArrayList<UseCaseHandler> handlers;
	
	public UserHandler(UIFacade UIFacade, Clock clock,Catalogue catalogue, OrderBook orderBook, AssemblyLine assemblyLine,UserBook userBook, ArrayList<UseCaseHandler> handlers)throws NullPointerException{
		if(UIFacade == null || clock == null || catalogue == null || orderBook == null ||assemblyLine == null || userBook == null || handlers == null) throw new NullPointerException();
		this.UIFacade = UIFacade;
		this.clock = clock;
		this.catalogue = catalogue;
		this.orderBook = orderBook;
		this.assemblyLine = assemblyLine;
		this.userBook = userBook;
		this.handlers = handlers;
	}
	
	private User currentUser;
	
	public void login() {
		String name;
		
		name = this.UIFacade.getName();
				
		//check if user is new user
		if(userBook.getUserBook().containsKey(name)) this.currentUser = userBook.getUserBook().get(name);
		else {
			do {
				String role = this.UIFacade.getRole();
				if (role.equals("garageholder")) {
					currentUser = new GarageHolder(name);
				} else if (role.equals("manager")) {
					currentUser = new Manager(name);
				} else if (role.equals("worker")) {
					currentUser = new Worker(name);
				} else this.UIFacade.invalidAnswerPrompt();
			} while(currentUser==null);
			userBook.addUser(currentUser);
		}
	}
	
	//laat de ingelogde user doen wat hij zou kunnen doen
	public void performDuties(){
		UseCaseHandler useCaseHandler = currentUser.getRightHandler(handlers);
	
		if(useCaseHandler == null) UIFacade.invalidUserPrompt();
		else useCaseHandler.executeUseCase(currentUser);
	}
	
	public void logOut(){
		this.currentUser = null;
	}
}
