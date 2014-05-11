package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observable.ObservableAssemblyLine;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.vehicleOption.IVehicleOption;

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

	/**
	 * Construct a new AssemblyLine. Initializes a scheduler and an amount of workbenches.
	 * 
	 * @param clock
	 *            The clock that has to be accessed by this AssemblyLine.
	 *            
	 * @throws IllegalArgumentException
	 *             Thrown when one or both of the parameters are null.
	 */
	public AssemblyLine(ClockObserver clockObserver, ImmutableClock clock, AssemblyLineState assemblyLineState) {
		if (clockObserver == null || clock == null || assemblyLineState == null) {
			throw new IllegalArgumentException();
		}
		workbenches = new ArrayList<IWorkBench>();
		currentJobs = new ArrayList<IJob>();
		initializeWorkbenches();
		observers = new ArrayList<>();
		this.scheduler = new Scheduler(workbenches.size(), clockObserver, clock);
		this.assemblyLineState = assemblyLineState;
	}

	/**
	 * Add a workbench to the assemblyLine.
	 * 
	 * @param bench
	 *            The workbench you want to add.
	 * @throws IllegalArgumentException
	 *             Thrown when the parameter is null.
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
	 * @throws NoSuitableJobFoundException
	 * 		Thrown when no job can be scheduled by the scheduler.
	 *  
	 * @throws UnmodifiableException 
	 * 		Thrown when an IOrder has no deadline yet.
	 * 
	 * @throws IllegalStateException
	 * 		Thrown when the assemblyLine cannot advance.
	 */
	public void advance() throws UnmodifiableException, NoSuitableJobFoundException {
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
			updateCompletedOrder(lastJob.get().getOrder().getEstimatedTime());
		}
	}

	
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
	 * @param	job that needs to be scheduled
	 * 
	 * @throws	IllegalArgumentException when the given parameter is null
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

	public ArrayList<Integer> getBlockingWorkBenches() {
		ArrayList<Integer> notCompletedBenches = new ArrayList<Integer>();
		List<IWorkBench> workBenches = getWorkbenches();
		for (int i = 0; i < workBenches.size(); i++)
			if (!workBenches.get(i).isCompleted())
				notCompletedBenches.add(i + 1);
		return notCompletedBenches;
	}

	public List<IJob> getCurrentJobs() {
		return new ImmutableList.Builder<IJob>().addAll(currentJobs).build();
	}
	
	public Scheduler getCurrentScheduler() {
		return this.scheduler;
	}
	
	public String getCurrentSchedulingAlgorithm() {
		return this.scheduler.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return A list of IWorkBenches.
	 */
	public List<IWorkBench> getWorkbenches() {
		return workbenches;
	}

	/**
	 * Initializes the workbenches at the start of the program.
	 * 
	 */
	private void initializeWorkbenches() {
		Set<String> responsibilitiesCarBodyPost = new HashSet<>();
		responsibilitiesCarBodyPost.add("Paint");
		responsibilitiesCarBodyPost.add("Assembly");
		addWorkBench(new WorkBench(responsibilitiesCarBodyPost, "car body"));

		Set<String> responsibilitiesDrivetrainPost = new HashSet<>();
		responsibilitiesDrivetrainPost.add("Engine");
		responsibilitiesDrivetrainPost.add("Gearbox");
		addWorkBench(new WorkBench(responsibilitiesDrivetrainPost, "drivetrain"));

		Set<String> responsibilitiesAccesoiresPost = new HashSet<>();
		responsibilitiesAccesoiresPost.add("Seats");
		responsibilitiesAccesoiresPost.add("Airco");
		responsibilitiesAccesoiresPost.add("Wheels");
		addWorkBench(new WorkBench(responsibilitiesAccesoiresPost,
				"accessories"));

	}

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

	public void attachObserver(AssemblyLineObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.add(observer);
	}

	public void detachObserver(AssemblyLineObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.remove(observer);
	}

	@Override
	public void updateCompletedOrder(ImmutableClock aClock) {
		for (AssemblyLineObserver observer : observers) {
			observer.updateCompletedOrder(aClock);
		}
	}
	
	public Set<Set<IVehicleOption>> getAllCarOptionsInPendingOrders() {
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

	@Override
	public AssemblyLineState getState() {
		return assemblyLineState;
	}

}
