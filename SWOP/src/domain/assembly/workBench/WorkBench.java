package domain.assembly.workBench;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.task.ITask;

/**
 * Represents a WorkBench from an assemblyLine.
 * 
 */
public class WorkBench implements IWorkBench {

	private Set<String> responsibilities;
	private Optional<IJob> currentJob;
	private List<ITask> currentTasks;
	private final WorkbenchType workbenchType;

	/**
	 * Construct a new Workbench.
	 * 
	 * @param 	responsibilities
	 *            A list of strings. The types of Tasks that have to be
	 *            performed by this WorkBench.
	 * @param 	workbenchType
	 *            A name for this workbench
	 * @throws 	IllegalArgumentException
	 *            Thrown when workbenchName==null or isEmpty -if responsibilities==null
	 */
	public WorkBench(Set<String> responsibilities, WorkbenchType type) {
		if (type == null || responsibilities==null) {
			throw new IllegalArgumentException();
		}
		this.workbenchType = type;
		this.setResponsibilities(responsibilities);
		setCurrentTasks(new ArrayList<ITask>());
		Optional<IJob> nullJob = Optional.absent();
		setCurrentJob(nullJob);
	}


	@Override
	public WorkbenchType getWorkbenchType() {
		return workbenchType;
	}


	@Override
	public Optional<IJob> getCurrentJob() {
		return currentJob;
	}

	@Override
	public void setCurrentJob(Optional<IJob> optional) {
		if (optional == null)
			throw new IllegalArgumentException();
		this.currentJob = optional;
	}


	@Override
	public Set<String> getResponsibilities() {
		return responsibilities;
	}

	/**
	 * Set a new list of responsibilities for this WorkBench
	 * 
	 * @param 	responsibilities
	 *            A list of responsibilities
	 *            
	 * @throws 	IllegalArgumentException
	 *             If responsibilities==null
	 */
	public void setResponsibilities(Set<String> responsibilities) {
		if (responsibilities == null)
			throw new IllegalArgumentException();
		this.responsibilities = responsibilities;
	}

	/**
	 * Add a responsibility to this WorkBench.
	 * 
	 * @param 	responibility
	 *            The responsibility you want to add
	 *            
	 * @throws 	IllegalArgumentException
	 *             If responsibility==null or isEmpty
	 */
	public void addResponsibility(String responibility) {
		if (responibility == null || responibility.isEmpty())
			throw new IllegalArgumentException();
		responsibilities.add(responibility);
	}


	@Override
	public List<ITask> getCurrentTasks() {
		return currentTasks;
	}

	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * 
	 * @param 	list
	 *            A list of tasks.
	 *            
	 * @throws 	IllegalArgumentException
	 *             If currentTasks==null
	 */
	public void setCurrentTasks(List<ITask> list) {
		if (list == null)
			throw new IllegalArgumentException();
		this.currentTasks = list;
	}

	@Override
	public void chooseTasksOutOfJob() {
		if (getCurrentJob() == null || !getCurrentJob().isPresent()) {
			setCurrentTasks(new ArrayList<ITask>());
			return;
		}
		List<ITask> tasks = new ArrayList<>();
		for (ITask task : getCurrentJob().get().getTasks())
			if (getResponsibilities().contains(task.getTaskDescription())
					&& !task.isCompleted()) {
				tasks.add(task);
			}
		setCurrentTasks(tasks);
	}


	@Override
	public boolean isCompleted() {
		for (ITask task : getCurrentTasks())
			if (!task.isCompleted())
				return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getWorkbenchType().toString();
	}

	//TODO doc
	@Override
	public void completeChosenTaskAtChosenWorkBench(ITask task){
		for (ITask t : this.currentTasks) {
			if (t.equals(task)) {
				for (IAction action : t.getActions()) {
					action.setCompleted(true);
				}
				break;
			}
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentJob == null) ? 0 : currentJob.hashCode());
		result = prime * result
				+ ((currentTasks == null) ? 0 : currentTasks.hashCode());
		result = prime
				* result
				+ ((responsibilities == null) ? 0 : responsibilities.hashCode());
		result = prime * result
				+ ((workbenchType == null) ? 0 : workbenchType.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IWorkBench other = null;
		try{
			other = (IWorkBench) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (currentJob == null) {
			if (other.getCurrentJob() != null)
				return false;
		} else if (!currentJob.equals(other.getCurrentJob()))
			return false;
		if (currentTasks == null) {
			if (other.getCurrentTasks() != null)
				return false;
		} else if (!currentTasks.equals(other.getCurrentTasks()))
			return false;
		if (responsibilities == null) {
			if (other.getResponsibilities() != null)
				return false;
		} else if (!responsibilities.equals(other.getResponsibilities()))
			return false;
		if (workbenchType != other.getWorkbenchType())
			return false;
		return true;
	}


}
