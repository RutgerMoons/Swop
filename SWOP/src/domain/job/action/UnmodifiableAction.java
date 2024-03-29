package domain.job.action;

import domain.exception.UnmodifiableException;

/**
 * A class representing an unmodifiable Action. It implements an Action
 * where only the getters are accessible.
 */
public class UnmodifiableAction implements IAction {

	private IAction action;
	/**
	 * Create an unmodifiable version of the given Action.
	 * 
	 * @param 	action
	 * 			The mutable action
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the given action is null
	 */
	public UnmodifiableAction(IAction action){
		if(action==null)
			throw new IllegalArgumentException();
		this.action = action;
	}
	
	@Override
	public boolean isCompleted() {
		return action.isCompleted();
	}

	@Override
	public String getDescription() {
		return action.getDescription();
	}
	
	@Override
	public String toString(){
		return action.toString();
	}
	
	@Override
	public void setCompleted(boolean isCompleted) throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	
	@Override
	public void setDescription(String description) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public int hashCode() {
		return action.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return action.equals(obj);
	}
}