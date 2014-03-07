package assembly;

import java.util.ArrayList;
import java.util.List;

import order.Order;

/**
 * 
 * Represents a Job. A Job is actually 1 car.
 *
 */
public class Job {

	private Order order;
	private List<Task> taskList;

	/**
	 * 
	 * @param order
	 * 			The order on which the Job is based.
	 */
	public Job(Order order){
		setOrder(order);
		setTasks(new ArrayList<Task>());
	}
	
	/**
	 * Get the Order on which the Job is based.
	 * 
	 * @return
	 * 			The order on which the Job is based.
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @param order
	 * 			The order you want to allocate to this Job.
	 * @throws IllegalArgumentException
	 * 			If order==null
	 */
	public void setOrder(Order order) {
		if(order==null)
			throw new IllegalArgumentException();
		else
			this.order = order;
	}

	/**
	 * Get the tasks that has to be completed before the Job(Car) is finished.
	 * @return
	 * 			A list of tasks.
	 */
	public List<Task> getTasks() {
		return taskList;
	}

	/**
	 * Set a new list of tasks that have to be completed before the Job(Car) is finished.
	 * @param tasks
	 * 			A list of tasks.
	 * @throws IllegalArgumentException
	 * 			If tasks==null
	 */
	public void setTasks(List<Task> tasks) {
		if(tasks==null)
			throw new IllegalArgumentException();
		else
			this.taskList = tasks;
	}

	/**
	 * Add a new task to the Job.
	 * @param task
	 * 			The task you want to add.
	 * @throws IllegalArgumentException
	 * 			If task==null
	 */
	public void addTask(Task task){
		if(task==null)
			throw new IllegalArgumentException();
		else
			getTasks().add(task);
	}

	/**
	 * Check if the Job(Car) is completed.
	 * @return
	 * 			True if all Tasks are fully completed.
	 * 			False if one or more Tasks are not fully completed.
	 */
	public boolean isCompleted(){
		for(Task task: taskList)
			if(!task.isCompleted())
				return false;		
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result
				+ ((taskList == null) ? 0 : taskList.hashCode());
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
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (taskList == null) {
			if (other.taskList != null)
				return false;
		} else if (!taskList.equals(other.taskList))
			return false;
		return true;
	}
	
	
}
