package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import view.ClientCommunication;
import domain.exception.UnmodifiableException;
import domain.facade.Facade;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmBatch;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorBatch;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.users.AccessRight;
import domain.vehicle.vehicleOption.VehicleOption;

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
	 * 			The Facade this flowcontroller uses to access the domain logic.
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
		List<String> possible = this.getPossibleSchedulingAlgorithms();
		this.clientCommunication.showAlgorithms(facade.getCurrentSchedulingAlgorithm(), possible);
		chooseAlgorithm(possible);
	}
	
	private List<String> getPossibleSchedulingAlgorithms() {
		List<String> listOfAlgorithms = new ArrayList<String>();
		listOfAlgorithms.add("Fifo");
		listOfAlgorithms.add("Batch");
		return listOfAlgorithms;
	}

	/**
	 * Lets the user pick the next Scheduling Algorithm and based on the choice
	 * alters the flow.
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
		// let user pick from all the batches
		// switch to batch with the chosen batch
		
		Set<Set<VehicleOption>> batches = facade.getAllCarOptionsInPendingOrders();
		if (batches.size() <= 0) {
			clientCommunication.showNoBatchesAvailable();
			return;
		}
		clientCommunication.showBatches(batches);
	    ArrayList<Set<VehicleOption>> batchList = new ArrayList<>(batches);
//	    ArrayList<String> sets = new ArrayList<String>();
//		sets.add(0, "Possible Batches:");
//		int i = 1;
//		for (Iterator<Set<VehicleOption>> iterator = batches.iterator(); iterator.hasNext();) {
//			Set<VehicleOption> s = (Set<VehicleOption>) iterator.next();
//			String tmp = "";
//			for (VehicleOption o : s) {
//				tmp += o.toString() + ", ";
//			}
//			tmp = tmp.substring(0, tmp.length() - 2);
//			sets.add(i, Integer.toString(i) + ". " + tmp);
//			i++;
//		}
//		
//		clientCommunication.showBatches(sets);
		int index = clientCommunication.getIndex(batches.size()) - 1;

		ArrayList<VehicleOption> toSet = new ArrayList<VehicleOption>(batchList.get(index));
		facade.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorBatch(toSet));
		
		String tmp = "";
		for (VehicleOption o : batchList.get(index)) {
			tmp += o.toString() + ", ";
		}
		tmp = tmp.substring(0, tmp.length() - 2);
		
		clientCommunication.showAlgorithmSwitched("Batch", tmp);
	}
	
	

}
