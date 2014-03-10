package ui;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import order.OrderBook;
import users.GarageHolder;
import users.Manager;
import users.User;
import users.UserBook;
import users.Worker;
import assembly.AssemblyLine;
import car.CarModelCatalogue;
import clock.Clock;

public class UserHandler {
	
	private UserBook userBook; 
	private UIFacade UIFacade;
	private ArrayList<UseCaseHandler> handlers;
	
	public UserHandler(UIFacade UIFacade, UserBook userBook, ArrayList<UseCaseHandler> handlers)throws NullPointerException{
		if(UIFacade == null || userBook == null || handlers == null) {
			throw new NullPointerException();
		}
			
		this.UIFacade = UIFacade;
		this.userBook = userBook;
		this.handlers = handlers;
	}
	
	private User currentUser;
	
	public void login() {
		String name;
		
		name = this.UIFacade.getName();
				
		//check if user is new user
		if(userBook.getUserBook().containsKey(name)) {
			this.currentUser = userBook.getUserBook().get(name);
		}
		else {
				currentUser = assignRole(name);
				userBook.addUser(currentUser);
		}
	}
	
	private User assignRole(String name) {
		String role = this.UIFacade.chooseRole();
		if (role.equalsIgnoreCase("garageholder"))
			return new GarageHolder(name);
		else if (role.equalsIgnoreCase("manager"))
			return new Manager(name);
		else if (role.equalsIgnoreCase("worker"))
			return new Worker(name);
		else { // this is never reached
			return null;
		}
	}
	
	//laat de ingelogde user doen wat hij zou kunnen doen
	public void performDuties() throws InvalidObjectException{
		UseCaseHandler useCaseHandler = currentUser.getRightHandler(handlers);
	
		if(useCaseHandler == null) {
			throw new InvalidObjectException("");
		}
		else {
			useCaseHandler.executeUseCase(currentUser);
		}
	}
	
	public void logOut(){
		this.currentUser = null;
	}
}
