package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.job.jobComparator.JobComparatorDeadLine;
import domain.job.jobComparator.JobComparatorOrderTime;
import domain.order.order.CustomOrder;
import domain.order.order.StandardOrder;
import domain.order.order.UnmodifiableOrder;
import domain.order.orderVisitor.IOrderVisitor;

/**
 * An abstract class representing a possible scheduling algorithm used for 
 * scheduling Jobs on an AssemblyLine.
 */
public abstract class SchedulingAlgorithm {
	
	protected PriorityQueue<IJob> customJobs;
	protected List<Optional<IJob>> jobsStartOfDay;
	protected PriorityQueue<IJob> standardJobs;
	protected List<WorkBenchType> workBenchTypes;
	
	/**
	 * Creates a scheduling algorithm.
	 * 
	 * @param 	workBenchTypes
	 * 			A list of WorkBenchTypes representing all the types the AssemblyLine consists off
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the list is null
	 */
	public SchedulingAlgorithm(List<WorkBenchType> workBenchTypes) {
		if (workBenchTypes == null) {
			throw new IllegalArgumentException();
		}
		this.workBenchTypes = workBenchTypes;
		this.customJobs = new PriorityQueue<>(10, new JobComparatorDeadLine());
		this.standardJobs = new PriorityQueue<IJob>(10, new JobComparatorOrderTime());
		this.jobsStartOfDay = new ArrayList<>();
	}
	
	/**
	 * The scheduling algorithm will add the given custom job to it's internal data structure.
	 * 
	 * @param	customJob
	 * 			The Job needed to be added
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the job is null
	 */
	public void addCustomJob(IJob customjob) {
		if (customjob == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs.add(customjob);
	}
	
	/**
	 * The scheduling algorithm will add the given standard Job to it's internal data structure.
	 * 
	 * @param	standardjob
	 * 			The Job needed to be added
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the Job is null
	 */
	public void addStandardJob(IJob standardjob) {
		if (standardjob == null) {
			throw new IllegalArgumentException();
		}
		this.standardJobs.add(standardjob);
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
	
	protected boolean canAssembleJobInTime(Optional<IJob> job, int currentTotalProductionTime, int minutesTillEndOfDay)  {
		if(job == null || !job.isPresent()) {
			return false;
		}
		return job.get().getTotalProductionTime() <= minutesTillEndOfDay - currentTotalProductionTime;
	}
	
	/**
	 * Returns a priorityQueue containing all the pending custom jobs (sorted on deadline).
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
	 * Calculates and returns the estimated time (at this moment) for the given Job.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when one or more of the parameters are null
	 */
	public abstract void setEstimatedTime(IJob job, ImmutableClock currentTime, ArrayList<Optional<IJob>> jobsOnAssemblyLine) ;
	
	protected Optional<IJob> getJobWithHighestWorkBenchIndex() {
		int index = this.workBenchTypes.size() - 1;
		while (index >= 0) {
			for (Iterator<IJob> iterator = customJobs.iterator(); iterator.hasNext();) {
				IJob job = iterator.next();
				if (job.getMinimalIndex() == index) {
					return Optional.fromNullable(job);
				}
			}

			index--;
		}
		Optional<IJob> absentJob = Optional.absent();
		return absentJob;
	}
	
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
			index++; //TODO fout met index out of bounds
		}
		return biggest;
	}
	
	/**
	 * Returns a list containing all the pending standard jobs (no specific order).
	 */
	public List<IJob> getStandardJobs() {
		ArrayList<IJob> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		return Collections.unmodifiableList(list);
	}

	/**
	 * The scheduling algorithm tries to find the best Job in the circumstances to be scheduled at the current time.
	 * This Job is returned, if no Job is found an error is thrown.
	 */
	public abstract Optional<IJob> retrieveNext(int minutesTillEndOfDay, ImmutableClock currentTime,
												ArrayList<Optional<IJob>> jobsOnAssemblyLine);
	
	/**
	 * The scheduling algorithm will look for custom Jobs that can be placed in a time saving way.
	 * When a next Job is retrieved, one of these custom Jobs, (if any) will be returned.
	 */
	public abstract void startNewDay();	
	
	@Override
	public abstract String toString();
	
	/**
	 * The scheduling algorithm gets all the internal data used to schedule Jobs and will initialize its
	 * internal data structures with the given parameters. This method should be used when switching
	 * from one scheduling algorithm to another one.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when one or more of the parameters are null
	 */
	public abstract void transform(PriorityQueue<IJob> customjobs, List<IJob> standardjobs);

	/**
	 * Returns all the Jobs the SchedulingAlgorithm has in an unmodifiable list.
	 */
	public List<IJob> removeUnscheduledJobs() {
		List<IJob> allJobs = new ArrayList<>();
		allJobs.addAll(customJobs);
		allJobs.addAll(getStandardJobs());
		this.customJobs = new PriorityQueue<>(10, new JobComparatorDeadLine());
		this.standardJobs = new PriorityQueue<IJob>(10, new JobComparatorOrderTime());
		return Collections.unmodifiableList(allJobs);
	}
	
	/**
	 * Add a WorkBenchType to the list.
	 *  
	 * @param 	type
	 * 			The new WorkBenchType to be added
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown the parameter is null
	 */
	public void addWorkBenchType(WorkBenchType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		} 
		this.workBenchTypes.add(type);
	}
	
	/**
	 * The given Job will be scheduled by the SchedulingAlgorithm.
	 * 
	 * @param 	job
	 * 			The Job needed to be scheduled
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 */
	public void addJobToAlgorithm(IJob job) {
		if (job == null) {
			throw new IllegalArgumentException();
		}
		OrderVisitor visitor = new OrderVisitor(job);
		job.acceptVisit(visitor);
	}
	
	private class OrderVisitor implements IOrderVisitor {
		
		private IJob jobToAdd;
		
		public OrderVisitor(IJob job) {
			this.jobToAdd = job;
		}

		/**
		 * Add the job as the custom order
		 */
		@Override
		public void visit(CustomOrder order) {
			addCustomJob(this.jobToAdd);
		}

		/**
		 * Add the job as the standard order
		 */
		@Override
		public void visit(StandardOrder order) {
			addStandardJob(this.jobToAdd);
		}

		/**
		 * Throws an IllegalArgumentException
		 */
		@Override
		public void visit(UnmodifiableOrder order) {
			throw new IllegalArgumentException();
		}
	}
	
}