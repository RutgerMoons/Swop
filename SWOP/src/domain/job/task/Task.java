package domain.job.task;

import java.util.ArrayList;
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
		return this.actionList;
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
}
