package controller.useCase;

import java.util.List;

import controller.UseCaseFlowController;
import view.IClientCommunication;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * A class representing the order of execution for the 'Check assembly line status' use case.
 */
public class CheckAssemblyLineFlowController extends UseCaseFlowController{

	/**
	 * Construct a new CheckAssemblyLineFlowController.
	 * 
	 * @param 	accessRight
	 * 			The AccessRight needed to perform this use case
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user
	 * 
	 * @param 	facade
	 * 			The Facade this UseCaseFlowcontroller uses to access the domain logic
	 */
	public CheckAssemblyLineFlowController(AccessRight accessRight,IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	
	@Override
	public void executeUseCase(){
		List<IAssemblyLine> allAssemblyLines = facade.getAssemblyLines();
		IAssemblyLine chosenAssemblyLine = clientCommunication.chooseAssemblyLine(allAssemblyLines);
		this.clientCommunication.showAssemblyLine(chosenAssemblyLine);		
	}
}
