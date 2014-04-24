package domain.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.car.CarOption;

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
		return new SchedulingAlgorithmFifo(SchedulingAlgorithmType.FIFO, amountOfWorkBenches);
	}
	
	/**
	 * Returns a new Batch Scheduling Algorithm
	 */
	public SchedulingAlgorithmBatch createBatch(List<CarOption> carParts) {
		return new SchedulingAlgorithmBatch(SchedulingAlgorithmType.BATCH, carParts, amountOfWorkBenches);
	}
	
	/**
	 * Returns a list of all the possible Algorithm types as a list of Strings
	 */
	public ArrayList<String> getPossibleAlgorithmTypes() {
		return new ArrayList<>(Arrays.asList("Fifo", "Batch"));
	}

}
