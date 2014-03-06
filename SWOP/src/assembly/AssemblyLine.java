package assembly;

import java.util.ArrayList;
import java.util.List;

import order.Order;
import clock.Clock;

/**
 * 
 * Represents an AssemblyLine. It contains the workbenches and the pending jobs. It also stores the overtime of the day before.
 *
 */
public class AssemblyLine {

	private Clock clock;
	private List<WorkBench> workbenches;
	private List<Job> currentJobs;
	private int overtime;
	/**
	 * Construct a new AssemblyLine.
	 * @param clock
	 * 			The clock that has to be accessed by this AssemblyLine.
	 */
	public AssemblyLine(Clock clock){
		this.clock = clock;
		workbenches = new ArrayList<WorkBench>();
		currentJobs = new ArrayList<Job>();
		initializeWorkbenches();
	}

	private void initializeWorkbenches() {
		List<String> responsibilitiesCarBodyPost = new ArrayList<>();
		responsibilitiesCarBodyPost.add("Paint"); 
		responsibilitiesCarBodyPost.add("Assembly");
		getWorkbenches().add(new WorkBench(responsibilitiesCarBodyPost));

		List<String> responsibilitiesDrivetrainPost = new ArrayList<>();
		responsibilitiesDrivetrainPost.add("Engine"); 
		responsibilitiesDrivetrainPost.add("Gearbox");
		getWorkbenches().add(new WorkBench(responsibilitiesDrivetrainPost));

		List<String> responsibilitiesAccesoiresPost = new ArrayList<>();
		responsibilitiesAccesoiresPost.add("Seats"); 
		responsibilitiesAccesoiresPost.add("Airco");
		responsibilitiesAccesoiresPost.add("Wheels");
		getWorkbenches().add(new WorkBench(responsibilitiesAccesoiresPost));

	}

	/**
	 * 
	 * @return
	 * 			All the workbenches that are available for this assemblyline.
	 */
	public List<WorkBench> getWorkbenches() {
		return workbenches;
	}

	/**
	 * 
	 * @param workbenches
	 * 			The list of workbenches you want to allocate to the assemblyline.
	 * @throws IllegalArgumentException
	 * 			If workbenches==null
	 */
	public void setWorkbenches(List<WorkBench> workbenches) {
		if(workbenches==null)
			throw new IllegalArgumentException();
		this.workbenches = workbenches;
	}

	/**
	 * 
	 * @return
	 * 			A list of all the pending jobs for this assemblyline.
	 */
	public List<Job> getCurrentJobs() {
		return currentJobs;
	}

	/**
	 * 
	 * @param currentJobs
	 * 			The list of jobs you want to allocate to the assemblyline.
	 * @throws IllegalArgumentException
	 * 			If currentJobs==null
	 */
	public void setCurrentJobs(List<Job> currentJobs) {
		if(currentJobs==null)
			throw new IllegalArgumentException();
		this.currentJobs = currentJobs;
	}

	/**
	 * 
	 * @return
	 * 			The overtime of the previous day.
	 */
	public int getOvertime() {
		return overtime;
	}

	/**
	 * 
	 * @param overtime
	 * 			Set the overtime of the previous day.
	 * @throws IllegalArgumentException
	 * 			If overtime<0
	 */
	public void setOvertime(int overtime) {
		if(overtime<0)
			throw new IllegalArgumentException();
		this.overtime = overtime;
	}

	/**
	 * 
	 * @return
	 * 			The clock that's available.
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * Add a Job to the assemblyline.
	 * @param job
	 * 			The job you want to add.
	 * @throws IllegalArgumentException
	 * 			If job==null
	 * 
	 */
	public void addJob(Job job){
		if(job==null)
			throw new IllegalArgumentException();
		else getCurrentJobs().add(job);
	}

	/**
	 * Add a workbench to the assemblyline.
	 * @param bench
	 * 			The workbench you want to add.
	 * @throws IllegalArgumentException
	 * 			If bench==null
	 */
	public void addWorkBench(WorkBench bench){
		if(bench==null)
			throw new IllegalArgumentException();
		else getWorkbenches().add(bench);
	}	

	/**
	 * This method advances the workbenches if all the workbenches are completed. It shifts the jobs to it's next workstation.
	 */
	public void advance(){
		//kijken of alle workbenches completed zijn, anders moogt ge ni advancen.
		for(WorkBench bench: getWorkbenches())
			if(!bench.isCompleted())
				return;

		Job lastJob = null;
		for(int i=0; i<getWorkbenches().size(); i++){
			WorkBench bench = getWorkbenches().get(i);	
			if(i==0){	//als het de eerste workbench is (de meest linkse op tekeningen, dan moet je een nieuwe job nemen.
				lastJob = bench.getCurrentJob();
				if(bench.getCurrentJob()==null){	//Dit is voor bij de start van een nieuwe werkdag, dan heeft de workbench geen currentjob
					bench.setCurrentJob(getCurrentJobs().get(0));	//de eerste uit de lijst halen dus.
				}else{
					int index = getCurrentJobs().indexOf(bench.getCurrentJob());	
					if((index+1)<getCurrentJobs().size()){		//om indexoutofbounds te voorkomen
						bench.setCurrentJob(getCurrentJobs().get(index+1));
					}else bench.setCurrentJob(null);	//als er geen nieuwe jobs meer zijn, dan moet je zeggen dat de workbench niets te doen heeft
				}
			}else{ //Als het niet de eerste is, moet je de job van de vorige workbench nemen.
				Job prev = bench.getCurrentJob();
				bench.setCurrentJob(lastJob);
				lastJob = prev;
			}
			bench.chooseTasksOutOfJob();	//dan de taken laten selecteren door de workbench
		}
		if(lastJob !=null && lastJob.isCompleted()){
			getCurrentJobs().remove(lastJob);	//als de job completed is, dus de auto('s), dan moet je de job natuurlijk removen.
			lastJob.getOrder().completeCar();
		}
	}


	/**
	 * This method converts an order to a list of Jobs, 1 for each car.
	 * @param order
	 * 			The order that needs to be converted to a list of jobs.
	 * @return
	 * 			A list of jobs.
	 */
	public List<Job> convertOrderToJob(Order order){
		if(order==null)
			throw new IllegalArgumentException();

		List<Job> jobs = new ArrayList<>();
		for(int i=0; i<order.getQuantity(); i++){
			Job job = new Job(order);
			//CarModel model = order.getDescription();


			Task paint = new Task("Paint");
			Action actionPaint = new Action("Paint");
			paint.addAction(actionPaint);

			Task assembly = new Task("Assembly");
			Action actionAssembly = new Action("Assembly");
			assembly.addAction(actionAssembly);

			Task engine = new Task("Engine");
			Action actionEngine = new Action("Engine");
			engine.addAction(actionEngine);

			Task gearbox = new Task("Gearbox");
			Action actionGearbox = new Action("Gearbox");
			gearbox.addAction(actionGearbox);

			Task seats = new Task("Seats");
			Action actionSeats = new Action("Seats");
			seats.addAction(actionSeats);

			Task airco = new Task("Airco");
			Action actionAirco = new Action("Airco");
			airco.addAction(actionAirco);

			Task wheels = new Task("Wheels");
			Action actionWheels = new Action("Wheels");
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
	 * Get the estimated time when all this jobs are completed.
	 * @param jobs
	 * 			The jobs you want to check on.
	 * @return
	 */
	public int getEstimatedTime(List<Job> jobs){
		int time = getWorkbenches().size(); //1 uur per workbench




		return time;
	}

}
