package controller;

import view.ClientCommunication;
import domain.facade.Facade;
import domain.users.AccessRight;



/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseFlowController {
	
	protected final AccessRight accessRight;
	protected ClientCommunication clientCommunication;
	protected Facade facade;
	
	/**
	 * Construct a new FlowController
	 * 
	 * @param 	accessRight
	 * 				The accessRight needed to perform the use case
	 * 
	 * @param 	clientCommunication
	 * 				The IClientCommunication the FlowController uses to communicate with the user
	 * 
	 * @param 	facade
	 * 				The Facade the Flowcontroller uses to access the domain logic
	 */
	public UseCaseFlowController(AccessRight accessRight, ClientCommunication clientCommunication, Facade facade){
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

	public AccessRight getAccessRight() {
		return accessRight;
	}
	
}
