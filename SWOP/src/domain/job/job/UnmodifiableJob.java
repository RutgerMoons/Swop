package domain.job.job;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.order.order.IOrder;
import domain.order.order.UnmodifiableOrder;
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
		return Collections.unmodifiableList(job.getTasks());
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
	public void setOrder(IOrder order) {
		throw new UnmodifiableException();
		
	}

	@Override
	public void setTasks(List<ITask> tasks) {
		throw new UnmodifiableException();
		
	}

	@Override
	public void addTask(ITask task) {
		throw new UnmodifiableException();
		
	}

	@Override
	public void setMinimalIndex(int index) {
		throw new UnmodifiableException();
		
	}

	@Override
	public int getMinimalIndex() {
		return job.getMinimalIndex();
	}
	
	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableCollection(job.getVehicleOptions());
	}
	
	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(this.job.getTimeAtWorkBench());
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return job.getVehicleSpecification();
	}
}