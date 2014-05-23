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
	 * Execute the use case in some steps <br>
	 * 1. show the user's pending orders <br>
	 * 2. show the user's completed orders <br>
	 * 3. possibly obtaining a new order <br>
	 * 3.1. choose the VehicleSpecification <br>
	 * 3.2. choose the VehicleOptions for the new Vehicle <br>
	 * 3.3. choose how many Vehicles there have to be ordered <br> 
	 * 3.4. process the order or stop executing the use case <br>
	 */
	@Override
	public void executeUseCase() {
		this.showOrders();
		this.placeNewOrder();
	}

	private void showOrders() {
		List<IOrder> pendingOrders = facade.getPendingOrders();
		this.clientCommunication.showPendingOrders(pendingOrders);
		List<IOrder> completedOrders = facade.getCompletedOrders();
		this.clientCommunication.showCompletedOrders(completedOrders);

	}

	private void placeNewOrder(){
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