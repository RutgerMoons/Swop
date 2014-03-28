package ui;

import java.util.ArrayList;

import users.User;
import users.Worker;
import assembly.Action;
import assembly.AssemblyLine;
import assembly.IWorkBench;
import assembly.Task;
import assembly.WorkBench;

/**
 * 
 * Defines the program flow for the 'Perform Assembly Tasks' use case.
 *
 */
public class AssembleFlowController extends UseCaseFlowController {

	private IClientCommunication UIFacade;

	/**
	 * Construct a new AssembleHandler.
	 * @param iClientCommunication
	 * 			The UIfacade this AssembleHandler has to use to communicate with the user.
	 * @param assemblyLine
	 * 			The assemblyline at which the user can perform assemblytasks.
	 */
	public AssembleFlowController(IClientCommunication iClientCommunication, String accessRight) {
		super(accessRight);
		this.UIFacade = iClientCommunication;
	}


	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	@Override
	public void executeUseCase(){
		WorkBench workBench = chooseWorkBench(user);
		chooseTask(user,workBench);

	} 

	/**
	 * Get the workbench at which the user wants to perform tasks.
	 * @param user
	 * @return
	 * 			The workbench the user has chosen to perform tasks at.
	 */
	public WorkBench chooseWorkBench(User user){
		ArrayList<String> workbenches = new ArrayList<String>();
		for(IWorkBench w : this.assemblyLine.getWorkbenches()){
			workbenches.add(w.getWorkbenchName());
		}

		int workbenchIndex = this.UIFacade.chooseWorkBench(assemblyLine.getWorkbenches().size(), workbenches) - 1;
		return (WorkBench) this.assemblyLine.getWorkbenches().get(workbenchIndex);
	}

	/**
	 * Let the user choose and perform a task from the tasks at the workbench he has previously chosen.
	 * @param user
	 * @param workbench
	 * 			WorkBench at which the user wants to perform assemblytasks.
	 */
	public void chooseTask(User user, WorkBench workbench){
		if(workbench.isCompleted()){
			UIFacade.showWorkBenchCompleted();
			if(UIFacade.askContinue())
				executeUseCase(user);
		}
		else{		
			ArrayList<Task> tasks = new ArrayList<Task>();
			ArrayList<String> tasksStrings = new ArrayList<String>();
			for (int i = 0; i < workbench.getCurrentTasks().size(); i++) {
				if(!workbench.getCurrentTasks().get(i).isCompleted()){
					tasks.add((Task) workbench.getCurrentTasks().get(i));
					tasksStrings.add(workbench.getCurrentTasks().get(i).getTaskDescription());
				}
			}	
			int chosenTaskNumber = this.UIFacade.chooseTask(tasksStrings)-1;
			this.UIFacade.showChosenTask(tasks.get(chosenTaskNumber).toString());
			endTask(user, tasks.get(chosenTaskNumber), workbench);
		}
	}

	/**
	 * End/complete the given task.
	 * @param user
	 * @param task
	 * @param workbench
	 * 			WorkBench at which the given task was performed.
	 */
	private void endTask(User user, Task task, WorkBench workbench){
		this.UIFacade.askFinished();

		for (int i = 0; i < task.getActions().size(); i++) {
			((Action) task.getActions().get(i)).setCompleted(true);
		}
		chooseTask(user, workbench);
	}

}