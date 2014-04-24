package domain.job;

import java.util.List;

import com.google.common.collect.ImmutableList;

import domain.exception.ImmutableException;


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
		return new ImmutableList.Builder<IAction>().addAll(task.getActions()).build();
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
	@Override
	public void setActions(List<IAction> actions) throws ImmutableException {
		throw new ImmutableException();
	}
	@Override
	public void addAction(IAction action) throws ImmutableException {
		throw new ImmutableException();
	}
	@Override
	public void setTaskDescription(String taskDescription) throws ImmutableException {
		throw new ImmutableException();
	}
}
