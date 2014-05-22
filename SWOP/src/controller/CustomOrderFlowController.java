package controller;

import java.util.List;

import view.IClientCommunication;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.users.AccessRight;
import domain.vehicle.vehicle.IVehicle;

/**
 * A class representing the order of execution for the 'Order single task' use case.
 *
 */
public class CustomOrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new CustomOrderFlowController.
	 * 
	 * @param 	accessRight
	 * 			The AccessRight needed to perform this use case
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user
	 * 
	 * @param 	facade
	 * 			The Facade this UseCaseFlowcontroller uses to access the domain logic
	 */
	public CustomOrderFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Execute the use case in some steps
	 * 1. show all pending orders of the user
	 * 2. show all completed orders of the user
	 * 3. show all the custom tasks
	 * 4. let the user order an custom order
	 * 5. ask if the user is sure that he wants to order
	 * 6. process the order or stop executing the use case
	 */
	@Override
	public void executeUseCase() {
		clientCommunication.showPendingOrders(facade.getPendingOrders());
		clientCommunication.showCompletedOrders(facade.getCompletedOrders());
		
		String customTaskDescription = clientCommunication.showCustomTasks(facade.getCustomTasks());
		List<IVehicle> vehicles = facade.getCustomOptions(customTaskDescription);
		IVehicle model = clientCommunication.showSpecificCustomTasks(vehicles);

		int deadlineInMinutes = clientCommunication.askDeadline();
		int days = deadlineInMinutes/Clock.MINUTESINADAY;
		int minutes = deadlineInMinutes%Clock.MINUTESINADAY;
		ImmutableClock deadline = new ImmutableClock(days, minutes);
		
		if(clientCommunication.askContinue()){
			ImmutableClock time = facade.processCustomOrder(model, deadline);
			clientCommunication.showCustomOrder(time);
		}else{
			executeUseCase();
		}
	}

}