package ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ui.ClientCommunication;
import controller.FlowControllerFactory;
import controller.UseCaseFlowController;
import controller.UserFlowController;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;


/**
 * This is the class where the program starts running.
 * 
 */
public class AssemAssist {
	
	private static Facade facade;
	private static ArrayList<UseCaseFlowController> flowControllers;
	private static UserFlowController userFlowController;
	private static ClientCommunication clientCommunication;
	

	/**
	 * Initializes all datastructures that the program needs from the start.
	 */ 
	public static void initializeData() {
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new CarOption("sport", CarOptionCategory.BODY), CarOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("performance 2.5l V6", CarOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new CarOption("sport", CarOptionCategory.BODY), new CarOption("ultra 3l V8", CarOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new CarOption("ultra 3l V8", CarOptionCategory.ENGINE), new CarOption("manual", CarOptionCategory.AIRCO)));
		
		clientCommunication = new ClientCommunication();
		facade = new Facade(bindingRestrictions, optionalRestrictions);
		FlowControllerFactory flowControllerFactory = new FlowControllerFactory(clientCommunication, facade);
		flowControllers = flowControllerFactory.createFlowControllers();
		userFlowController = new UserFlowController(clientCommunication, facade, flowControllers);
	}

	public static void main(String[] args) {
		initializeData();
		startProgram();
	}

	/**
	 * Starts the program.
	 */
	public static void startProgram() {
		do {
			userFlowController.login();
			try {
				userFlowController.performDuties();
			} catch (IllegalArgumentException | ImmutableException e) {
			}
			userFlowController.logout();
		} while (true);

	}

}
