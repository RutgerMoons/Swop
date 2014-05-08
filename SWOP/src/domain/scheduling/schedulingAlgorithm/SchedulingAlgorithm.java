package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.IJob;

/**
 * Abstract class that represents a possible scheduling algorithm used for 
 * scheduling Jobs on an AssemblyLine
 */
public abstract class SchedulingAlgorithm {
	
	/**
	 * The scheduling algorithm gets all the internal data used to schedule jobs and will initialize its
	 * internal data structures with the given parameters. This method should be used when switching
	 * from one scheduling algorithm to another one.
	 * 
	 * @throws IllegalArgumentException
	 * 			Thrown when one or more of the parameters are null
	 */
	public abstract void transform(PriorityQueue<IJob> customjobs, ArrayList<IJob> standardjobs, ArrayList<Optional<IJob>> history);
	
	/**
	 * The scheduling algorithm tries to find the best job in the circumstances to be scheduled at the current time
	 * this Job is returned, if no job is found an error is thrown.
	 * 
	 * @throws NoSuitableJobFoundException
	 * 			Thrown when no job can be scheduled this day
	 */
	public abstract Optional<IJob> retrieveNext(int minutesTillEndOfDay, ImmutableClock currentTime) 
			throws NoSuitableJobFoundException;
	
	/**
	 * returns a priorityQueue containing all the pending custom jobs (sorted on deadline)
	 */
	public abstract PriorityQueue<IJob> getCustomJobs();
	
	/**
	 * returns a list containing all the pending standard jobs (no specific order)
	 */
	public abstract ArrayList<IJob> getStandardJobs();
	
	/**
	 * returns a list of all the jobs currently on the assembly line
	 */
	public abstract ArrayList<Optional<IJob>> getHistory();
	
	/**
	 * The scheduling algorithm will add the given custom job to it's internal data structure
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when the job is null
	 */
	public abstract void AddCustomJob(IJob customjob);
	
	/**
	 * The scheduling algorithm will add the given standard job to it's internal data structure
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when the job is null
	 */
	public abstract void AddStandardJob(IJob standardjob) ;
	
	/**
	 * The scheduling algorithm will look for custom jobs that can be placed in a time saving way.
	 * When a next job is retrieved, one of these custom jobs, (if any) will be returned.
	 */
	public abstract void startNewDay();

	/**
	 * Calculates and returns the estimated time (at this moment) for the given Job
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when one or more of the parameters are null
	 */
	public abstract int getEstimatedTimeInMinutes(IJob job, ImmutableClock currentTime) ;
	
	/**
	 * Add the given job to history
	 */
	protected abstract void addToHistory(Optional<IJob> job);
	
	/**
	 * Adds the given job to the specified list. If the list gets too long (size() > amountOfWorkbenches)
	 * The oldest Job in the list will be removed.
	 */
	protected abstract void addToList(Optional<IJob> job, ArrayList<Optional<IJob>> list);
	
	/**
	 * Returns the name.
	 */
	public abstract String toString();
	
}
