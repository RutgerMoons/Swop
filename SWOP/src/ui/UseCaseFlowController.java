package ui;

import facade.Facade;



/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseFlowController {
	
	protected final String accessRight;
	protected IClientCommunication clientCommunication;
	protected Facade facade;
	
	public UseCaseFlowController(String accessRight, IClientCommunication clientCommunication, Facade facade){
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
	 */
	public abstract void executeUseCase();

	public String getAccessRight() {
		return accessRight;
	}
	
}
