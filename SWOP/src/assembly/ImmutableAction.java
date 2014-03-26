package assembly;

public class ImmutableAction implements IAction {

	private Action action;
	public ImmutableAction(Action action){
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
