package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.assembly.workBench.WorkBenchType;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;

public abstract class SchedulingAlgorithmCreator {
	
	public abstract SchedulingAlgorithm createSchedulingAlgorithm(List<WorkBenchType> workBenchTypes);

}
