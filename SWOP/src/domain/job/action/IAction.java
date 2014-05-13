package domain.job.action;


/**
 * Interface for limiting access to standard Actions.
 */
public interface IAction {
	
	/**
	 * Check if the action is completed
	 * 
	 * @return 	True if action is fully completed. False if action is not fully
	 *         	completed.
	 */
	public boolean isCompleted();

	/**
	 * Set the completed state of this action.
	 * 
	 * @param 	isCompleted
	 *            If true, the action is completed. if false, the action is not
	 *            completed.
	 */
	public void setCompleted(boolean isCompleted);
	
	
	/**
	 * Get the description of the action
	 */
	public String getDescription();
	
	
	/**
	 * Set the description of this action
	 * 
	 * @throws 	IllegalArgumentException
	 *             If description==null or empty
	 */
	public void setDescription(String description);
	
}
