package view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.FlowControllerFactory;
import controller.UseCaseFlowController;
import controller.UserFlowController;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;


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
	 * Initializes all data structures that the program needs from the start.
	 */ 
	public static void initializeData() {
		Set<BindingRestriction> bindingRestrictions = new HashSet<>();
		Set<OptionalRestriction> optionalRestrictions = new HashSet<>();
		optionalRestrictions.add(new OptionalRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), VehicleOptionCategory.SPOILER, false));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("performance 2.5l V6", VehicleOptionCategory.ENGINE)));
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("sport", VehicleOptionCategory.BODY), new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("ultra 3l V8", VehicleOptionCategory.ENGINE), new VehicleOption("manual", VehicleOptionCategory.AIRCO)));
		
		bindingRestrictions.add(new BindingRestriction(new VehicleOption("platform truck", VehicleOptionCategory.BODY), new VehicleOption("heavy duty", VehicleOptionCategory.WHEEL)));
		
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
			} catch (IllegalArgumentException | UnmodifiableException e) {
			}
			userFlowController.logout();
		} while (true);

	}

}
