package assembly;

import java.util.List;

import exception.ImmutableException;
import order.IOrder;

public interface IJob {

	/**
	 * Get the Order on which the Job is based.
	 * 
	 * @return The Order on which the Job is based.
	 */
	public IOrder getOrder();
	
	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @param order
	 *            The Order you want to allocate to this Job.
	 * @throws ImmutableException 
	 * 		  	  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If order==null
	 */
	public void setOrder(IOrder order) throws ImmutableException;
	
	/**
	 * Get the tasks that have to be completed before the Job(Car) is finished.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getTasks();
	
	/**
	 * Set a new list of tasks that have to be completed before the Job(Car) is
	 * finished.
	 * 
	 * @param tasks
	 *            A list of tasks.
	 * @throws ImmutableException
	 * 			  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If tasks==null
	 */
	public void setTasks(List<ITask> tasks) throws ImmutableException;
	
	/**
	 * Add a new task to the Job.
	 * 
	 * @param task
	 *            The task you want to add.
	 * @throws ImmutableException 
	 * 		 	  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If task==null
	 */
	public void addTask(ITask task) throws ImmutableException;
	
	/**
	 * Check if the Job(Car) is completed.
	 * 
	 * @return True if all Tasks are fully completed. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean isCompleted();
	
	
}
