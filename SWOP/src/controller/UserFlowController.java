package controller;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import view.IClientCommunication;
import domain.exception.UnmodifiableException;
import domain.exception.RoleNotYetAssignedException;
import domain.facade.Facade;
import domain.users.AccessRight;


/**
 * 
 * Defines the program flow associated with user-login.
 * In between login and logout it calls executes the proper UseCaseHandler, depending on which user has logged in.
 *
 */
public class UserFlowController {
	
	private ArrayList<UseCaseFlowController> useCaseFlowControllers;
	private Facade facade;
	private IClientCommunication ClientCommunication;
	
	/**
	 * Creates a new UserHandler.
	 * @param iClientCommunication
	 * 			The UIfacade this UserHandler has to use to communicate with the user.
	 * @param userBook
	 * 			The UserBook this Userhandler uses to complete the login-process.
	 * @param useCaseFlowControllers
	 * 			An ArrayList containing all the UseCaseHandlers.
	 * @throws NullPointerException
	 * 			if one of the given arguments is null.
	 */
	public UserFlowController(IClientCommunication iClientCommunication, Facade facade, ArrayList<UseCaseFlowController> useCaseFlowControllers) throws NullPointerException{
		if(iClientCommunication == null || facade == null || useCaseFlowControllers == null) {
			throw new NullPointerException();
		}
			
		this.ClientCommunication = iClientCommunication;
		this.facade = facade;
		this.useCaseFlowControllers = useCaseFlowControllers;
	}
	
	/**
	 * Assign an existing user to the current user (identify the existing user by the name the user gives),
	 * or create a new user with the given name and assign that user to the current user.
	 */
	public void login() {
		String name = this.ClientCommunication.getName();
		try {		
			facade.login(name);
		} catch (RoleNotYetAssignedException r) {
			String role = this.ClientCommunication.chooseRole();
			facade.createAndAddUser(name, role);
		} catch (IllegalArgumentException i) {
			ClientCommunication.invalidAnswerPrompt();
			login();
		}
	}
	
	/**
	 * Set the currentUser to null, indicating there is currently no user logged in.
	 */
	public void logout(){
		ClientCommunication.logout();
		facade.logout();
	}
	
	/**
	 * Have the user execute the use case he has the authorization to execute. 
	 * @throws UnmodifiableException 
	 * @throws IllegalArgumentException 
	 * @throws InvalidObjectException
	 * 			If the user doesn't have authorization to execute any of the use cases.
	 */
	public void performDuties() throws IllegalArgumentException, UnmodifiableException{
		List<AccessRight> accessRights = facade.getAccessRights();
		UseCaseFlowController useCaseHandler = selectUseCaseFlowController(accessRights, useCaseFlowControllers);
		
		if(useCaseHandler == null) {
			throw new IllegalArgumentException();
		}
		else {
			useCaseHandler.executeUseCase();
		}
	}

	private UseCaseFlowController selectUseCaseFlowController(List<AccessRight> accessRights, ArrayList<UseCaseFlowController> flowControllers) {
		ArrayList<String> accessRightsAsStrings = new ArrayList<String>();
		for (AccessRight a : accessRights) {
			accessRightsAsStrings.add(a.toString());
		}
		int index = ClientCommunication.getFlowControllerIndex(accessRightsAsStrings);
	
		AccessRight accessRight = accessRights.get(index-1);
		for(UseCaseFlowController flowController : flowControllers){
			if(flowController.getAccessRight().equals(accessRight)){
				return flowController;
			}
		}
		return null;
	}
}
