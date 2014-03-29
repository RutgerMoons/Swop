package ui;

import java.util.ArrayList;

import users.User;
import assembly.Action;
import assembly.IWorkBench;
import assembly.Task;
import assembly.WorkBench;
import facade.Facade;
import facade.IFacade;

/**
 * TODO : update documentatie
 * Defines the program flow for the 'Perform Assembly Tasks' use case.
 *
 */
public class AssembleFlowController extends UseCaseFlowController {

	private IClientCommunication clientCommunication;
	private IFacade iFacade;

	/**
	 * Construct a new AssembleHandler.
	 * @param iClientCommunication
	 * 			The UIfacade this AssembleHandler has to use to communicate with the user.
	 */
	public AssembleFlowController(IClientCommunication iClientCommunication, Facade facade,  String accessRight){
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
		ArrayList<String> workbenches = iFacade.getWorkBenchNames();
		int workbenchIndex = this.clientCommunication.chooseWorkBench(workbenches.size(), workbenches) - 1;
		chooseTask(workbenchIndex);
	}

	/**
	 * Let the user choose and perform a task from the tasks at the workbench he has previously chosen.
	 * 
	 */
	public void chooseTask(int workbenchIndex){
		ArrayList<String> tasksAtWorkbench = iFacade.getTasksOfChosenWorkBench(workbenchIndex);
		if(tasksAtWorkbench.isEmpty()){
			clientCommunication.showWorkBenchCompleted();
			if(clientCommunication.askContinue())
				executeUseCase();
		}
		else{	
			int chosenTaskNumber = this.clientCommunication.chooseTask(tasksAtWorkbench)-1;
			this.clientCommunication.showChosenTask(tasksAtWorkbench.get(chosenTaskNumber).toString());
			this.clientCommunication.askFinished();
			iFacade.completeChosenTaskAtChosenWorkBench(workbenchIndex, chosenTaskNumber);
			chooseTask(workbenchIndex);
		}
	}
}