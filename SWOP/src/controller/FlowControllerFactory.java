package controller;

import java.util.ArrayList;

import view.ClientCommunication;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * A class representing the a way to create all the necessary FlowControllers. 
 *
 */
public class FlowControllerFactory {

	private final ClientCommunication iClientCommunication;
	private final Facade facade;
	
	/**
	 * Create a new FlowControllerFactory.
	 * 
	 * @param 	clientCommunication
	 * 			The clientCommunication that all the FlowControllers created by this factory will use to communicate with the user
	 * 
	 * @param 	facade
	 * 			The Facade that all the FlowControllers created by this factory will use to access domain logic
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when clientCommunication or facade is null
	 */
	public FlowControllerFactory(ClientCommunication clientCommunication, Facade facade) {
		if (clientCommunication == null || facade == null) {
			throw new IllegalArgumentException();
		}
		this.iClientCommunication = clientCommunication;
		this.facade = facade;
	}
	
	/**
	 * Create all the necessary FlowControllers and return them.
	 */
	public ArrayList<UseCaseFlowController> createFlowControllers() {
		ArrayList<UseCaseFlowController> flowControllers = new ArrayList<UseCaseFlowController>();
		flowControllers.add(new AssembleFlowController(AccessRight.ASSEMBLE, iClientCommunication, facade));
		flowControllers.add(new OrderFlowController(AccessRight.ORDER, iClientCommunication, facade));
		flowControllers.add(new ShowOrderDetailsFlowController(AccessRight.SHOWDETAILS, iClientCommunication, facade));
		flowControllers.add(new CustomOrderFlowController(AccessRight.CUSTOMORDER, iClientCommunication, facade));
		flowControllers.add(new CheckAssemblyLineFlowController(AccessRight.CHECKLINE, iClientCommunication, facade));
		flowControllers.add(new CheckStatisticsFlowController(AccessRight.STATISTICS, iClientCommunication, facade));
		flowControllers.add(new AdaptSchedulingAlgorithmFlowController(AccessRight.SWITCH_SCHEDULING_ALGORITHM,
																		iClientCommunication, facade));
		flowControllers.add(new ChangeOperationalStatusFlowController(AccessRight.SWITCH_OPERATIONAL_STATUS,
				iClientCommunication, facade));
		return flowControllers;
	}
}