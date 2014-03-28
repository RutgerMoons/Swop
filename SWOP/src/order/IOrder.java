package order;

import car.ICarModel;

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
	
	/**
	 * Get the estimated time untill completion.
	 * 
	 * @return An array of 2 integers, with the first the days untill completion
	 *         (1 is tomorrow), the second integer gives the time of completion
	 *         (in minutes) on that day.
	 */
	public int[] getEstimatedTime();
	
	
	
}
