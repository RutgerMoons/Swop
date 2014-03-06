package assembly;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a WorkBench from an assemblyline.
 *
 */
public class WorkBench {

	private List<String> responsibilities;
	private Job currentJob;
	private List<Task> currentTasks;
	
	/**
	 * Construct a new Workbench.
	 * @param responsibilities
	 * 			A list of strings. The types of Tasks that have to be performed by this WorkBench.
	 */
	public WorkBench(List<String> responsibilities){
		this.setResponsibilities(responsibilities);
		setCurrentTasks(new ArrayList<Task>());
	}
	
	/**
	 * Get the current Job(Car) that is on this WorkBench. 
	 * @return
	 * 			The current Job this WorkBench is working on.
	 */
	public Job getCurrentJob() {
		return currentJob;
	}

	/**
	 * Allocate a new Job(Car) to this WorkBench.
	 * @param currentJob
	 * 				The job you want to allocate to the WorkBench.
	 */
	public void setCurrentJob(Job currentJob) {
		this.currentJob = currentJob;		//deze mag null zijn, aan het begin van de dag, het eind van de dag en indien
											//er geen auto's meer geproduceerd moeten worden.
	}
	
	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks that have to be performed by this workbench. 
	 * @return
	 * 			A list of responsibilities.
	 */
	public List<String> getResponsibilities() {
		return responsibilities;
	}
	
	/**
	 * Set a new list of responsibilities for thies WorkBench.
	 * @param responsibilities
	 * 			A list of responsibilities.
	 * @throws IllegalArgumentException
	 * 			If responsibilities==null
	 */
	public void setResponsibilities(List<String> responsibilities) {
		if(responsibilities==null)
			throw new IllegalArgumentException();
		this.responsibilities = responsibilities;
	}
	
	/**
	 * Add a responsibility to this WorkBench.
	 * @param responibility
	 * 			The responsibility you want to add.
	 * @throws IllegalArgumentException
	 * 			If responsibility==null or if the responsibility is empty.
	 */
	public void addResponsibility(String responibility){
		if(responibility==null || responibility.equals(""))
			throw new IllegalArgumentException();
		getResponsibilities().add(responibility);
	}
	
	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * @return
	 * 			A list of tasks.
	 */
	public List<Task> getCurrentTasks() {
		return currentTasks;
	}
	
	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * @param currentTasks
	 * 			A list of tasks.
	 * @throws IllegalArgumentException
	 * 			If currentTasks==null
	 */
	public void setCurrentTasks(List<Task> currentTasks) {
		if(currentTasks==null)
			throw new IllegalArgumentException();
		this.currentTasks = currentTasks;
	}
	
	/**
	 * Selects the Tasks that are valid for this Workbench. 
	 * The taskDescription is checked against the responsibilities.
	 */
	public void chooseTasksOutOfJob(){
		if(getCurrentJob()==null)
			return;
		List<Task> tasks = new ArrayList<>();
		for(Task task: getCurrentJob().getTasks())
			if(getResponsibilities().contains(task.getTaskDescription()) && !task.isCompleted()){
				tasks.add(task);
			}
		setCurrentTasks(tasks);
	}
	
	/**
	 * Check if the WorkBench has completed all of his Tasks.
	 * @return
	 * 			True if all the Tasks are completed.
	 * 			False if one or more Tasks are not completed yet. 
	 */
	public boolean isCompleted(){
		for(Task task: getCurrentTasks())
			if(!task.isCompleted())
				return false;
		return true;
	}
	
}
