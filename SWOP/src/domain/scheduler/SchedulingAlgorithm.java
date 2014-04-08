package domain.scheduler;

import java.util.ArrayList;
import java.util.PriorityQueue;

import domain.assembly.Job;

public abstract class SchedulingAlgorithm {
	
	public abstract void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs);
	
	public abstract Job retrieveNext(int minutesTillEndOfDay);
	
	public abstract PriorityQueue<Job> getCustomJobs();
	
	public abstract ArrayList<Job> getStandardJobs();

}
