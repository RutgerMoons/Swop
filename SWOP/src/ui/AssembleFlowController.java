package ui;

import java.util.ArrayList;

import facade.Facade;

/**
 * TODO : update documentatie
 * Defines the program flow for the 'Perform Assembly Tasks' use case.
 *
 */
public class AssembleFlowController extends UseCaseFlowController {

	/**
	 * Construct a new AssembleHandler.
	 * @param iClientCommunication
	 * 			The UIfacade this AssembleHandler has to use to communicate with the user.
	 */
	public AssembleFlowController(String accessRight, IClientCommunication iClientCommunication, Facade facade) {
		super(accessRight, iClientCommunication, facade);
	}


	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
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
			this.clientCommunication.askFinished();
			facade.completeChosenTaskAtChosenWorkBench(workbenchIndex, chosenTaskNumber);
			chooseTask(workbenchIndex);
		}
	}
}