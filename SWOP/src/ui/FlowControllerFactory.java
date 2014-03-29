package ui;

import java.util.ArrayList;

import facade.IFacade;

public class FlowControllerFactory {

	private final IClientCommunication iClientCommunication;
	private final IFacade facade;
	
	public FlowControllerFactory(IClientCommunication iClientCommunication, IFacade facade) {
		if (iClientCommunication == null || facade == null) {
			throw new IllegalArgumentException();
		}
		this.iClientCommunication = iClientCommunication;
		this.facade = facade;
	}
	
	public ArrayList<UseCaseFlowController> createFlowControllers() {
		ArrayList<UseCaseFlowController> flowControllers = new ArrayList<UseCaseFlowController>();
		flowControllers.add(new AdvanceAssemblyLineFlowController("Advance assemblyline", iClientCommunication, facade));
		flowControllers.add(new AssembleFlowController("Complete assembly tasks", iClientCommunication, facade));
		flowControllers.add(new OrderFlowController("Order", iClientCommunication, facade));
		return flowControllers;
	}
	
	
}
