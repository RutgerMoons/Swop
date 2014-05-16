package domain.job.action;


/**
 * An interface used to represent an Action. An Action consists of an description
 * and a boolean for indicating if the Action is completed.
 */
public interface IAction {
	
	/**
	 * Check if the action is completed.
	 * 
	 * @return 	True if action is fully completed. False if action is not fully
	 *         	completed
	 */
	public boolean isCompleted();

	/**
	 * Set the state of this action to the given parameter.
	 * 
	 * @param 	isCompleted
	 *          If true, the action is completed. If false, the action is not
	 *          completed
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
	 *          Thrown when the description is null or empty
	 */
	public void setDescription(String description);
}