package domain.job.task;

import java.util.Collections;
import java.util.List;

import domain.exception.UnmodifiableException;
import domain.job.action.IAction;


/**
 * A class representing an unmodifiable Task. It implements ITask where only the getters are accessible.
 */
public class UnmodifiableTask implements ITask{

	private ITask task;

	/**
	 * Create an unmodifiable task given an ITask.
	 * 
	 * @param 	task
	 * 			The mutable Task
	 */
	public UnmodifiableTask(ITask task){
		if(task==null)
			throw new IllegalArgumentException();
		this.task = task;
	}
	
	@Override
	public List<IAction> getActions() {
		return Collections.unmodifiableList(task.getActions());
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
	public void setActions(List<IAction> actions) throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	
	@Override
	public void addAction(IAction action) throws UnmodifiableException {
		throw new UnmodifiableException();
	}
	
	@Override
	public void setTaskDescription(String taskDescription) throws UnmodifiableException {
		throw new UnmodifiableException();
	}

	@Override
	public int hashCode() {
		return task.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return task.equals(obj);
	}
}