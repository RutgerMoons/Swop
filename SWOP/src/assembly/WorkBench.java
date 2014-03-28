package assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Represents a WorkBench from an assemblyline.
 * 
 */
public class WorkBench implements IWorkBench {

	private Set<String> responsibilities;
	private Optional<IJob> currentJob;
	private List<ITask> currentTasks;
	private final String workbenchName;

	/**
	 * Construct a new Workbench.
	 * 
	 * @param responsibilities
	 *            A list of strings. The types of Tasks that have to be
	 *            performed by this WorkBench.
	 * @param workbench_name
	 *            A name for this workbench
	 * @throws IllegalArgumentException
	 *             -if workbenchName==null or isEmpty -if responsibilities==null
	 */
	public WorkBench(Set<String> responsibilities, String workbenchName) {
		if (workbenchName == null || workbenchName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.workbenchName = workbenchName;
		this.setResponsibilities(responsibilities);
		setCurrentTasks(new ArrayList<ITask>());
		Optional<IJob> nullJob = Optional.absent();
		setCurrentJob(nullJob);
	}

	/**
	 * Get the name of the workbench
	 * 
	 * @return The name of the workbench
	 */
	public String getWorkbenchName() {
		return workbenchName;
	}

	/**
	 * Get the current Job(Car) that is on this WorkBench.
	 * 
	 * @return The current Job this WorkBench is working on. If there is no Job
	 *         available, the Job is represented by Optional.absent().
	 */
	public Optional<IJob> getCurrentJob() {
		if(currentJob.isPresent()){
			IJob immutable = new ImmutableJob(currentJob.get());
			Optional<IJob> job = Optional.fromNullable(immutable);
			return job;
		}
		return currentJob;
	}

	/**
	 * Allocate a new Job(Car) to this WorkBench.
	 * 
	 * @param optional
	 *            The job you want to allocate to the WorkBench.
	 * @throws IllegalArgumentException
	 *             if currentJob == null
	 */
	public void setCurrentJob(Optional<IJob> optional) {
		if (optional == null)
			throw new IllegalArgumentException();
		this.currentJob = optional;
	}

	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks
	 * that have to be performed by this workbench.
	 * 
	 * @return A list of responsibilities.
	 */
	public Set<String> getResponsibilities() {
		return new ImmutableSet.Builder<String>().addAll(responsibilities)
				.build();
	}

	/**
	 * Set a new list of responsibilities for thies WorkBench.
	 * 
	 * @param responsibilities
	 *            A list of responsibilities.
	 * @throws IllegalArgumentException
	 *             If responsibilities==null
	 */
	public void setResponsibilities(Set<String> responsibilities) {
		if (responsibilities == null)
			throw new IllegalArgumentException();
		this.responsibilities = responsibilities;
	}

	/**
	 * Add a responsibility to this WorkBench.
	 * 
	 * @param responibility
	 *            The responsibility you want to add.
	 * @throws IllegalArgumentException
	 *             If responsibility==null or isEmpty.
	 */
	public void addResponsibility(String responibility) {
		if (responibility == null || responibility.isEmpty())
			throw new IllegalArgumentException();
		responsibilities.add(responibility);
	}

	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getCurrentTasks() {
		return new ImmutableList.Builder<ITask>().addAll(currentTasks).build();
	}

	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * 
	 * @param list
	 *            A list of tasks.
	 * @throws IllegalArgumentException
	 *             If currentTasks==null
	 */
	public void setCurrentTasks(List<ITask> list) {
		if (list == null)
			throw new IllegalArgumentException();
		this.currentTasks = list;
	}

	/**
	 * Selects the Tasks that are valid for this Workbench. The taskDescription
	 * is checked against the responsibilities from the Workbench.
	 */
	public void chooseTasksOutOfJob() {
		if (getCurrentJob() == null || !getCurrentJob().isPresent()) {
			setCurrentTasks(new ArrayList<ITask>());
			return;
		}
		List<ITask> tasks = new ArrayList<>();
		for (ITask task : getCurrentJob().get().getTasks())
			if (getResponsibilities().contains(task.getTaskDescription())
					&& !task.isCompleted()) {
				tasks.add(task);
			}
		setCurrentTasks(tasks);
	}

	/**
	 * Check if the WorkBench has completed all of his Tasks.
	 * 
	 * @return True if all the Tasks are completed. False if one or more Tasks
	 *         are not completed yet.
	 */
	public boolean isCompleted() {
		for (ITask task : getCurrentTasks())
			if (!task.isCompleted())
				return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getWorkbenchName();
	}
}
