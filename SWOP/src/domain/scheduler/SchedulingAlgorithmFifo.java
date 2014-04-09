package domain.scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.job.IJob;
import domain.job.Job;
import domain.job.JobComparatorDeadLine;
import domain.job.JobComparatorOrderTime;

public class SchedulingAlgorithmFifo extends SchedulingAlgorithm {
	
	private PriorityQueue<IJob> customJobs;
	private PriorityQueue<IJob> standardJobs;
	private final int amountOfWorkBenches;
	private ArrayList<Optional<IJob>> jobsStartOfDay;
	private ArrayList<Optional<IJob>> history;
	
	public SchedulingAlgorithmFifo(int amountOfWorkBenches) {
		customJobs = new PriorityQueue<IJob>(0, new JobComparatorDeadLine());
		standardJobs = new PriorityQueue<IJob>(0, new JobComparatorOrderTime());
		this.amountOfWorkBenches = amountOfWorkBenches;
		this.jobsStartOfDay = new ArrayList<>();
		history = new ArrayList<Optional<IJob>>();
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

	@Override
	public Optional<IJob> retrieveNext(int minutesTillEndOfDay, UnmodifiableClock currentTime) 
			throws NoSuitableJobFoundException {
		/* 
		 * step 0: check if jobsStartOfDay contains any jobs..
		 * step 1: check if you have to force some custom jobs
		 * step 2: check if you can put the first car on the assemblyLine
		 * step 3: check if custom job can be put on the assemblyLine
		 */
		//Step 0:
		if (jobsStartOfDay.size() > 0) {
			Optional<IJob> toReturn = jobsStartOfDay.remove(0);
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
	protected void addToHistory(Optional<IJob> job) {
		this.addToList(job, this.history);
		}

	/**
	 * If a custom job has to be forced, return the most urgent
	 * return null if no job has to be forced.
	 */
	private IJob hasToForceCustomJob(UnmodifiableClock currentTime) {
		int idx = 0;
		for (IJob job : customJobs) {
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
	
	private boolean canAssembleJobInTime(IJob job, int currentTotalProductionTime, int minutesTillEndOfDay) {
		return job.getOrder().getProductionTime() >= minutesTillEndOfDay - currentTotalProductionTime;
	}
	
	private IJob getJobWithHighestWorkBenchIndex() {
		int index = this.amountOfWorkBenches - 1;
		while (index >= 0) {
			for (Iterator<IJob> iterator = customJobs.iterator(); iterator.hasNext();) {
				IJob job = iterator.next();
				if (job.getFirstWorkbenchIndex() == index) {
					return job;
				}
			}
			
			index--;
		}
		return null;
	}

	@Override
	public PriorityQueue<IJob> getCustomJobs() {
		PriorityQueue<IJob> pq = new PriorityQueue<>(customJobs.size(), new JobComparatorDeadLine());
		pq.addAll(this.customJobs);
		return pq;
	}

	@Override
	public ArrayList<IJob> getStandardJobs() {
		ArrayList<IJob> list = new ArrayList<>();
		list.addAll(this.standardJobs);
		return list;
	}

	@Override
	public void AddCustomJob(IJob customJob) {
		if (customJob == null) {
			throw new IllegalArgumentException();
		}
		this.customJobs.add(customJob);
	}

	@Override
	public void AddStandardJob(IJob standardJob) {
		if (standardJob == null) {
			throw new IllegalArgumentException();
		}
		this.standardJobs.add(standardJob);
	}

	@Override
	public void startNewDay() {
		this.jobsStartOfDay = new ArrayList<>();
		for (int i = amountOfWorkBenches - 1; i > 0; i--) {
			Optional<IJob> toAdd = Optional.absent();
			for (Iterator<IJob> iterator = customJobs.iterator(); iterator.hasNext();) {
				IJob job = iterator.next();
				if (job.getFirstWorkbenchIndex() == i) {
					toAdd = Optional.fromNullable(job);
					break;
				}
			}
			jobsStartOfDay.add(toAdd);
		}
		
	}

	@Override
	public int getEstimatedTimeInMinutes(IJob job, UnmodifiableClock currentTime) {
		if (job == null || currentTime == null) {
			throw new IllegalArgumentException();
		}
		
		if(customJobs.contains(job)) {
			try {
				return job.getOrder().getDeadline().minus(currentTime);
			} 
			catch (NotImplementedException e) {	}
		}
		ArrayList<Optional<IJob>> previousJobs = this.getHistory();
		int totalProductionTime = 0;
		for (Iterator<IJob> iterator = standardJobs.iterator(); iterator.hasNext();) {
			IJob j = iterator.next();
			if (!j.equals(job)) {
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
		return totalProductionTime;
	}

	
	private int getMaximum(ArrayList<Optional<IJob>> list){
		int biggest = 0;
		for(Optional<IJob> job : list){
			int currentTimeAtWorkbenchForThisJob = job.get().getOrder().getDescription().getSpecification().getTimeAtWorkBench();
			if(job.isPresent() && currentTimeAtWorkbenchForThisJob >= biggest){
				biggest = currentTimeAtWorkbenchForThisJob;
			}
		}
		return biggest;
	}
	
	
	
	@Override
	public ArrayList<Optional<IJob>> getHistory() {
		ArrayList<Optional<IJob>> historyCopy = new ArrayList<>();
		historyCopy.addAll(this.history);
		return historyCopy;
	}

	@Override
	protected void addToList(Optional<IJob> job, ArrayList<Optional<IJob>> list) {
		if (job == null || list == null) {
			throw new IllegalArgumentException();
		}
		history.add(job);
		if (list.size() > this.amountOfWorkBenches) {
			list.remove(0);
		}
		
	}

}