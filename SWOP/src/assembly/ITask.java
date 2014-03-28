package assembly;

import java.util.List;

public interface ITask {

	/**
	 * Get the Actions that this Task contains.
	 * 
	 * @return An Immutable list of Actions.
	 */
	public List<IAction> getActions();
	
	/**
	 * Checks if the Task is completed.
	 * 
	 * @return True if all Actions are completed. False if one or more Actions
	 *         are completed.
	 */
	public boolean isCompleted();
	
	/**
	 * Get the description of this Task.
	 * 
	 * @return The description of this Task.
	 */
	public String getTaskDescription();
	
	
}
