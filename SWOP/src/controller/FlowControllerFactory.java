package controller;

import java.util.ArrayList;

import view.ClientCommunication;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * This class is used to create all the necessary FlowControllers. 
 *
 */
public class FlowControllerFactory {

	private final ClientCommunication iClientCommunication;
	private final Facade facade;
	
	/**
	 * Create a new FlowControllerFactory.
	 * @param iClientCommunication
	 * 			The IClientCommunication that all the FlowControllers created by this factory will use to communicate with the  user.
	 * @param facade
	 * 			The Facade that all the FlowControllers created by this factory will use to access domain logic.
	 */
	public FlowControllerFactory(ClientCommunication iClientCommunication, Facade facade) {
		if (iClientCommunication == null || facade == null) {
			throw new IllegalArgumentException();
		}
		this.iClientCommunication = iClientCommunication;
		this.facade = facade;
	}
	
	/**
	 * Create all the necesarry FlowControllers and return them.
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
		return flowControllers;
	}
	
	
}
