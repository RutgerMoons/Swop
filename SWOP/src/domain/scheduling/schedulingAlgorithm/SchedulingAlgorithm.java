package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.IJob;
import domain.job.JobComparatorDeadLine;
import domain.job.JobComparatorOrderTime;

/**
 * Abstract class that represents a possible scheduling algorithm used for 
 * scheduling Jobs on an AssemblyLine
 */
public abstract class SchedulingAlgorithm {
	
	protected final int amountOfWorkBenches;
	protected PriorityQueue<IJob> customJobs;
	protected ArrayList<Optional<IJob>> history;
	protected ArrayList<Optional<IJob>> jobsStartOfDay;
	protected PriorityQueue<IJob> standardJobs;
	
	public SchedulingAlgorithm(int amountOfWorkBenches) {
		this.amountOfWorkBenches = amountOfWorkBenches;
		this.history = new ArrayList<>();
		this.customJobs = new PriorityQueue<>(10, new JobComparatorDeadLine());
		this.standardJobs = new PriorityQueue<IJob>(10, new JobComparatorOrderTime());
		this.jobsStartOfDay = new ArrayList<>();
	}
	
	/**
	 * The scheduling algorithm will add the given custom job to it's internal data structure
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when the job is null
	 */
	public void addCustomJob(IJob customjob) {
		if (customjob == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs.add(customjob);
	}
	
	/**
	 * The scheduling algorithm will add the given standard job to it's internal data structure
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when the job is null
	 */
	public void addStandardJob(IJob standardjob) {
		if (standardjob == null) {
			throw new IllegalArgumentException();
		}
		this.standardJobs.add(standardjob);
	}
	
	/**
	 * Add the given job to history
	 */
	protected void addToHistory(Optional<IJob> job) {
		this.addToList(job, history);
	}
	
	/**
	 * Adds the given job to the specified list. If the list gets too long (size() > amountOfWorkbenches)
	 * The oldest Job in the list will be removed.
	 */
	protected void addToList(Optional<IJob> job, ArrayList<Optional<IJob>> list) {
		list.add(job);
		if (list.size() > this.amountOfWorkBenches) {
			list.remove(0);
		}
	}
	
	protected boolean canAssembleJobInTime(IJob job, int currentTotalProductionTime, int minutesTillEndOfDay)  {
		if(job == null) {
			return false;
		}
		return job.getOrder().getProductionTime() <= minutesTillEndOfDay - currentTotalProductionTime;
	}
	
	/**
	 * returns a priorityQueue containing all the pending custom jobs (sorted on deadline)
	 */
	public PriorityQueue<IJob> getCustomJobs() {
		PriorityQueue<IJob> pq;
		if(customJobs.size() != 0){
			pq = new PriorityQueue<>(customJobs.size(), new JobComparatorDeadLine());
		}
		else {
			pq = new PriorityQueue<>(1, new JobComparatorDeadLine());
		}
		pq.addAll(this.customJobs);
		return pq;
	}
	
	/**
	 * Calculates and returns the estimated time (at this moment) for the given Job
	 * 
	 * @throws IllegalArgumentException
	 * 			thrown when one or more of the parameters are null
	 */
	public abstract int getEstimatedTimeInMinutes(IJob job, ImmutableClock currentTime) ;
	
	/**
	 * returns a list of all the jobs currently on the assembly line
	 */
	public ArrayList<Optional<IJob>> getHistory() {
		ArrayList<Optional<IJob>> historyCopy = new ArrayList<>();
		historyCopy.addAll(this.history);
		return historyCopy;
	}
	
	protected IJob getJobWithHighestWorkBenchIndex() {
		int index = this.amountOfWorkBenches - 1;
		while (index >= 0) {
			for (Iterator<IJob> iterator = customJobs.iterator(); iterator.hasNext();) {
				IJob job = iterator.next();
				if (job.getMinimalIndex() == index) {
					return job;
				}
			}

			index--;
		}
		return null;
	}
	
	protected int getMaximum(ArrayList<Optional<IJob>> list) {
		int biggest = 0;
		for(Optional<IJob> job : list){
			if(job.isPresent()){
				int currentTimeAtWorkbenchForThisJob = job.get().getTimeAtWorkBench();
				if(currentTimeAtWorkbenchForThisJob >= biggest){
					biggest = currentTimeAtWorkbenchForThisJob;
				}
			}
		}
		return biggest;
	}
	
	/**
	 * returns a list containing all the pending standard jobs (no specific order)
	 */
	public ArrayList<IJob> getStandardJobs() {
		ArrayList<IJob> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		return list;
	}

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
	 * The scheduling algorithm will look for custom jobs that can be placed in a time saving way.
	 * When a next job is retrieved, one of these custom jobs, (if any) will be returned.
	 */
	public abstract void startNewDay();	
	/**
	 * Returns the name.
	 */
	public abstract String toString();
	
	/**
	 * The scheduling algorithm gets all the internal data used to schedule jobs and will initialize its
	 * internal data structures with the given parameters. This method should be used when switching
	 * from one scheduling algorithm to another one.
	 * 
	 * @throws IllegalArgumentException
	 * 			Thrown when one or more of the parameters are null
	 */
	public abstract void transform(PriorityQueue<IJob> customjobs, ArrayList<IJob> standardjobs, ArrayList<Optional<IJob>> history);
	
}
