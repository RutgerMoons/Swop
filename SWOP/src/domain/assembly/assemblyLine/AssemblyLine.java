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
import domain.exception.NoSuitableJobFoundException;
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
 * A class representing an assembly line. It contains the workbenches and the current jobs on these workbenches.
 * It notifies the attached observers when an order is completed. Each AssemblyLine has a scheduler.
 */
public class AssemblyLine implements IAssemblyLine, ObservableAssemblyLine, ObservableAssemblyLineState {

	private List<IJob> currentJobs;
	private List<IWorkBench> workbenches;
	private List<AssemblyLineObserver> observers;
	private Scheduler scheduler;
	private AssemblyLineState assemblyLineState;
	private Set<VehicleSpecification> responsibilities;
	private List<AssemblyLineStateObserver> assemblyLineStateObservers;
	/**
	 * Construct a new AssemblyLine. Initializes a scheduler.
	 * 
	 * @param 	clock
	 *          The clock that has to be accessed by this AssemblyLine
	 *            
	 * @throws 	IllegalArgumentException
	 *             Thrown when one or both of the parameters are null
	 */
	public AssemblyLine(ClockObserver clockObserver, ImmutableClock clock, AssemblyLineState assemblyLineState, Set<VehicleSpecification> responsiblities) {
		if (clockObserver == null || clock == null || assemblyLineState == null || responsiblities==null) {
			throw new IllegalArgumentException();
		}
		workbenches = new ArrayList<IWorkBench>();
		currentJobs = new ArrayList<IJob>();
		observers = new ArrayList<>();
		assemblyLineStateObservers = new ArrayList<AssemblyLineStateObserver>();
		this.scheduler = new Scheduler(clockObserver, clock);
		this.assemblyLineState = assemblyLineState;
		this.responsibilities = responsiblities;
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
	public void addWorkBench(IWorkBench bench) {
		if (bench == null)
			throw new IllegalArgumentException();
		workbenches.add(bench);
	}

	/**
	 * This method advances the workbenches if all the workbenches are
	 * completed. It shifts the jobs to it's next workstation.
	 * It notifies its observers when an order is completed.
	 * 
	 * @throws 	NoSuitableJobFoundException
	 * 			Thrown when no job can be scheduled by the scheduler
	 */
	public void advance() throws NoSuitableJobFoundException {
		if (!canAdvance()) {
			throw new IllegalStateException();
		}

		Optional<IJob> lastJob = Optional.absent();
		for (int i = 0; i < getWorkbenches().size(); i++) {
			IWorkBench bench = getWorkbenches().get(i);
			if (i == 0) {
				lastJob = bench.getCurrentJob();
				bench.setCurrentJob(this.scheduler.retrieveNextJob());
			} else {
				Optional<IJob> prev = bench.getCurrentJob();
				bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob(); 
		}
		if (lastJob.isPresent()	&& lastJob.get().isCompleted()) {
			currentJobs.remove(lastJob.get());
			lastJob.get().getOrder().completeCar();
			updateCompletedOrder(lastJob.get().getOrder());
		}
	}

	@Override
	public boolean canAdvance() {
		List<IWorkBench> workBenches = getWorkbenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted())
				return false;
		return true;
	}

	/**
	 * This method first sets the index of the first workbench that has to complete some tasks
	 * of the job. Then the job is passed to the scheduler.
	 * 
	 * @param	job 
	 * 			The job that needs to be scheduled
	 * 
	 * @throws	IllegalArgumentException 
	 * 			Thrown when the given parameter is null
	 */
	public void schedule(IJob job) {
		if (job == null) {
			throw new IllegalArgumentException();
		}
		job.setMinimalIndex(getMinimalIndexOfWorkbench(job));
		this.scheduler.addJobToAlgorithm(job);


	}

	/**
	 * Method for returning the index of the first workbench needed to complete the
	 * given job. It returns a value of -1 when there's no workbench found to complete this job.
	 */
	private int getMinimalIndexOfWorkbench(IJob job) {
		for (int i = 0; i < getWorkbenches().size(); i++) {
			for (ITask task : job.getTasks()) {
				if (getWorkbenches().get(i).getResponsibilities().contains(task.getTaskDescription())) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public List<IWorkBench> getBlockingWorkBenches() {
		ArrayList<IWorkBench> notCompletedBenches = new ArrayList<>();
		List<IWorkBench> workBenches = getWorkbenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted())
				notCompletedBenches.add(new UnmodifiableWorkBench(this.workbenches.get(i)));
		return Collections.unmodifiableList(notCompletedBenches);
	}

	/**
	 * Returns an unmodifiable list of the current Jobs on the AssemblyLine.
	 */
	@Override
	public List<IJob> getCurrentJobs() {
		return Collections.unmodifiableList(currentJobs);
	}

	/**
	 * Returns the current Scheduling Algorithm used by the Scheduler used by this AssemblyLine.
	 */
	public String getCurrentSchedulingAlgorithm() {
		return this.scheduler.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return An unmodifiable list of IWorkBenches.
	 */
	@Override
	public List<IWorkBench> getWorkbenches() {
		return Collections.unmodifiableList(workbenches);
	}

	@Override
	public String toString() {
		String result = "Responsibilities: ";
		for(VehicleSpecification specification: getResponsibilities()){
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
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.scheduler.getAllCarOptionsInPendingOrders());
	}

	/**
	 * Method for asking the Scheduler to switch to the algorithm the given creator can create.
	 * 
	 * @param	creator is responsible for creating the correct SchedulingAlgorithm
	 */
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

	/**
	 * Matches the given workbench to one of its own. If a match is found, 
	 * the request is passed on.
	 * 
	 * @param	workbench to be matched
	 */
	public int completeChosenTaskAtChosenWorkBench(IWorkBench workbench, ITask task, ImmutableClock elapsed) {
		for (IWorkBench wb : this.workbenches) {
			if (wb.equals(workbench)) {
				wb.completeChosenTaskAtChosenWorkBench(task);
				this.scheduler.advanceInternalClock(elapsed);
				return this.scheduler.getTotalMinutesOfInternalClock();
			}
		}
		throw new IllegalStateException();
	}

	/**
	 * Returns an unmodifiable list containing all the pending StandardJobs.
	 */
	public List<IJob> getStandardJobs() {
		return Collections.unmodifiableList(this.scheduler.getStandardJobs());
	}

	@Override
	public void setState(AssemblyLineState state) {
		AssemblyLineState previousState = assemblyLineState;
		this.assemblyLineState = state;
		updatAssemblyLineState(previousState, state);
	}

	@Override
	public Set<VehicleSpecification> getResponsibilities() {
		return Collections.unmodifiableSet(responsibilities);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((assemblyLineState == null) ? 0 : assemblyLineState
						.hashCode());
		result = prime * result
				+ ((currentJobs == null) ? 0 : currentJobs.hashCode());
		result = prime * result
				+ ((observers == null) ? 0 : observers.hashCode());
		result = prime
				* result
				+ ((responsibilities == null) ? 0 : responsibilities.hashCode());
		result = prime * result
				+ ((scheduler == null) ? 0 : scheduler.hashCode());
		result = prime * result
				+ ((workbenches == null) ? 0 : workbenches.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IAssemblyLine other = null;
		try{
			other = (IAssemblyLine) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (assemblyLineState != other.getState())
			return false;
		if (currentJobs == null) {
			if (other.getCurrentJobs() != null)
				return false;
		} else if (!currentJobs.equals(other.getCurrentJobs()))
			return false;
		if (responsibilities == null) {
			if (other.getResponsibilities() != null)
				return false;
		} else if (!responsibilities.equals(other.getResponsibilities()))
			return false;
		if (workbenches == null) {
			if (other.getWorkbenches() != null)
				return false;
		} else if (!workbenches.equals(other.getWorkbenches()))
			return false;
		return true;
	}

	public List<IJob> removeUnscheduledJobs() {
		return Collections.unmodifiableList(scheduler.removeUnscheduledJobs());
	}

	@Override
	public void attachObserver(AssemblyLineStateObserver observer) {
		if(observer==null){
			throw new IllegalArgumentException();
		}
		assemblyLineStateObservers.add(observer);
	}

	@Override
	public void detachObserver(AssemblyLineStateObserver observer) {
		if(observer==null){
			throw new IllegalArgumentException();
		}
		assemblyLineStateObservers.remove(observer);
	}

	@Override
	public void updatAssemblyLineState(AssemblyLineState previousState,
			AssemblyLineState currentState) {
		for(AssemblyLineStateObserver observer: assemblyLineStateObservers){
			observer.updateAssemblyLineState(previousState, currentState);
		}
	}
}
