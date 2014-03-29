package ui;

import java.util.ArrayList;

import facade.IFacade;

/**
 * 
 * Defines the program flow for the 'Advance Assembly Line' use case.
 *
 */
public class AdvanceAssemblyLineFlowController extends UseCaseFlowController{

	/**
	 * Construct a new AdvanceAssemblyHandler.
	 * @param UIFacade
	 * 			The UIfacade this AssembleHandler has to use to communicate with the user.
	 * @param assemblyLine
	 * 			The assemblyline the user will advance.
	 * @param clock
	 * 			The clock used by the given assemblyline.
	 */
	public AdvanceAssemblyLineFlowController(String accessRight, IClientCommunication iClientCommunication, IFacade facade){
		super(accessRight, iClientCommunication, facade);
	}

	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	@Override
	public void executeUseCase(){
		if(this.clientCommunication.askAdvance()) {
			showCurrentAssemblyLine();
			showFutureAssemblyLine();
			if(this.clientCommunication.askContinue())
				advanceAssemblyLine();
		} 
		else {
			
		}
	}

	/**
	 * Show the user the current assemblyline.
	 */
	public void showCurrentAssemblyLine() {
		this.clientCommunication.showAssemblyLine(facade.getAssemblyLineAsString(), "current");
	}

	/**
	 * Show the user what the assemblyline would look like if he succesfully advances the assemblyline.
	 */
	public void showFutureAssemblyLine(){
		this.clientCommunication.showAssemblyLine(facade.getFutureAssemblyLineAsString(), "future");
	}

	/**
	 * Advance the assemblyline if it can be advanced.
	 * Show which benches are preventing the assemblyline from advancing.
	 */
	public void advanceAssemblyLine() {
		if(facade.canAssemblyLineAdvance()){
			int elapsed = this.clientCommunication.getElapsedTime();
			if (elapsed >= 0) {
				facade.advanceClock(elapsed);
			}
			else {
				facade.startNewDay();
			}
			try{
				facade.advanceAssemblyLine();
			} catch(IllegalStateException e){
				System.out.println("You can't advance the assemblyline, because there are no orders.");
			}
		}
		else {
			this.clientCommunication.showBlockingBenches(facade.getBlockingWorkBenches());
		}
		showCurrentAssemblyLine();
		this.clientCommunication.askFinished();
	}

}
