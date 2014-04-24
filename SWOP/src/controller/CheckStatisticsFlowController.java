package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * Defines the program flow for the 'Check production statistics' use case.
 *
 */
public class CheckStatisticsFlowController extends UseCaseFlowController {

	/**
	 * Construct a new CheckStatisticsFlowController.
	 * @param accessRight
	 * 			The accessRight needed to perform this use case.
	 * @param clientCommunication
	 * 			The IClientCommunication this FlowController uses to communicate with the user.
	 * @param facade
	 * 			The Facade this Flowcontroller uses to access the domain logic.
	 */
	public CheckStatisticsFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, ImmutableException {
		clientCommunication.showStatistics(facade.getStatistics());

	}

}
