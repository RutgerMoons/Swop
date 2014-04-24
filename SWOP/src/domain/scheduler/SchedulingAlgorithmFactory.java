package domain.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.car.CarOption;

public class SchedulingAlgorithmFactory {
	
	private final int amountOfWorkBenches;
	
	public SchedulingAlgorithmFactory(int amountOfWorkBenches) {
		this.amountOfWorkBenches = amountOfWorkBenches;
	}
	
	public SchedulingAlgorithm createFifo() {
		return new SchedulingAlgorithmFifo(SchedulingAlgorithmType.FIFO, amountOfWorkBenches);
	}
	
	public SchedulingAlgorithmBatch createBatch(List<CarOption> carParts) {
		return new SchedulingAlgorithmBatch(SchedulingAlgorithmType.BATCH, carParts, amountOfWorkBenches);
	}
	
	public ArrayList<String> getPossibleAlgorithmTypes() {
		return new ArrayList<>(Arrays.asList("Fifo", "Batch"));
	}

}
