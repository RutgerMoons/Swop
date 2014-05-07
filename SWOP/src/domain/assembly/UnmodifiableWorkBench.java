package domain.assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import domain.exception.UnmodifiableException;
import domain.job.IJob;
import domain.job.ITask;
import domain.job.UnmodifiableJob;


/**
 * Create an Immutable WorkBench, only the getters are accessible.
 *
 */
public class UnmodifiableWorkBench implements IWorkBench {

	private IWorkBench bench;
	
	/**
	 * Create an Immutable WorkBench.
	 *  
	 * @param bench
	 * 			The mutable WorkBench.
	 */
	public UnmodifiableWorkBench(IWorkBench bench){
		if(bench==null)
			throw new IllegalArgumentException();
		this.bench = bench;
	}
	@Override
	public String getWorkbenchName() {
		return bench.getWorkbenchName();
	}

	@Override
	public Optional<IJob> getCurrentJob() {
		Optional<IJob> currentJob = bench.getCurrentJob();
		if(currentJob.isPresent()){
			IJob immutable = new UnmodifiableJob(currentJob.get());
			Optional<IJob> job = Optional.fromNullable(immutable);
			return job;
		}
		return currentJob;
	}

	@Override
	public Set<String> getResponsibilities() {
		return new ImmutableSet.Builder<String>().addAll(bench.getResponsibilities())
				.build();
	}

	@Override
	public List<ITask> getCurrentTasks() {
		return new ImmutableList.Builder<ITask>().addAll(bench.getCurrentTasks()).build();
	}

	@Override
	public boolean isCompleted() {
		return bench.isCompleted();
	}
	
	@Override
	public String toString(){
		return bench.toString();
	}
	@Override
	public void setCurrentJob(Optional<IJob> optional) throws UnmodifiableException {
		throw new UnmodifiableException();		
	}
	@Override
	public void setResponsibilities(Set<String> responsibilities) throws UnmodifiableException {
		throw new UnmodifiableException();		
	}
	@Override
	public void addResponsibility(String responibility) throws UnmodifiableException {
		throw new UnmodifiableException();		
	}
	@Override
	public void setCurrentTasks(List<ITask> list) throws UnmodifiableException {
		throw new UnmodifiableException();		
	}
	@Override
	public void chooseTasksOutOfJob() throws UnmodifiableException {
		throw new UnmodifiableException();		
	}

}
