<<<<<<< HEAD
package domain.job.task;

import java.util.Collections;
import java.util.List;

import domain.exception.UnmodifiableException;
import domain.job.action.IAction;


/**
 * Create an Immutable Task, only the getters are accessible.
 *
 */
public class UnmodifiableTask implements ITask{

	private ITask task;

	/**
	 * Create an Immutable Task
	 * 
	 * @param 	task
	 * 				The mutable Task
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
=======
package domain.job.task;

import java.util.List;

import com.google.common.collect.ImmutableList;

import domain.exception.UnmodifiableException;
import domain.job.action.IAction;


/**
 * Create an unmodifiable Task, only the getters are accessible.
 *
 */
public class UnmodifiableTask implements ITask{

	private ITask task;

	/**
	 * Create an unmodifiable Task
	 * 
	 * @param 	task
	 * 				The mutable Task
	 */
	public UnmodifiableTask(ITask task){
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
	public void setActions(List<IAction> actions){
		throw new UnmodifiableException();
	}
	
	@Override
	public void addAction(IAction action){
		throw new UnmodifiableException();
	}
	
	@Override
	public void setTaskDescription(String taskDescription){
		throw new UnmodifiableException();
	}
>>>>>>> 57db3d1871d8363a99989326339b426e1cbd2bfc
}