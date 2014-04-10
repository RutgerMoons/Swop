package domain.job;

import java.util.List;

import com.google.common.collect.ImmutableList;

import domain.exception.ImmutableException;
import domain.order.IOrder;
import domain.order.ImmutableOrder;


/**
 * Create an Immutable Job, only the getters are accessible.
 * 
 */
public class ImmutableJob implements IJob {

	private IJob job;

	/**
	 * Create the Immutable Job.
	 * 
	 * @param job
	 * 			The mutable Job.
	 */
	public ImmutableJob(IJob job) {
		if (job == null)
			throw new IllegalArgumentException();
		this.job = job;
	}

	@Override
	public IOrder getOrder() {
		return new ImmutableOrder(job.getOrder());
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
	public void setOrder(IOrder order) throws ImmutableException {
		throw new ImmutableException();
		
	}

	@Override
	public void setTasks(List<ITask> tasks) throws ImmutableException {
		throw new ImmutableException();
		
	}

	@Override
	public void addTask(ITask task) throws ImmutableException {
		throw new ImmutableException();
		
	}

	@Override
	public void setMinimalIndex(int index) throws ImmutableException {
		throw new ImmutableException();
		
	}

	@Override
	public int getMinimalIndex() {
		// TODO Auto-generated method stub
		return job.getMinimalIndex();
	}

}
