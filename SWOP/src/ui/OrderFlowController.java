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
public class OrderFlowController extends UseCaseFlowController{

	private IClientCommunication clientCommunication;
	private IFacade iFacade;

	/**
	 * Construct a new OrderHandler
	 * @param iClientCommunication
	 * 			The UIfacade this OrderHandler has to use to communicate with the user.
	 */
	public OrderFlowController (IClientCommunication iClientCommunication, Facade facade,  String accessRight){
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
		try {
			ArrayList<String> pendingOrders = iFacade.getPendingOrders();
			this.clientCommunication.showPendingOrders(pendingOrders);
			ArrayList<String> completedOrders = iFacade.getCompletedOrders();
			this.clientCommunication.showCompletedOrders(completedOrders);
		} catch (NoPendingOrdersException e) {
			this.clientCommunication.showPendingOrders(null);
		} catch (NoCompletedOrdersException e){
			this.clientCommunication.showCompletedOrders(null);
		}
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
			String model = clientCommunication.chooseModel(iFacade.getCarModels());
			// Om ��n of andere reden vind ie het niet nodig om de IllegalArgument te catchen?
			String realModel = iFacade.getCarModelFromCatalogue(model);
			int quantity = clientCommunication.getQuantity();
			int[] estimatedTime = new int[1];
			estimatedTime[0] = -1;
			clientCommunication.showOrder(quantity, realModel, estimatedTime);
			if(!this.clientCommunication.askContinue()){
				this.executeUseCase();
			}
			else{
				int[] time = iFacade.processOrder(realModel, quantity);
				clientCommunication.showOrder(quantity, realModel, time);
			}
		}
	}
}
