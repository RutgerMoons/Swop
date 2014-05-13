package domain.job.job;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.order.IOrder;
import domain.order.UnmodifiableOrder;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;


/**
 * Create an Immutable Job, only the getters are accessible.
 * 
 */
public class UnmodifiableJob implements IJob {

	private IJob job;

	/**
	 * Create the Immutable Job.
	 * 
	 * @param job
	 * 			The mutable Job.
	 */
	public UnmodifiableJob(IJob job) {
		if (job == null)
			throw new IllegalArgumentException();
		this.job = job;
	}

	@Override
	public IOrder getOrder() {
		return new UnmodifiableOrder(job.getOrder());
	}

	@Override
	public List<ITask> getTasks() {
		return new ImmutableList.Builder<ITask>().addAll(job.getTasks()).build();
	}

	@Override
	public boolean isCompleted() {
		return job.isCompleted();
	}

	@Override
	public int hashCode() {
		return job.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return job.equals(obj);
	}

	@Override
	public String toString() {
		return job.toString();
	}

	@Override
	public void setOrder(IOrder order) throws UnmodifiableException {
		throw new UnmodifiableException();
		
	}

	@Override
	public void setTasks(List<ITask> tasks) throws UnmodifiableException {
		throw new UnmodifiableException();
		
	}

	@Override
	public void addTask(ITask task) throws UnmodifiableException {
		throw new UnmodifiableException();
		
	}

	@Override
	public void setMinimalIndex(int index) throws UnmodifiableException {
		throw new UnmodifiableException();
		
	}

	@Override
	public int getMinimalIndex() {
		return job.getMinimalIndex();
	}
	
	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return job.getVehicleOptions();
	}
	
	@Override
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm) {
		if (schedulingAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		this.job.addToSchedulingAlgorithm(schedulingAlgorithm);
	}
	
	@Override
	public int getTimeAtWorkBench() {
		return this.job.getTimeAtWorkBench();
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return job.getVehicleSpecification();
	}
}

