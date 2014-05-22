package domain.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

import domain.observer.observable.ObservableOrderBook;
import domain.observer.observers.OrderBookObserver;
import domain.observer.observes.ObservesAssemblyLine;
import domain.order.order.IOrder;

/**
 * A class representing a virtual book of pending and completed IOrders.
 */
public class OrderBook implements ObservesAssemblyLine, ObservableOrderBook {

	private Multimap<String, IOrder> pendingOrders;
	private Multimap<String, IOrder> completedOrders;
	private List<OrderBookObserver> observers;

	/**
	 * Create a new OrderBook.
	 */
	public OrderBook() {
		pendingOrders =  ArrayListMultimap.create();
		completedOrders = ArrayListMultimap.create();
		this.observers = new ArrayList<>();
	}

	/**
	 * Get all the pending IOrders. The key is the name of the garage holder that ordered the IOrder.
	 */
	public Multimap<String, IOrder> getPendingOrders() {
		return new ImmutableListMultimap.Builder<String, IOrder>().putAll(pendingOrders).build();
	}


	/**
	 * Get all the completed IOrders. The key is the name of the garage holder that ordered the IOrder.
	 */
	public Multimap<String, IOrder> getCompletedOrders() {
		return new ImmutableListMultimap.Builder<String, IOrder>().putAll(completedOrders).build();
	}

	/**
	 * Method for updating the OrderBook. A given order needs to be moved from
	 * pendingOrders to completedOrders. 
	 * 
	 * @param	order
	 * 			The order that needs to be updated
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the order is null or the order isn't fully completed or the pendingOrders doesn't 
	 * 			have the given order
	 */
	public void updateOrderBook(IOrder order) {
		if (order == null || !this.pendingOrders.containsValue(order)) {
			throw new IllegalArgumentException();
		}

		if (order.getPendingCars() == 0){
			Collection<IOrder> pending = this.pendingOrders.get(order.getGarageHolder());
			pending.remove(order);
			this.completedOrders.put(order.getGarageHolder(), order);
		}
	}

	/**
	 * Add a newly created IOrder to the OrderBook. It has to be a pending order.
	 * 
	 * @param 	order
	 * 			The IOrder you want to add to the OrderBook
	 */
	public void addOrder(IOrder order){
		if(order.getPendingCars()<=0){
			throw new IllegalArgumentException();
		}
		this.pendingOrders.put(order.getGarageHolder(), order);
		updatePlacedOrder(order);
	}

	@Override
	public void updateCompletedOrder(IOrder order) {
		if(order==null)
			throw new IllegalArgumentException();
		updateOrderBook(order);
	}

	@Override
	public void attachObserver(OrderBookObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.add(observer);
	}

	@Override
	public void detachObserver(OrderBookObserver observer) {
		if (observer == null) {
			throw new IllegalArgumentException();
		}
		observers.remove(observer);			
	}

	@Override
	public void updatePlacedOrder(IOrder order) {
		if(order==null){
			throw new IllegalArgumentException();
		}
		for (OrderBookObserver observer : this.observers) {
			observer.notifyNewOrder(order);
		}
	}
}
