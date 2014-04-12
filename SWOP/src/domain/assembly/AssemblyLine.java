package domain.assembly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import domain.car.CarOption;
import domain.car.ICarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
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
import domain.scheduler.Scheduler;

/**
 * 
 * Represents an AssemblyLine. It contains the workbenches and the pending jobs.
 * It also stores the overtime of the day before.
 * 
 */
public class AssemblyLine {

	private List<IJob> currentJobs;
	private List<IWorkBench> workbenches;
	private ArrayList<AssemblyLineObserver> observers;
	private Scheduler scheduler;

	/**
	 * Construct a new AssemblyLine.
	 * 
	 * @param clock
	 *            The clock that has to be accessed by this AssemblyLine.
	 * @throws IllegalArgumentException
	 *             If clock==null
	 */
	public AssemblyLine(ClockObserver clockObserver) {
		if (clockObserver == null) {
			throw new IllegalArgumentException();
		}
		workbenches = new ArrayList<IWorkBench>();
		currentJobs = new ArrayList<IJob>();
		initializeWorkbenches();
		this.scheduler = new Scheduler(workbenches.size(), clockObserver);
	}

	/**
	 * Add a workbench to the assemblyline.
	 * 
	 * @param bench
	 *            The workbench you want to add.
	 * @throws IllegalArgumentException
	 *             If bench==null
	 */
	public void addWorkBench(IWorkBench bench) {
		if (bench == null)
			throw new IllegalArgumentException();
		workbenches.add(bench);
	}

	/**
	 * This method advances the workbenches if all the workbenches are
	 * completed. It shifts the jobs to it's next workstation.
	 * @throws NoSuitableJobFoundException 
	 * @throws ImmutableException 
	 * @throws NotImplementedException 
	 * 
	 * @throws IllegalStateException
	 *             If there are no currentJobs
	 */
	public void advance() throws ImmutableException, NoSuitableJobFoundException, NotImplementedException {
		if (!canAdvance()) {
			throw new IllegalStateException();
		}
		
		Optional<IJob> lastJob = Optional.absent();
		for (int i = 0; i < getWorkbenches().size(); i++) {
			IWorkBench bench = getWorkbenches().get(i);
			if (i == 0) {
				lastJob = bench.getCurrentJob();
				bench.setCurrentJob(this.scheduler.retrieveNextJob());
			} else { // Als het niet de eerste is, moet je de job van de vorige
				// workbench nemen.
				Optional<IJob> prev = bench.getCurrentJob();
				bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob(); // dan de taken laten selecteren door
			// de workbench
		}
		if (lastJob.isPresent()	&& lastJob.get().isCompleted()) {
			currentJobs.remove(lastJob.get()); // als de job completed is,
												// dus de auto('s), dan moet
												// je de job natuurlijk
												// removen.
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
	 * This method converts an order to a list of Jobs, 1 for each car.
	 * 
	 * @param order
	 *            The order that needs to be converted to a list of jobs.
	 * @return A list of jobs.
	 * @throws ImmutableException 
	 * 
	 * @throws IllegalArgumentException
	 *             if order==null
	 */
	public List<IJob> convertOrderToJob(IOrder order) throws ImmutableException {
		if (order == null) {
			throw new IllegalArgumentException();
		}
		
		ICarModel model = order.getDescription();
		List<IJob> jobs = new ArrayList<>();
		for (int i = 0; i < order.getQuantity(); i++) {
			IJob job = new Job(order);
			for (CarOption part : model.getCarParts().values()) {
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
	
	public int convertCustomOrderToJob(CustomOrder order) throws ImmutableException, NotImplementedException {
		if (order == null) {
			throw new IllegalArgumentException();
		}
		List<IJob> jobs = convertOrderToJob(order);
		for (IJob job : jobs) {
			this.scheduler.addCustomJob(job);
		}
		
		return scheduler.getEstimatedTimeInMinutes(jobs.get(0));
	}
	
	public int convertStandardOrderToJob(StandardOrder order) throws ImmutableException, NotImplementedException {
		if (order == null) {
			throw new IllegalArgumentException();
		}
		List<IJob> jobs = convertOrderToJob(order);
		for (IJob job : jobs) {
			this.scheduler.addStandardJob(job);
		}
		
		return scheduler.getEstimatedTimeInMinutes(jobs.get(0));
	}
	
	public int getMinimalIndexOfWorkbench(IJob job) {
		if (job == null) {
			throw new IllegalArgumentException();
		}
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

	/**
	 * Get all the pending jobs for this AssemblyLine.
	 * 
	 * @return A list representing the current jobs.
	 */
	public List<IJob> getCurrentJobs() {
		return new ImmutableList.Builder<IJob>().addAll(currentJobs).build();
	}

	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return A list of IWorkBenches.
	 */
	public List<IWorkBench> getWorkbenches() {
		return new ImmutableList.Builder<IWorkBench>().addAll(workbenches)
				.build();
	}

	/**
	 * Initializes the workbenches at the start of the program.
	 * 
	 */
	private void initializeWorkbenches() {// gemakkelijk om een nieuwe workbench
		// toe te voegen om te initializeren
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
	
	public void notifyObserverCompleteOrder(UnmodifiableClock aClock) {
		for (AssemblyLineObserver observer : observers) {
			observer.updateCompletedOrder(aClock);
		}
	}
	
	public int getEstimatedTimeInMinutes(IJob job, UnmodifiableClock currentTime) throws NotImplementedException {
		return this.scheduler.getEstimatedTimeInMinutes(job);
	}
	
}
