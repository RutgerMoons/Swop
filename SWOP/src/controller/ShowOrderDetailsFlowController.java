package controller;

import com.google.common.base.Optional;

import view.ClientCommunication;
import domain.facade.Facade;
import domain.order.IOrder;
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
			ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() {
		Optional<IOrder> order = clientCommunication.chooseOrder(facade.getPendingOrders(), facade.getCompletedOrders());

		if(order.isPresent()){
			clientCommunication.showOrderDetails(order.get());
		}
		if(clientCommunication.askContinue()){
			executeUseCase();
		}

	}


}
