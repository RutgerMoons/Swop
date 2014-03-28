package assembly;

import java.util.ArrayList;
import java.util.List;

import order.IOrder;
import order.Order;

import com.google.common.collect.ImmutableList;

/**
 * 
 * Represents a Job. A Job is actually 1 car.
 * 
 */
public class Job implements IJob {

	private IOrder order;
	private List<ITask> taskList;

	/**
	 * Construct a new Job.
	 * 
	 * @param order
	 *            The order on which the Job is based.
	 * @throws IllegalArgumentException
	 *             if order==null
	 */
	public Job(IOrder order) {
		setOrder(order);
		setTasks(new ArrayList<ITask>());
	}

	/**
	 * Get the Order on which the Job is based.
	 * 
	 * @return The Order on which the Job is based.
	 */
	public IOrder getOrder() {
		return order;
	}

	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @param order
	 *            The Order you want to allocate to this Job.
	 * @throws IllegalArgumentException
	 *             If order==null
	 */
	public void setOrder(IOrder order) {
		if (order == null)
			throw new IllegalArgumentException();
		else
			this.order = order;
	}

	/**
	 * Get the tasks that have to be completed before the Job(Car) is finished.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getTasks() {
		return new ImmutableList.Builder<ITask>().addAll(taskList).build();
	}

	/**
	 * Set a new list of tasks that have to be completed before the Job(Car) is
	 * finished.
	 * 
	 * @param tasks
	 *            A list of tasks.
	 * @throws IllegalArgumentException
	 *             If tasks==null
	 */
	public void setTasks(List<ITask> tasks) {
		if (tasks == null)
			throw new IllegalArgumentException();
		else
			this.taskList = tasks;
	}

	/**
	 * Add a new task to the Job.
	 * 
	 * @param task
	 *            The task you want to add.
	 * @throws IllegalArgumentException
	 *             If task==null
	 */
	public void addTask(ITask task) {
		if (task == null)
			throw new IllegalArgumentException();
		else
			taskList.add(task);
	}

	/**
	 * Check if the Job(Car) is completed.
	 * 
	 * @return True if all Tasks are fully completed. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean isCompleted() {
		for (ITask task : taskList)
			if (!task.isCompleted())
				return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + taskList.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (!order.equals(other.order))
			return false;

		return true;
	}

}
