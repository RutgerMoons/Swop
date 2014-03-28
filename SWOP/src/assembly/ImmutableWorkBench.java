package assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

public class ImmutableWorkBench implements IWorkBench {

	private IWorkBench bench;
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
		return bench.getResponsibilities();
	}

	@Override
	public List<ITask> getCurrentTasks() {
		return bench.getCurrentTasks();
	}

	@Override
	public boolean isCompleted() {
		return bench.isCompleted();
	}
	
	@Override
	public String toString(){
		return bench.toString();
	}

}
