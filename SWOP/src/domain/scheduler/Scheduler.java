package domain.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.car.CarOption;
import domain.clock.UnmodifiableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.IJob;
import domain.observer.ClockObserver;
import domain.observer.LogsClock;
/**
 * This object is responsible for maintaining a scheduling algorithm, a certain amount of workbenches and shifts.
 * It also keeps track of current time by using a ClockObserver.
 * It's responsible for shifting between scheduling algorithms and decouples the assemblyLine and the used scheduling
 * algorithm.
 */
public class Scheduler implements LogsClock {

	private SchedulingAlgorithm schedulingAlgorithm;
	//private final int amountOfWorkBenches;
	private ArrayList<Shift> shifts;
	private UnmodifiableClock clock;
	private SchedulingAlgorithmFactory schedulingAlgorithmFactory;

	/**
	 * Constructs a scheduler and initializes the shifts.
	 * @param amountOfWorkBenches
	 * 		The amount of workbenches on the associated assemblyLine.
	 * @param clockObserver
	 * 		The observer used to keep track of time.
	 * @param clock
	 * 		The current time at initialisation.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the clockObserver or the clock is null.
	 */
	public Scheduler(int amountOfWorkBenches, ClockObserver clockObserver, UnmodifiableClock clock) {
		if (clockObserver == null || clock==null) {
			throw new IllegalArgumentException();
		}
		// this observer should stay referenced in the facade to avoid garbage collection
		clockObserver.attachLogger(this);
		this.clock = clock;
		//this.amountOfWorkBenches = amountOfWorkBenches;
		shifts = new ArrayList<>();
		Shift shift1 = new Shift(360, 840, 0); 	//shift van 06:00 tot 14:00
		Shift shift2 = new Shift(840, 1320, 0);	//shift van 14:00 tot 22:00
		shifts.add(shift1);
		shifts.add(shift2);
		this.schedulingAlgorithmFactory = new SchedulingAlgorithmFactory(amountOfWorkBenches);
		switchToFifo();
	}

	/**
	 * Passes the custom job to the current scheduling algorithm.
	 */
	public void addCustomJob(IJob customJob) {
		this.schedulingAlgorithm.AddCustomJob(customJob);
	}

	/**
	 * Passes the standard job to the current scheduling algorithm.
	 */
	public void addStandardJob(IJob standardJob){
		this.schedulingAlgorithm.AddStandardJob(standardJob);
	}

	/**
	 * Method for switching to the Fifo algorithm.
	 * All the different kinds of jobs are retrieved from the current scheduling algorithm and given to
	 * the fifo algorithm created by the factory.
	 */
	public void switchToFifo()  {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = this.schedulingAlgorithmFactory.createFifo();
		}
		else {
			PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<IJob>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = this.schedulingAlgorithmFactory.createFifo();
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}

	/**
	 * Method for switching to the Batch algorithm.
	 * All the different kinds of jobs are retrieved from the current scheduling algorithm and given to
	 * the Batch algorithm created by the factory.
	 */
	public void switchToBatch(List<CarOption> carOptions) {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = this.schedulingAlgorithmFactory.createBatch(carOptions); 
		}
		else {
			PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
			ArrayList<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			ArrayList<Optional<IJob>> history = this.schedulingAlgorithm.getHistory();
			this.schedulingAlgorithm = this.schedulingAlgorithmFactory.createBatch(carOptions);
			this.schedulingAlgorithm.transform(customJobs, standardJobs, history);
		}
	}

	/**
	 * Passes the next job to the assemblyLine.
	 * 
	 * @throws NoSuitableJobFoundException
	 * 		Thrown when there're no more jobs.
	 */
	public Optional<IJob> retrieveNextJob() throws NoSuitableJobFoundException{
		// (einduur laatste shift - beginuur eerste shift) - currentTime
		int minutesTillEndOfDay = shifts.get(shifts.size() - 1).getEndOfShift()
				- this.clock.getMinutes();
		return this.schedulingAlgorithm.retrieveNext(minutesTillEndOfDay, clock);
	}

	/**
	 * Method for updating the current time.
	 * 
	 * @param currentTime
	 * 			The new value for the current time
	 * 
	 * @throws IllegalArgumentException
	 * 			Exception is thrown when currentTime is null
	 */
	@Override
	public void advanceTime(UnmodifiableClock currentTime) {
		if(currentTime == null){
			throw new IllegalArgumentException();
		}
		this.clock = currentTime;
	}

	/**
	 * Method called on the start of a new day. The time is updated
	 * to the given clock and the schedulingAlgorithm will look for a time saving arrangement
	 * for the custom jobs at the start of the day.
	 */
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

	/**
	 * Returns the time in minutes the job will take (at this moment)
	 */
	public int getEstimatedTimeInMinutes(IJob job){
		return this.schedulingAlgorithm.getEstimatedTimeInMinutes(job, this.clock);
	}
	
	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public String getCurrentSchedulingAlgorithmAsString() {
		return this.schedulingAlgorithm.getSchedulingAlgorithmType().toString();
	}
	
	/**
	 * Returns a list of all the possible scheduling algorithms as Strings.
	 */
	public ArrayList<String> getPossibleSchedulingAlgorithms() {
		return this.schedulingAlgorithmFactory.getPossibleAlgorithmTypes();
	}

}
