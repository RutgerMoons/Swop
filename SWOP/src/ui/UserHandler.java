package ui;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import exception.RoleNotYetAssignedException;
import facade.IFacade;
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
	
	private UIFacade UIFacade;
	private IFacade iFacade;
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
	public UserHandler(UIFacade UIFacade, IFacade iFacade, ArrayList<UseCaseHandler> handlers) throws NullPointerException{
		if(UIFacade == null || iFacade == null || handlers == null) {
			throw new NullPointerException();
		}
			
		this.UIFacade = UIFacade;
		this.iFacade = iFacade;
		this.handlers = handlers;
	}
	
	/**
	 * Assign an existing user to the current user (identify the existing user by the name the user gives),
	 * or create a new user with the given name and assign that user to the current user.
	 */
	public void login() {
		String name = this.UIFacade.getName();
		try {		
			iFacade.login(name);
		} catch (RoleNotYetAssignedException r) {
			String role = this.UIFacade.chooseRole();
			iFacade.createAndAddUser(name, role);
		} catch (IllegalArgumentException i) {
			UIFacade.invalidAnswerPrompt();
			login();
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
	public void performDuties(){
		UseCaseHandler useCaseHandler = currentUser.getRightHandler(handlers);
	
		if(useCaseHandler == null) {
			throw new IllegalArgumentException();
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
