package controller;

import view.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * Defines the program flow for the 'Check assemby line status' use case.
 *
 */
public class CheckAssemblyLineFlowController extends UseCaseFlowController{

	/**
	 * Construct a new CheckAssemblyLineFlowController.
	 * @param accessRight
	 * 			The accessRight needed to perform this use case.
	 * @param clientCommunication
	 * 			The IClientCommunication this FlowController uses to communicate with the user.
	 * @param facade
	 * 			The Facade this Flowcontroller uses to access the domain logic.
	 */
	public CheckAssemblyLineFlowController(AccessRight accessRight,IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	
	@Override
	public void executeUseCase() throws IllegalArgumentException,ImmutableException {
		this.clientCommunication.showAssemblyLine(facade.getAssemblyLineAsString(), "current");		
	}
}
