package domain.order;

import java.util.Collection;

import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.job.IJob;
import domain.scheduling.schedulingAlgorithm.SchedulingAlgorithm;
import domain.vehicle.IVehicle;
import domain.vehicle.IVehicleOption;

/**
 * Interface for limiting access to standard Orders.
 */
public interface IOrder {

	/**
	 * Returns the name of the garageholder.
	 */
	public String getGarageHolder();
	
	/**
	 * Method for retrieving the amount of cars still to be completed.
	 */
	public int getPendingCars();
	
	/**
	 * Method for retrieving the amount of cars ordered.
	 */
	public int getQuantity();
	
	/**
	 * Returns an Immutable CarModel for encapsulation.
	 */
	public IVehicle getDescription();
	
	/**
	 * Get the production time of the order, so how long it takes to produce all the cars. 
	 */
	public int getProductionTime();
	
	/**
	 * Get the deadline of a CustomOrder.
	 *  
	 * @throws NotImplementedException
	 * 			if the Order isn't a CustomOrder
	 */
	public ImmutableClock getDeadline() throws NotImplementedException;
	
	/**
	 * Set the deadline of the CustomOrder
	 * @throws NotImplementedException
	 * 			if the Order isn't a CustomOrder
	 * @throws UnmodifiableException
	 * 			if the IOrder is an ImmutableOrder.
	 */
	public void setDeadline(ImmutableClock clock) throws NotImplementedException, UnmodifiableException;
	
	/**
	 * Get the estimated time until completion.
	 */
	public ImmutableClock getEstimatedTime();
	
	/**
	 * Set the estimated time of completion of the Order.
	 */
	public void setEstimatedTime(ImmutableClock clock) throws UnmodifiableException;
	 
	/**
	 * Get the time when the Order is ordered.
	 */
	public ImmutableClock getOrderTime();

	/**
	 * Set the time when the Order is ordered. 
	 * @throws UnmodifiableException
	 * 			if the IOrder is an ImmutableOrder.
	 */
	public void setOrderTime(ImmutableClock clock) throws UnmodifiableException;
	
	/**
	 * Method for decreasing the amount of pendingCars each time an car of the
	 * order is completed.
	 * @throws UnmodifiableException 
	 * 			If the IOrder is an ImmutableOrder
	 */
	public void completeCar() throws UnmodifiableException;
	
	/**
	 * @return	Collection of all the VehicleOptions of which the ordered cars consist
	 */
	public Collection<IVehicleOption> getVehicleOptions();
	
	/**
	 * Let this decide how it should be added
	 * 
	 * @param	schedulingAlgorithm job needs to be added to
	 * 
	 * @param	job that needs to be added
	 */
	public void addToSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm, IJob job);
	
}