package assembly;

import java.util.List;

public class WorkBench {

	private List<String> responsibilities;
	private Job currentJob;
	private Task currentTask;
	
	public WorkBench(List<String> responsibilities){
		this.setResponsibilities(responsibilities);
	}
	public Job getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Job currentJob) {
		if(currentJob==null)
			throw new IllegalArgumentException();
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
	
	public Task getCurrentTask() {
		return currentTask;
	}
	public void setCurrentTask(Task currentTask) {
		if(currentTask==null)
			throw new IllegalArgumentException();
		this.currentTask = currentTask;
	}
	
	public void chooseNextTask(){
		for(Task task: getCurrentJob().getTasks())
			if(getResponsibilities().contains(task.getTaskDescription()) && !task.isCompleted()){
				setCurrentTask(task);
				break;
			}
			
	}
}
