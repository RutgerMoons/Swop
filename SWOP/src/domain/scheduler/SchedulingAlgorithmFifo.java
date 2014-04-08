package domain.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.Job;
import domain.job.JobComparatorDeadLine;
import domain.job.JobComparatorOrderTime;

public class SchedulingAlgorithmFifo extends SchedulingAlgorithm {
	
	private PriorityQueue<Job> customJobs;
	private PriorityQueue<Job> standardJobs;
	private final int amountOfWorkBenches;
	private final int workingMinutes;
	
	public SchedulingAlgorithmFifo(int amountOfWorkBenches, int workingMinutes) {
		customJobs = new PriorityQueue<>(0, new JobComparatorDeadLine());
		standardJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
		this.amountOfWorkBenches = amountOfWorkBenches;
		this.workingMinutes = workingMinutes;
	}

	@Override
	public void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs) {
		if(customJobs == null || standardJobs == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		this.standardJobs.addAll(standardJobs);
	}

	@Override
	public Job retrieveNext(int currentTotalProductionTime, int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException {
		/*
		 * step 1: check if you have to force some custom jobs
		 * step 2: check if you can put the first car on the assemblyLine
		 * step 3: check if custom job can be put on the assemblyLine
		 */
		Job toSchedule = hasToForceCustomJob(currentTime);
		//Step 1:
		if (toSchedule != null && canAssembleJobInTime(toSchedule, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(toSchedule);
			return toSchedule;
		}
		
		//Step 2:
		if (canAssembleJobInTime(standardJobs.peek(), currentTotalProductionTime, minutesTillEndOfDay)) {
			return standardJobs.poll();
		}
		
		//Step 3:
		Job jobWithHighestWorkBenchIndex = getJobWithHighestWorkBenchIndex();
		if (canAssembleJobInTime(jobWithHighestWorkBenchIndex, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(jobWithHighestWorkBenchIndex);
			return jobWithHighestWorkBenchIndex;
		}
		throw new NoSuitableJobFoundException();
	}
	
	/**
	 * If a custom job has to be forced, return the most urgent
	 * return null if no job has to be forced.
	 */
	private Job hasToForceCustomJob(UnmodifiableClock currentTime) {
		// daarna verdergaan met workingMinutes
		int idx = 0;
		for (Job job : customJobs) {
			try {
				if (job.getOrder().getDeadline().minus(currentTime) - (idx * job.getOrder().getProductionTime()) <= 0) {
					return job;
				}
			} catch (NotImplementedException e) {
				// verkeerde queue, komt niet voor..
			}
			idx++;
		}
		return null;
	}
	
	private boolean canAssembleJobInTime(Job job, int currentTotalProductionTime, int minutesTillEndOfDay) {
		return job.getOrder().getProductionTime() >= minutesTillEndOfDay - currentTotalProductionTime;
	}
	
	private Job getJobWithHighestWorkBenchIndex() {
		int index = this.amountOfWorkBenches - 1;
		while (index >= 0) {
			for (Iterator iterator = customJobs.iterator(); iterator.hasNext();) {
				Job job = (Job) iterator.next();
				if (job.getFirstWorkbenchIndex() == index) {
					return job;
				}
			}
			
			index--;
		}
		return null;
	}

	@Override
	public PriorityQueue<Job> getCustomJobs() {
		PriorityQueue<Job> pq = new PriorityQueue<>(customJobs.size(), new JobComparatorDeadLine());
		pq.addAll(this.customJobs);
		return pq;
	}

	@Override
	public ArrayList<Job> getStandardJobs() {
		ArrayList<Job> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		return list;
	}

}
