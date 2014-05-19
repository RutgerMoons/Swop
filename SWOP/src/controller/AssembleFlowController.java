package controller;

import java.util.List;

import com.google.common.base.Optional;

import view.IClientCommunication;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.job.task.ITask;
import domain.users.AccessRight;


/**
 * A class representing the order of execution for the 'Perform Assembly Tasks' use case.
 *
 */
public class AssembleFlowController extends UseCaseFlowController {

	/**
	 * Construct a new AssembleFlowController.
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
	public AssembleFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}


	/**
	 * Execute the use case in some steps
	 * 1. show all assemblyLines and choose an assemblyLine
	 * 2. show all workbench for this chosen assemblyLine and choose a workbench
	 * 3. show all tasks on the chosen workbench and choose a task
	 * 4. execute the chosen task and notify the Facade
	 */
	@Override
	public void executeUseCase(){
		//choose assembly Line
		List<IAssemblyLine> allAssemblyLines = facade.getAssemblyLines();
		//naar clientcommunication
		IAssemblyLine chosenAssemblyLine = clientCommunication.chooseAssemblyLine(allAssemblyLines);
		// choose workbench
		IWorkBench bench = this.chooseWorkBench(chosenAssemblyLine);
		// choose task
		Optional<ITask> task = this.chooseTask(bench);
		if (task.isPresent()){
			ImmutableClock clock = this.retrieveElapsedTime();
			facade.completeChosenTaskAtChosenWorkBench(chosenAssemblyLine, bench, task.get(), clock);
			this.chooseTask(bench);
		}
		else {
			this.executeUseCase();
		}
	} 

	/**
	 * Get the workbench at which the user wants to perform tasks
	 */
	public IWorkBench chooseWorkBench(IAssemblyLine chosenAssemblyLine){
		IWorkBench chosenWorkbench = clientCommunication.chooseWorkBench(chosenAssemblyLine.getWorkBenches());
		return chosenWorkbench;
	}

	/**
	 * Let the user choose and perform a task from the tasks at the workbench he has previously chosen
	 */
	public Optional<ITask> chooseTask(IWorkBench workBench){
		List<ITask> tasksAtWorkbench = workBench.getCurrentTasks();
		if(tasksAtWorkbench.isEmpty()){
			clientCommunication.showWorkBenchCompleted();
			if(clientCommunication.askContinue())
				//executeUseCase();
				return Optional.absent();
		}
		else{	
			//choose ITask, not the index of the task
			ITask chosenTask = this.clientCommunication.chooseTask(tasksAtWorkbench);
			this.clientCommunication.showChosenTask(chosenTask);
			if(this.clientCommunication.askContinue()){
				return Optional.fromNullable(chosenTask);
			}
		}
		return Optional.absent();
	}
	
	/**
	 * Asks the user how much time he needed for completing the task and returns an
	 * ImmutableClock
	 */
	public ImmutableClock retrieveElapsedTime(){
		int time = clientCommunication.getElapsedTime();
		int days = time/Clock.MINUTESINADAY;
		int minutes = time%Clock.MINUTESINADAY;
		return new ImmutableClock(days, minutes);
	}
}