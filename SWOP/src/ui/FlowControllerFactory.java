package ui;

import java.util.ArrayList;

import facade.Facade;

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
		flowControllers.add(new AdvanceAssemblyLineFlowController("Advance assemblyline", iClientCommunication, facade));
		flowControllers.add(new AssembleFlowController("Complete assembly tasks", iClientCommunication, facade));
		flowControllers.add(new OrderFlowController("Order", iClientCommunication, facade));
		return flowControllers;
	}
	
	
}
