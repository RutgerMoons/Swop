package domain.scheduler;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.Job;

public abstract class SchedulingAlgorithm {
	
	public abstract void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs, ArrayList<Optional<Job>> history);
	
	public abstract Optional<Job> retrieveNext(int currentTotalProductionTime, int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException;
	
	public abstract PriorityQueue<Job> getCustomJobs();
	
	public abstract ArrayList<Job> getStandardJobs();
	
	public abstract ArrayList<Optional<Job>> getHistory();
	
	public abstract void AddCustomJob(Job customJob);
	
	public abstract void AddStandardJob(Job standardJob);
	
	public abstract void startNewDay();

	public abstract int getEstimatedTimeInMinutes(Job job, UnmodifiableClock currentTime);
	
	protected abstract void addToHistory(Optional<Job> job);
	
	protected abstract void addToList(Optional<Job> job, ArrayList<Optional<Job>> list);
	
}
