package assembly;

import java.util.List;

import order.IOrder;

public interface IJob {

	/**
	 * Get the Order on which the Job is based.
	 * 
	 * @return The Order on which the Job is based.
	 */
	public IOrder getOrder();
	
	/**
	 * Get the tasks that have to be completed before the Job(Car) is finished.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getTasks();
	
	/**
	 * Check if the Job(Car) is completed.
	 * 
	 * @return True if all Tasks are fully completed. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean isCompleted();
	
	
}
