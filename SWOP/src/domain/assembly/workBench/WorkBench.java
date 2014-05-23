package domain.assembly.workBench;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.task.ITask;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 *  A class representing a workbench. Each WorkBench has a WorkBenchType and the Job it currently has to work on. 
 *  Based on the current Job, the WorkBench retrieves all the Tasks it has to complete.
 */
public class WorkBench implements IWorkBench {

	private Optional<IJob> currentJob;
	private List<ITask> currentTasks;
	private final WorkBenchType workbenchType;

	/**
	 * Construct a new Workbench.
	 * 
	 * @param 	type
	 *          The type of WorkBench this WorkBench belongs to
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when type is null
	 */
	public WorkBench(WorkBenchType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		this.workbenchType = type;
		setCurrentTasks(new ArrayList<ITask>());
		Optional<IJob> nullJob = Optional.absent();
		setCurrentJob(nullJob);
	}


	@Override
	public WorkBenchType getWorkbenchType() {
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
	public Set<VehicleOptionCategory> getResponsibilities() {
		return Collections.unmodifiableSet(workbenchType.getResponsibilities());
	}

	@Override
	public List<ITask> getCurrentTasks() {
		return Collections.unmodifiableList(currentTasks);
	}

	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * 
	 * @param 	list
	 *          A list of tasks
	 *            
	 * @throws 	IllegalArgumentException
	 *          Thrown when list is null
	 */
	public void setCurrentTasks(List<ITask> list) {
		if (list == null)
			throw new IllegalArgumentException();
		this.currentTasks = list;
	}

	@Override
	public void chooseTasksOutOfJob() {
		if (!getCurrentJob().isPresent()) {
			setCurrentTasks(new ArrayList<ITask>());
			return;
		}
		List<ITask> tasks = new ArrayList<>();
		for (ITask task : getCurrentJob().get().getTasks())
			if (getResponsibilities().contains(VehicleOptionCategory.valueOf(task.getTaskDescription().toUpperCase()))
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
				+ currentJob.hashCode();
		result = prime * result
				+ currentTasks.hashCode();
		result = prime * result
				+ workbenchType.hashCode();
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
		if (!currentJob.equals(other.getCurrentJob()))
			return false;
		if (!currentTasks.equals(other.getCurrentTasks()))
			return false;
		if (workbenchType != other.getWorkbenchType())
			return false;
		return true;
	}
}