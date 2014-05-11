package controller;

import java.util.List;

import view.ClientCommunication;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.job.task.ITask;
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
	public AssembleFlowController(AccessRight accessRight, ClientCommunication iClientCommunication, Facade facade) {
		super(accessRight, iClientCommunication, facade);
	}


	/**
	 * Execute the use case.
	 */
	@Override
	public void executeUseCase(){
		IWorkBench bench = chooseWorkBench();
		chooseTask(bench);
	} 

	/**
	 * Get the workbench at which the user wants to perform tasks.
	 */
	public IWorkBench chooseWorkBench(){
		//choose assembly Line
		List<IAssemblyLine> allAssemblyLines = facade.getAssemblyLines();
		//naar clientcommunication
		IAssemblyLine chosenAssemblyLine = clientCommunication.chooseAssemblyLine(allAssemblyLines);
		//choose workbench from assemblyLine
		IWorkBench chosenWorkbench = clientCommunication.chooseWorkBench(chosenAssemblyLine.getWorkbenches());


		return chosenWorkbench;
	}

	/**
	 * Let the user choose and perform a task from the tasks at the workbench he has previously chosen.
	 * 
	 */
	public void chooseTask(IWorkBench workBench){
		List<ITask> tasksAtWorkbench = workBench.getCurrentTasks();
		if(tasksAtWorkbench.isEmpty()){
			clientCommunication.showWorkBenchCompleted();
			if(clientCommunication.askContinue())
				executeUseCase();
		}
		else{	
			//choose ITask, not the index of the task
			ITask chosenTask = this.clientCommunication.chooseTask(tasksAtWorkbench);
			this.clientCommunication.showChosenTask(chosenTask);
			if(this.clientCommunication.askContinue()){
				int time = clientCommunication.getElapsedTime();
				int days = time/Clock.MINUTESINADAY;
				int minutes = time%Clock.MINUTESINADAY;
				ImmutableClock clock = new ImmutableClock(days, minutes);
				facade.completeChosenTaskAtChosenWorkBench(chosenTask, clock);
				chooseTask(workBench);
			}
		}
	}
}
