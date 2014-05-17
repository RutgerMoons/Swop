package domain.scheduling.schedulingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.job.job.IJob;
import domain.job.jobComparator.JobComparatorOrderTime;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Represents a scheduling algorithm used for scheduling Jobs on an AssemblyLine.
 * Uses a set of CarOptions and gives a higher priority to Jobs that contain
 * all of these CarOptions.
 */
public class SchedulingAlgorithmBatch extends SchedulingAlgorithm {

	private PriorityQueue<IJob> batchJobs;
	private ArrayList<Optional<IJob>> jobsStartOfDay;
	private List<VehicleOption> vehicleOptions;

	public SchedulingAlgorithmBatch(List<VehicleOption> carParts, List<WorkBenchType> workBenchTypes) {
		super(workBenchTypes);
		if (carParts == null) {
			throw new IllegalArgumentException();
		}
		this.vehicleOptions = carParts;
		batchJobs = new PriorityQueue<IJob>(10, new JobComparatorOrderTime());
	}

	@Override
	public void addStandardJob(IJob standardJob){
		if (standardJob == null) {
			throw new IllegalArgumentException();
		}
		try {
			if (standardJob.getVehicleOptions().containsAll(this.vehicleOptions)) {
				this.batchJobs.add(standardJob);
			}
			else{
				this.standardJobs.add(standardJob);
			}
		} catch (NotImplementedException e) {}
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
	public void setEstimatedTime(IJob job, ImmutableClock currentTime, ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		if (job == null || currentTime == null) {
			throw new IllegalArgumentException();
		}

		if(customJobs.contains(job)) {
			try {
				int total = job.getOrder().getDeadline().minus(currentTime);
				int days = total/Clock.MINUTESINADAY;
				int minutes = total%Clock.MINUTESINADAY;
				
				job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
			} 
			catch (NotImplementedException e) {	}
		}
		List<Optional<IJob>> previousJobs = jobsOnAssemblyLine;
		int totalProductionTime = 0;
		for (Iterator<IJob> iterator = batchJobs.iterator(); iterator.hasNext();) {
			IJob j = iterator.next();
			if (iterator.hasNext()) {
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);			
			}
			else if(this.batchJobs.contains(job)){
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);
				for(int i = 0; i< this.workBenchTypes.size() -1;i++){
					Optional<IJob> absentJob = Optional.absent();
					addToList(absentJob, previousJobs);
					totalProductionTime += this.getMaximum(previousJobs);
				}
				int days = totalProductionTime/Clock.MINUTESINADAY;
				int minutes = totalProductionTime%Clock.MINUTESINADAY;
				job.getOrder().setEstimatedTime(new ImmutableClock(days, minutes));
			}
		}

		for (Iterator<IJob> iterator = standardJobs.iterator(); iterator.hasNext();) {
			IJob j = iterator.next();
			if (iterator.hasNext()) {
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);			
			}
			else{
				addToList(Optional.fromNullable(j), previousJobs);
				totalProductionTime += this.getMaximum(previousJobs);
				for(int i = 0; i< this.workBenchTypes.size() - 1 ;i++){
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

	@Override
	public List<IJob> getStandardJobs() {
		ArrayList<IJob> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		list.addAll(this.batchJobs);
		return Collections.unmodifiableList(list);
	}


	/**
	 * If a custom job has to be forced, return the most urgent
	 * return null if no job has to be forced.
	 */
	private Optional<IJob> hasToForceCustomJob(ImmutableClock currentTime) {
		int idx = 0;
		for (IJob job : customJobs) {
			try {
				if (job.getOrder().getDeadline().minus(currentTime) - ((idx + job.getMinimalIndex() +1) * job.getOrder().getProductionTime()) <= 0) {
					return Optional.fromNullable(job);
				}
			} catch (NotImplementedException e) {
				// verkeerde queue, komt niet voor..
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
		 * step 0: check in the beginning of the day if custom jobs can be executed
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
		if (canAssembleJobInTime(Optional.fromNullable(batchJobs.peek()), currentTotalProductionTime, minutesTillEndOfDay)) {
			Optional<IJob> toReturn = Optional.fromNullable(batchJobs.poll());
			return toReturn;
		}
		
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
		return "Batch";
	}
	
	@Override
	public void transform(PriorityQueue<IJob> customJobs, List<IJob> standardJobs) {
		if(customJobs == null || standardJobs == null){
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		//split jobs into the two remaining queues based on carParts

		for(IJob job : standardJobs){
			try {
				if(job.getOrder().getDescription().getVehicleSpecification().getVehicleOptions().values().containsAll(this.vehicleOptions)){
					this.batchJobs.add(job);
				}
				else{
					this.standardJobs.add(job);
				}
			} catch (NotImplementedException n) {
				continue;
				// this error can't occur
			}
		}
	}

}
