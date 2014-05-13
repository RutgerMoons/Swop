package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import domain.assembly.workBench.IWorkBench;
import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.job.job.IJob;
import domain.job.task.ITask;
import domain.observer.observable.ObservableAssemblyLine;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.order.IOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Represents an AssemblyLine. It contains the workbenches and the current jobs on these workbenches.
 * It notifies the attached observers when an order is completed. Each assemblyLine has a scheduler.
 * 
 */
public class AssemblyLine implements IAssemblyLine, ObservableAssemblyLine {

	private List<IJob> currentJobs;
	private List<IWorkBench> workbenches;
	private List<AssemblyLineObserver> observers;
	private Scheduler scheduler;
	private AssemblyLineState assemblyLineState;
	private Set<VehicleSpecification> responsibilities;

	/**
	 * Construct a new AssemblyLine. Initializes a scheduler and an amount of workbenches.
	 * 
	 * @param 	clock
	 *            The clock that has to be accessed by this AssemblyLine
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
		this.scheduler = new Scheduler(clockObserver, clock);
		this.assemblyLineState = assemblyLineState;
		this.responsibilities = responsiblities;
	}

	/**
	 * Add a workbench to the assemblyLine.
	 * 
	 * @param 	bench
	 *            The workbench you want to add
	 * @throws 	IllegalArgumentException
	 *             Thrown when the parameter is null
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
	 * 				Thrown when no job can be scheduled by the scheduler
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

	//TODO Doc
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
	 * 				The job that needs to be scheduled
	 * 
	 * @throws	IllegalArgumentException 
	 * 				Thrown when the given parameter is null
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

	//TODO doc
	@Override
	public ArrayList<Integer> getBlockingWorkBenches() {
		ArrayList<Integer> notCompletedBenches = new ArrayList<Integer>();
		List<IWorkBench> workBenches = getWorkbenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted())
				notCompletedBenches.add(i + 1);
		return notCompletedBenches;
	}

	/**
	 * Returns an Immutable list of the current jobs on the assemblyLine
	 */
	@Override
	public List<IJob> getCurrentJobs() {
		return new ImmutableList.Builder<IJob>().addAll(currentJobs).build();
	}
	
	/**
	 * Returns the current scheduler used by the AssemblyLine;
	 */
	@Override
	public Scheduler getCurrentScheduler() {
		return this.scheduler;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getCurrentSchedulingAlgorithm() {
		return this.scheduler.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return A list of IWorkBenches.
	 */
	@Override
	public List<IWorkBench> getWorkbenches() {
		return workbenches;
	}



	//TODO change to string
	@Override
	public String toString() {
		String assemblyLineString = "";
		IWorkBench workbench;
		String completed;
		for (int i = 0; i < this.getWorkbenches().size(); i++) {
			workbench = this.getWorkbenches().get(i);
			assemblyLineString += "," + "-workbench " + (i + 1) + ": "
					+ this.getWorkbenches().get(i).getWorkbenchName();
			for (int j = 0; j < workbench.getCurrentTasks().size(); j++) {
				if (workbench.getCurrentTasks().get(j).isCompleted()) {
					completed = "completed";
				} else {
					completed = "not completed";
				}
				assemblyLineString += ","
						+ "  *"
						+ workbench.getCurrentTasks().get(j)
						.getTaskDescription() + ": " + completed;
			}
		}
		return assemblyLineString.replaceFirst(",", "");
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
		return this.scheduler.getAllCarOptionsInPendingOrders();
	}

	/**
	 * Method for asking the scheduler to switch to the algorithm the given creator can create.
	 * 
	 * @param	creator is responsible for creating the correct SchedulingAlgorithm
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.scheduler.switchToAlgorithm(creator, this.workbenches.size());
	}

	/**
	 * Method for returning the current state of the assemblyLine.
	 */
	@Override
	public AssemblyLineState getState() {
		return assemblyLineState;
	}
	
	//TODO: equals..
	
	/**
	 * Matches the given workbench to one of its own. If a match is found, 
	 * the request is passed on.
	 * 
	 * @param	workbench to be matched
	 */
	public void completeChosenTaskAtChosenWorkBench(IWorkBench workbench, ITask task){
		for (IWorkBench wb : this.workbenches) {
			if (wb.equals(workbench)) {
				wb.completeChosenTaskAtChosenWorkBench(task);
				break;
			}
		}
	}
	
	/**
	 * returns a list containing all the pending standard jobs (no specific order)
	 */
	public List<IJob> getStandardJobs() {
		return this.scheduler.getStandardJobs();
	}

	@Override
	public void setState(AssemblyLineState state) {
		this.assemblyLineState = state;
		
	}

	@Override
	public Set<VehicleSpecification> getResponsibilities() {
		return responsibilities;
	}
	
}
