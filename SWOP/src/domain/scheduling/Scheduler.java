package domain.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import com.google.common.base.Optional;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.observer.observers.ClockObserver;
import domain.observer.observes.ObservesClock;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
/**
 * A class representing the responsibility for maintaining a scheduling algorithm and shifts.
 * It also keeps track of current time by using a ClockObserver.
 * It's responsible for switching between scheduling algorithms and decouples the AssemblyLine and 
 * the used SchedulingAlgorithm.
 */
public class Scheduler implements ObservesClock {

	private SchedulingAlgorithm schedulingAlgorithm;
	private List<Shift> shifts;
	private ImmutableClock internalClock;

	/**
	 * Constructs a scheduler and initializes the shifts.

	 * @param 	clockObserver
	 * 			The observer used to keep track of time
	 * 
	 * @param 	clock
	 * 			The current time at initialization
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the clockObserver or the clock is null
	 */
	public Scheduler(ClockObserver clockObserver, ImmutableClock clock) {
		if (clockObserver == null || clock==null) {
			throw new IllegalArgumentException();
		}
		clockObserver.attachLogger(this);
		this.internalClock = clock;
		shifts = new ArrayList<>();
		Shift shift1 = new Shift(360, 840, 0); 	//shift van 06:00 tot 14:00
		Shift shift2 = new Shift(840, 1320, 0);	//shift van 14:00 tot 22:00
		shifts.add(shift1);
		shifts.add(shift2);
	}

	/**
	 * Adds the Job to the currently used SchedulingAlgorithm.
	 * 
	 * @param	job 
	 * 			Job needed to be added to the currently used SchedulingAlgorithm
	 * 
	 * @param	jobsOnAssemblyLine
	 * 			A list with all the Jobs currently on the AssemblyLine
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 */
	public void addJobToAlgorithm(IJob job, ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		this.schedulingAlgorithm.addJobToAlgorithm(job);
		this.schedulingAlgorithm.setEstimatedTime(job, internalClock, jobsOnAssemblyLine);
	}

	/**
	 * Switch the algorithm to a given other algorithm.
	 * 	
	 * @param 	creator
	 * 			Creator used to create the other SchedulingAlgorithm to which is going to be switched
	 * 
	 * @param 	workBenchTypes
	 * 			A list of WorkBenchTypes representing all the types the AssemblyLine consists off
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when one or both parameters are null
	 */
	public void switchToAlgorithm(SchedulingAlgorithmCreator creator, List<WorkBenchType> workBenchTypes) {
		if( creator == null || workBenchTypes == null){
			throw new IllegalArgumentException();
		}
		else{
			if (this.schedulingAlgorithm == null ) {
				this.schedulingAlgorithm = creator.createSchedulingAlgorithm(workBenchTypes);
			} else {
				PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
				List<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
				this.schedulingAlgorithm = creator.createSchedulingAlgorithm(workBenchTypes);
				this.schedulingAlgorithm.transform(customJobs, standardJobs);
			}
		}
	}

	/**
	 * Passes the next Job to the AssemblyLine.
	 * 
	 * @param	jobsOnAssemblyLine
	 * 			The jobs currently scheduled on the AssemblyLine
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 */
	public Optional<IJob> retrieveNextJob(ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		// (einduur laatste shift - beginuur eerste shift) - currentTime
		if( jobsOnAssemblyLine == null){
			throw new IllegalArgumentException();
		}
		int minutesTillEndOfDay = shifts.get(shifts.size() - 1).getEndOfShift()
				- this.internalClock.getMinutes();
		return this.schedulingAlgorithm.retrieveNext(minutesTillEndOfDay, internalClock, jobsOnAssemblyLine);
	}

	@Override
	public void advanceTime(ImmutableClock currentTime) {}

	/**
	 * Method called on the start of a new day. The time is updated
	 * to the given clock and the schedulingAlgorithm will look for a time saving arrangement
	 * for the custom jobs at the start of the day.
	 * 
	 * @param	newDay
	 * 			The time at which the new day will start
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 */
	@Override
	public void startNewDay(ImmutableClock newDay) {
		if (newDay == null) {
			throw new IllegalArgumentException();
		}
		int newOvertime = Math.max(this.internalClock.getMinutes() - this.shifts.get(this.shifts.size() - 1).getEndOfShift(), 0);
		this.shifts.get(this.shifts.size() - 1).setNewOvertime(newOvertime);
		this.internalClock = newDay;
		this.schedulingAlgorithm.startNewDay();
	}

	/**
	 * Returns the type of the currently used SchedulingAlgorithm.
	 */
	public String getCurrentSchedulingAlgorithm() {
		if (this.schedulingAlgorithm == null) {
			return "No scheduling algorithm used at the moment.";
		}
		return this.schedulingAlgorithm.toString();
	}

	/**
	 * Returns a list containing all the pending standard Jobs ( in no specific order).
	 */
	public List<IJob> getStandardJobs() {
		return Collections.unmodifiableList(this.schedulingAlgorithm.getStandardJobs());
	}

	/**
	 * Returns a list containing all the Jobs in the current SchedulingAlgorithm.
	 */
	public List<IJob> removeUnscheduledJobs() {
		return Collections.unmodifiableList(schedulingAlgorithm.removeUnscheduledJobs());
	}

	/**
	 * Advances the internal Clock used by the Scheduler. 
	 * 
	 * @param	elapsed
	 * 			Advance the internal clock with this ImmutableClock
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void advanceInternalClock(ImmutableClock elapsed) {
		if (elapsed == null) {
			throw new IllegalArgumentException();
		}
		this.internalClock = this.internalClock.getImmutableClockPlusExtraMinutes(elapsed.getTotalInMinutes());
	}

	/**
	 * Returns the minutes of the internal Clock of the Scheduler. 
	 */
	public int getTotalMinutesOfInternalClock() {
		return this.internalClock.getMinutes();
	}

	/**
	 * Add a WorkBenchType to the currently used SchedulingAlgorithm.
	 * 
	 * @param 	type
	 * 			The WorkBenchType needed to be added
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given parameter is null
	 * 
	 * @throws	IllegalStateException
	 * 			Thrown when the current schedulingAlgorithm is null
	 */
	public void addWorkBenchType(WorkBenchType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		if (this.schedulingAlgorithm == null) {
			throw new IllegalStateException();
		}
		this.schedulingAlgorithm.addWorkBenchType(type);
	}
}
