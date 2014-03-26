package assembly;

import java.util.List;

public interface ITask {

	
	public List<IAction> getActions();
	
	public boolean isCompleted();
	
	public String getTaskDescription();
	
	
}
