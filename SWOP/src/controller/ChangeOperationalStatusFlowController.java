package controller;

import view.ClientCommunication;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * Defines the program flow for the 'Change operational status' use case.
 * 
 */
public class ChangeOperationalStatusFlowController extends
UseCaseFlowController {

	public ChangeOperationalStatusFlowController(AccessRight accessRight,
			ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

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
