package ui;

import java.util.ArrayList;

public class FlowControllerFactory {

	private final IClientCommunication iClientCommunication;
	public FlowControllerFactory(IClientCommunication iClientCommunication) {
		this.iClientCommunication = iClientCommunication;
	}
	
	public ArrayList<UseCaseFlowController> createFlowControllers() {
		ArrayList<UseCaseFlowController> flowControllers = new ArrayList<UseCaseFlowController>();
		flowControllers.add(new AdvanceAssemblyLineFlowController(iClientCommunication, "Advance assemblyLine"));
		flowControllers.add(new AssembleFlowController(iClientCommunication, "Complete assembly tasks"));
		flowControllers.add(new OrderFlowController(iClientCommunication, "Order"));
		return flowControllers;
	}
	
	
}
