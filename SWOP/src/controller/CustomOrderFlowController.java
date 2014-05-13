package controller;

import java.util.List;

import view.ClientCommunication;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.users.AccessRight;
import domain.vehicle.vehicle.IVehicle;

/**
 * Defines the program flow for the 'Order single task' use case.
 *
 */
public class CustomOrderFlowController extends UseCaseFlowController {

	/**
	 * Construct a new CustomOrderFlowController.
	 * @param accessRight
	 * 			The accessRight needed to perform this use case.
	 * @param clientCommunication
	 * 			The IClientCommunication this FlowController uses to communicate with the user.
	 * @param facade
	 * 			The Facade this Flowcontroller uses to access the domain logic.
	 */
	public CustomOrderFlowController(AccessRight accessRight,
			ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() {
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