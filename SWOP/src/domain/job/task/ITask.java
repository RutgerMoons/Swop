package domain.job.task;

import java.util.List;

import domain.exception.UnmodifiableException;
import domain.job.action.IAction;

/**
 * Interface for limiting access to standard Tasks.
 */
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
	 * @throws UnmodifiableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             If actions==null
	 */
	public void setActions(List<IAction> actions) throws UnmodifiableException;
	
	/**
	 * Add an Action to this Task.
	 * 
	 * @throws UnmodifiableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             if action==null
	 */
	public void addAction(IAction action) throws UnmodifiableException;
	
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
	 * @throws UnmodifiableException 
	 * 		 	  If the ITask is an ImmutableTask.
	 * @throws IllegalArgumentException
	 *             if taskDescription==null or isEmpty
	 */
	public void setTaskDescription(String taskDescription) throws UnmodifiableException;
	
	
}
