package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;

/**
 * A class representing a scheduling algorithm used for scheduling Jobs on an AssemblyLine.
 * Uses the time of ordering and gives a higher priority to Jobs that are ordered first.
 */
public class SchedulingAlgorithmFifo extends SchedulingAlgorithm {

	/**
	 * Creates a SchedulingAlgorithm which has as scheduling policy : "First come, first served".
	 * 
	 * @param 	workBenchTypes
	 * 			A list of WorkBenchTypes representing all the types the AssemblyLine consists off
	 */
	public SchedulingAlgorithmFifo(List<WorkBenchType> workBenchTypes) {
		super(workBenchTypes);
	}

	private int getCurrentTotalProductionTime(ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		int time = 0;
		List<Optional<IJob>> historyCopy = new ArrayList<>(jobsOnAssemblyLine);
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
	public void setEstimatedTime(IJob job, ImmutableClock currentTime, ArrayList<Optional<IJob>> jobsOnAssemblyLine){
		if (job == null || currentTime == null) {
			throw new IllegalArgumentException();
		}

		if(customJobs.contains(job)) {

			int total = job.getOrder().getDeadline().minus(currentTime);
			int days = total/Clock.MINUTESINADAY;
			int minutes = total%Clock.MINUTESINADAY;

			job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
			return;
		}
		List<Optional<IJob>> previousJobs = new ArrayList<>(jobsOnAssemblyLine);
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
				for(int i = 0; i< this.workBenchTypes.size() - 1;i++){
					Optional<IJob> absentJob = Optional.absent();
					addToList(absentJob, previousJobs);
					totalProductionTime += this.getMaximum(previousJobs);
				}
			}
		}
		int days = totalProductionTime/Clock.MINUTESINADAY;
		int minutes = totalProductionTime%Clock.MINUTESINADAY;
		job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
	}

	/**
	 * If a custom job has to be forced, return the most urgent
	 * return null if no job has to be forced.
	 */
	private Optional<IJob> hasToForceCustomJob(ImmutableClock currentTime) {
		int idx = 0;
		for (IJob job : customJobs) {
			if (job.getOrder().getDeadline().minus(currentTime) - ((idx + job.getMinimalIndex() +1) * job.getOrder().getProductionTime()) <= 0) {
				return Optional.fromNullable(job);
			}
			idx++;
		}
		Optional<IJob> absentJob = Optional.absent();
		return absentJob;
	}


	@Override
	public Optional<IJob> retrieveNext(int minutesTillEndOfDay, ImmutableClock currentTime,
			ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
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
			return toReturn;
		}

		Optional<IJob> toSchedule = hasToForceCustomJob(currentTime);
		int currentTotalProductionTime = getCurrentTotalProductionTime(jobsOnAssemblyLine);
		//Step 1:
		if (toSchedule != null && canAssembleJobInTime(toSchedule, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(toSchedule);
			Optional<IJob> toReturn = toSchedule;
			return toReturn;
		}

		//Step 2:
		if (canAssembleJobInTime(Optional.fromNullable(standardJobs.peek()), currentTotalProductionTime, minutesTillEndOfDay)) {
			Optional<IJob> toReturn = Optional.fromNullable(standardJobs.poll());
			return toReturn;
		}

		//Step 3:
		Optional<IJob> jobWithHighestWorkBenchIndex = getJobWithHighestWorkBenchIndex();
		if (canAssembleJobInTime(jobWithHighestWorkBenchIndex, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(jobWithHighestWorkBenchIndex);
			return jobWithHighestWorkBenchIndex;
		}
		return Optional.absent();
	}



	@Override
	public void startNewDay() {
		this.jobsStartOfDay = new ArrayList<>();
		for (int i = this.workBenchTypes.size() - 1; i >= 0; i--) {
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

	@Override
	public String toString() {
		return "Fifo";
	}

	@Override
	public void transform(PriorityQueue<IJob> customJobs, List<IJob> standardJobs) {
		if(customJobs == null || standardJobs == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		this.standardJobs.addAll(standardJobs);
	}

}
