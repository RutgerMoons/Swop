package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmBatch;
import domain.vehicle.vehicleOption.VehicleOption;

public class SchedulingAlgorithmCreatorBatch extends SchedulingAlgorithmCreator {
	
	private final List<VehicleOption> carParts;
	
	public SchedulingAlgorithmCreatorBatch(List<VehicleOption> carParts) {
		if (carParts == null) {
			throw new IllegalArgumentException();
		}
		this.carParts = carParts;
	}

	@Override
	public SchedulingAlgorithm createSchedulingAlgorithm(int amountOfWorkBenches) {
		return new SchedulingAlgorithmBatch(this.carParts, amountOfWorkBenches);
	}

}
