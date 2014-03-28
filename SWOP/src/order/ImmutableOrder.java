package order;

import car.ICarModel;

/**
 * Create an Immutable Order, only the getters are accessible.
 *
 */
public class ImmutableOrder implements IOrder {

	private Order order;

	/**
	 * Create an Immutable Order.
	 * 
	 * @param order
	 * 			The mutable Order.
	 */
	public ImmutableOrder(Order order){
		if(order==null)
			throw new IllegalArgumentException();
		this.order = order;
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
		return order.getDescription();
	}

	@Override
	public int[] getEstimatedTime() {
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
}
