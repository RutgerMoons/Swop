package ui;

import facade.IFacade;


/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseFlowController {
	
	protected final String accessRight;
	protected IClientCommunication clientCommunication;
	protected IFacade facade;
	
	public UseCaseFlowController(String accessRight, IClientCommunication clientCommunication, IFacade iFacade){
		if (accessRight == null || clientCommunication == null || iFacade == null) {
			throw new NullPointerException();
		}
		this.accessRight = accessRight;
		this.clientCommunication = clientCommunication;
		this.facade = iFacade;
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
