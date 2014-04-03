package assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import exception.ImmutableException;

/**
 * Create an Immutable WorkBench, only the getters are accessible.
 *
 */
public class ImmutableWorkBench implements IWorkBench {

	private IWorkBench bench;
	
	/**
	 * Create an Immutable WorkBench.
	 *  
	 * @param bench
	 * 			The mutable WorkBench.
	 */
	public ImmutableWorkBench(IWorkBench bench){
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
		return bench.getCurrentJob();
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
	public void setCurrentJob(Optional<IJob> optional) throws ImmutableException {
		throw new ImmutableException();		
	}
	@Override
	public void setResponsibilities(Set<String> responsibilities) throws ImmutableException {
		throw new ImmutableException();		
	}
	@Override
	public void addResponsibility(String responibility) throws ImmutableException {
		throw new ImmutableException();		
	}
	@Override
	public void setCurrentTasks(List<ITask> list) throws ImmutableException {
		throw new ImmutableException();		
	}
	@Override
	public void chooseTasksOutOfJob() throws ImmutableException {
		throw new ImmutableException();		
	}

}
