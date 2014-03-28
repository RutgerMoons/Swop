package assembly;

import java.util.List;

/**
 * Create an Immutable Task, only the getters are accessible.
 *
 */
public class ImmutableTask implements ITask{

	private ITask task;

	/**
	 * Create an Immutable Task.
	 * 
	 * @param task
	 * 			The mutable Task.
	 */
	public ImmutableTask(ITask task){
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
