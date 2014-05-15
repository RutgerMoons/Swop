package domain.assembly.workBench;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.job.job.UnmodifiableJob;
import domain.job.task.ITask;
import domain.job.task.UnmodifiableTask;


/**
 * Create an Immutable WorkBench, only the getters are accessible.
 *
 */
public class UnmodifiableWorkBench implements IWorkBench {

	private IWorkBench bench;
	
	/**
	 * Create an Immutable WorkBench.
	 *  
	 * @param 	bench
	 * 			The mutable WorkBench.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the given bench is null
	 */
	public UnmodifiableWorkBench(IWorkBench bench){
		if(bench==null)
			throw new IllegalArgumentException();
		this.bench = bench;
	}
	
	//TODO doc
	@Override
	public WorkbenchType getWorkbenchType() {
		return bench.getWorkbenchType();
	}

	//TODO doc
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

	//TODO doc
	@Override
	public Set<String> getResponsibilities() {
		return Collections.unmodifiableSet(bench.getResponsibilities());
	}

	//TODO doc
	@Override
	public List<ITask> getCurrentTasks() {
		List<ITask> unmodifiable = new ArrayList<>();
		for(ITask task: bench.getCurrentTasks()){
			unmodifiable.add(new UnmodifiableTask(task));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	//TODO doc
	@Override
	public boolean isCompleted() {
		return bench.isCompleted();
	}
	
	@Override
	public String toString(){
		return bench.toString();
	}
	
	@Override
	public void setCurrentJob(Optional<IJob> retrieveNextJob) {
		throw new UnmodifiableException();
	}
	
	@Override
	public void chooseTasksOutOfJob() {
		throw new UnmodifiableException();		
	}
	
	@Override
	public void completeChosenTaskAtChosenWorkBench(ITask task) {
		throw new UnmodifiableException();		
	}
}
