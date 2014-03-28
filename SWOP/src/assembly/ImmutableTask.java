package assembly;

import java.util.List;

public class ImmutableTask implements ITask{

	private Task task;

	public ImmutableTask(Task task){
		if(task==null)
			throw new IllegalArgumentException();
		this.task = task;
	}
	@Override
	public List<IAction> getActions() {
		return task.getActions();
	}

	@Override
	public boolean isCompleted() {
		return task.isCompleted();
	}

	@Override
	public String getTaskDescription() {
		return task.getTaskDescription();
	}

	@Override
	public String toString(){
		return task.toString();
	}
}
