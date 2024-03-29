package controller.useCase;

import com.google.common.base.Optional;

import controller.UseCaseFlowController;
import view.IClientCommunication;
import domain.facade.Facade;
import domain.order.order.IOrder;
import domain.users.AccessRight;

/**
 * A class representing the order of execution for the 'Show order details' use case.
 */
public class ShowOrderDetailsFlowController extends UseCaseFlowController{

	/**
	 * Construct a new ShowOrderDetailsFlowController.
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
	public ShowOrderDetailsFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Execute the use case in some steps <br>
	 * 1. show all pending and completed orders <br>
	 * 2. let user choose an order he want to look at in detail <br>
	 * 3. show the details of the chosen order <br>
	 */
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
