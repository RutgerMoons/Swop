package controller;

import java.util.List;

import view.ClientCommunication;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.order.Delay;
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
	public CheckStatisticsFlowController(AccessRight accessRight, ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, UnmodifiableException {
		//clientCommunication.showStatistics(facade.getStatistics());
		// Show average amount of vehicles produced in a day
		int averageDays = facade.getAverageDays();
		clientCommunication.showAverageDays(averageDays);
		int medianDays = facade.getMedianDays();
		clientCommunication.showMedianDays(medianDays);
		List<Integer> detailedDays = facade.getDetailedDays();
		clientCommunication.showDetailsDays(detailedDays);
		int averageDelays = facade.getAverageDelays();
		clientCommunication.showAverageDelays(averageDelays);
		int medianDelays = facade.getMedianDelays();
		clientCommunication.showMedianDelays(medianDelays);
		List<Delay> detailedDelays = facade.getDetailedDelays();
		clientCommunication.showDetailedDelays(detailedDelays);
		
	}

}
