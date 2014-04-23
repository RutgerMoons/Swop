package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class CustomOrderFlowController extends UseCaseFlowController {

	public CustomOrderFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException,
	ImmutableException {
		customOrder();
	}

	private void customOrder() throws IllegalArgumentException, ImmutableException {
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
				}
			}
		}else{
			executeUseCase();
		}
	}

}
