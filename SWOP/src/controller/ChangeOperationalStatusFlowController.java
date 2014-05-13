package controller;

import view.ClientCommunication;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.facade.Facade;
import domain.users.AccessRight;

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
		if(clientCommunication.askAdvance()){
			AssemblyLineState state = clientCommunication.chooseStatus(assemblyLine.getState());
			facade.changeState(assemblyLine, state);
		}


	}

}
