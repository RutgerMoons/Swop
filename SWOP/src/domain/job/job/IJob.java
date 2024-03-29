package domain.job.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.order.order.IOrder;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * An interface used to represent a Job. A Job consists of an IOrder, a list of
 * ITasks and an index, indicating which workbench it needs first.
 */
public interface IJob {

	/**
	 * Get the Order on which the Job is based.
	 */
	public IOrder getOrder();

	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @param 	order
	 * 			The new order
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
	 * @return 	A list of tasks
	 */
	public List<ITask> getTasks();

	/**
	 * Set a new list of tasks that have to be completed before the Job is
	 * finished.
	 * 
	 * @param	tasks
<<<<<<< HEAD
	 * 			The new List of ITasks
=======
	 * 			The new list of ITasks.
>>>>>>> a2af1d3d03c2f3789f23ba67172ac7e243afb60c
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
	 * @param	task
	 * 			The new task
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
	 *         are not fully completed
	 */
	public boolean isCompleted();

	/**
	 * Set a new index that represents the first WorkBench at which some Tasks of this Job need to be
	 * completed.
	 * 
	 * @param	index
	 * 			The new index
	 */
	public void setMinimalIndex(int index);

	/**
	 * Returns an integer that represents the index of the first WorkBench 
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
	public Map<WorkBenchType, Integer> getTimeAtWorkBench();

	/**
	 * Get the specification of the Vehicle from the Order.
	 */
	public VehicleSpecification getVehicleSpecification();

	/**
	 * Retrieve the production time to complete this Job at the given type of WorkBench.
	 * 
	 * @param	workBenchType 
	 * 			It's used to determine the time this job will take to complete
	 * 
	 * @return	The amount of minutes to complete this job at the given type of WorkBench
	 */
	public int getProductionTime(WorkBenchType workBenchType);

	/**
	 * Get the total production time for the Job to finish.
	 */
	public int getTotalProductionTime();

	/**
	 * Visit the order.
	 * 
	 * @param	visitor	
	 * 			The IOrderVisitor that visits the IOrder
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when visitor is null
	 */
	public void acceptVisit(IOrderVisitor visitor);
}
