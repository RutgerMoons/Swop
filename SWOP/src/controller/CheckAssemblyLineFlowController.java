package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class CheckAssemblyLineFlowController extends UseCaseFlowController{

	public CheckAssemblyLineFlowController(AccessRight accessRight,IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException,ImmutableException {
		this.clientCommunication.showAssemblyLine(facade.getAssemblyLineAsString(), "current");		
	}
}
