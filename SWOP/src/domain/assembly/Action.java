package domain.assembly;

/**
 * This class represents an Action that is available for the workers.
 * 
 */
public class Action implements IAction {

	private boolean isCompleted;
	private String description;

	/**
	 * Construct a new Action.
	 * 
	 * @param description
	 *            The description for the action.
	 * @throws IllegalArgumentException
	 * 			if description==null of isEmpty.
	 */
	public Action(String description) {
		this.setDescription(description);
	}

	
	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException();
		this.description = description;
	}

	@Override
	public String toString() {
		return getDescription();
	}

}
