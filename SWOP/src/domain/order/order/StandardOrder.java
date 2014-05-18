package domain.order.order;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * A class representing an order of a Vehicle.
 * It consists of a Vehicle, the garage holder who ordered the Vehicle, a quantity,
 * the time when you ordered it and the estimated time of completion.
 */
public class StandardOrder implements IOrder {

	private IVehicle description;
	private String garageholder;
	private int quantity, pendingCars;
	private ImmutableClock estimatedTime;
	private ImmutableClock orderTime;

	/**
	 * Create a new Order.
	 *  
	 * @param 	holder
	 * 			The garage holder that orders the Vehicles
	 * 
	 * @param 	description
	 * 			The vehicle that has to be manufactured
	 * 
	 * @param 	quantity
	 * 			How many Vehicles there have to be manufactured
	 * 
	 * @param 	orderTime
	 * 			The time when this order is placed
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null, the holder is empty or the quantity is less than or equal to zero
	 */
	public StandardOrder(String holder, Vehicle description, int quantity, ImmutableClock orderTime) {
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
		setOrderTime(orderTime);
	}

	private void setGarageHolder(String holder) {
		if (holder == null || holder.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.garageholder = holder;
	}

	@Override
	public String getGarageHolder() {
		return this.garageholder;
	}

	private void setPendingCars(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException();
		}
		this.pendingCars = quantity;
	}

	
	@Override
	public int getPendingCars() {
		return this.pendingCars;
	}

	private void setQuantity(int quantity) {
		if (quantity <= 0) {
			throw new IllegalArgumentException();
		}
		this.quantity = quantity;
	}

	
	@Override
	public int getQuantity() {
		return this.quantity;
	}

	private void setDescription(IVehicle description2) {
		if (description2 == null) {
			throw new IllegalArgumentException();
		}
		this.description = description2;
	}
	
	@Override
	public IVehicle getDescription() {
		return description;
	}

	@Override
	public ImmutableClock getOrderTime() {
		return orderTime;
	}

	@Override
	public void setOrderTime(ImmutableClock orderTime) {
		if(orderTime == null){
			throw new IllegalArgumentException();
		}
		this.orderTime = orderTime;
	}

	@Override
	public ImmutableClock getEstimatedTime() {
		return this.estimatedTime;
	}

	@Override
	public void setEstimatedTime(ImmutableClock clock) {
		if (clock == null) {
			throw new IllegalArgumentException();
		}
		this.estimatedTime = clock;
	}

	@Override
	public void completeCar() {
		this.setPendingCars(--pendingCars);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + garageholder.hashCode();
		result = prime * result + pendingCars;
		result = prime * result + quantity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		IOrder other;
		try{
		other = (IOrder) obj;
		} catch (ClassCastException e){
			return false;
		}
		if (!description.equals(other.getDescription()))
			return false;
		if (!garageholder.equals(other.getGarageHolder()))
			return false;
		if (quantity != other.getQuantity())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getQuantity() + " " + this.getDescription();
	}

	@Override
	public ImmutableClock getDeadline(){
		throw new NotImplementedException();
	}

	@Override
	public void setDeadline(ImmutableClock clock){
		throw new NotImplementedException();
	}
	
	@Override
	public int getProductionTime() {
		int time = 0;
		for(WorkBenchType type: getTimeAtWorkBench().keySet()){
			time += getTimeAtWorkBench().get(type);
		}
		return time;
	}

	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableCollection(this.getDescription().getVehicleOptions().values());
	}
	
	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(this.getDescription().getTimeAtWorkBench());
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return description.getVehicleSpecification();
	}
	
	@Override
	public void acceptVisit(IOrderVisitor visitor) {
		visitor.visit(this);
	}
	
}
