package domain.job;

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

	@Override
	public boolean isCompleted() {
		return this.isCompleted;
	}

	@Override
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription(String description) {
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException();
		this.description = description;
	}

	@Override
	public String toString() {
		return this.getDescription();
	}

}
