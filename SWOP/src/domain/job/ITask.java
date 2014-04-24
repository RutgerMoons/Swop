package domain.job;

import java.util.List;

import domain.exception.ImmutableException;


public interface ITask {

	/**
	 * Get the Actions that this Task contains.
	 * 
	 * @return An Immutable list of Actions.
	 */
	public List<IAction> getActions();
	
	/**
	 * Set the list of Actions that this Task contains.
	 * 
	 * @throws ImmutableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             If actions==null
	 */
	public void setActions(List<IAction> actions) throws ImmutableException;
	
	/**
	 * Add an Action to this Task.
	 * 
	 * @throws ImmutableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             if action==null
	 */
	public void addAction(IAction action) throws ImmutableException;
	
	/**
	 * Checks if the Task is completed.
	 * 
	 * @return True if all Actions are completed. False if one or more Actions
	 *         are completed.
	 */
	public boolean isCompleted();
	
	/**
	 * Get the description of this Task.
	 */
	public String getTaskDescription();
	
	/**
	 * Set the description of this Task.
	 * .
	 * @throws ImmutableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             if taskDescription==null or isEmpty
	 */
	public void setTaskDescription(String taskDescription) throws ImmutableException;
	
	
}
