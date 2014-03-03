package assembly;

import java.util.ArrayList;
import java.util.List;

import Order.Order;

public class Job {

	private Order order;
	private List<Task> taskList;

	public Job(Order order){
		setOrder(order);
		setTasks(new ArrayList<Task>());
	}
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		if(order==null)
			throw new IllegalArgumentException();
		else
			this.order = order;
	}

	public List<Task> getTasks() {
		return taskList;
	}

	public void setTasks(List<Task> tasks) {
		if(tasks==null)
			throw new IllegalArgumentException();
		else
			this.taskList = tasks;
	}

	public void addTask(Task task){
		if(task==null)
			throw new IllegalArgumentException();
		else
			getTasks().add(task);
	}

	public boolean isCompleted(){
		for(Task task: taskList)
			if(!task.isCompleted())
				return false;		
		return true;
	}
}
