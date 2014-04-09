package domain.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import domain.car.CarPart;
import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.Job;
import domain.job.JobComparatorDeadLine;
import domain.job.JobComparatorOrderTime;

public class SchedulingAlgorithmBatch extends SchedulingAlgorithm {

	private PriorityQueue<Job> customJobs;
	private PriorityQueue<Job> standardJobs;
	private PriorityQueue<Job> batchJobs;
	private List<CarPart> carParts;
	private final int amountOfWorkBenches;
	
	public SchedulingAlgorithmBatch(List<CarPart> carParts, int amountOfWorkBenches) {
		if (carParts == null) {
			throw new IllegalArgumentException();
		}
		this.carParts = carParts;
		this.amountOfWorkBenches = amountOfWorkBenches;
		customJobs = new PriorityQueue<>(0, new JobComparatorDeadLine());
		standardJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
		batchJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
	}
	
	@Override
	public void transform(PriorityQueue<Job> customJobs, ArrayList<Job> jobs) {
		if(customJobs == null || jobs == null ){
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		
		//split jobs into the two remaining queues based on carParts
		for(Job job : jobs){
			if(job.getOrder().getDescription().getCarParts().values().containsAll(this.carParts)){
				this.batchJobs.add(job);
			}
			else{
				this.standardJobs.add(job);
			}
		}
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
		//TODO: batch shizzle
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
			for (Iterator<Job> iterator = customJobs.iterator(); iterator.hasNext();) {
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
		list.addAll(this.batchJobs);
		return list;
	}

	@Override
	public void AddCustomJob(Job customJob) {
		if (customJob == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs.add(customJob);
	}

	@Override
	public void AddStandardJob(Job standardJob) {
		if (standardJob == null) {
			throw new IllegalArgumentException();
		}
		if(standardJob.getOrder().getDescription().getCarParts().values().containsAll(this.carParts)){
			this.batchJobs.add(standardJob);
		}
		else{
			this.standardJobs.add(standardJob);
		}
	}

}
