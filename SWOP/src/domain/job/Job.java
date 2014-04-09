package domain.job;

import java.util.ArrayList;
import java.util.List;

import domain.order.IOrder;


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

	@Override
	public IOrder getOrder() {
		return order;
	}

	@Override
	public void setOrder(IOrder order) {
		if (order == null)
			throw new IllegalArgumentException();
		else
			this.order = order;
	}

	
	@Override
	public List<ITask> getTasks() {
		return taskList;
	}

	@Override
	public void setTasks(List<ITask> tasks) {
		if (tasks == null)
			throw new IllegalArgumentException();
		else
			this.taskList = tasks;
	}

	@Override
	public void addTask(ITask task) {
		if (task == null)
			throw new IllegalArgumentException();
		else
			taskList.add(task);
	}

	
	@Override
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
