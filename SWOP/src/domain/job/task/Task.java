package domain.job.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.job.action.IAction;

/**
 * Represents a single task that contains a certain amount of Actions.
 * 
 */
public class Task implements ITask {

	private List<IAction> actionList;
	private String taskDescription;

	/**
	 * Construct a new Task.
	 * 
	 * @param 	taskDescription
	 *            	The description you want to give to this Task
	 * @throws 	IllegalArgumentException
	 *             if taskDescription==null or isEmpty
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
		result = prime * result
				+ ((actionList == null) ? 0 : actionList.hashCode());
		result = prime * result
				+ ((taskDescription == null) ? 0 : taskDescription.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		ITask other=null;
		try{
			other = (ITask) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (actionList == null) {
			if (other.getActions() != null)
				return false;
		} else if (!actionList.equals(other.getActions()))
			return false;
		if (taskDescription == null) {
			if (other.getTaskDescription() != null)
				return false;
		} else if (!taskDescription.equals(other.getTaskDescription()))
			return false;
		return true;
	}
}
