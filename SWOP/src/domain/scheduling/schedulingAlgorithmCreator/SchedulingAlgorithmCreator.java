package domain.scheduling.schedulingAlgorithmCreator;

import java.util.List;

import domain.assembly.workBench.WorkBenchType;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;

/**
 * A abstract class representing the creation of a SchedulingAlgorithm.
 */
public abstract class SchedulingAlgorithmCreator {

	/**
	 * Constructs a SchedulingAlgorithm.
	 * 
	 * @param 	workBenchTypes
	 * 			A list of WorkBenchTypes representing all the types the AssemblyLine consists off
	 * 
	 * @return 	SchedulingAlgorithm
	 * 			The algorithm it created
	 */
	public abstract SchedulingAlgorithm createSchedulingAlgorithm(List<WorkBenchType> workBenchTypes);

}
