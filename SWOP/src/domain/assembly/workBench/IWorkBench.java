package domain.assembly.workBench;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.job.job.IJob;
import domain.job.task.ITask;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * An interface representing a workbench. A WorkBench
 * is responsible for completing Tasks. Each WorkBench has a WorkBenchType.
 */
public interface IWorkBench {

	/**
	 * Get the type of the workbench.
	 */
	public WorkBenchType getWorkbenchType();
	
	/**
	 * Get the current Job that is on this WorkBench.
	 * 
	 * @return 	The current Job this WorkBench is working on. If there is no Job
	 *         	available, the Job is represented by Optional.absent().
	 */
	public Optional<IJob> getCurrentJob();
	
	/**
	 * Get the list of responsibilities of this WorkBench. So the types of VehicleOptions
	 * that have to be assembled at this WorkBench.
	 * 
	 * @return 	A list of responsibilities.
	 */
	public Set<VehicleOptionCategory> getResponsibilities();
	
	/**
	 * Get the current Tasks that have to be completed by this WorkBench.
	 * 
	 * @return 	An unmodifiable list of Tasks.
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
	 * Allocate a new Job to this WorkBench.
	 * 
	 * @param 	job
	 *          The job you want to allocate to the WorkBench
	 *          
	 * @throws 	IllegalArgumentException
	 *          Thrown when the parameter is null
	 */
	public void setCurrentJob(Optional<IJob> job);

	/**
	 * Selects the Tasks that this Workbench can complete.
	 */
	public void chooseTasksOutOfJob();

	/**
	 * Matches the given task to one of its own. If a match is found, 
	 * all the actions will be set to completed.
	 * 
	 * @param	task 
	 * 			The Task to be matched and set to completed
	 */
	void completeChosenTaskAtChosenWorkBench(ITask task);
}