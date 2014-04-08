package domain.order;

import domain.car.ICarModel;
import domain.car.ImmutableCarModel;
import domain.clock.UnmodifiableClock;
import domain.exception.ImmutableException;

/**
 * Create an Immutable Order, only the getters are accessible.
 *
 */
public class ImmutableOrder implements IOrder {

	private IOrder order;

	/**
	 * Create an Immutable Order.
	 * 
	 * @param iOrder
	 * 			The mutable Order.
	 */
	public ImmutableOrder(IOrder iOrder){
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
	public ICarModel getDescription() {
		return new ImmutableCarModel(order.getDescription());
	}

	@Override
	public UnmodifiableClock getEstimatedTime() {
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
	public void setEstimatedTime(UnmodifiableClock clock) throws ImmutableException {
		throw new ImmutableException();
	}

	@Override
	public void completeCar() throws ImmutableException {
		throw new ImmutableException();
	}
}
