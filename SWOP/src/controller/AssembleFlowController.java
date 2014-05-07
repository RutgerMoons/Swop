package controller;

import java.util.ArrayList;

import view.IClientCommunication;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.facade.Facade;
import domain.users.AccessRight;


/**
 * Defines the program flow for the 'Perform Assembly Tasks' use case.
 *
 */
public class AssembleFlowController extends UseCaseFlowController {

	/**
	 * Construct a new AssembleFlowController.
	 * @param accessRight
	 * 			The AccessRight needed to perform this use case.
	 * @param iClientCommunication
	 * 			The IClientCommpunication this AssembleFlowController has to use to communicate with the user.
	 * @param facade 
	 * 			The Facade this FlowController uses to access the domain logic.
	 */
	public AssembleFlowController(AccessRight accessRight, IClientCommunication iClientCommunication, Facade facade) {
		super(accessRight, iClientCommunication, facade);
	}


	/**
	 * Execute the use case.
	 */
	@Override
	public void executeUseCase(){
		chooseWorkBench();
	} 

	/**
	 * Get the workbench at which the user wants to perform tasks.
	 */
	public void chooseWorkBench(){
		ArrayList<String> workbenches = facade.getWorkBenchNames();
		int workbenchIndex = this.clientCommunication.chooseWorkBench(workbenches.size(), workbenches) - 1;
		chooseTask(workbenchIndex);
	}

	/**
	 * Let the user choose and perform a task from the tasks at the workbench he has previously chosen.
	 * 
	 */
	public void chooseTask(int workbenchIndex){
		ArrayList<String> tasksAtWorkbench = facade.getTasksOfChosenWorkBench(workbenchIndex);
		if(tasksAtWorkbench.isEmpty()){
			clientCommunication.showWorkBenchCompleted();
			if(clientCommunication.askContinue())
				executeUseCase();
		}
		else{	
			int chosenTaskNumber = this.clientCommunication.chooseTask(tasksAtWorkbench)-1;
			this.clientCommunication.showChosenTask(tasksAtWorkbench.get(chosenTaskNumber).toString());
			if(this.clientCommunication.askContinue()){
				try {
					int time = clientCommunication.getElapsedTime();
					facade.completeChosenTaskAtChosenWorkBench(workbenchIndex, chosenTaskNumber, time);
				} catch (UnmodifiableException e) {
				}
				catch ( NoSuitableJobFoundException e ){
					chooseTask(workbenchIndex);
				}
				chooseTask(workbenchIndex);
			}
		}
	}
}
