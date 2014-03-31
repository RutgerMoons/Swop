package ui;

import java.util.ArrayList;

import exception.NoCompletedOrdersException;
import exception.NoPendingOrdersException;
import facade.Facade;
import facade.IFacade;

/**
 * TODO : documentatie updaten
 * Defines the program flow for the 'Order New Car' use case.
 *
 */
public class OrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new OrderHandler
	 * @param iClientCommunication
	 * 			The UIfacade this OrderHandler has to use to communicate with the user.
	 */
	public OrderFlowController(String accessRight, IClientCommunication iClientCommunication, IFacade facade) {
		super(accessRight, iClientCommunication, facade);
	}

	/**
	 * Execute the use case.
	 */
	@Override
	public void executeUseCase() throws IllegalArgumentException{
		this.showOrders();
		placeNewOrder(); 
	}

	/**
	 * Shows the user's current pending (with estimated time of completion) and completed orders.
	 */
	public void showOrders(){
			ArrayList<String> pendingOrders = facade.getPendingOrders();
			this.clientCommunication.showPendingOrders(pendingOrders);
			ArrayList<String> completedOrders = facade.getCompletedOrders();
			this.clientCommunication.showCompletedOrders(completedOrders);

	}

	/**
	 * Retrieves all the needed input of the user for processing an order.
	 * All this information it gives to the iFacade.
	 */
	public void placeNewOrder(){
		if(!this.clientCommunication.askContinue()) {
			this.executeUseCase();
		}
		else{
			String model = clientCommunication.chooseModel(facade.getCarModels());
			// Om ��n of andere reden vind ie het niet nodig om de IllegalArgument te catchen?
			String realModel = facade.getCarModelFromCatalogue(model);
			int quantity = clientCommunication.getQuantity();
			int[] estimatedTime = new int[1];
			estimatedTime[0] = -1;
			clientCommunication.showOrder(quantity, realModel, estimatedTime);
			if(!this.clientCommunication.askContinue()){
				this.executeUseCase();
			}
			else{
				int[] time = facade.processOrder(realModel, quantity);
				clientCommunication.showOrder(quantity, realModel, time);
			}
		}
	}
}
