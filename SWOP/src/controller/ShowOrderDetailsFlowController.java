package controller;

import view.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * Defines the program flow for the 'Show order details' use case.
 *
 */
public class ShowOrderDetailsFlowController extends UseCaseFlowController{

	/**
	 * Construct a new ShowOrderDetailsFlowController.
	 * @param accessRight
	 * 			The accessRight needed to perform this use case.
	 * @param clientCommunication
	 * 			The IClientCommunication this FlowController uses to communicate with the user.
	 * @param facade
	 * 			The Facade this Flowcontroller uses to access the domain logic.
	 */
	public ShowOrderDetailsFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException,ImmutableException {
		checkOrderDetails();
	}

	/**
	 * Get the details of the order the user chose and show them to the user.
	 */
	public void checkOrderDetails() throws IllegalArgumentException, ImmutableException {
		String order = clientCommunication.chooseOrder(facade.getPendingOrders(), facade.getCompletedOrders());
		if(!order.equals("No Order")){
			clientCommunication.showOrderDetails(facade.getOrderDetails(order));

			if(clientCommunication.askContinue()){
				executeUseCase();
			}
		}
	}


}
