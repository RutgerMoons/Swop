package domain.assembly;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.exception.UnmodifiableException;
import domain.job.IJob;
import domain.job.ITask;

/**
 * Interface for limiting access to standard WorkBenches.
 */
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
	 * Allocate a new Job(Car) to this WorkBench.
	 * 
	 * @param optional
	 *            The job you want to allocate to the WorkBench.
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 * @throws IllegalArgumentException
	 *             if currentJob == null
	 */
	public void setCurrentJob(Optional<IJob> optional) throws UnmodifiableException;
	
	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks
	 * that have to be performed by this workbench.
	 * 
	 * @return A list of responsibilities.
	 */
	public Set<String> getResponsibilities();
	
	/**
	 * Set a new list of responsibilities for thies WorkBench.
	 * 
	 * @param responsibilities
	 *            A list of responsibilities.
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 * @throws IllegalArgumentException
	 *             If responsibilities==null
	 */
	public void setResponsibilities(Set<String> responsibilities) throws UnmodifiableException;
	
	/**
	 * Add a responsibility to this WorkBench.
	 * 
	 * @param responibility
	 *            The responsibility you want to add.
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 * @throws IllegalArgumentException
	 *             If responsibility==null or isEmpty.
	 */
	public void addResponsibility(String responibility) throws UnmodifiableException;
	
	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getCurrentTasks();
	
	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * 
	 * @param list
	 *            A list of tasks.
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 * @throws IllegalArgumentException
	 *             If currentTasks==null
	 */
	public void setCurrentTasks(List<ITask> list) throws UnmodifiableException;
	
	/**
	 * Selects the Tasks that are valid for this Workbench. The taskDescription
	 * is checked against the responsibilities from the Workbench.
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 */
	public void chooseTasksOutOfJob() throws UnmodifiableException;
	
	/**
	 * Check if the WorkBench has completed all of his Tasks.
	 * 
	 * @return True if all the Tasks are completed. False if one or more Tasks
	 *         are not completed yet.
	 */
	public boolean isCompleted();
	
}
