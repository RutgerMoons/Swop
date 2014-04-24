package controller;

import java.util.ArrayList;

import ui.IClientCommunication;


import domain.facade.Facade;
import domain.users.AccessRight;


public class FlowControllerFactory {

	private final IClientCommunication iClientCommunication;
	private final Facade facade;
	
	public FlowControllerFactory(IClientCommunication iClientCommunication, Facade facade) {
		if (iClientCommunication == null || facade == null) {
			throw new IllegalArgumentException();
		}
		this.iClientCommunication = iClientCommunication;
		this.facade = facade;
	}
	
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
