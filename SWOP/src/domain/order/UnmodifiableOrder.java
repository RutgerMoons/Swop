package domain.order;

import domain.car.IVehicle;
import domain.car.UnmodifiableVehicle;
import domain.clock.ImmutableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;

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
	public void setEstimatedTime(ImmutableClock clock) throws ImmutableException {
		throw new ImmutableException();
	}

	@Override
	public void completeCar() throws ImmutableException {
		throw new ImmutableException();
	}

	@Override
	public ImmutableClock getDeadline() throws NotImplementedException {
		return order.getDeadline();
	}

	@Override
	public void setDeadline(ImmutableClock clock)
			throws NotImplementedException, ImmutableException {
		throw new ImmutableException();
	}

	@Override
	public ImmutableClock getOrderTime() {
		return order.getOrderTime();
	}

	@Override
	public void setOrderTime(ImmutableClock clock) throws ImmutableException {
		throw new ImmutableException();
	}

	@Override
	public int getProductionTime() {
		return order.getProductionTime();
	}
}
