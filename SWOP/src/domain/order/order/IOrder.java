package domain.order.order;

import java.util.Collection;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.exception.UnmodifiableException;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * An interface used to represent an Order. All types of order need to implement this interface. 
 */
public interface IOrder {

	/**
	 * Returns the name of the garage holder.
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
	 * Get the IVehicle that belongs to this IOrder.
	 */
	public IVehicle getDescription();
	
	/**
	 * Get the production time of the order, so how long it takes to produce all the cars. 
	 */
	public int getProductionTime();
	
	/**
	 * Get the deadline of a CustomOrder.
	 *  
	 * @throws	NotImplementedException
	 * 			Thrown when the Order isn't a CustomOrder
	 */
	public ImmutableClock getDeadline();
	
	/**
	 * Set the deadline of the CustomOrder
	 * 
	 * @throws	NotImplementedException
	 * 			Thrown when the Order isn't a CustomOrder
	 * 
	 * @throws 	UnmodifiableException
	 * 			Thrown when the IOrder is an unmodifiable Order
	 */
	public void setDeadline(ImmutableClock clock);
	
	/**
	 * Get the estimated time until completion.
	 */
	public ImmutableClock getEstimatedTime();
	
	/**
	 * Set the estimated time of completion of the Order.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the clock is null
	 */
	public void setEstimatedTime(ImmutableClock clock);
	 
	/**
	 * Get the time when the IOrder is ordered.
	 */
	public ImmutableClock getOrderTime();

	/**
	 * Set the time when the IOrder is ordered.
	 * 
	 * @throws 	UnmodifiableException
	 * 			Thrown when the IOrder is an unmodifiable order
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the clock is null
	 */
	public void setOrderTime(ImmutableClock clock) ;
	
	/**
	 * Method for decreasing the amount of pendingCars each time a car of the
	 * IOrder is completed.
	 * 
	 * @throws	UnmodifiableException 
	 * 			If the IOrder is an unmodifiable Order
	 */
	public void completeCar();
	
	/**
	 * Get all the VehicleOptions of which the ordered IVehicles consist.
	 */
	public Collection<VehicleOption> getVehicleOptions();
	
	/**
	 * Get the time the specification has to spend on a WorkBench.
	 */
	public Map<WorkBenchType, Integer> getTimeAtWorkBench();

	/**
	 * Get the VehicleSpecification of the IVehicle from the IOrder.
	 */
	public VehicleSpecification getVehicleSpecification();
	
	/*-------------------------------------------------
	 * 
	 * VISITABLE
	 * 
	 * ----------------------------------------------*/
	/**
	 * Let the IOrderVisitor visit the IOrder and the IOrder decides what to do.
	 * 
	 * @param 	visitor
	 * 			The visitor that visits the IOrder
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when visitor is null
	 */
	public void acceptVisit(IOrderVisitor visitor);
}
