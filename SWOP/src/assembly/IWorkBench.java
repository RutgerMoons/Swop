package assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

public interface IWorkBench {

	/**
	 * Get the name of the workbench
	 * 
	 * @return The name of the workbench
	 */
	public String getWorkbenchName();
	
	/**
	 * Get the current Job(Car) that is on this WorkBench.
	 * 
	 * @return The current Job this WorkBench is working on. If there is no Job
	 *         available, the Job is represented by Optional.absent().
	 */
	public Optional<IJob> getCurrentJob();
	
	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks
	 * that have to be performed by this workbench.
	 * 
	 * @return A list of responsibilities.
	 */
	public Set<String> getResponsibilities();
	
	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getCurrentTasks();
	
	/**
	 * Check if the WorkBench has completed all of his Tasks.
	 * 
	 * @return True if all the Tasks are completed. False if one or more Tasks
	 *         are not completed yet.
	 */
	public boolean isCompleted();
	
}
