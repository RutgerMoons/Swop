package domain.job.job;

import java.util.Collection;
import java.util.List;

import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.order.IOrder;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Interface for limiting access to standard Jobs.
 */
public interface IJob {

	/**
	 * Get the Order on which the Job is based.
	 */
	public IOrder getOrder();

	/**
	 * Allocate a new Order to this Job.
	 * 
	 * @throws UnmodifiableException 
	 * 		  	  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If order==null
	 */
	public void setOrder(IOrder order) throws UnmodifiableException;

	/**
	 * Get the tasks that have to be completed before the Job(Car) is finished.
	 * 
	 * @return An Immutable list of tasks.
	 */
	public List<ITask> getTasks();

	/**
	 * Set a new list of tasks that have to be completed before the Job(Car) is
	 * finished.
	 * 
	 * @throws UnmodifiableException
	 * 			  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If tasks==null
	 */
	public void setTasks(List<ITask> tasks) throws UnmodifiableException;

	/**
	 * Add a new task to the Job.
	 * 
	 * @throws UnmodifiableException 
	 * 		 	  If the IJob is an ImmutableJob.
	 * @throws IllegalArgumentException
	 *             If task==null
	 */
	public void addTask(ITask task) throws UnmodifiableException;

	/**
	 * Check if the Job(Car) is completed.
	 * 
	 * @return True if all Tasks are fully completed. False if one or more Tasks
	 *         are not fully completed.
	 */
	public boolean isCompleted();

	/**
	 * Set the minimalIndex of the first workbench at which some Tasks of this Job will need to be completed qto the given index.
	 */
	public void setMinimalIndex(int index) throws UnmodifiableException;

	/**
	 * @return
	 * 		An integer that represents the index of the first workbench at which some Tasks of this Job will need to be completed.
	 */
	public int getMinimalIndex();
	
	/**
	 * @return	Collection of all the VehicleOptions of which the ordered cars consist
	 */
	public Collection<VehicleOption> getVehicleOptions();
	
	/**
	 * Passes request to the order and let it decide how it should be added
	 * 
	 * @param	schedulingAlgorithm this needs to be added to
	 */
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm);
	
	/**
	 * Get the time the specification has to spend on a workbench.
	 */
	public int getTimeAtWorkBench();

	/**
	 * Get the specification of the vehicle from the order.
	 */
	public VehicleSpecification getVehicleSpecification();
	

}
