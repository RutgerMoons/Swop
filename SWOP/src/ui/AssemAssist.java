package ui;

import java.util.ArrayList;

import facade.Facade;
import facade.IFacade;

/**
 * This is the class where the program starts running.
 * 
 */
public class AssemAssist {
	
	private static IFacade iFacade;
	private static ArrayList<UseCaseFlowController> flowControllers;
	private static UserFlowController userFlowController;
	private static ClientCommunication clientCommunication;
	

	/**
	 * Initializes all datastructures that the program needs from the start.
	 */
	public static void initializeData() {

		clientCommunication = new ClientCommunication();
		iFacade = new Facade();
		FlowControllerFactory flowControllerFactory = new FlowControllerFactory(clientCommunication);
		flowControllers = flowControllerFactory.createFlowControllers();
		userFlowController = new UserFlowController(clientCommunication, iFacade, flowControllers);
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
			userFlowController.performDuties();
			userFlowController.logout();
		} while (true);

	}

}
