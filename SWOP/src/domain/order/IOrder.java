package domain.order;

import domain.car.IVehicle;
import domain.clock.ImmutableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

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
	 * @throws ImmutableException
	 * 			if the IOrder is an ImmutableOrder.
	 */
	public void setDeadline(ImmutableClock clock) throws NotImplementedException, ImmutableException;
	
	/**
	 * Get the estimated time until completion.
	 */
	public ImmutableClock getEstimatedTime();
	
	/**
	 * Set the estimated time of completion of the Order.
	 */
	public void setEstimatedTime(ImmutableClock clock) throws ImmutableException;
	 
	/**
	 * Get the time when the Order is ordered.
	 */
	public ImmutableClock getOrderTime();

	/**
	 * Set the time when the Order is ordered. 
	 * @throws ImmutableException
	 * 			if the IOrder is an ImmutableOrder.
	 */
	public void setOrderTime(ImmutableClock clock) throws ImmutableException;
	
	/**
	 * Method for decreasing the amount of pendingCars each time an car of the
	 * order is completed.
	 * @throws ImmutableException 
	 * 			If the IOrder is an ImmutableOrder
	 */
	public void completeCar() throws ImmutableException;
	
}