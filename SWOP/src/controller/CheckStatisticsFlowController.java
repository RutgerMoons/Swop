package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class CheckStatisticsFlowController extends UseCaseFlowController {

	public CheckStatisticsFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, ImmutableException {
		clientCommunication.showStatistics(facade.getStatistics());

	}

}
