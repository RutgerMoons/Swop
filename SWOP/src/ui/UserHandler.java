package ui;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import users.GarageHolder;
import users.Manager;
import users.User;
import users.UserBook;
import users.Worker;

/**
 * 
 * Defines the program flow associated with user-login.
 * In between login and logout it calls executes the proper UseCaseHandler, depending on which user has logged in.
 *
 */
public class UserHandler {
	
	private UserBook userBook; 
	private UIFacade UIFacade;
	private ArrayList<UseCaseHandler> handlers;
	
	/**
	 * Creates a new UserHandler.
	 * @param UIFacade
	 * 			The UIfacade this UserHandler has to use to communicate with the user.
	 * @param userBook
	 * 			The UserBook this Userhandler uses to complete the login-process.
	 * @param handlers
	 * 			An ArrayList containing all the UseCaseHandlers.
	 * @throws NullPointerException
	 * 			if one of the given arguments is null.
	 */
	public UserHandler(UIFacade UIFacade, UserBook userBook, ArrayList<UseCaseHandler> handlers)throws NullPointerException{
		if(UIFacade == null || userBook == null || handlers == null) {
			throw new NullPointerException();
		}
			
		this.UIFacade = UIFacade;
		this.userBook = userBook;
		this.handlers = handlers;
	}
	
	/**
	 * User who has completed the login-process, and is being used as the current user.
	 */
	private User currentUser;
	
	/**
	 * Assign an existing user to the current user (identify the existing user by the name the user gives),
	 * or create a new user with the given name and assign that user to the current user.
	 */
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
	
	/**
	 * Create a new user of the type given by the user.
	 * @param name
	 * 			The new user's name.
	 * @return
	 * 			The newly created user.
	 */
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
	
	/**
	 * Have the user execute the use case he has the authorization to execute. 
	 * @throws InvalidObjectException
	 * 			If the user doesn't have authorization to execute any of the use cases.
	 */
	public void performDuties() throws InvalidObjectException{
		UseCaseHandler useCaseHandler = currentUser.getRightHandler(handlers);
	
		if(useCaseHandler == null) {
			throw new InvalidObjectException("");
		}
		else {
			useCaseHandler.executeUseCase(currentUser);
		}
	}
	
	/**
	 * Set the currentUser to null, indicating there is currently no user logged in.
	 */
	public void logOut(){
		this.currentUser = null;
	}
}
