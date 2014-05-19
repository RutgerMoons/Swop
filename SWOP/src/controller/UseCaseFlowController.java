package controller;

import view.IClientCommunication;
import domain.facade.Facade;
import domain.users.AccessRight;



/**
 * An abstract class representing the order of execution for a specific use case.
 */
public abstract class UseCaseFlowController {
	
	protected final AccessRight accessRight;
	protected IClientCommunication clientCommunication;
	protected Facade facade;
	
	/**
	 * Construct a new FlowController
	 * 
	 * @param 	accessRight
	 * 			The AccessRight needed to perform this use case
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user
	 * 
	 * @param 	facade
	 * 			The Facade this UseCaseFlowcontroller uses to access the domain logic
	 */
	public UseCaseFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade){
		if (accessRight == null || clientCommunication == null || facade == null) {
			throw new NullPointerException();
		}
		this.accessRight = accessRight;
		this.clientCommunication = clientCommunication;
		this.facade = facade;
	}
	
	/**
	 * Execute the use case
	 */
	public abstract void executeUseCase();

	/**
	 * Returns the AccesRight of this use case.
	 */
	public AccessRight getAccessRight() {
		return accessRight;
	}
	
}
