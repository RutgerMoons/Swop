package assembly;

import java.util.ArrayList;
import java.util.List;

public class Task {

	private List<Action> actionList;
	
	public Task(){
		setActions(new ArrayList<Action>());
	}

	public List<Action> getActions() {
		return actionList;
	}

	public void setActions(List<Action> actions) {
		this.actionList = actions;
	}
	
	public void addAction(Action action){
		getActions().add(action);
	}
	
	public boolean isCompleted(){
		for(Action action: getActions())
			if(!action.isCompleted())
				return false;
		return true;
	}
}
