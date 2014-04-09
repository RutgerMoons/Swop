package domain.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

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
	private ArrayList<Optional<Job>> jobsStartOfDay;
	private ArrayList<Optional<Job>> history;
	
	public SchedulingAlgorithmFifo(int amountOfWorkBenches) {
		customJobs = new PriorityQueue<>(0, new JobComparatorDeadLine());
		standardJobs = new PriorityQueue<Job>(0, new JobComparatorOrderTime());
		this.amountOfWorkBenches = amountOfWorkBenches;
		this.jobsStartOfDay = new ArrayList<>();
		history = new ArrayList<Optional<Job>>();
	}

	@Override
	public void transform(PriorityQueue<Job> customJobs, ArrayList<Job> standardJobs, ArrayList<Optional<Job>> history) {
		if(customJobs == null || standardJobs == null || history == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs = customJobs;
		this.history = history;
		this.standardJobs.addAll(standardJobs);
	}

	@Override
	public Optional<Job> retrieveNext(int currentTotalProductionTime, int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException {
		/* 
		 * step 0: check if jobsStartOfDay contains any jobs..
		 * step 1: check if you have to force some custom jobs
		 * step 2: check if you can put the first car on the assemblyLine
		 * step 3: check if custom job can be put on the assemblyLine
		 */
		//Step 0:
		if (jobsStartOfDay.size() > 0) {
			Optional<Job> toReturn = jobsStartOfDay.remove(0);
			addToHistory(toReturn);
			return toReturn;
		}
		
		Job toSchedule = hasToForceCustomJob(currentTime);
		//Step 1:
		if (toSchedule != null && canAssembleJobInTime(toSchedule, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(toSchedule);
			Optional<Job> toReturn = Optional.fromNullable(toSchedule);
			addToHistory(toReturn);
			return toReturn;
		}
		
		//Step 2:
		if (canAssembleJobInTime(standardJobs.peek(), currentTotalProductionTime, minutesTillEndOfDay)) {
			Optional<Job> toReturn = Optional.fromNullable(standardJobs.poll());
			addToHistory(toReturn);
			return toReturn;
		}
		
		//Step 3:
		Job jobWithHighestWorkBenchIndex = getJobWithHighestWorkBenchIndex();
		if (canAssembleJobInTime(jobWithHighestWorkBenchIndex, currentTotalProductionTime, minutesTillEndOfDay)) {
			customJobs.remove(jobWithHighestWorkBenchIndex);
			Optional<Job> toReturn = Optional.fromNullable(jobWithHighestWorkBenchIndex);
			addToHistory(toReturn);
			return toReturn;
		}
		throw new NoSuitableJobFoundException();
	}
	
	@Override
	protected void addToHistory(Optional<Job> job) {
		this.addToList(job, this.history);
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
		this.standardJobs.add(standardJob);
	}

	@Override
	public void startNewDay() {
		this.jobsStartOfDay = new ArrayList<>();
		for (int i = amountOfWorkBenches - 1; i > 0; i--) {
			Optional<Job> toAdd = Optional.absent();
			for (Iterator<Job> iterator = customJobs.iterator(); iterator.hasNext();) {
				Job job = (Job) iterator.next();
				if (job.getFirstWorkbenchIndex() == i) {
					toAdd = Optional.fromNullable(job);
					break;
				}
			}
			jobsStartOfDay.add(toAdd);
		}
		
	}

	@Override
	public int getEstimatedTimeInMinutes(Job job, UnmodifiableClock currentTime) {
		if (job == null || currentTime == null) {
			throw new IllegalArgumentException();
		}
		
		if(customJobs.contains(job)) {
			try {
				return job.getOrder().getDeadline().minus(currentTime);
			} 
			catch (NotImplementedException e) {	}
		}
		ArrayList<Optional<Job>> previousJobs = this.getHistory();
		int totalProductionTime = 0;
		ArrayList<Job> simulatedAssemblyLine = new ArrayList<>();
		for (Iterator<Job> iterator = customJobs.iterator(); iterator.hasNext();) {
			Job j = (Job) iterator.next();
			if (!j.equals(job)) {
				
				
			}
		}
		return 0;
	}

	
	private int getMaximum(ArrayList<Optional<Job>> list){
		int biggest = 0;
		for(Optional<Job> job : list){
			if(job.isPresent())
		}
	}
	
	
	
	@Override
	public ArrayList<Optional<Job>> getHistory() {
		ArrayList<Optional<Job>> historyCopy = new ArrayList<>();
		historyCopy.addAll(this.history);
		return historyCopy;
	}

	@Override
	protected void addToList(Optional<Job> job, ArrayList<Optional<Job>> list) {
		if (job == null || list == null) {
			throw new IllegalArgumentException();
		}
		history.add(job);
		if (list.size() > this.amountOfWorkBenches) {
			list.remove(0);
		}
		
	}

}
