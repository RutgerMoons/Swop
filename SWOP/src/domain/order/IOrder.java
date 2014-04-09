package domain.order;

import domain.car.ICarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

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
	public ICarModel getDescription();
	
	public int getProductionTime();
	
	public UnmodifiableClock getDeadline() throws NotImplementedException;
	
	public void setDeadline(UnmodifiableClock clock) throws NotImplementedException, ImmutableException;
	
	/**
	 * Get the estimated time until completion.
	 * 
	 * @return An array of 2 integers, with the first the days until completion
	 *         (1 is tomorrow), the second integer gives the time of completion
	 *         (in minutes) on that day.
	 */
	public UnmodifiableClock getEstimatedTime();
	
	/**
	 * Set the estimated time, the first integer gives the days from today
	 * before it's finished (so 1 is tomorrow); the second integer gives the
	 * time of completion (in minutes) on that day.
	 * 
	 * @param array
	 * @throws ImmutableException 
	 * 			If the IOrder is an ImmutableOrder.
	 */
	public void setEstimatedTime(UnmodifiableClock clock) throws ImmutableException;
	
	// TODO 
	public UnmodifiableClock getOrderTime();
	
	// TODO
	public void setOrderTime(UnmodifiableClock clock) throws ImmutableException;
	
	/**
	 * Method for decreasing the amount of pendingCars each time an car of the
	 * order is completed.
	 * @throws ImmutableException 
	 * 			If the IOrder is an ImmutableOrder
	 */
	public void completeCar() throws ImmutableException;
	
}
