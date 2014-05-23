package controller.useCase;

import java.util.List;

import controller.UseCaseFlowController;
import view.IClientCommunication;
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
	public CheckStatisticsFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Execute the use case in some steps
	 * 1. show average of vehicles produced
	 * 2. show median of vehicles produced
	 * 3. show some details of some days
	 * 4. show average of delays
	 * 5. show median of delays
	 * 6. show some details of some delays
	 */
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