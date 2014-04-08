package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;



/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseFlowController {
	
	protected final AccessRight accessRight;
	protected IClientCommunication clientCommunication;
	protected Facade facade;
	
	public UseCaseFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade){
		if (accessRight == null || clientCommunication == null || facade == null) {
			throw new NullPointerException();
		}
		this.accessRight = accessRight;
		this.clientCommunication = clientCommunication;
		this.facade = facade;
	}
	
	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 * @throws ImmutableException 
	 * @throws IllegalArgumentException 
	 */
	public abstract void executeUseCase() throws IllegalArgumentException, ImmutableException;

	public AccessRight getAccessRight() {
		return accessRight;
	}
	
}
