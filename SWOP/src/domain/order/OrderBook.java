package domain.order;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

import domain.clock.ImmutableClock;
import domain.observer.observable.ObservableOrderBook;
import domain.observer.observers.OrderBookObserver;
import domain.observer.observes.ObservesAssemblyLine;

/**
 * this class will be used to keep record of two kinds of orders. these two
 * kinds of orders are the pending orders and the completed orders. this class
 * will be initialized upon creation.
 * 
 * 
 */
public class OrderBook implements ObservesAssemblyLine, ObservableOrderBook {

	private Multimap<String, IOrder> pendingOrders;
	private Multimap<String, IOrder> completedOrders;
	private List<OrderBookObserver> observers;

	/**
	 * Create a new OrderBook
	 */
	public OrderBook() {
		initializeBook();
	}

	/**
	 * Method for retrieving all the pending orders. An Immutable MultiMap will be returned.
	 */
	public Multimap<String, IOrder> getPendingOrders() {
		return new ImmutableListMultimap.Builder<String, IOrder>().putAll(pendingOrders).build();
	}


	/**
	 * Method for retrieving all the completed orders. An Immutable MultiMap will be returned.
	 */
	public Multimap<String, IOrder> getCompletedOrders() {
		return new ImmutableListMultimap.Builder<String, IOrder>().putAll(completedOrders).build();
	}

	/**
	 * Method for updating the OrderBook. A given order needs to be moved from
	 * pendingOrders to completedOrders. The method checks if the associated
	 * garageholder already has completed orders. If so, the order is simply
	 * added to the corresponding arrayList. If not the name of the garageholder
	 * is added as key and the order is added to a new arrayList.
	 * 
	 * @param 	order
	 * 				The order that needs to be updated
	 */
	public void updateOrderBook(IOrder order) {
		if (order == null) {
			throw new IllegalArgumentException();
		}

		List<IOrder> pending = (List<IOrder>) this.pendingOrders.get(order.getGarageHolder());
		if (order.getPendingCars()<=0 && pending.contains(order)) {
			pending.remove(order);
			this.completedOrders.put(order.getGarageHolder(), order);
		}
	}

	/**
	 * Method for initializing OrderBook. It initializes both Multimaps :
	 * pendingOrders and completedOrders.
	 */
	public void initializeBook() {
		pendingOrders =  ArrayListMultimap.create();
		completedOrders = ArrayListMultimap.create();
		this.observers = new ArrayList<>();
	}

	/**
	 * Method for adding an order to the pending orders list. It checks if the
	 * garageholder already has pending orders. If so the order is simply added
	 * to the corresponding arraylist. If not, the garageholders name is added
	 * as a key to the hashmap and the order is added to a new arraylist and
	 * this arraylist is added to pendingOrders.
	 * 
	 * @param 	order
	 * 				The StandardOrder you want to add.
	 */
	public void addOrder(IOrder order, ImmutableClock currentTime){
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
