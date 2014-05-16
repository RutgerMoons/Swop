package domain.assembly.workBench;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.job.job.IJob;
import domain.job.task.ITask;

/**
 * Interface for limiting access to standard WorkBenches.
 */
public interface IWorkBench {

	/**
	 * Get the type of the workbench.
	 */
	public WorkBenchType getWorkbenchType();
	
	/**
	 * Get the current Job(Car) that is on this WorkBench.
	 * 
	 * @return 	The current Job this WorkBench is working on. If there is no Job
	 *         	available, the Job is represented by Optional.absent().
	 */
	public Optional<IJob> getCurrentJob();
	
	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks
	 * that have to be performed by this workbench.
	 * 
	 * @return 	A list of responsibilities.
	 */
	public Set<String> getResponsibilities();
	
	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * 
	 * @return 	An Immutable list of tasks.
	 */
	public List<ITask> getCurrentTasks();
	
	/**
	 * Check if the WorkBench has completed all of his Tasks.
	 * 
	 * @return 	True if all the Tasks are completed. False if one or more Tasks
	 *         	are not completed yet.
	 */
	public boolean isCompleted();

	/**
	 * Allocate a new Job(Car) to this WorkBench.
	 * 
	 * @param 	optional
	 *            The job you want to allocate to the WorkBench.
	 * @throws 	IllegalArgumentException
	 *             if currentJob == null
	 */
	public void setCurrentJob(Optional<IJob> retrieveNextJob);

	/**
	 * Selects the Tasks that are valid for this Workbench. The taskDescription
	 * is checked against the responsibilities from the Workbench.
	 */
	public void chooseTasksOutOfJob();

	/**
	 * Matches the given task to one of its own. If a match is found, 
	 * all the actions will be set to completed.
	 * 
	 * @param	task to be matched and set to completed
	 */
	void completeChosenTaskAtChosenWorkBench(ITask task);
}