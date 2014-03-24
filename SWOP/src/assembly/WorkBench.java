package assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

/**
 * Represents a WorkBench from an assemblyline.
 * 
 */
public class WorkBench {

	private Set<String> responsibilities;
	private Optional<Job> currentJob;
	private List<Task> currentTasks;
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
	 *             -if workbenchName==null or isEmpty
	 *             -if responsibilities==null
	 */
	public WorkBench(Set<String> responsibilities, String workbenchName) {
		if (workbenchName == null || workbenchName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.workbenchName = workbenchName;
		this.setResponsibilities(responsibilities);
		setCurrentTasks(new ArrayList<Task>());
		Optional<Job> nullJob = Optional.absent();
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
	public Optional<Job> getCurrentJob() {
		return currentJob;
	}

	/**
	 * Allocate a new Job(Car) to this WorkBench.
	 * 
	 * @param currentJob
	 *            The job you want to allocate to the WorkBench.
	 * @throws IllegalArgumentException
	 *             if currentJob == null
	 */
	public void setCurrentJob(Optional<Job> currentJob) {
		if (currentJob == null)
			throw new IllegalArgumentException();
		this.currentJob = currentJob;
	}

	/**
	 * Get the list of responsibilities of this WorkBench. So the types of Tasks
	 * that have to be performed by this workbench.
	 * 
	 * @return A list of responsibilities.
	 */
	public Set<String> getResponsibilities() {
		return responsibilities;
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
		getResponsibilities().add(responibility);
	}

	/**
	 * Get the current tasks that have to be completed by this WorkBench.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<Task> getCurrentTasks() {
		ImmutableList<Task> immutable = new ImmutableList.Builder<Task>()
				.addAll(currentTasks).build();
		return immutable;
	}

	/**
	 * Set the tasks that have to be completed by this WorkBench.
	 * 
	 * @param currentTasks
	 *            A list of tasks.
	 * @throws IllegalArgumentException
	 *             If currentTasks==null
	 */
	public void setCurrentTasks(List<Task> currentTasks) {
		if (currentTasks == null)
			throw new IllegalArgumentException();
		this.currentTasks = currentTasks;
	}

	/**
	 * Selects the Tasks that are valid for this Workbench. The taskDescription
	 * is checked against the responsibilities from the Workbench.
	 */
	public void chooseTasksOutOfJob() {
		if (getCurrentJob() == null || !getCurrentJob().isPresent()) {
			setCurrentTasks(new ArrayList<Task>());
			return;
		}
		List<Task> tasks = new ArrayList<>();
		for (Task task : getCurrentJob().get().getTasks())
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
		for (Task task : getCurrentTasks())
			if (!task.isCompleted())
				return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getWorkbenchName();
	}
}
