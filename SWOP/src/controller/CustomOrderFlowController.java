package controller;

import view.IClientCommunication;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.facade.Facade;
import domain.users.AccessRight;

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
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException,
	UnmodifiableException {
		customOrder();
	}

	/**
	 * Place a new CustomOrder, as described by the user.
	 */
	private void customOrder() throws IllegalArgumentException, UnmodifiableException {
		String customTaskDescription = clientCommunication.showCustomTasks(facade.getCustomTasks());

		String model = clientCommunication.showCustomTasks(facade.getSpecificCustomTasks(customTaskDescription));

		String deadline = clientCommunication.askDeadline();

		if(clientCommunication.askContinue()){
			String time = facade.processCustomOrder(model, deadline);
			clientCommunication.showCustomOrder(time);
			if (facade.canAssemblyLineAdvance()) {
				try {
					facade.advanceAssemblyLine();
				} catch (NoSuitableJobFoundException n) {
					//no problem :)
				} catch (IllegalArgumentException e){
					executeUseCase();
				}
			}
		}else{
			executeUseCase();
		}
	}

}