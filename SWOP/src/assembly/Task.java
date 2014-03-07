package assembly;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single task that contains a certain amount of tasks.
 *
 */
public class Task {

	private List<Action> actionList;
	private String taskDescription;

	/**
	 * Construct a new Task.
	 * @param taskDescription
	 * 			The description you want to give to this Task.
	 */
	public Task(String taskDescription){
		setActions(new ArrayList<Action>());
		this.setTaskDescription(taskDescription);
	}

	/**
	 * Get the Actions that this Task contains.
	 * @return
	 * 			A list of Actions.
	 */
	public List<Action> getActions() {
		return actionList;
	}

	/**
	 * Set the list of Actions that this Task contains.
	 * @param actions
	 * 			A list of Actions.
	 * @throws IllegalArgumentException
	 * 			If actions==null
	 */
	public void setActions(List<Action> actions) {
		if(actions==null)
			throw new IllegalArgumentException();
		else
			this.actionList = actions;
	}

	/**
	 * Add an Action to this Task.
	 * @param action
	 * 			The action you want to add.
	 */
	public void addAction(Action action){
		if(action==null)
			throw new IllegalArgumentException();
		else
			getActions().add(action);
	}

	/**
	 * Checks if the Task is completed.
	 * @return
	 * 			True if all Actions are completed.
	 * 			False if one or more Actions are completed.
	 */
	public boolean isCompleted(){
		for(Action action: getActions())
			if(!action.isCompleted())
				return false;
		return true;
	}

	/**
	 * Get the description of this Task.
	 * @return
	 * 			The description of this Task.
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * Set the description of this Task.
	 * @param taskDescription
	 * 			The description you want to give to this Task.
	 */
	public void setTaskDescription(String taskDescription) {
		if(taskDescription==null || taskDescription.equals(""))
			throw new IllegalArgumentException();
		this.taskDescription = taskDescription;
	}
	
	public String toString(){
		return getTaskDescription();
	}
}
