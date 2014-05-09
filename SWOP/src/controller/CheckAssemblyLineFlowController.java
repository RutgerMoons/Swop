package controller;

import java.util.List;
import java.util.ArrayList;

import view.ClientCommunication;
import domain.assembly.IAssemblyLine;
import domain.exception.UnmodifiableException;
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
	public CheckAssemblyLineFlowController(AccessRight accessRight,ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	
	@Override
	public void executeUseCase() throws IllegalArgumentException,UnmodifiableException {
		List<IAssemblyLine> allAssemblyLines = facade.getAssemblyLines();
		IAssemblyLine chosenAssemblyLine = clientCommunication.chooseAssemblyLine(allAssemblyLines);
		this.clientCommunication.showAssemblyLine(chosenAssemblyLine);		
	}
}
