package controller.useCase;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

import controller.UseCaseFlowController;
import view.IClientCommunication;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.order.order.IOrder;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing the order of execution for the 'Order New Car' use case.
 * 
 */
public class OrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new OrderFlowController
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
	public OrderFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Execute the use case. 
	 * 1. show user's pending orders
	 * 2. show user's completed orders
	 * 3. possibly obtaining a new order
	 */
	@Override
	public void executeUseCase() {
		this.showOrders();
		this.placeNewOrder();
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
	 * this information it gives to the facade.
	 */
	public void placeNewOrder(){
		if (!this.clientCommunication.askContinue()) {
			return;
		} else {
			String model = clientCommunication.chooseModel(facade
					.getVehicleSpecifications());
			VehicleSpecification realModel = facade.getVehicleSpecificationFromCatalogue(model);
			facade.createNewVehicle(realModel);

			List<VehicleOption> chosenParts = createModel();
			int quantity = clientCommunication.getQuantity();
			clientCommunication.showOrder(quantity, realModel, chosenParts);
			if (!this.clientCommunication.askContinue()) {
				return;
			} else {
				ImmutableClock time = null;
				try {
					time = facade.processOrder(quantity);
				} catch (IllegalStateException e) {
					clientCommunication.showInvalidModel();
					placeNewOrder();
				}
				clientCommunication.showOrder(quantity, realModel, chosenParts,
						time);
			}
		}
	}

	private List<VehicleOption> createModel() {
		List<VehicleOption> chosenParts = new ArrayList<>();
		for (VehicleOptionCategory type : facade.getVehicleOptionCategory()) {
			List<VehicleOption> parts = facade.getRemainingVehicleOptions(type);
			
			if (!parts.isEmpty()) {
				Optional<VehicleOption> part = clientCommunication.choosePart(parts);
				if (part.isPresent()) {
					facade.addPartToVehicle(part.get());
					chosenParts.add(part.get());
				}
			}
		}
		return chosenParts;

	}
}