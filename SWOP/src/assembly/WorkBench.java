package assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

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
	 * @param workbenchName
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

	
	public String getWorkbenchName() {
		return workbenchName;
	}


	public Optional<IJob> getCurrentJob() {
		if(currentJob.isPresent()){
			IJob immutable = new ImmutableJob(currentJob.get());
			Optional<IJob> job = Optional.fromNullable(immutable);
			return job;
		}
		return currentJob;
	}

	public void setCurrentJob(Optional<IJob> optional) {
		if (optional == null)
			throw new IllegalArgumentException();
		this.currentJob = optional;
	}

	
	public Set<String> getResponsibilities() {
		return responsibilities;
	}

	public void setResponsibilities(Set<String> responsibilities) {
		if (responsibilities == null)
			throw new IllegalArgumentException();
		this.responsibilities = responsibilities;
	}

	public void addResponsibility(String responibility) {
		if (responibility == null || responibility.isEmpty())
			throw new IllegalArgumentException();
		responsibilities.add(responibility);
	}


	public List<ITask> getCurrentTasks() {
		return currentTasks;
	}

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
