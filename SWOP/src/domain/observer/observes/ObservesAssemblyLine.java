package domain.observer.observes;

import domain.order.order.IOrder;

/**
 * An interface used as a gateway for addressing complex objects and notifying 
 * them in case of a completed order by the observed AssemblyLine. 
 */
public interface ObservesAssemblyLine {
	/**
	 * Every observing object will be notified that an Order is completed.
	 * 
	 * @param	order
	 * 			The IOrder that is completed
	 */
	public void updateCompletedOrder(IOrder order);
	
}
