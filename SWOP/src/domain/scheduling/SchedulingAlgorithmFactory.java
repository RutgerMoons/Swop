package domain.scheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.vehicle.VehicleOption;

/**
 * A clean way to construct Scheduling Algorithms.
 */
public class SchedulingAlgorithmFactory {
	
	private final int amountOfWorkBenches;
	
	public SchedulingAlgorithmFactory(int amountOfWorkBenches) {
		this.amountOfWorkBenches = amountOfWorkBenches;
	}
	
	/**
	 * Returns a new Fifo Scheduling Algorithm
	 */
	public SchedulingAlgorithm createFifo() {
		return new SchedulingAlgorithmFifo(amountOfWorkBenches);
	}
	
	/**
	 * Returns a new Batch Scheduling Algorithm
	 */
	public SchedulingAlgorithmBatch createBatch(List<VehicleOption> carParts) {
		return new SchedulingAlgorithmBatch(carParts, amountOfWorkBenches);
	}
	
	/**
	 * Returns a list of all the possible Algorithm types as a list of Strings
	 */
	public ArrayList<String> getPossibleAlgorithmTypes() {
		return new ArrayList<>(Arrays.asList("Fifo", "Batch"));
	}

}
