package domain.job.action;

/**
 * A class represents an Action that is available for the workers. Action is an
 * implementation of IAction.
 */
public class Action implements IAction {

	private boolean isCompleted;
	private String description;

	/**
	 * Construct a new Action.
	 * 
	 * @param description
	 *            The description for the action
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown when the description null or it's empty
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + (isCompleted ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IAction other;
		try {
			other = (IAction) obj;
		} catch (ClassCastException e) {
			return false;
		}
		if (!description.equals(other.getDescription()))
			return false;
		if (isCompleted != other.isCompleted())
			return false;
		return true;
	}
}