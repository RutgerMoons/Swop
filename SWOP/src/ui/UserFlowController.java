package ui;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import exception.RoleNotYetAssignedException;
import facade.IFacade;

/**
 * 
 * Defines the program flow associated with user-login.
 * In between login and logout it calls executes the proper UseCaseHandler, depending on which user has logged in.
 *
 */
public class UserFlowController {
	
	private ArrayList<UseCaseFlowController> useCaseFlowControllers;
	private IFacade iFacade;
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
	public UserFlowController(IClientCommunication iClientCommunication, IFacade iFacade, ArrayList<UseCaseFlowController> useCaseFlowControllers) throws NullPointerException{
		if(iClientCommunication == null || iFacade == null || useCaseFlowControllers == null) {
			throw new NullPointerException();
		}
			
		this.ClientCommunication = iClientCommunication;
		this.iFacade = iFacade;
		this.useCaseFlowControllers = useCaseFlowControllers;
	}
	
	/**
	 * Assign an existing user to the current user (identify the existing user by the name the user gives),
	 * or create a new user with the given name and assign that user to the current user.
	 */
	public void login() {
		String name = this.ClientCommunication.getName();
		try {		
			iFacade.login(name);
		} catch (RoleNotYetAssignedException r) {
			String role = this.ClientCommunication.chooseRole();
			iFacade.createAndAddUser(name, role);
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
		iFacade.logout();
	}
	
	/**
	 * Have the user execute the use case he has the authorization to execute. 
	 * @throws InvalidObjectException
	 * 			If the user doesn't have authorization to execute any of the use cases.
	 */
	public void performDuties(){
		List<String> accessRights = iFacade.getAccessRights();
		UseCaseFlowController useCaseHandler = selectUseCaseFlowController(accessRights, useCaseFlowControllers);
		
		if(useCaseHandler == null) {
			throw new IllegalArgumentException();
		}
		else {
			useCaseHandler.executeUseCase();
		}
	}

	private UseCaseFlowController selectUseCaseFlowController(List<String> accessRights, ArrayList<UseCaseFlowController> flowControllers) {
		int index = ClientCommunication.getFlowControllerIndex(accessRights);
	
		String accessRight = accessRights.get(index-1);
		for(UseCaseFlowController flowController : flowControllers){
			if(flowController.getAccessRight().equals(accessRight)){
				return flowController;
			}
		}
		return null;
	}
}
