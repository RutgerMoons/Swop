package domain.assembly;

import java.util.ArrayList;
import java.util.List;

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
	 * @param taskDescription
	 *            The description you want to give to this Task.
	 * @throws IllegalArgumentException
	 *             if taskDescription==null or isEmpty.
	 */
	public Task(String taskDescription) {
		setActions(new ArrayList<IAction>());
		this.setTaskDescription(taskDescription);
	}

	
	public List<IAction> getActions() {
		return actionList;
	}

	public void setActions(List<IAction> actions) {
		if (actions == null)
			throw new IllegalArgumentException();
		else
			this.actionList = actions;
	}

	public void addAction(IAction action) {
		if (action == null)
			throw new IllegalArgumentException();
		else
			actionList.add(action);
	}

	
	public boolean isCompleted() {
		for (IAction action : getActions())
			if (!action.isCompleted())
				return false;
		return true;
	}

	
	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		if (taskDescription == null || taskDescription.isEmpty())
			throw new IllegalArgumentException();
		this.taskDescription = taskDescription;
	}

	@Override
	public String toString() {
		String taskString = this.getTaskDescription() + ","
				+ "Required actions:";
		for (int i = 0; i < this.getActions().size(); i++) {
			taskString += " " + (i + 1) + "."
					+ this.getActions().get(i).getDescription() + ",";
		}
		if (getActions().size() != 0)
			taskString = taskString.substring(0, taskString.length() - 1);
		return taskString;
	}
}
