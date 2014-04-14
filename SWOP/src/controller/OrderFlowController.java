package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * TODO : documentatie updaten Defines the program flow for the 'Order New Car'
 * use case.
 * 
 */
public class OrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new OrderHandler
	 * 
	 * @param iClientCommunication
	 *            The UIfacade this OrderHandler has to use to communicate with
	 *            the user.
	 */
	public OrderFlowController(AccessRight accessRight,
			IClientCommunication iClientCommunication, Facade facade) {
		super(accessRight, iClientCommunication, facade);
	}

	/**
	 * Execute the use case.
	 * 
	 * @throws ImmutableException
	 */
	@Override
	public void executeUseCase() throws IllegalArgumentException,
			ImmutableException {
		this.showOrders();
		placeNewOrder();
	}

	/**
	 * Shows the user's current pending (with estimated time of completion) and
	 * completed orders.
	 */
	public void showOrders() {
		ArrayList<String> pendingOrders = facade.getPendingOrders();
		this.clientCommunication.showPendingOrders(pendingOrders);
		ArrayList<String> completedOrders = facade.getCompletedOrders();
		this.clientCommunication.showCompletedOrders(completedOrders);

	}

	/**
	 * Retrieves all the needed input of the user for processing an order. All
	 * this information it gives to the iFacade.
	 * 
	 * @throws ImmutableException
	 */
	public void placeNewOrder() throws ImmutableException {
		if (!this.clientCommunication.askContinue()) {
			this.executeUseCase();
		} else {
			String model = clientCommunication.chooseModel(facade
					.getCarModels());
			// Om ��n of andere reden vind ie het niet nodig om de
			// IllegalArgument te catchen?
			String realModel = facade.getCarModelFromCatalogue(model);
			facade.createNewModel(realModel);

			List<String> chosenParts = createModel();

			int quantity = clientCommunication.getQuantity();
			String estimatedTime = "";
			clientCommunication.showOrder(quantity, realModel, chosenParts,
					estimatedTime);
			if (!this.clientCommunication.askContinue()) {
				this.executeUseCase();
			} else {
				String time = "";

				try {
					time = facade.processOrder(realModel, quantity);
				} catch (IllegalStateException | NotImplementedException e) {
					clientCommunication.showInvalidModel();
					placeNewOrder();
				}
				clientCommunication.showOrder(quantity, realModel, chosenParts,
						time);
			}
		}
	}

	private List<String> createModel() {
		List<String> chosenParts = new ArrayList<>();
		for (String type : facade.getCarPartTypes()) {
			Set<String> parts = facade.getParts(type);
			if (!parts.isEmpty()) {
				String part = clientCommunication.choosePart(parts);
				facade.addPartToModel(type, part);
				if (!part.equals("Select nothing")) {
					chosenParts.add(part);
				}
			}
		}
		return chosenParts;

	}
}
