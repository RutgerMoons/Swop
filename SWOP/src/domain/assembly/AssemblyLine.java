package domain.assembly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.UnmodifiableException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.Job;
import domain.job.Task;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.IOrder;
import domain.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.IVehicleOption;

/**
 * Represents an AssemblyLine. It contains the workbenches and the current jobs on these workbenches.
 * It notifies the attached observers when an order is completed. Each assemblyLine has a scheduler.
 * 
 */
public class AssemblyLine implements IAssemblyLine{

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

	public void addWorkBench(IWorkBench bench) {
		if (bench == null)
			throw new IllegalArgumentException();
		workbenches.add(bench);
	}

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
			notifyObserverCompleteOrder(lastJob.get().getOrder().getEstimatedTime());
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
	 * This method converts an order to a list of Jobs, 1 for each car and sets
	 * the minimal index for each job. This index refers to the first workbench that is
	 * needed to complete the job. The method returns a list of Jobs.
	 * 
	 * @param order
	 *            The order that needs to be converted to a list of jobs.
	 * 
	 * @throws UnmodifiableException 
	 * 		Thrown when an IOrder has no deadline yet.
	 * 
	 * @throws IllegalArgumentException
	 *       Thrown when the given parameter is null
	 */
	private List<IJob> convertOrderToJob(IOrder order) throws UnmodifiableException {
		List<IJob> jobs = new ArrayList<>();
		for (int i = 0; i < order.getQuantity(); i++) {
			IJob job = new Job(order);
			for (IVehicleOption part : order.getVehicleOptions()) {
				ITask task = new Task(part.getTaskDescription());
				IAction action = new Action(part.getActionDescription());
				task.addAction(action);
				job.addTask(task);
			}
			job.setMinimalIndex(getMinimalIndexOfWorkbench(job));
			jobs.add(job);
		}
		return new ImmutableList.Builder<IJob>().addAll(jobs).build();
	}


	public int convertCustomOrderToJob(CustomOrder order) throws UnmodifiableException{
		if (order == null) {
			throw new IllegalArgumentException();
		}
		List<IJob> jobs = convertOrderToJob(order);
		for (IJob job : jobs) {
			this.scheduler.addJobToAlgorithm(job);
		}
		return scheduler.getEstimatedTimeInMinutes(jobs.get(jobs.size() - 1));
	}


	public int convertStandardOrderToJob(StandardOrder order) throws UnmodifiableException{
		if (order == null) {
			throw new IllegalArgumentException();
		}
		List<IJob> jobs = convertOrderToJob(order);
		for (IJob job : jobs) {
			this.scheduler.addJobToAlgorithm(job);
		}

		return scheduler.getEstimatedTimeInMinutes(jobs.get(jobs.size() - 1));
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

	public void notifyObserverCompleteOrder(ImmutableClock aClock) {
		for (AssemblyLineObserver observer : observers) {
			observer.updateCompletedOrder(aClock);
		}
	}
	
	public Set<Set<IVehicleOption>> getAllCarOptionsInPendingOrders() {
		return this.scheduler.getAllCarOptionsInPendingOrders();
	}

	@Override
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.scheduler.switchToAlgorithm(creator, this.workbenches.size());
	}

	@Override
	public AssemblyLineState getState() {
		return assemblyLineState;
	}

}
