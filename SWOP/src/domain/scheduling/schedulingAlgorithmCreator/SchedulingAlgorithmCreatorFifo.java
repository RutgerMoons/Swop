package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.assembly.workBench.WorkBenchType;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmFifo;

public class SchedulingAlgorithmCreatorFifo extends SchedulingAlgorithmCreator {

	@Override
	public SchedulingAlgorithm createSchedulingAlgorithm(List<WorkBenchType> workBenchTypes) {
		return new SchedulingAlgorithmFifo(workBenchTypes);
	}

}
