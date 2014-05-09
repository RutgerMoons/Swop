package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import view.ClientCommunication;
import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.order.IOrder;
import domain.users.AccessRight;
import domain.vehicle.IVehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

/**
 * Defines the program flow for the 'Order New Car' use case.
 * 
 */
public class OrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new OrderFlowController
	 * 
	 * @param iClientCommunication
	 *            The UIfacade this OrderFlowController has to use to communicate with
	 *            the user.
	 */
	public OrderFlowController(AccessRight accessRight,
			ClientCommunication iClientCommunication, Facade facade) {
		super(accessRight, iClientCommunication, facade);
	}

	/**
	 * Execute the use case.
	 * 
	 * @throws UnmodifiableException
	 */
	@Override
	public void executeUseCase() throws IllegalArgumentException,
			UnmodifiableException {
		this.showOrders();
		placeNewOrder();
	}

	/**
	 * Shows the user's current pending (with estimated time of completion) and
	 * completed orders.
	 */
	public void showOrders() {
		List<IOrder> pendingOrders = facade.getPendingOrders();
		this.clientCommunication.showPendingOrders(pendingOrders);
		List<IOrder> completedOrders = facade.getCompletedOrders();
		this.clientCommunication.showCompletedOrders(completedOrders);

	}

	/**
	 * Retrieves all the needed input of the user for processing an order. All
	 * this information it gives to the iFacade.
	 * 
	 * @throws UnmodifiableException
	 */
	public void placeNewOrder() throws UnmodifiableException {
		if (!this.clientCommunication.askContinue()) {
			return;
		} else {
			String model = clientCommunication.chooseModel(facade
					.getCarModelSpecifications());
			VehicleSpecification realModel = facade.getCarModelSpecificationFromCatalogue(model);
			facade.createNewModel(realModel);

			List<IVehicleOption> chosenParts = createModel();

			int quantity = clientCommunication.getQuantity();
			String estimatedTime = "";
			clientCommunication.showOrder(quantity, realModel, chosenParts,
					estimatedTime);
			if (!this.clientCommunication.askContinue()) {
				return;
			} else {
				ImmutableClock time = null;
				try {
					time = facade.processOrder(quantity);
				} catch (IllegalStateException | NotImplementedException e) {
					clientCommunication.showInvalidModel();
					placeNewOrder();
				}
				clientCommunication.showOrder(quantity, realModel, chosenParts,
						time);
				if (facade.canAssemblyLineAdvance()) {
					try {
						facade.advanceAssemblyLine();
					} catch (NoSuitableJobFoundException n) {
						//no problem :)
					}
				}
			}
		}
	}

	private List<IVehicleOption> createModel() {
		List<IVehicleOption> chosenParts = new ArrayList<>();
		for (VehicleOptionCategory type : facade.getCarPartTypes()) {
			Set<IVehicleOption> parts = facade.getParts(type);
			if (!parts.isEmpty()) {
				IVehicleOption part = clientCommunication.choosePart(parts);
				facade.addPartToModel(part);
				if (!part.equals("Select nothing")) {
					chosenParts.add(part);
				}
			}
		}
		return chosenParts;

	}
}
