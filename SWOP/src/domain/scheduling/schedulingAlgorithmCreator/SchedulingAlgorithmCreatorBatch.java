package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.assembly.workBench.WorkBenchType;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmBatch;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing the creation of a SchedulingAlgorithmBatch.
 */
public class SchedulingAlgorithmCreatorBatch extends SchedulingAlgorithmCreator {
	
	private final List<VehicleOption> carParts;
	
	/**
	 * Constructs a creator for SchedulingAlgorithmBatch.
	 * 
	 * @param 	vehicleOptions
	 * 			The list off VehicleOptions that will be prioritized by SchedulingAlgorithmBatch
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 */
	public SchedulingAlgorithmCreatorBatch(List<VehicleOption> vehicleOptions) {
		if (vehicleOptions == null) {
			throw new IllegalArgumentException();
		}
		this.carParts = vehicleOptions;
	}

	@Override
	public SchedulingAlgorithm createSchedulingAlgorithm(List<WorkBenchType> workBenchTypes) {
		return new SchedulingAlgorithmBatch(this.carParts, workBenchTypes);
	}

}
