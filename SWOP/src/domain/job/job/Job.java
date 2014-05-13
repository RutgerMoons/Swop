package domain.job.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import domain.job.task.ITask;
import domain.order.IOrder;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;


/**
 * 
 * Represents a Job. A Job is actually 1 car.
 * 
 */
public class Job implements IJob {

	private IOrder order;
	private List<ITask> taskList;
	private int minimalIndex;

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
		result = prime * result + taskList.hashCode() + getTasks().hashCode() + getTimeAtWorkBench();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IJob other;
		try{
			other = (IJob) obj; //IJob ipv Job
		} catch (ClassCastException e){
			return false;
		}
		if (!order.equals(other.getOrder()))//other.getOrder()
			return false;
		if(!getTasks().equals(other.getTasks()))
			return false;
		if(getTimeAtWorkBench()!=other.getTimeAtWorkBench())
			return false;
		return true;
	}

	@Override
	public void setMinimalIndex(int index) {
		this.minimalIndex = index;
	}

	@Override
	public int getMinimalIndex() {
		return this.minimalIndex;
	}

	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return this.order.getVehicleOptions();
	}

	@Override
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm) {
		if (schedulingAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		this.order.addToSchedulingAlgorithm(schedulingAlgorithm, this);
		
	}
	
	@Override
	public int getTimeAtWorkBench() {
		return this.order.getTimeAtWorkBench();
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return order.getVehicleSpecification();
	}

}
