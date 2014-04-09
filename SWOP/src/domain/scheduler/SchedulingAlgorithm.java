package domain.scheduler;

import java.util.ArrayList;
import java.util.PriorityQueue;

import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.Job;

public abstract class SchedulingAlgorithm {
	
	public abstract void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs);
	
	public abstract Job retrieveNext(int currentTotalProductionTime, int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException;
	
	public abstract PriorityQueue<Job> getCustomJobs();
	
	public abstract ArrayList<Job> getStandardJobs();
	
	public abstract void AddCustomJob(Job customJob);
	
	public abstract void AddStandardJob(Job standardJob);

}
