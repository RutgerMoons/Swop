package assembly;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Represents a single task that contains a certain amount of Actions.
 * 
 */
public class Task {

	private List<Action> actionList;
	private String taskDescription;

	/**
	 * Construct a new Task.
	 * 
	 * @param taskDescription
	 *            The description you want to give to this Task.
	 * @throws	IllegalArgumentException
	 * 			if taskDescription==null or isEmpty.
	 */
	public Task(String taskDescription) {
		setActions(new ArrayList<Action>());
		this.setTaskDescription(taskDescription);
	}

	/**
	 * Get the Actions that this Task contains.
	 * 
	 * @return An Immutable list of Actions.
	 */
	public List<Action> getActions() {
		ImmutableList<Action> immutable = new ImmutableList.Builder<Action>()
				.addAll(actionList).build();
		return immutable;
	}

	/**
	 * Set the list of Actions that this Task contains.
	 * 
	 * @param actions
	 *            A list of Actions.
	 * @throws IllegalArgumentException
	 *             If actions==null
	 */
	public void setActions(List<Action> actions) {
		if (actions == null)
			throw new IllegalArgumentException();
		else
			this.actionList = actions;
	}

	/**
	 * Add an Action to this Task.
	 * 
	 * @param action
	 *            The action you want to add.
	 * @throws IllegalArgumentException
	 *             if action==null
	 */
	public void addAction(Action action) {
		if (action == null)
			throw new IllegalArgumentException();
		else
			actionList.add(action);
	}

	/**
	 * Checks if the Task is completed.
	 * 
	 * @return True if all Actions are completed. False if one or more Actions
	 *         are completed.
	 */
	public boolean isCompleted() {
		for (Action action : getActions())
			if (!action.isCompleted())
				return false;
		return true;
	}

	/**
	 * Get the description of this Task.
	 * 
	 * @return The description of this Task.
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * Set the description of this Task.
	 * 
	 * @param taskDescription
	 *            The description you want to give to this Task.
	 * @throws IllegalArgumentException
	 *             if taskDescription==null or isEmpty
	 */
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
