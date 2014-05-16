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
 * Interface for limiting access to standard Orders
 */
public interface IOrder {

	/**
	 * Returns the name of the garageholder
	 */
	public String getGarageHolder();
	
	/**
	 * Method for retrieving the amount of cars still to be completed
	 */
	public int getPendingCars();
	
	/**
	 * Method for retrieving the amount of cars ordered
	 */
	public int getQuantity();
	
	/**
	 * Returns an unmodifiable Vehicle for encapsulation
	 */
	public IVehicle getDescription();
	
	/**
	 * Get the production time of the order, so how long it takes to produce all the cars 
	 */
	public int getProductionTime();
	
	/**
	 * Get the deadline of a CustomOrder
	 *  
	 * @throws 	NotImplementedException
	 * 				if the Order isn't a CustomOrder
	 */
	public ImmutableClock getDeadline();
	
	/**
	 * Set the deadline of the CustomOrder
	 * 
	 * @throws 	NotImplementedException
	 * 				if the Order isn't a CustomOrder
	 * 
	 * @throws 	UnmodifiableException
	 * 				if the IOrder is an unmodifiable Order
	 */
	public void setDeadline(ImmutableClock clock);
	
	/**
	 * Get the estimated time until completion
	 */
	public ImmutableClock getEstimatedTime();
	
	/**
	 * Set the estimated time of completion of the Order
	 */
	public void setEstimatedTime(ImmutableClock clock);
	 
	/**
	 * Get the time when the Order is ordered
	 */
	public ImmutableClock getOrderTime();

	/**
	 * Set the time when the Order is ordered 
	 * 
	 * @throws 	UnmodifiableException
	 * 				if the IOrder is an unmodifiable Orde
	 */
	public void setOrderTime(ImmutableClock clock) ;
	
	/**
	 * Method for decreasing the amount of pendingCars each time an car of the
	 * order is completed
	 * 
	 * @throws 	UnmodifiableException 
	 * 				If the IOrder is an unmodifiable Order
	 */
	public void completeCar();
	
	/**
	 * @return	Collection of all the VehicleOptions of which the ordered cars consist
	 */
	public Collection<VehicleOption> getVehicleOptions();
	
	/**
	 * Get the time the specification has to spend on a workbench
	 */
	public Map<WorkBenchType, Integer> getTimeAtWorkBench();

	/**
	 * Get the specification of the vehicle from the order.
	 * 
	 * @throws	NotImplementedException
	 * 				If the order is a custom order
	 */
	public VehicleSpecification getVehicleSpecification();
	
	/*-------------------------------------------------
	 * 
	 * VISITABLE
	 * 
	 * ----------------------------------------------*/
	public void acceptVisit(IOrderVisitor visitor);
}
