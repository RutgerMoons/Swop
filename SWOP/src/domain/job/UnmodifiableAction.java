package domain.job;

import domain.exception.UnmodifiableException;

/**
 * Create an Immutable Action, only the getters are accessible.
 *
 */
public class UnmodifiableAction implements IAction {

	private IAction action;
	/**
	 * Create the Immutable Action.
	 * 
	 * @param action
	 * 			The mutable action.
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

}
