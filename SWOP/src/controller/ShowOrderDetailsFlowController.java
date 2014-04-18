package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class ShowOrderDetailsFlowController extends UseCaseFlowController{

	public ShowOrderDetailsFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException,
	ImmutableException {
		checkOrderDetails();
	}

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
