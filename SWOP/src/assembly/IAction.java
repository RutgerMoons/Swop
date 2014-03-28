package assembly;

public interface IAction {
	
	/**
	 * Check if the action is completed.
	 * 
	 * @return True if action is fully completed. False if action is not fully
	 *         completed.
	 */
	public boolean isCompleted();
	
	/**
	 * Get the description of the action.
	 * 
	 * @return A String representing description of the action.
	 */
	public String getDescription();
	
	
}
