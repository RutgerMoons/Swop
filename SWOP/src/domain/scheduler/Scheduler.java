package domain.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.car.CarPart;
import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.Job;
import domain.observer.ClockObserver;
import domain.observer.LogsClock;

public class Scheduler implements LogsClock {

	private SchedulingAlgorithm schedulingAlgorithm;
	private final int amountOfWorkBenches;
	private ArrayList<Shift> shifts;
	private UnmodifiableClock clock;
	
	public Scheduler(int amountOfWorkBenches, ClockObserver clockObserver) {
		if (clockObserver == null) {
			throw new IllegalArgumentException();
		}
		// this observer should stay referenced in the facade to avoid garbage collection
		clockObserver.attachLogger(this);
		this.amountOfWorkBenches = amountOfWorkBenches;
		switchToFifo();
		shifts = new ArrayList<>();
		Shift shift1 = new Shift(360, 840, 0); 	//shift van 06:00 tot 14:00
		Shift shift2 = new Shift(840, 1320, 0);	//shift van 14:00 tot 22:00
		shifts.add(shift1);
		shifts.add(shift2);
	}
	
	public void addCustomJob(Job customJob) {
		this.schedulingAlgorithm.AddCustomJob(customJob);
	}
	
	public void addStandardJob(Job standardJob) {
		this.schedulingAlgorithm.AddStandardJob(standardJob);
	}
	
	public void switchToFifo() {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches); 
		}
		else {
			PriorityQueue<Job> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<Job> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<Job>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}
	
	public void switchToBatch(List<CarPart> carParts) {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carParts, amountOfWorkBenches); 
		}
		else {
			PriorityQueue<Job> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<Job> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<Job>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carParts, amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}
	
	public Optional<Job> retrieveNextJob(int currentTotalProductionTime) throws NoSuitableJobFoundException {
		// (einduur laatste shift - beginuur eerste shift) - currentTime
		int minutesTillEndOfDay = shifts.get(shifts.size() - 1).getEndOfShift()
									- shifts.get(0).getStartOfShift()
									- this.clock.getMinutes();
		return this.schedulingAlgorithm.retrieveNext(currentTotalProductionTime, minutesTillEndOfDay, clock);
	}

	@Override
	public void advanceTime(UnmodifiableClock currentTime) {
		this.clock = currentTime;
	}

	@Override
	public void startNewDay(UnmodifiableClock newDay) {
		if (newDay == null) {
			throw new IllegalArgumentException();
		}
		this.clock = newDay;
		this.schedulingAlgorithm.startNewDay();
	}
	
	public int getEstimatedTimeInMinutes(Job job) {
		return this.schedulingAlgorithm.getEstimatedTimeInMinutes(job, this.clock);
	}
	
}
