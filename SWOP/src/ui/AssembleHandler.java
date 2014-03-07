package ui;

import java.util.ArrayList;

import order.OrderBook;
import users.GarageHolder;
import users.User;
import users.UserBook;
import users.Worker;
import assembly.AssemblyLine;
import assembly.Task;
import assembly.WorkBench;
import car.CarModelCatalogue;
import clock.Clock;

public class AssembleHandler extends UseCaseHandler{
	
	private UIFacade UIFacade;
	private AssemblyLine assemblyLine;
	
	public AssembleHandler(UIFacade UIFacade, AssemblyLine assemblyLine){
		this.UIFacade = UIFacade;
		this.assemblyLine = assemblyLine;
	}

	public boolean mayUseThisHandler(User user){
		if (user instanceof Worker) return true;
		else return false;
	}
	
	public void executeUseCase(User user){
		WorkBench workBench = chooseWorkBench(user);
		chooseTask(user,workBench);
	}
	
	private WorkBench chooseWorkBench(User user){
		int workbenchIndex = this.UIFacade.getWorkBench();
		return this.assemblyLine.getWorkbenches().get(workbenchIndex);
	}
	
	private void chooseTask(User user, WorkBench workbench){
		if(workbench.isCompleted()){
			UIFacade.showWorkBenchCompleted();
			if(UIFacade.askContinue())
				executeUseCase(user);
		}
		else{		
			ArrayList<Task> tasks = new ArrayList<>();
			for (int i = 0; i < workbench.getCurrentTasks().size(); i++) {
				if(!workbench.getCurrentTasks().get(i).isCompleted())
					tasks.add(workbench.getCurrentTasks().get(i));
			}	
		int chosenTaskNumber = this.UIFacade.chooseTask(tasks);
		endTask(user, tasks.get(chosenTaskNumber), workbench);
		}
	}
	
	private void endTask(User user, Task task, WorkBench workbench){
		this.UIFacade.askFinished();
		
		for (int i = 0; i < task.getActions().size(); i++) {
			task.getActions().get(i).setCompleted(true);
		}
		chooseTask(user, workbench);
	}
	
}