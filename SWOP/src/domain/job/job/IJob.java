package domain.job.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkbenchType;
import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.order.order.IOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * An interface used to represent an Job. An Job consists of an iOrder, a list of
 * ITasks, an index, indicating which workbench it needs first, and a boolean 
 * for indicating if the Action is completed.
 */
public interface IJob {

	/**
	 * Get the Order on which the Job is based.
	 */
	public IOrder getOrder();

	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @throws 	UnmodifiableException
	 * 			Thrown when the IJob is unmodifiable
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when	the given parameter is null
	 */
	public void setOrder(IOrder order);

	/**
	 * Get the tasks that have to be completed before the Job is finished.
	 * 
	 * @return 	An immutable list of tasks
	 */
	public List<ITask> getTasks();

	/**
	 * Set a new list of tasks that have to be completed before the Job is
	 * finished.
	 * 
	 * @throws 	UnmodifiableException
	 * 			Thrown when the IJob is an unmodifiable Job
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the given parameter is null
	 */
	public void setTasks(List<ITask> tasks);

	/**
	 * Add a new task to the Job.
	 * 
	 * @throws 	UnmodifiableException 
	 * 		 	Thrown when the IJob is an unmodifiable Job
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the given parameter is null
	 */
	public void addTask(ITask task);

	/**
	 * Check if the Job(Car) is completed.
	 * 
	 * @return 	True if all Tasks are fully completed. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean isCompleted();

	/**
	 * The given index represents the first WorkBench at which some Tasks of this Job need to be
	 * completed.
	 */
	public void setMinimalIndex(int index);

	/**
	 * Returns an integer that represents the index of the first workbench 
	 * at which some Tasks of this Job will need to be completed.
	 */
	public int getMinimalIndex();
	
	/**
	 * Returns a collection of all the VehicleOptions of which the ordered cars consist.
	 */
	public Collection<VehicleOption> getVehicleOptions();
	
	/**
	 * Get the time the specification has to spend on a workbench.
	 */
	public Map<WorkbenchType, Integer> getTimeAtWorkBench();

	/**
	 * Get the specification of the vehicle from the order
	 */
	public VehicleSpecification getVehicleSpecification();

	/**
	 * Check if the job can be handled by the responsibilities.
	 * 
	 * @param 	responsibilities
	 * 			The responsibilities of the AssemblyLine
	 * 
	 * @return 	True if all TODO. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean canBeHandled(Set<VehicleSpecification> responsibilities);
	

}
