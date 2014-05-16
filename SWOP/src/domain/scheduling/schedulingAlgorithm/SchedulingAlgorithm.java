package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.job.IJob;
import domain.job.jobComparator.JobComparatorDeadLine;
import domain.job.jobComparator.JobComparatorOrderTime;
import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;
import domain.order.orderVisitor.IOrderVisitor;

/**
 * Abstract class that represents a possible scheduling algorithm used for 
 * scheduling Jobs on an AssemblyLine
 */
public abstract class SchedulingAlgorithm {
	
	protected PriorityQueue<IJob> customJobs;
	protected List<Optional<IJob>> history;
	protected List<Optional<IJob>> jobsStartOfDay;
	protected PriorityQueue<IJob> standardJobs;
	protected List<WorkBenchType> workBenchTypes;
	
	public SchedulingAlgorithm(List<WorkBenchType> workBenchTypes) {
		if (workBenchTypes == null) {
			throw new IllegalArgumentException();
		}
		this.workBenchTypes = workBenchTypes;
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
	protected void addToList(Optional<IJob> job, List<Optional<IJob>> list) {
		list.add(job);
		if (list.size() > this.workBenchTypes.size()) {
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
	public abstract void setEstimatedTime(IJob job, ImmutableClock currentTime) ;
	
	/**
	 * returns a list of all the jobs currently on the assembly line
	 */
	public List<Optional<IJob>> getHistory() {
		ArrayList<Optional<IJob>> historyCopy = new ArrayList<>();
		historyCopy.addAll(this.history);
		return Collections.unmodifiableList(historyCopy);
	}
	
	protected IJob getJobWithHighestWorkBenchIndex() {
		int index = this.workBenchTypes.size() - 1;
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
	
	//TODO: List of workBenchTypes meegeven
	protected int getMaximum(List<Optional<IJob>> list) {
		int biggest = 0;
		int index = 0;
		for (Optional<IJob> job : list) {
			if (job.isPresent()) {
				int currentTimeAtWorkbenchForThisJob = job.get().getProductionTime(workBenchTypes.get(index));
				if (currentTimeAtWorkbenchForThisJob >= biggest) {
					biggest = currentTimeAtWorkbenchForThisJob;
				}
			}
			index++;
		}
		return biggest;
	}
	
	/**
	 * returns a list containing all the pending standard jobs (no specific order)
	 */
	public List<IJob> getStandardJobs() {
		ArrayList<IJob> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		return Collections.unmodifiableList(list);
	}

	/**
	 * The scheduling algorithm tries to find the best job in the circumstances to be scheduled at the current time
	 * this Job is returned, if no job is found an error is thrown.
	 * 
	 */
	public abstract Optional<IJob> retrieveNext(int minutesTillEndOfDay, ImmutableClock currentTime);
	
	/**
	 * The scheduling algorithm will look for custom jobs that can be placed in a time saving way.
	 * When a next job is retrieved, one of these custom jobs, (if any) will be returned.
	 */
	public abstract void startNewDay();	
	/**
	 * Returns the name.
	 */
	@Override
	public abstract String toString();
	
	/**
	 * The scheduling algorithm gets all the internal data used to schedule jobs and will initialize its
	 * internal data structures with the given parameters. This method should be used when switching
	 * from one scheduling algorithm to another one.
	 * 
	 * @throws IllegalArgumentException
	 * 			Thrown when one or more of the parameters are null
	 */
	public abstract void transform(PriorityQueue<IJob> customjobs, List<IJob> standardjobs, List<Optional<IJob>> history);

	public List<IJob> removeUnscheduledJobs() {
		List<IJob> allJobs = new ArrayList<>();
		allJobs.addAll(customJobs);
		allJobs.addAll(getStandardJobs());
		this.customJobs = new PriorityQueue<>(10, new JobComparatorDeadLine());
		this.standardJobs = new PriorityQueue<IJob>(10, new JobComparatorOrderTime());
		return Collections.unmodifiableList(allJobs);
	}
	
	public void addJobToAlgorithm(IJob job) {
		if (job == null) {
			throw new IllegalArgumentException();
		}
		OrderVisitor visitor = new OrderVisitor(job);
		job.getOrder().acceptVisit(visitor);
	}
	
	private class OrderVisitor implements IOrderVisitor {
		
		private IJob jobToAdd;
		
		public OrderVisitor(IJob job) {
			this.jobToAdd = job;
		}

		/**
		 * add the job as the custom order
		 */
		@Override
		public void visit(CustomOrder order) {
			addCustomJob(this.jobToAdd);
		}

		/**
		 * add the job as the standard order
		 */
		@Override
		public void visit(StandardOrder order) {
			addStandardJob(this.jobToAdd);
		}

		/**
		 * throws an IllegalArgumentException
		 */
		@Override
		public void visit(UnmodifiableOrder order) {
			throw new IllegalArgumentException();
		}
	}
	
}
