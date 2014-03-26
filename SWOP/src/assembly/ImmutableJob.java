package assembly;

import java.util.List;

import order.IOrder;

public class ImmutableJob implements IJob {

	private Job job;
	public ImmutableJob(Job job){
		this.job = job;
	}
	@Override
	public IOrder getOrder() {
		return job.getOrder();
	}

	@Override
	public List<ITask> getTasks() {
		return job.getTasks();
	}

	@Override
	public boolean isCompleted() {
		return job.isCompleted();
	}
	
	@Override
	public int hashCode(){
		return job.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return job.equals(obj);
	}

	@Override
	public String toString(){
		return job.toString();
	}

}
