package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import view.ClientCommunication;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.users.AccessRight;
import domain.vehicle.VehicleOption;

/**
 * Defines the program flow for the 'Adapt scheduling algorithm' use case.
 *
 */
public class AdaptSchedulingAlgorithmFlowController  extends UseCaseFlowController {

	/**
	 * Construct a new AdaptSchedulingAlgorithmFlowController.
	 * @param accessRight
	 * 			The accessRight needed to perform this use case.
	 * @param clientCommunication
	 * 			The IClientCommunication this FlowController uses to communicate with the user.
	 * @param facade
	 * 			The Facade this Flowcontroller uses to access the domain logic.
	 */
	public AdaptSchedulingAlgorithmFlowController(AccessRight accessRight, ClientCommunication clientCommunication, Facade facade) {
		super(accessRight, clientCommunication, facade);
	}

	@Override
	public void executeUseCase() throws IllegalArgumentException, UnmodifiableException {
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
	
	/**
	 * Changes the scheduling algorithm to the chosen type.
	 */
	private void chooseStandardAlgorithm(String type) {
		if (type.equalsIgnoreCase("Fifo")) {
			facade.switchToFifo();
			clientCommunication.showAlgorithmSwitched(type);
		}
	}
	
	/**
	 * Changes the Scheduling algorithm to batch with a batch specified by the user.
	 */
	private void chooseBatch() {
		// present all possible batches
		// let user pick from all the batches
		// switch to batch with the chosen batch
		
		Set<Set<VehicleOption>> batches = facade.getAllCarOptionsInPendingOrders();
		if (batches.size() <= 0) {
			clientCommunication.showNoBatchesAvailable();
			return;
		}
	    ArrayList<Set<VehicleOption>> batchList = new ArrayList<>(batches);
	    ArrayList<String> sets = new ArrayList<String>();
		sets.add(0, "Possible Batches:");
		int i = 1;
		for (Iterator<Set<VehicleOption>> iterator = batches.iterator(); iterator.hasNext();) {
			Set<VehicleOption> s = (Set<VehicleOption>) iterator.next();
			String tmp = "";
			for (VehicleOption o : s) {
				tmp += o.toString() + ", ";
			}
			tmp = tmp.substring(0, tmp.length() - 2);
			sets.add(i, Integer.toString(i) + ". " + tmp);
			i++;
		}
		
		clientCommunication.showBatches(sets);
		int index = clientCommunication.getIndex(sets.size()) - 1;
		
		ArrayList<VehicleOption> toSet = new ArrayList<VehicleOption>(batchList.get(index));
		facade.switchToBatch(toSet);
		
		String tmp = "";
		for (VehicleOption o : batchList.get(index)) {
			tmp += o.toString() + ", ";
		}
		tmp = tmp.substring(0, tmp.length() - 2);
		
		clientCommunication.showAlgorithmSwitched("Batch", tmp);
	}
	
	

}
