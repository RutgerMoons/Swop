package domain.order.order;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Class representing an order from a garageholder. There are 5 attributes
 * specifying a certain order : the description/type of carModel the
 * garageholder order, the name of the garageholder. Moreover the amount of cars
 * ordered and the amount of cars yet to be completed, are specific attributes
 * of an order. Each order also has an estimated time. This is an estimation of
 * when the order will be completed. This time is expressed in days and minutes.
 * 
 * TODO doc
 */
public class StandardOrder implements IOrder {

	private IVehicle description;
	private String garageholder;
	private int quantity, pendingCars;
	private ImmutableClock estimatedTime;
	private ImmutableClock orderTime;

	/**
	 * Constructor of an Order, given the name of the orderer, the type of
	 * carModel and the amount of cars to be ordered.
	 */
	public StandardOrder(String holder, Vehicle description, int quantity, ImmutableClock orderTime) {
		this.setDescription(description);
		this.setGarageHolder(holder);
		this.setQuantity(quantity);
		this.setPendingCars(quantity);
		setOrderTime(orderTime);
	}

	/**
	 * Assignes the given name to the name of the garageholder
	 */
	private void setGarageHolder(String holder) {
		if (holder == null || holder.equals(" ")) {
			throw new IllegalArgumentException();
		}
		this.garageholder = holder;
	}

	@Override
	public String getGarageHolder() {
		return this.garageholder;
	}

	/**
	 * Changing the amount of pendingCars to the given amount. That's how other
	 * classes may check if the order is completed or not. The method checks if
	 * the given amount is lower than zero. If so an IllegalArgumentException is
	 * thrown.
	 */
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

	/**
	 * Method for assigning the amount of cars ordered to the given amount. The
	 * method checks if the given amount is lower than zero. If so an
	 * IllegalArgumentException is thrown.
	 */
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

	/**
	 * Assigning the type/name of the ordered carModel to the given description.
	 * The method throws an IllegalArgumentException is the given name equals
	 * null.s
	 */
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
		if (getClass() != obj.getClass())
			return false;
		StandardOrder other = (StandardOrder) obj;
		if (!description.equals(other.description))
			return false;
		if (!garageholder.equals(other.garageholder))
			return false;
		if (quantity != other.quantity)
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
		return description.getSpecification();
	}

	@Override
	public boolean canBeHandled(Set<VehicleSpecification> responsibilities) {
		return description.canBeHandled(responsibilities);
	}
	
	@Override
	public void acceptVisit(IOrderVisitor visitor) {
		visitor.visit(this);
	}
}
