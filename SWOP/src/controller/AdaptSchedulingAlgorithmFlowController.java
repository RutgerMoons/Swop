package controller;

import java.util.ArrayList;

import ui.IClientCommunication;
import domain.exception.ImmutableException;
import domain.facade.Facade;
import domain.users.AccessRight;

public class AdaptSchedulingAlgorithmFlowController  extends UseCaseFlowController {

	public AdaptSchedulingAlgorithmFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, ImmutableException {
		if(this.clientCommunication.askContinue()) {
			showAlgorithms();
		}
	}
	
	/**
	 * Shows the currently used Scheduling Algorithm used and 
	 * offers an overview of all the possible Scheduling Algorithms
	 */
	private void showAlgorithms() {
		// show current algorithm
		// show possible algorithms
		ArrayList<String> possible = facade.getPossibleSchedulingAlgorithms();
		ArrayList<String> algorithms = new ArrayList<String>();
		algorithms.add(0, "Possible Algorithms:");
		for (int i = 0; i < possible.size(); i++) {
			algorithms.add(i+1, Integer.toString(i + 1) + ". " + possible.get(i));
		}
		this.clientCommunication.showAlgorithms(facade.getCurrentSchedulingAlgorithmAsString(), algorithms);
		
		chooseAlgorithm(possible);
	}
	
	/**
	 * Lets the user pick the next Scheduling Algorithm and based on the choice
	 * alters the flow.
	 */
	private void chooseAlgorithm(ArrayList<String> possible) {
		int index = clientCommunication.getIndex(possible.size()) - 1;
		
		if (possible.get(index).equalsIgnoreCase("batch")) {
			chooseBatch();
		}
		else {
			chooseStandardAlgorithm(possible.get(index));
		}
	}
	
	private void chooseStandardAlgorithm(String type) {
		if (type.equalsIgnoreCase("Fifo")) {
			facade.switchToFifo();
			clientCommunication.showAlgorithmSwitched(type);
		}
	}
	
	private void chooseBatch() {
		// present all possible batches
		// let user pick from all the batches
		// switch to batch with the chosen batch
	}
	
	

}
