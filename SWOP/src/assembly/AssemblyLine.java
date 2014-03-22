package assembly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import order.Order;
import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import clock.Clock;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * 
 * Represents an AssemblyLine. It contains the workbenches and the pending jobs.
 * It also stores the overtime of the day before.
 * 
 */
public class AssemblyLine {

	private Clock clock;
	private List<WorkBench> workbenches;
	private List<Job> currentJobs;
	private int overtime;

	/**
	 * Construct a new AssemblyLine.
	 * 
	 * @param clock
	 *            The clock that has to be accessed by this AssemblyLine.
	 * @throws IllegalArgumentException
	 *             If clock==null
	 */
	public AssemblyLine(Clock clock) {
		if (clock == null)
			throw new IllegalArgumentException();
		this.clock = clock;
		workbenches = new ArrayList<WorkBench>();
		currentJobs = new ArrayList<Job>();
		initializeWorkbenches();
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

	/**
	 * Get the WorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return A list of WorkBenches.
	 */
	public List<WorkBench> getWorkbenches() {
		ImmutableList<WorkBench> immutable = new ImmutableList.Builder<WorkBench>()
				.addAll(workbenches).build();
		return immutable;
	}

	/**
	 * Assign a list of workbenches to this AssemblyLine.
	 * 
	 * @param workbenches
	 *            A list of WorkBenches.
	 * @throws IllegalArgumentException
	 *             If workbenches==null
	 */
	public void setWorkbenches(List<WorkBench> workbenches) {
		if (workbenches == null)
			throw new IllegalArgumentException();
		this.workbenches = workbenches;
	}

	/**
	 * Get all the pending jobs for this AssemblyLine.
	 * 
	 * @return A list of Jobs.
	 */
	public List<Job> getCurrentJobs() {
		ImmutableList<Job> immutable = new ImmutableList.Builder<Job>().addAll(
				currentJobs).build();
		return immutable;
	}

	/**
	 * Assign a list of jobs you want to the AssemblyLine.
	 * 
	 * @param currentJobs
	 *            A list of Jobs.
	 * @throws IllegalArgumentException
	 *             If currentJobs==null
	 */
	public void setCurrentJobs(List<Job> currentJobs) {
		if (currentJobs == null)
			throw new IllegalArgumentException();
		this.currentJobs = currentJobs;
	}

	/**
	 * Get the overtime of the previous day.
	 * 
	 * @return An integer representing the overtime.
	 */
	public int getOvertime() {
		return overtime;
	}

	/**
	 * Set the overtime.
	 * 
	 * @param overtime
	 *            An integer representing the overtime.
	 * @throws IllegalArgumentException
	 *             If overtime<0
	 */
	public void setOvertime(int overtime) {
		if (overtime < 0)
			throw new IllegalArgumentException();
		this.overtime = overtime;
	}

	/**
	 * Get the clock.
	 * 
	 * @return The Clock that's available.
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * Add a Job to the assemblyline.
	 * 
	 * @param job
	 *            The job you want to add.
	 * @throws IllegalArgumentException
	 *             If job==null
	 * 
	 */
	public void addJob(Job job) {
		if (job == null)
			throw new IllegalArgumentException();
		else
			currentJobs.add(job);
	}

	public void addMultipleJobs(List<Job> jobs) {
		currentJobs.addAll(jobs);
	}

	/**
	 * Add a workbench to the assemblyline.
	 * 
	 * @param bench
	 *            The workbench you want to add.
	 * @throws IllegalArgumentException
	 *             If bench==null
	 */
	public void addWorkBench(WorkBench bench) {
		if (bench == null)
			throw new IllegalArgumentException();
		else
			workbenches.add(bench);
	}

	/**
	 * This method advances the workbenches if all the workbenches are
	 * completed. It shifts the jobs to it's next workstation.
	 * 
	 * @throws IllegalStateException
	 *             If there are no currentJobs
	 */
	public void advance() {
		if (getCurrentJobs().size() == 0) // als er geen volgende jobs zijn.
			throw new IllegalStateException(
					"You can't advance if there is no next Job!");

		Optional<Job> lastJob = Optional.absent();
		for (int i = 0; i < getWorkbenches().size(); i++) {
			WorkBench bench = getWorkbenches().get(i);
			if (i == 0) { // als het de eerste workbench is (de meest linkse op
				// tekeningen, dan moet je een nieuwe job nemen.
				if (bench.getCurrentJob() != null)
					lastJob = bench.getCurrentJob();
				else
					lastJob = Optional.absent();
				if ((22 * 60 - clock.getMinutes() - (overtime * 60)) < (getWorkbenches()
						.size() * 60)) {
					Optional<Job> optional = Optional.absent();
					bench.setCurrentJob(optional);
				} else if (bench.getCurrentJob() == null) {
					bench.setCurrentJob(Optional.fromNullable(getCurrentJobs()
							.get(0)));
				}
			} else { // Als het niet de eerste is, moet je de job van de vorige
				// workbench nemen.
				Optional<Job> prev = bench.getCurrentJob();
				bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob(); // dan de taken laten selecteren door
			// de workbench
		}
		if (lastJob != null && lastJob.isPresent()
				&& lastJob.get().isCompleted()) {
			currentJobs.remove(lastJob.get()); // als de job completed is,
												// dus de auto('s), dan moet
												// je de job natuurlijk
												// removen.
			lastJob.get().getOrder().completeCar();
		}

		if ((22 * 60 - getClock().getMinutes()) < 0) {// overtime zetten
			overtime = Math.abs(22 * 60 - getClock().getMinutes());
		}
	}

	/**
	 * This method converts an order to a list of Jobs, 1 for each car.
	 * 
	 * @param order
	 *            The order that needs to be converted to a list of jobs.
	 * @return A list of jobs.
	 */
	public List<Job> convertOrderToJob(Order order) {
		if (order == null)
			throw new IllegalArgumentException();

		CarModel model = order.getDescription();

		// Het is lelijk ik weet het, maar het kan echt niet anders.. denk ik
		List<Job> jobs = new ArrayList<>();
		for (int i = 0; i < order.getQuantity(); i++) {
			Job job = new Job(order);

			Task assembly = new Task("Assembly");
			Action actionAssembly = new Action("Put on a "
					+ model.getCarParts().get(Body.class) + "body");
			assembly.addAction(actionAssembly);

			Task paint = new Task("Paint");
			Action actionPaint = new Action("Paint car "
					+ model.getCarParts().get(Color.class));
			paint.addAction(actionPaint);

			Task engine = new Task("Engine");
			Action actionEngine = new Action("Install the "
					+ model.getCarParts().get(Engine.class) + " engine");
			engine.addAction(actionEngine);

			Task gearbox = new Task("Gearbox");
			Action actionGearbox = new Action("Put in the "
					+ model.getCarParts().get(Gearbox.class) + " gearbox");
			gearbox.addAction(actionGearbox);

			Task seats = new Task("Seats");
			Action actionSeats = new Action("Install "
					+ model.getCarParts().get(Seat.class) + " seats");
			seats.addAction(actionSeats);

			Task airco = new Task("Airco");
			Action actionAirco = new Action("Install the "
					+ model.getCarParts().get(Airco.class) + " airco");
			airco.addAction(actionAirco);

			Task wheels = new Task("Wheels");
			Action actionWheels = new Action("Put on "
					+ model.getCarParts().get(Wheel.class) + " wheels");
			wheels.addAction(actionWheels);

			job.addTask(paint);
			job.addTask(assembly);
			job.addTask(engine);
			job.addTask(gearbox);
			job.addTask(seats);
			job.addTask(airco);
			job.addTask(wheels);

			jobs.add(job);
		}
		return jobs;
	}

	/**
	 * Set the estimated time when all the jobs are completed.
	 * 
	 * The Jobs from the order have to be added to the list of currentJobs.
	 * 
	 * 
	 * @param order
	 *            The order to set the estimated time to.
	 */
	public void calculateEstimatedTime(Order order) {
		if (getWorkbenches().size() == 0)
			throw new IllegalStateException("There are no workbenches!");
		int indexLastJob = getIndexOf(order);
		if (indexLastJob < 0)
			throw new IllegalStateException(
					"The jobs of the order aren't added to the currentJobs");
		int[] array = new int[2];
		int days = 0;
		int time = 0;
		int timeOnWorkBench = getWorkbenches().size() * 60; // 1 uur per
		// workbench
		int timeTillFirstWorkbench = 0;
		if (getWorkbenches().get(0).getCurrentJob() != null
				&& getWorkbenches().get(0).getCurrentJob().isPresent()) {
			// Hoeveel jobs er nog voorstaan in de wachtrij.

			int indexJobOnFirstWorkbench = (getCurrentJobs()
					.indexOf(getWorkbenches().get(0).getCurrentJob().get()));
			timeTillFirstWorkbench = (indexLastJob - indexJobOnFirstWorkbench) * 60;
		} else {
			timeTillFirstWorkbench = indexLastJob * 60;
		}

		time = timeOnWorkBench + timeTillFirstWorkbench;

		int tenOClockInMinutes = 22 * 60;
		int lastCarOnFirstBench = tenOClockInMinutes - getWorkbenches().size()
				* 60;

		int beginTime = 6 * 60; // Om 6:00u beginnen ze te werken
		int nbOfCarsPerDay = (lastCarOnFirstBench - beginTime) / 60; // 1 auto
		// per
		// uur
		int nbOfCarsToday = (lastCarOnFirstBench - clock.getMinutes()) / 60; // aantal
		// auto's
		// die
		// vandaag
		// nog
		// kunnen
		// verwerkt
		// worden.
		if (nbOfCarsToday > nbOfCarsPerDay)
			nbOfCarsToday = nbOfCarsPerDay;

		if (timeTillFirstWorkbench / 60 <= nbOfCarsToday) {
			days = clock.getDay();
			time += clock.getMinutes();
		} else {
			days = 1; // je weet al dat vandaag het niet gaat lukken
			timeTillFirstWorkbench -= nbOfCarsToday * 60;
			days += (timeTillFirstWorkbench / 60) / nbOfCarsPerDay
					+ clock.getDay();
			time = (timeTillFirstWorkbench / 60) % nbOfCarsPerDay * 60
					+ beginTime;
		}

		array[0] = days;
		array[1] = time;
		order.setEstimatedTime(array);
	}

	private int getIndexOf(Order order) {
		int index = -1;
		for (Job job : getCurrentJobs())
			if (job.getOrder().equals(order))
				index = getCurrentJobs().indexOf(job) + order.getQuantity();
		return index;
	}

	/**
	 * Get a clone of this AssemblyLine, advanced 1 hour forward.
	 * 
	 * @return An AssemblyLine.
	 */
	public AssemblyLine getFutureAssemblyLine() {
		AssemblyLine line = new AssemblyLine(getClock());
		ArrayList<WorkBench> clones = new ArrayList<>();
		line.setCurrentJobs(getCurrentJobs());
		for (WorkBench bench : getWorkbenches()) {
			WorkBench copy = new WorkBench(bench.getResponsibilities(),
					bench.getWorkbenchName());
			copy.setCurrentJob(bench.getCurrentJob());
			copy.setCurrentTasks(bench.getCurrentTasks());
			clones.add(copy);
		}
		line.setWorkbenches(clones);
		try {
			line.advance();

		} catch (IllegalStateException e) {
		}
		return line;
	}

	@Override
	public String toString() {
		String assemblyLineString = "";
		WorkBench workbench;
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
}
