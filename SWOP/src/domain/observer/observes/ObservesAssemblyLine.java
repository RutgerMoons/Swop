package domain.observer.observes;

<<<<<<< HEAD
import domain.clock.ImmutableClock;
=======
>>>>>>> a224fc603c04cf7fb6480d24993d5fd3809fc554
import domain.order.IOrder;

/**
 * AssemblyLineObservers use this interface as a gateway for addressing complex objects and notifying 
 * them in case of a completed order by the observed assemblyLine
 */
public interface ObservesAssemblyLine {

<<<<<<< HEAD
	public void updateCompletedOrder(IOrder order);
=======
	public void updateCompletedOrder(IOrder orderO);
>>>>>>> a224fc603c04cf7fb6480d24993d5fd3809fc554
	
}
