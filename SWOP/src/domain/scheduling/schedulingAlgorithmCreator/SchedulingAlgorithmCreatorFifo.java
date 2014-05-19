package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.assembly.workBench.WorkBenchType;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithmFifo;

/**
 * A class representing the creation of a SchedulingAlgorithm with scheduling policy: Fist in, first out.
 */
public class SchedulingAlgorithmCreatorFifo extends SchedulingAlgorithmCreator {

	@Override
	public SchedulingAlgorithm createSchedulingAlgorithm(List<WorkBenchType> workBenchTypes) {
		return new SchedulingAlgorithmFifo(workBenchTypes);
	}

}
