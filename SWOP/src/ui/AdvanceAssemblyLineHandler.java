package ui;

import java.util.ArrayList;

import users.Manager;
import users.User;
import assembly.AssemblyLine;
import clock.Clock;

/**
 * 
 * Defines the program flow for the 'Advance Assembly Line' use case.
 *
 */
public class AdvanceAssemblyLineHandler extends UseCaseHandler{

	private UIFacade UIfacade;
	private AssemblyLine assemblyLine;
	private Clock clock;
	
	/**
	 * Construct a new AdvanceAssemblyHandler.
	 * @param UIFacade
	 * 			The UIfacade this AssembleHandler has to use to communicate with the user.
	 * @param assemblyLine
	 * 			The assemblyline the user will advance.
	 * @param clock
	 * 			The clock used by the given assemblyline.
	 */
	public AdvanceAssemblyLineHandler(UIFacade UIFacade, AssemblyLine assemblyLine, Clock clock){
		UIfacade = UIFacade;
		this.assemblyLine = assemblyLine;
		this.clock = clock;
	}
	
	/**
	 * Indicates if the user is authorized to be part of the use case.
	 * @param user
	 * 			The user of which we want to get to know if he's authorized.
	 * @return
	 * 			A boolean indicating if the user is authorized.
	 */
	@Override
	public boolean mayUseThisHandler(User user){
		if (user instanceof Manager) return true;
		else return false;
	}
	
	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	@Override
	public void executeUseCase(User user){
		if(this.UIfacade.askAdvance()){
			showCurrentAssemblyLine();
			showFutureAssemblyLine();
			if(this.UIfacade.askContinue())
				advanceAssemblyLine();
		}
	}

	/**
	 * Show the user the current assemblyline.
	 */
	private void showCurrentAssemblyLine() {
		this.UIfacade.showAssemblyLine(this.assemblyLine.toString(), "current");
	}
	
	/**
	 * Show the user what the assemblyline would look like if he succesfully advances the assemblyline.
	 */
	private void showFutureAssemblyLine(){
		this.UIfacade.showAssemblyLine(this.assemblyLine.getFutureAssemblyLine().toString(), "future");
	}
	
	/**
	 * Advance the assemblyline if it can be advanced.
	 * Show which benches are preventing the assemblyline from advancing.
	 */
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
