package domain.job.task;

import java.util.List;

import domain.exception.UnmodifiableException;
import domain.job.action.IAction;

/**
 * An interface describing a task. A Task consists of an description and a
 *  list of IActions.
 */
public interface ITask {

	/**
	 * Get all IActions that this ITask contains.
	 * 
	 * @return 	An list of Actions
	 */
	public List<IAction> getActions();
	
	/**
	 * Set the list of IActions that this ITask contains.
	 * 
	 * @param	actions
	 * 			The new list of IActions
	 * 
	 * @throws 	UnmodifiableException 
	 * 		 	Thrown when the ITask is an unmodifiable Task
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the given list is null
	 */
	public void setActions(List<IAction> actions);
	
	/**
	 * Add an IAction to this task.
	 * 
	 * @param	action
	 * 			The new IAction
	 * 
	 * @throws 	UnmodifiableException 
	 * 		 	Thrown the ITask is an unmodifiable Task
	 *
	 * @throws 	IllegalArgumentException
	 *          Thrown when the given IAction is null
	 */
	public void addAction(IAction action);
	
	/**
	 * Checks if the ITask is completed.
	 * 
	 * @return True if all IActions are completed. False if one or more IActions
	 *         aren't completed.
	 */
	public boolean isCompleted();
	
	/**
	 * Returns the description of this task.
	 */
	public String getTaskDescription();
	
	/**
	 * Set the description of this task.
	 * 
	 * @param	taskDescription
	 * 			The new description
	 * 
	 * @throws 	UnmodifiableException 
	 * 		 	Thrown when the ITask is an unmodifiable Task
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when taskDescription is null or empty
	 */
	public void setTaskDescription(String taskDescription);
}