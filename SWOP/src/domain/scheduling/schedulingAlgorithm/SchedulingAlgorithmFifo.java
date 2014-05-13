package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.job.IJob;
import domain.job.jobComparator.JobComparatorDeadLine;
import domain.job.jobComparator.JobComparatorOrderTime;

/**
 * Represents a scheduling algorithm used for scheduling Jobs on an AssemblyLine.
 * Uses the time of ordering and gives a higher priority to Jobs that are ordered first.
 */
public class SchedulingAlgorithmFifo extends SchedulingAlgorithm {

	public SchedulingAlgorithmFifo(int amountOfWorkBenches) {
		super(amountOfWorkBenches);
	}

	private int getCurrentTotalProductionTime() {
		int time = 0;
		ArrayList<Optional<IJob>> historyCopy = getHistory();
		if (historyCopy.size() == 0) {
			return 0;
		}
		historyCopy.remove(0);
		while (historyCopy.size() > 0) {
			time += getMaximum(historyCopy);
			historyCopy.remove(0);
		}
		return time;
	}

	@Override
	public void setEstimatedTime(IJob job, ImmutableClock currentTime){
		if (job == null || currentTime == null) {
			throw new IllegalArgumentException();
		}

		if(customJobs.contains(job)) {
			try {
				int total = job.getOrder().getDeadline().minus(currentTime);
				int days = total/60;
				int minutes = total%60;
				
				job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
			} 
			catch (NotImplementedException e) {	}
		}
		ArrayList<Optional<IJob>> previousJobs = this.getHistory();
		int totalProductionTime = 0;
		for (Iterator<IJob> iterator = standardJobs.iterator(); iterator.hasNext();) {
			IJob j = iterator.next();
			if (iterator.hasNext()) {
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);			
			}
			else{
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);
				for(int i = 0; i< this.amountOfWorkBenches-1;i++){
					Optional<IJob> absentJob = Optional.absent();
					addToList(absentJob, previousJobs);
					totalProductionTime += this.getMaximum(previousJobs);
				}
			}
		}
		int days = totalProductionTime/60;
		int minutes = totalProductionTime%60;
		job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
	}

	/**
	 * If a custom job has to be forced, return the most urgent
	 * return null if no job has to be forced.
	 */
	private IJob hasToForceCustomJob(ImmutableClock currentTime) {
		int idx = 0;
		for (IJob job : customJobs) {
			try{
				if (job.getOrder().getDeadline().minus(currentTime) - ((idx + job.getMinimalIndex() +1) * job.getOrder().getProductionTime()) <= 0) {
					return job;
				}
			} catch (NotImplementedException e) {
				// verkeerde queue, komt niet voor..
			}
			idx++;
		}
		return null;
	}


	@Override
	public Optional<IJob> retrieveNext(int minutesTillEndOfDay, ImmutableClock currentTime) 
			throws NoSuitableJobFoundException{
		/* 
		 * step 0: check if jobsStartOfDay contains any jobs..
		 * step 1: check if you have to force some custom jobs
		 * step 2: check if you can put the first car on the assemblyLine
		 * step 3: check if custom job can be put on the assemblyLine
		 */
		//Step 0:
		if (jobsStartOfDay.size() > 0) {
			Optional<IJob> toReturn = jobsStartOfDay.remove(0);
			this.customJobs.remove(toReturn.get());
			addToHistory(toReturn);
			return toReturn;
		}

		IJob toSchedule = hasToForceCustomJob(currentTime);
		int currentTotalProductionTime = getCurrentTotalProductionTime();
		//Step 1:
		if (toSchedule != null && canAssembleJobInTime(toSchedule, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(toSchedule);
			Optional<IJob> toReturn = Optional.fromNullable(toSchedule);
			addToHistory(toReturn);
			return toReturn;
		}

		//Step 2:
		if (canAssembleJobInTime(standardJobs.peek(), currentTotalProductionTime, minutesTillEndOfDay)) {
			Optional<IJob> toReturn = Optional.fromNullable(standardJobs.poll());
			addToHistory(toReturn);
			return toReturn;
		}

		//Step 3:
		IJob jobWithHighestWorkBenchIndex = getJobWithHighestWorkBenchIndex();
		if (canAssembleJobInTime(jobWithHighestWorkBenchIndex, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(jobWithHighestWorkBenchIndex);
			Optional<IJob> toReturn = Optional.fromNullable(jobWithHighestWorkBenchIndex);
			addToHistory(toReturn);
			return toReturn;
		}
		throw new NoSuitableJobFoundException();
	}



	@Override
	public void startNewDay() {
		this.jobsStartOfDay = new ArrayList<>();
		for (int i = amountOfWorkBenches - 1; i >= 0; i--) {
			Optional<IJob> toAdd = Optional.absent();
			for (Iterator<IJob> iterator = customJobs.iterator(); iterator.hasNext();) {
				IJob job = iterator.next();
				if (job.getMinimalIndex() == i) {
					toAdd = Optional.fromNullable(job);
					break;
				}
			}
			if(toAdd.isPresent()){
				jobsStartOfDay.add(toAdd);
			}
		}

	}

	public String toString() {
		return "Fifo";
	}
	
	@Override
	public void transform(PriorityQueue<IJob> customJobs, ArrayList<IJob> standardJobs, ArrayList<Optional<IJob>> history) {
		if(customJobs == null || standardJobs == null || history == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		this.history = history;
		this.standardJobs.addAll(standardJobs);
	}

}
