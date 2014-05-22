package controller;

import view.IClientCommunication;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.users.AccessRight;

/**
 * A class representing the order of execution for the 'Change operational status' use case.
 */
public class ChangeOperationalStatusFlowController extends
UseCaseFlowController {

	/**
	 * Construct a new ChangeOperationalStatusFlowController.
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
	public ChangeOperationalStatusFlowController(AccessRight accessRight,
			IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	/**
	 * Executes the use case meaning :
	 * 1. show all AssemblyLines and let the user choose one
	 * 2. show the status of the chosen AssemblyLine
	 * 3. possibly change the AssemblyLineState of the chosen AssemblyLine
	 */
	@Override
	public void executeUseCase() {
		IAssemblyLine assemblyLine = clientCommunication.chooseAssemblyLine(facade.getAssemblyLines());
		clientCommunication.showStatus(assemblyLine.getState());
		if(clientCommunication.askContinue()){
			AssemblyLineState state = clientCommunication.chooseStatus(facade.getAssemblyLineStates(), assemblyLine.getState());
			ImmutableClock clock = this.retrieveElapsedTime();
			facade.changeState(assemblyLine, state, clock);
		}
	}
	
	/**
	 * Asks the user how much time he needed for completing the task and returns an
	 * ImmutableClock.
	 */
	public ImmutableClock retrieveElapsedTime(){
		int time = clientCommunication.getElapsedTime();
		int days = time/Clock.MINUTESINADAY;
		int minutes = time%Clock.MINUTESINADAY;
		return new ImmutableClock(days, minutes);
	}
}