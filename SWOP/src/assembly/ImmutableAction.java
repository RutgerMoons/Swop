package assembly;

public class ImmutableAction implements IAction {

	private IAction action;
	public ImmutableAction(IAction action){
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

}
