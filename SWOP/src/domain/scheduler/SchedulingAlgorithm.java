package domain.scheduler;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.IJob;

public abstract class SchedulingAlgorithm {
	
	public abstract void transform(PriorityQueue<IJob> customjobs, ArrayList<IJob> standardjobs, ArrayList<Optional<IJob>> history);
	
	public abstract Optional<IJob> retrieveNext(int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException;
	
	public abstract PriorityQueue<IJob> getCustomJobs();
	
	public abstract ArrayList<IJob> getStandardJobs();
	
	public abstract ArrayList<Optional<IJob>> getHistory();
	
	public abstract void AddCustomJob(IJob customjob);
	
	public abstract void AddStandardJob(IJob standardjob) ;
	
	public abstract void startNewDay();

	public abstract int getEstimatedTimeInMinutes(IJob job, UnmodifiableClock currentTime) ;
	
	protected abstract void addToHistory(Optional<IJob> job);
	
	protected abstract void addToList(Optional<IJob> job, ArrayList<Optional<IJob>> list);
	
}
