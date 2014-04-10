package domain.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.car.CarOption;
import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.IJob;
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
	
	public void addCustomJob(IJob customJob) {
		this.schedulingAlgorithm.AddCustomJob(customJob);
	}
	
	public void addStandardJob(IJob standardJob) {
		this.schedulingAlgorithm.AddStandardJob(standardJob);
	}
	
	public void switchToFifo() {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches); 
		}
		else {
			PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<IJob>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = new SchedulingAlgorithmFifo(amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}
	
	public void switchToBatch(List<CarOption> carOptions) {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carOptions, amountOfWorkBenches); 
		}
		else {
			PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<IJob>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = new SchedulingAlgorithmBatch(carOptions, amountOfWorkBenches);
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}
	
	public Optional<IJob> retrieveNextJob() throws NoSuitableJobFoundException {
		// (einduur laatste shift - beginuur eerste shift) - currentTime
		int minutesTillEndOfDay = shifts.get(shifts.size() - 1).getEndOfShift()
									- shifts.get(0).getStartOfShift()
									- this.clock.getMinutes();
		return this.schedulingAlgorithm.retrieveNext(minutesTillEndOfDay, clock);
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
		int newOvertime = Math.max(this.clock.getMinutes() - this.shifts.get(this.shifts.size() - 1).getEndOfShift(), 0);
		this.shifts.get(this.shifts.size() - 1).setNewOvertime(newOvertime);
		this.clock = newDay;
		this.schedulingAlgorithm.startNewDay();
	}
	
	public int getEstimatedTimeInMinutes(IJob job) {
		return this.schedulingAlgorithm.getEstimatedTimeInMinutes(job, this.clock);
	}
	
}
