package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import view.IClientCommunication;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorBatch;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.users.AccessRight;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing the order of execution for the 'Adapt scheduling algorithm' use case.
 */
public class AdaptSchedulingAlgorithmFlowController  extends UseCaseFlowController {

	/**
	 * Construct a new AdaptSchedulingAlgorithmFlowController.
	 * 
	 * @param 	accessRight
	 * 			The accessRight needed to perform this use case
	 * 
	 * @param 	clientCommunication
	 * 			The ClientCommunication this FlowController uses to communicate with the user
	 * 
	 * @param 	facade
	 * 			The Facade this UseCaseFlowController uses to access the domain logic
	 */
	public AdaptSchedulingAlgorithmFlowController(AccessRight accessRight, IClientCommunication clientCommunication, Facade facade) {
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
		List<String> possible = this.getPossibleSchedulingAlgorithms();
		this.clientCommunication.showAlgorithms(facade.getCurrentSchedulingAlgorithm(), possible);
		chooseAlgorithm(possible);
	}

	/**
	 * Lets the user pick the next Scheduling Algorithm and based on the choice
	 * the flow alters.
	 */
	private void chooseAlgorithm(List<String> possible) {
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
			SchedulingAlgorithmCreatorFifo fifo = new SchedulingAlgorithmCreatorFifo();
			facade.switchToSchedulingAlgorithm(fifo);
			clientCommunication.showAlgorithmSwitched(type);
		}
	}
	
	/**
	 * Changes the Scheduling algorithm to batch with a batch specified by the user.
	 */
	private void chooseBatch() {
		// present all possible batches
		Set<Set<VehicleOption>> batches = facade.getAllVehicleOptionsInPendingOrders();
		if (batches.size() <= 0) {
			clientCommunication.showNoBatchesAvailable();
			return;
		}
		
	    ArrayList<Set<VehicleOption>> batchList = new ArrayList<>(batches);
	    clientCommunication.showBatches(batches);
	    // let user pick from all the batches
	    int index = clientCommunication.getIndex(batches.size()) - 1;

		ArrayList<VehicleOption> toSet = new ArrayList<VehicleOption>(batchList.get(index));
		// switch to batch with the chosen batch
		List<VehicleOption> unModifiableToSet = Collections.unmodifiableList(toSet);
		facade.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorBatch(toSet));
		clientCommunication.showAlgorithmSwitched("Batch", unModifiableToSet);
	}
	
	/**
	 * Returns an immutable list of the possible scheduling algorithms.
	 */
	private List<String> getPossibleSchedulingAlgorithms() {
		List<String> listOfAlgorithms = new ArrayList<String>();
		listOfAlgorithms.add("Fifo");
		listOfAlgorithms.add("Batch");
		return Collections.unmodifiableList(listOfAlgorithms);
	}
}
