package controller;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.facade.Facade;
import domain.users.AccessRight;

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
	public AdvanceAssemblyLineFlowController(AccessRight accessRight, IClientCommunication iClientCommunication, Facade facade){
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
			} catch(IllegalStateException | ImmutableException | NoSuitableJobFoundException | NotImplementedException e){
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
