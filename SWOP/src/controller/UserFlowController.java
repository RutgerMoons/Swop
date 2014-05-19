package controller;

import java.util.ArrayList;
import java.util.List;

import view.IClientCommunication;
import domain.exception.RoleNotYetAssignedException;
import domain.facade.Facade;
import domain.users.AccessRight;


/**
 * A class representing the order of execution associated with logging a user in.
 * In between login and logout it calls executes the proper UseCaseFlowControllers, depending on which user has logged in.
 */
public class UserFlowController {
	
	private ArrayList<UseCaseFlowController> useCaseFlowControllers;
	private Facade facade;
	private IClientCommunication clientCommunication;
	
	/**
	 * Creates a new UserHandler.
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user
	 * 
	 * @param 	useCaseFlowControllers
	 * 			An ArrayList containing all the UseCaseFowControllers
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when one of the given arguments is null
	 */
	public UserFlowController(IClientCommunication clientCommunication, Facade facade, ArrayList<UseCaseFlowController> useCaseFlowControllers){
		if(clientCommunication == null || facade == null || useCaseFlowControllers == null) {
			throw new IllegalArgumentException();
		}
			
		this.clientCommunication = clientCommunication;
		this.facade = facade;
		this.useCaseFlowControllers = useCaseFlowControllers;
	}
	
	/**
	 * Login a user. Ask his name through clientCommunication and try to log him in. If this fails,
	 * the method is going to create the new user.
	 */
	public void login() {
		String name = this.clientCommunication.getName();
		try {		
			facade.login(name);
		} catch (RoleNotYetAssignedException r) {
			String role = this.clientCommunication.chooseRole();
			facade.createAndAddUser(name, role);
			facade.login(name);
		} catch (IllegalArgumentException i) {
			clientCommunication.invalidAnswerPrompt();
			login();
		}
	}
	
	/**
	 * Method for logging out.
	 */
	public void logout(){
		clientCommunication.logout();
		facade.logout();
	}
	
	/**
	 * Ask the user which one of his access rights it wants to use.
	 */
	public void performDuties(){
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
		int index = clientCommunication.getFlowControllerIndex(accessRightsAsStrings);
	
		AccessRight accessRight = accessRights.get(index-1);
		for(UseCaseFlowController flowController : flowControllers){
			if(flowController.getAccessRight().equals(accessRight)){
				return flowController;
			}
		}
		return null;
	}
}