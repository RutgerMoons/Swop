package domain.job;

import domain.exception.ImmutableException;

public interface IAction {
	
	/**
	 * Check if the action is completed.
	 * 
	 * @return True if action is fully completed. False if action is not fully
	 *         completed.
	 */
	public boolean isCompleted();

	/**
	 * Set the completed state of this action.
	 * 
	 * @param isCompleted
	 *            if true, the action is completed. if false, the action is not
	 *            completed.
	 * @throws ImmutableException 
	 * 			  If the IAction is an ImmutableAction.
	 */
	public void setCompleted(boolean isCompleted) throws ImmutableException;
	
	
	/**
	 * Get the description of the action.
	 */
	public String getDescription();
	
	
	/**
	 * Set the description of this action.
	 * @throws ImmutableException 
	 * 			  If the IAction is an ImmutableAction.
	 * @throws IllegalArgumentException
	 *             If description==null or empty
	 */
	public void setDescription(String description) throws ImmutableException;
	
}
