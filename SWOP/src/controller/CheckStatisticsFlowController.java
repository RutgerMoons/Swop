package controller;

import java.util.List;

import view.ClientCommunication;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.order.Delay;
import domain.users.AccessRight;

/**
 *  A class representing the order of execution for the 'Check production statistics' use case.
 */
public class CheckStatisticsFlowController extends UseCaseFlowController {

	/**
	 * Construct a new CheckStatisticsFlowController.
	 * 
	 * @param 	accessRight
	 * 			The AccessRight needed to perform this use case.
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user.
	 * 
	 * @param 	facade
	 * 			The Facade this UseCaseFlowcontroller uses to access the domain logic.
	 */
	public CheckStatisticsFlowController(AccessRight accessRight, ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, UnmodifiableException {
		// Show average amount of vehicles produced in a day
		int averageDays = facade.getAverageDays();
		clientCommunication.showAverageDays(averageDays);
		// Show median amount of vehicles produced in a day
		int medianDays = facade.getMedianDays();
		clientCommunication.showMedianDays(medianDays);
		// The amount of cars produced for a number amount of days
		List<Integer> detailedDays = facade.getDetailedDays();
		clientCommunication.showDetailsDays(detailedDays);
		// The average delay on an order
		int averageDelays = facade.getAverageDelays();
		clientCommunication.showAverageDelays(averageDelays);
		// The median delay on an order
		int medianDelays = facade.getMedianDelays();
		clientCommunication.showMedianDelays(medianDelays);
		// The details of the latest delays.
		List<Delay> detailedDelays = facade.getDetailedDelays();
		clientCommunication.showDetailedDelays(detailedDelays);
	}
}