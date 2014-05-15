package domain.observer.observes;

import domain.order.order.IOrder;

/**
 * AssemblyLineObservers use this interface as a gateway for addressing complex objects and notifying 
 * them in case of a completed order by the observed assemblyLine
 */
public interface ObservesAssemblyLine {

	public void updateCompletedOrder(IOrder order);
	
}
