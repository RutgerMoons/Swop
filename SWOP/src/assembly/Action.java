package assembly;

/**
 * This class represents an Action that is available for the workers. 
 *
 */
public class Action {

	private boolean isCompleted;
	private String description;
	
	/**
	 * Construct a new Action.
	 * @param description
	 * 			The description for the action.
	 */
	public Action(String description){
		this.setDescription(description);
	}
	
	/**
	 * Check if the action is completed.
	 * @return
	 * 			True if action is fully completed.
	 * 			False if action is not fully completed.
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * Set the completed state of this action.
	 * @param isCompleted
	 * 			if true, the action is completed.
	 * 			if false, the action is not completed.
	 */
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	/**
	 * Get the description of the action.
	 * @return
	 * 			A String representing description of the action.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description of this action.
	 * @param description
	 * 			The description you want to give to this action represented as a String.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString(){
		return getDescription();
	}
	
}
