package assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

public interface IWorkBench {

	public String getWorkbenchName();
	
	public Optional<IJob> getCurrentJob();
	
	public Set<String> getResponsibilities();
	
	public List<ITask> getCurrentTasks();
	
	public boolean isCompleted();
	
}
