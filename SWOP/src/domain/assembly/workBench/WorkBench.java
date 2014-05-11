package domain.assembly.workBench;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.job.task.ITask;

/**
 * Represents a WorkBench from an assemblyLine.
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
	 * @param workbenchName
	 *            A name for this workbench
	 * @throws IllegalArgumentException
	 *            Thrown when workbenchName==null or isEmpty -if responsibilities==null
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

	
	@Override
	public String getWorkbenchName() {
		return workbenchName;
	}


	@Override
	public Optional<IJob> getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Optional<IJob> optional) {
		if (optional == null)
			throw new IllegalArgumentException();
		this.currentJob = optional;
	}

	
	@Override
	public Set<String> getResponsibilities() {
		return responsibilities;
	}

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
	 * @throws UnmodifiableException 
	 * 			  If the IWorkBench is an ImmutableWorkBench.
	 * @throws IllegalArgumentException
	 *             If responsibility==null or isEmpty.
	 */
	public void addResponsibility(String responibility) {
		if (responibility == null || responibility.isEmpty())
			throw new IllegalArgumentException();
		responsibilities.add(responibility);
	}


	@Override
	public List<ITask> getCurrentTasks() {
		return currentTasks;
	}

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
	public void setCurrentTasks(List<ITask> list) {
		if (list == null)
			throw new IllegalArgumentException();
		this.currentTasks = list;
	}

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

	
	@Override
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
