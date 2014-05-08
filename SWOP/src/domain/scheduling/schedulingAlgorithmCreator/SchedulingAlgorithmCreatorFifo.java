package domain.scheduling.schedulingAlgorithmCreator;

import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmFifo;

public class SchedulingAlgorithmCreatorFifo extends SchedulingAlgorithmCreator {

	@Override
	public SchedulingAlgorithm createSchedulingAlgorithm(int amountOfWorkBenches) {
		return new SchedulingAlgorithmFifo(amountOfWorkBenches);
	}

}
