package domain.scheduling.schedulingAlgorithmCreator;

import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;

public abstract class SchedulingAlgorithmCreator {
	
	public abstract SchedulingAlgorithm createSchedulingAlgorithm(int amountOfWorkbenches);

}
