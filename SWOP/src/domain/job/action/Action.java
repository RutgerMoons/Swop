package domain.job.action;

/**
 * This class represents an Action that is available for the workers.
 * Action is an implementation of IAction.
 */
public class Action implements IAction {

	private boolean isCompleted;
	private String description;

	/**
	 * Construct a new Action.
	 * 
	 * @param 	description
	 *          The description for the action
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the description null or it's empty
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