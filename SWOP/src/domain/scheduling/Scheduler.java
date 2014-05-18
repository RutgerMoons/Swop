package domain.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.job.job.IJob;
import domain.observer.observers.ClockObserver;
import domain.observer.observes.ObservesClock;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.vehicleOption.VehicleOption;
/**
 * This object is responsible for maintaining a scheduling algorithm, a certain amount of workbenches and shifts.
 * It also keeps track of current time by using a ClockObserver.
 * It's responsible for shifting between scheduling algorithms and decouples the assemblyLine and the used scheduling
 * algorithm.
 */
public class Scheduler implements ObservesClock {

	private SchedulingAlgorithm schedulingAlgorithm;
	private List<Shift> shifts;
	private ImmutableClock internalClock;

	/**
	 * Constructs a scheduler and initializes the shifts.
	 * @param amountOfWorkBenches
	 * 		The amount of workbenches on the associated assemblyLine.
	 * @param clockObserver
	 * 		The observer used to keep track of time.
	 * @param clock
	 * 		The current time at initialization.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the clockObserver or the clock is null.
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
	 * Adds the job to the currently used SchedulingAlgorithm
	 * 
	 * @param	job to be added to the currently used SchedulingAlgorithm
	 */
	public void addJobToAlgorithm(IJob job, ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		this.schedulingAlgorithm.addJobToAlgorithm(job);
		schedulingAlgorithm.setEstimatedTime(job, internalClock, jobsOnAssemblyLine);
	}

	public void switchToAlgorithm(SchedulingAlgorithmCreator creator, List<WorkBenchType> workBenchTypes) {
		if (this.schedulingAlgorithm == null) {
			this.schedulingAlgorithm = creator.createSchedulingAlgorithm(workBenchTypes);
		} else {
			PriorityQueue<IJob> customJobs = this.schedulingAlgorithm.getCustomJobs();
			List<IJob> standardJobs = this.schedulingAlgorithm.getStandardJobs();
			this.schedulingAlgorithm = creator.createSchedulingAlgorithm(workBenchTypes);
			this.schedulingAlgorithm.transform(customJobs, standardJobs);
		}
	}

	/**
	 * Passes the next job to the assemblyLine.
	 */
	public Optional<IJob> retrieveNextJob(ArrayList<Optional<IJob>> jobsOnAssemblyLine) {
		// (einduur laatste shift - beginuur eerste shift) - currentTime
		int minutesTillEndOfDay = shifts.get(shifts.size() - 1).getEndOfShift()
				- this.internalClock.getMinutes();
		return this.schedulingAlgorithm.retrieveNext(minutesTillEndOfDay, internalClock, jobsOnAssemblyLine);
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
	public void advanceTime(ImmutableClock currentTime) {}

	/**
	 * Method called on the start of a new day. The time is updated
	 * to the given clock and the schedulingAlgorithm will look for a time saving arrangement
	 * for the custom jobs at the start of the day.
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
	 * Returns the currently used Scheduling Algorithm Type
	 */
	public String getCurrentSchedulingAlgorithm() {
		if (this.schedulingAlgorithm == null) {
			return "No scheduling algorithm used at the moment.";
		}
		return this.schedulingAlgorithm.toString();
	}
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		HashSet<VehicleOption> set = new HashSet<>();
		List<IJob> jobs = this.schedulingAlgorithm.getStandardJobs();
		
		// get all the CarOptions that occur in the pending orders
		for (IJob job : jobs) {
			for (VehicleOption o : job.getVehicleOptions()) {
				set.add(o);
			}
		}
		
		// get all the CarOptions that occur in the pending orders 3 or more times
		HashSet<VehicleOption> threeOrMoreTimes = new HashSet<>();
		for (VehicleOption option : set) {
			int counter = 0;
			for (IJob job : jobs) {
				if (job.getOrder().getDescription().getVehicleOptions().values().contains(option)) {
					counter++;
				}
			}
			if (counter >= 3) {
				threeOrMoreTimes.add(option);
			}
		} 

		// get all the sets of CarOptions that occur in the pending orders 3 or more times
		Set<Set<VehicleOption>> toReturn = new HashSet<Set<VehicleOption>>();
	    Set<Set<VehicleOption>> powerSet = Sets.powerSet(threeOrMoreTimes);
	    for (Set<VehicleOption> subset : powerSet) {
      		if (subset.size() <= 0) {
      			continue;
      		}
      		int counter = 0;
      		for (IJob job : jobs) {
				if (job.getOrder().getDescription().getVehicleOptions().values().containsAll(subset)) {
					counter++;
				}
			}
      		if (counter >= 3) {
      			toReturn.add(subset);
      		}
	    }
		return Collections.unmodifiableSet(toReturn);
	}
	
	/**
	 * returns a list containing all the pending standard jobs (no specific order)
	 */
	public List<IJob> getStandardJobs() {
		return Collections.unmodifiableList(this.schedulingAlgorithm.getStandardJobs());
	}

	public List<IJob> removeUnscheduledJobs() {
		return Collections.unmodifiableList(schedulingAlgorithm.removeUnscheduledJobs());
	}

	public void advanceInternalClock(ImmutableClock elapsed) {
		if (elapsed == null) {
			throw new IllegalArgumentException();
		}
		this.internalClock = this.internalClock.getImmutableClockPlusExtraMinutes(elapsed.getTotalInMinutes());
	}

	public int getTotalMinutesOfInternalClock() {
		return this.internalClock.getMinutes();
	}
	
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
