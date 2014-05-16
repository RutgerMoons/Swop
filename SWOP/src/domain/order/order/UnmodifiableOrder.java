package domain.order.order;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.exception.UnmodifiableException;
import domain.order.orderVisitor.IOrderVisitor;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.UnmodifiableVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

/**
 * Create an Immutable Order, only the getters are accessible.
 *
 */
public class UnmodifiableOrder implements IOrder {

	IOrder order;
	/**
	 * Create an Immutable Order.
	 * 
	 * @param iOrder
	 * 			The mutable Order.
	 */
	public UnmodifiableOrder(IOrder iOrder){
		if(iOrder==null)
			throw new IllegalArgumentException();
		this.order = iOrder;
	}
	
	@Override
	public String getGarageHolder() {
		return order.getGarageHolder();
	}

	@Override
	public int getPendingCars() {
		return order.getPendingCars();
	}

	@Override
	public int getQuantity() {
		return order.getQuantity();
	}

	@Override
	public IVehicle getDescription() {
		return new UnmodifiableVehicle(order.getDescription());
	}

	@Override
	public ImmutableClock getEstimatedTime() {
		return order.getEstimatedTime();
	}
	
	@Override
	public int hashCode(){
		return order.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return order.equals(obj);
	}

	@Override
	public String toString(){
		return order.toString();
	}

	@Override
	public void setEstimatedTime(ImmutableClock clock) {
		throw new UnmodifiableException();
	}

	@Override
	public void completeCar() {
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getDeadline(){
		return order.getDeadline();
	}

	@Override
	public void setDeadline(ImmutableClock clock){
		throw new UnmodifiableException();
	}

	@Override
	public ImmutableClock getOrderTime() {
		return order.getOrderTime();
	}

	@Override
	public void setOrderTime(ImmutableClock clock) {
		throw new UnmodifiableException();
	}

	@Override
	public int getProductionTime() {
		return order.getProductionTime();
	}
	
	@Override
	public Collection<VehicleOption> getVehicleOptions() {
		return Collections.unmodifiableCollection(this.order.getVehicleOptions());
	}
	
	@Override
	public Map<WorkBenchType, Integer> getTimeAtWorkBench() {
		return Collections.unmodifiableMap(this.order.getTimeAtWorkBench());
	}

	@Override
	public VehicleSpecification getVehicleSpecification() {
		return order.getVehicleSpecification();
	}
	
	@Override
	public void acceptVisit(IOrderVisitor visitor) {
		visitor.visit(this);
	}
	
}
