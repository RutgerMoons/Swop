package assembly;

import java.util.ArrayList;
import java.util.List;

public class WorkBench {

	private List<String> responsibilities;
	private Job currentJob;
	private List<Task> currentTasks;
	public WorkBench(List<String> responsibilities){
		this.setResponsibilities(responsibilities);
		setCurrentTasks(new ArrayList<Task>());
	}
	public Job getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Job currentJob) {
		this.currentJob = currentJob;
	}
	
	public List<String> getResponsibilities() {
		return responsibilities;
	}
	
	public void setResponsibilities(List<String> responsibilities) {
		if(responsibilities==null)
			throw new IllegalArgumentException();
		this.responsibilities = responsibilities;
	}
	
	public void addResponsibility(String responibility){
		if(responibility==null || responibility.equals(""))
			throw new IllegalArgumentException();
		getResponsibilities().add(responibility);
	}
	
	public List<Task> getCurrentTasks() {
		return currentTasks;
	}
	
	public void setCurrentTasks(List<Task> currentTasks) {
		if(currentTasks==null)
			throw new IllegalArgumentException();
		this.currentTasks = currentTasks;
	}
	
	/**
	 * Selects the tasks that are valid for this Workbench. 
	 * The task description is checked against the responsibilities.
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
	
	public boolean isCompleted(){
		for(Task task: getCurrentTasks())
			if(!task.isCompleted())
				return false;
		return true;
	}
	
}
