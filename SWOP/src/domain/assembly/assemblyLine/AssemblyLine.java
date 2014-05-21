package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.UnmodifiableWorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.NoMoreJobsInTimeException;
import domain.exception.NoMoreJobsToScheduleException;
import domain.job.job.IJob;
import domain.job.task.ITask;
import domain.observer.observable.ObservableAssemblyLine;
import domain.observer.observable.ObservableAssemblyLineState;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.AssemblyLineStateObserver;
import domain.observer.observers.ClockObserver;
import domain.order.order.IOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing an assembly line. It contains the workbenches and the
 * current jobs on these workbenches. It notifies the attached observers when an
 * order is completed. Each AssemblyLine has a scheduler.
 */
public class AssemblyLine implements IAssemblyLine, ObservableAssemblyLine,
ObservableAssemblyLineState {

	private List<IWorkBench> workbenches;
	private List<AssemblyLineObserver> observers;
	private Scheduler scheduler;
	private AssemblyLineState assemblyLineState;
	private Set<VehicleSpecification> templatesAbleToAssemble;
	private List<AssemblyLineStateObserver> assemblyLineStateObservers;

	/**
	 * Construct a new AssemblyLine. Initializes a scheduler.
	 * 
	 * @param 	clock
	 *          The clock that has to be accessed by this AssemblyLine
	 *          
	 * @param	clockObserver
	 * 			The ClockObserver that has to be accessed by this AssemblyLine
	 * 
	 * @param	assemblyLineState
	 * 			The state the AssemblyLine has when it's initialised
	 * 
	 * @param	templates
	 * 			The VehicleSpecificatons that can be assembled on this AssemblyLine
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when one or both of the parameters are null
	 */
	public AssemblyLine(ClockObserver clockObserver, ImmutableClock clock,
			AssemblyLineState assemblyLineState,
			Set<VehicleSpecification> templates) {
		if (clockObserver == null || clock == null || assemblyLineState == null
				|| templates == null) {
			throw new IllegalArgumentException();
		}
		workbenches = new ArrayList<IWorkBench>();
		observers = new ArrayList<>();
		assemblyLineStateObservers = new ArrayList<AssemblyLineStateObserver>();
		this.scheduler = new Scheduler(clockObserver, clock);
		this.assemblyLineState = assemblyLineState;
		this.templatesAbleToAssemble = templates;
	}

	/**
	 * Add a workbench to the assemblyLine.
	 * 
	 * @param 	bench
	 *          The workbench you want to add
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the parameter is null
	 */
	@Override
	public void addWorkBench(IWorkBench bench) {
		if (bench == null)
			throw new IllegalArgumentException();
		workbenches.add(bench);
		try {
			this.scheduler.addWorkBenchType(bench.getWorkbenchType());
		} catch (IllegalStateException illegalState) { }
	}

	/**
	 * This method advances the workbenches if all the workbenches are
	 * completed. It shifts the jobs to it's next workstation. It notifies its
	 * observers when an order is completed.
	 * 
	 * @throws 	IllegalStateException
	 *          Thrown when the assemblyLine can not advance
	 * 
	 */
	public void advance() {
		if (!canAdvance()) {
			throw new IllegalStateException();
		}

		// begin at the last workBench
		// if you start at the front, it could be impossible to move a job
		// multiple spots

		for (int i = this.workbenches.size() - 1; i >= 0; i--) {
			IWorkBench bench = getWorkBenches().get(i);
			Optional<IJob> jobAtBench = bench.getCurrentJob();
			if (jobAtBench.isPresent()) {
				// if job is completed -> move as far as possible
				// else move until it can't go further (next workstation already
				// has a job)
				// or to the first IWorkBench that needs to complete tasks of
				// this job
				IJob jobToMove = jobAtBench.get();
				int indexOfFurthestEmptyWorkBench = getIndexOfFurthestEmptyWorkBench(i);
				if (jobToMove.isCompleted()) {
					if (indexOfFurthestEmptyWorkBench < 0) {
						emptyWorkbench(i);
						completeJob(jobToMove);
					} else {
						moveJobToWorkBench(i, indexOfFurthestEmptyWorkBench,
								jobAtBench);
					}
				} else {
					int index = i;
					boolean hasToAssemble = false;
					while (!hasToAssemble && index < (workbenches.size() - 1)) {
						index++;
						hasToAssemble = workbenchHasToAssembleJob(
								this.workbenches.get(index), jobToMove);
					}

					if (index < workbenches.size()) {
						moveJobToWorkBench(i, index, jobAtBench);
					}
				}
			}
		}

		ArrayList<Optional<IJob>> jobsOnAssemblyLine = getCurrentJobsOnAssemblyLine();
		try {
			Optional<IJob> nextJob = this.scheduler.retrieveNextJob(jobsOnAssemblyLine);
			this.workbenches.get(0).setCurrentJob(nextJob);
			this.workbenches.get(0).chooseTasksOutOfJob();
		} catch (NoMoreJobsInTimeException noTime) {
			boolean assemblyLineCompleted = true;
			for (IWorkBench workBench : this.workbenches) {
				if (workBench.getCurrentJob().isPresent()) {
					assemblyLineCompleted = false;
				}
			}
			if (assemblyLineCompleted) {
				this.setState(AssemblyLineState.FINISHED);
			} else {
				Optional<IJob> nextJob = Optional.absent();
				this.workbenches.get(0).setCurrentJob(nextJob);
				this.workbenches.get(0).chooseTasksOutOfJob();
			}
		} catch (NoMoreJobsToScheduleException noJobs) {
			boolean assemblyLineCompleted = true;
			for (IWorkBench workBench : this.workbenches) {
				if (workBench.getCurrentJob().isPresent()) {
					assemblyLineCompleted = false;
				}
			}
			if (assemblyLineCompleted) {
				this.setState(AssemblyLineState.IDLE);
			} else {
				Optional<IJob> nextJob = Optional.absent();
				this.workbenches.get(0).setCurrentJob(nextJob);
				this.workbenches.get(0).chooseTasksOutOfJob();
			}
		}
	}

	private ArrayList<Optional<IJob>> getCurrentJobsOnAssemblyLine() {
		ArrayList<Optional<IJob>> jobsOnAssemblyLine = new ArrayList<>();
		for (IWorkBench workBench : this.workbenches) {
			jobsOnAssemblyLine.add(workBench.getCurrentJob());
		}
		return jobsOnAssemblyLine;
	}

	/**
	 * If the job has a production greater than 0 at that workBench it returns
	 * true Else the workBench has no tasks from this job to complete, so it
	 * returns false
	 */
	private boolean workbenchHasToAssembleJob(IWorkBench workBench,
			IJob jobToMove) {
		return jobToMove.getProductionTime(workBench.getWorkbenchType()) > 0;
	}

	/**
	 * Sets the currentJob of this workBench to an absentJob
	 */
	private void emptyWorkbench(int index) {
		Optional<IJob> absentJob = Optional.absent();
		this.workbenches.get(index).setCurrentJob(absentJob);
	}

	/**
	 * Empties the workBench at CurrentWorkBenchIndex, moves the job to the
	 * workBench at indexOfFurthestEmptyWorkBench and this workBench chooses its
	 * tasks out of the job
	 */
	private void moveJobToWorkBench(int currentWorkBenchIndex,
			int indexOfFurthestEmptyWorkBench, Optional<IJob> jobToMove) {
		emptyWorkbench(currentWorkBenchIndex);
		this.workbenches.get(indexOfFurthestEmptyWorkBench).setCurrentJob(
				jobToMove);
		this.workbenches.get(indexOfFurthestEmptyWorkBench)
		.chooseTasksOutOfJob();
	}

	/**
	 * Finds and returns the index of the furthest empty workbench given an
	 * index. It goes over all the workbenches starting with the workbench one
	 * index further than the given index and stops when it finds a Job on a
	 * workbench.
	 * 
	 * @param 	currentIndex
	 *          Index of a workBench
	 */
	private int getIndexOfFurthestEmptyWorkBench(int currentIndex) {
		int workBenchIndex = currentIndex + 1;
		try {
			Optional<IJob> nextJob = this.workbenches.get(workBenchIndex)
					.getCurrentJob();
			while (!nextJob.isPresent()) {
				workBenchIndex++;
				nextJob = this.workbenches.get(workBenchIndex).getCurrentJob();
			}
		} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			return -1;
		}
		return workBenchIndex - 1;
	}

	/**
	 * Remove the Job from the list with all the current jobs on the AssemblyLine
	 *  and completes the order. Then the observers are notified of the completed Job.
	 */
	private void completeJob(IJob lastJob) {
		lastJob.getOrder().completeCar();
		if(lastJob.getOrder().getPendingCars() == 0){
			updateCompletedOrder(lastJob.getOrder());
		}
	}

	@Override
	public boolean canAdvance() {
		List<IWorkBench> workBenches = getWorkBenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted()) {
				return false;
			}
		return true;
	}

	/**
	 * This method first sets the index of the first workbench that has to
	 * complete some tasks of the Job. Then the Job is passed to the scheduler.
	 * 
	 * @param 	job
	 *          The Job that needs to be scheduled
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the given parameter is null
	 */
	public void schedule(IJob job) {
		if (job == null) {
			throw new IllegalArgumentException();
		}
		if(assemblyLineState.equals(AssemblyLineState.IDLE)){
			setState(AssemblyLineState.OPERATIONAL);
		}
		
		job.setMinimalIndex(getMinimalIndexOfWorkbench(job));
		this.scheduler.addJobToAlgorithm(job,
				this.getCurrentJobsOnAssemblyLine());

	}

	/**
	 * Method for returning the index of the first workbench needed to complete
	 * the given job. It returns a value of -1 when there's no workbench found
	 * to complete this job.
	 */
	private int getMinimalIndexOfWorkbench(IJob job) {
		int i = 0;
		for (IWorkBench workBench : this.workbenches) {
			if (job.getProductionTime(workBench.getWorkbenchType()) > 0) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	public List<IWorkBench> getBlockingWorkBenches() {
		ArrayList<IWorkBench> notCompletedBenches = new ArrayList<>();
		List<IWorkBench> workBenches = getWorkBenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted())
				notCompletedBenches.add(new UnmodifiableWorkBench(
						this.workbenches.get(i)));
		return Collections.unmodifiableList(notCompletedBenches);
	}

	/**
	 * Returns the current Scheduling Algorithm used by the Scheduler used by
	 * this AssemblyLine.
	 */
	public String getCurrentSchedulingAlgorithm() {
		return this.scheduler.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return 	An unmodifiable list of IWorkBenches.
	 */
	@Override
	public List<IWorkBench> getWorkBenches() {
		return Collections.unmodifiableList(workbenches);
	}

	@Override
	public String toString() {
		String result = "Responsibilities: ";
		for (VehicleSpecification specification : getResponsibilities()) {
			result += ", " + specification.getDescription();
		}

		return result.replaceFirst(", ", "");
	}

	@Override
	public void attachObserver(AssemblyLineObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.add(observer);
	}

	@Override
	public void detachObserver(AssemblyLineObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.remove(observer);
	}

	@Override
	public void updateCompletedOrder(IOrder order) {
		for (AssemblyLineObserver observer : observers) {
			observer.updateCompletedOrder(order);
		}
	}

	@Override
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.scheduler
				.getAllVehicleOptionsInPendingOrders());
	}

	/**
	 * Method for asking the Scheduler to switch to the algorithm the given
	 * creator can create.
	 * 
	 * @param 	creator
	 *          It's responsible for creating the correct SchedulingAlgorithm
	 */
	@Override
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		List<WorkBenchType> workBenchTypes = getWorkBenchTypes();
		this.scheduler.switchToAlgorithm(creator, workBenchTypes);
	}

	private List<WorkBenchType> getWorkBenchTypes() {
		List<WorkBenchType> workBenchTypes = new ArrayList<>();
		for (IWorkBench workBench : this.workbenches) {
			workBenchTypes.add(workBench.getWorkbenchType());
		}
		return workBenchTypes;
	}

	/**
	 * Method for returning the current state of the AssemblyLine.
	 */
	@Override
	public AssemblyLineState getState() {
		return assemblyLineState;
	}

	@Override
	public void setState(AssemblyLineState state) {
		if (state == null) {
			throw new IllegalArgumentException();
		}
		AssemblyLineState previousState = assemblyLineState;
		this.assemblyLineState = state;
		updatAssemblyLineState(previousState, state);
	}

	/**
	 * Matches the given workbench to one of its own. If a match is found, the
	 * request is passed on.
	 * 
	 * @param 	workbench
	 *          The WorkBench at which an Task is completed and it needs to be matched
	 */
	public int completeChosenTaskAtChosenWorkBench(IWorkBench workbench,
			ITask task, ImmutableClock elapsed) {
		for (IWorkBench wb : this.workbenches) {
			if (wb.equals(workbench)) {
				wb.completeChosenTaskAtChosenWorkBench(task);
				this.scheduler.advanceInternalClock(elapsed);
				if (canAdvance()) {
					advance();
				}
				return this.scheduler.getTotalMinutesOfInternalClock();
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * Returns an unmodifiable list containing all the pending StandardJobs.
	 */
	@Override
	public List<IJob> getStandardJobs() {
		return Collections.unmodifiableList(this.scheduler.getStandardJobs());
	}

	@Override
	public Set<VehicleSpecification> getResponsibilities() {
		return Collections.unmodifiableSet(templatesAbleToAssemble);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + assemblyLineState.hashCode();
		result = prime * result + templatesAbleToAssemble.hashCode();
		result = prime * result + workbenches.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IAssemblyLine other = null;
		try {
			other = (IAssemblyLine) obj;
		} catch (ClassCastException e) {
			return false;
		}
		if (assemblyLineState != other.getState())
			return false;
		if (!templatesAbleToAssemble.equals(other.getResponsibilities()))
			return false;
		if (!workbenches.equals(other.getWorkBenches()))
			return false;
		return true;
	}

	public List<IJob> removeUnscheduledJobs() {
		return Collections.unmodifiableList(scheduler.removeUnscheduledJobs());
	}

	@Override
	public void attachObserver(AssemblyLineStateObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		assemblyLineStateObservers.add(observer);
	}

	@Override
	public void detachObserver(AssemblyLineStateObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		assemblyLineStateObservers.remove(observer);
	}

	@Override
	public void updatAssemblyLineState(AssemblyLineState previousState,
			AssemblyLineState currentState) {
		for (AssemblyLineStateObserver observer : assemblyLineStateObservers) {
			observer.updateAssemblyLineState(previousState, currentState);
		}
	}
}
