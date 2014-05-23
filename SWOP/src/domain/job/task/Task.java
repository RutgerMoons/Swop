package domain.job.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.job.action.IAction;

/**
 * A class representing a single task that contains a certain amount of
 * IActions. Task is an implementation of ITask.
 */
public class Task implements ITask {

	private List<IAction> actionList;
	private String taskDescription;

	/**
	 * Construct a new Task.
	 * 
	 * @param 	taskDescription
	 *          The description you want to give to this Task
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when taskDescription is null or is empty
	 */
	public Task(String taskDescription) {
		setActions(new ArrayList<IAction>());
		this.setTaskDescription(taskDescription);
	}

	@Override
	public List<IAction> getActions() {
		return Collections.unmodifiableList(this.actionList);
	}

	@Override
	public void setActions(List<IAction> actions) {
		if (actions == null)
			throw new IllegalArgumentException();
		else
			this.actionList = actions;
	}

	@Override
	public void addAction(IAction action) {
		if (action == null)
			throw new IllegalArgumentException();
		else
			actionList.add(action);
	}

	@Override
	public boolean isCompleted() {
		for (IAction action : getActions())
			if (!action.isCompleted())
				return false;
		return true;
	}

	@Override
	public String getTaskDescription() {
		return this.taskDescription;
	}

	@Override
	public void setTaskDescription(String taskDescription) {
		if (taskDescription == null || taskDescription.isEmpty())
			throw new IllegalArgumentException();
		this.taskDescription = taskDescription;
	}

	@Override
	public String toString() {
		return this.taskDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + actionList.hashCode();
		result = prime * result + taskDescription.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ITask other = null;
		try {
			other = (ITask) obj;
		} catch (ClassCastException e) {
			return false;
		}
		if (!actionList.equals(other.getActions()))
			return false;
		if (!taskDescription.equals(other.getTaskDescription()))
			return false;
		return true;
	}
}