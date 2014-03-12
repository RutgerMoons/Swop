package ui;

import java.util.ArrayList;

import clock.Clock;
import assembly.AssemblyLine;
import assembly.Task;
import assembly.WorkBench;
import users.GarageHolder;
import users.Manager;
import users.User;

public class AdvanceAssemblyLineHandler extends UseCaseHandler{

	private UIFacade UIfacade;
	private AssemblyLine assemblyLine;
	private Clock clock;
	
	public AdvanceAssemblyLineHandler(UIFacade UIFacade, AssemblyLine assemblyLine, Clock clock){
		UIfacade = UIFacade;
		this.assemblyLine = assemblyLine;
		this.clock = clock;
	}
	
	public boolean mayUseThisHandler(User user){
		if (user instanceof Manager) return true;
		else return false;
	}
	
	public void executeUseCase(User user){
		if(this.UIfacade.askAdvance()){
			showCurrentAssemblyLine();
			showFutureAssemblyLine();
			if(this.UIfacade.askContinue())
				advanceAssemblyLine();
		}
	}

	private void showCurrentAssemblyLine() {
		this.UIfacade.showAssemblyLine(this.assemblyLine.toString(), "current");
	}
	
	private void showFutureAssemblyLine(){
		this.UIfacade.showAssemblyLine(this.assemblyLine.getFutureAssemblyLine().toString(), "future");
	}
	
	private void advanceAssemblyLine() {
		ArrayList<Integer> notCompletedBenches = new ArrayList<Integer>();
		for (int i = 0; i < this.assemblyLine.getWorkbenches().size(); i++)
			if(!this.assemblyLine.getWorkbenches().get(i).isCompleted())
				notCompletedBenches.add(i+1);
		
		if(notCompletedBenches.isEmpty()){
			assemblyLine.advance();
			int elapsed = this.UIfacade.getElapsedTime();
			if (elapsed >= 0) {
				clock.advanceTime(elapsed);
			}
			else {
				clock.startNewDay();
			}
		}
		else{
			this.UIfacade.showBlockingBenches(notCompletedBenches);
		}
		showCurrentAssemblyLine();
		this.UIfacade.askFinished();
	}

}
