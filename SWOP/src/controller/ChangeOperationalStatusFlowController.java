package controller;

import view.ClientCommunication;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * Defines the program flow for the 'Change operational status' use case.
 */
public class ChangeOperationalStatusFlowController extends
UseCaseFlowController {

	public ChangeOperationalStatusFlowController(AccessRight accessRight,
			ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Executes the use case meaning :
	 * 1. show all AssemblyLines and let the user choose one
	 * 2. show the status of the chosen AssemblyLine
	 * 3. possibly change the AssemblyLineState of the chosen AssemblyLine
	 */
	@Override
	public void executeUseCase() {
		IAssemblyLine assemblyLine = clientCommunication.chooseAssemblyLine(facade.getAssemblyLines());
		clientCommunication.showStatus(assemblyLine.getState());
		if(clientCommunication.askContinue()){
			AssemblyLineState state = clientCommunication.chooseStatus(facade.getAssemblyLineStates(), assemblyLine.getState());
			facade.changeState(assemblyLine, state);
		}
	}
}
