package domain.order;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

import domain.clock.ImmutableClock;
import domain.exception.ImmutableException;
import domain.exception.NotImplementedException;
import domain.observer.LogsAssemblyLine;

/**
 * this class will be used to keep record of two kinds of orders. these two
 * kinds of orders are the pending orders and the completed orders. this class
 * will be initialized upon creation.
 * 
 * 
 */
public class OrderBook implements LogsAssemblyLine{

	// private HashMap<String, ArrayList<Order>> pendingOrders;
	// private HashMap<String, ArrayList<Order>> completedOrders;
	private Multimap<String, IOrder> pendingOrders;
	private Multimap<String, IOrder> completedOrders;
	//TODO assemblyLineObserver zodat assembly genotified wordt wanneer een nieuw order binnenkomt
	/**
	 * Create a new OrderBook.
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
	 * @param order
	 */
	public void updateOrderBook(IOrder order) {
		if (order == null) {
			throw new IllegalArgumentException();
		}

		List<IOrder> pending = (List<IOrder>) this.pendingOrders.get(order.getGarageHolder());
		if (pending.contains(order)) {
			pending.remove(order);
			this.completedOrders.put(order.getGarageHolder(), order);
		}
	}

	/**
	 * Method for initializing OrderBook. It initializes both hashmaps :
	 * pendingOrders and completedOrders.
	 */
	public void initializeBook() {
		pendingOrders =  ArrayListMultimap.create();
		completedOrders = ArrayListMultimap.create();
	}

	/**
	 * Method for adding an order to the pending orders list. It checks if the
	 * garageholder already has pending orders. If so the order is simply added
	 * to the corresponding arraylist. If not, the garageholders name is added
	 * as a key to the hashmap and the order is added to a new arraylist and
	 * this arraylist is added to pendingOrders.
	 * 
	 * The estimated time of completion is automatically set to the order.
	 * @param order
	 * 			The StandardOrder you want to add.
	 * @throws ImmutableException 
	 * 			If the order is an ImmutableOrder.
	 * 			
	 */
	public void addOrder(StandardOrder order, ImmutableClock currentTime) throws ImmutableException {
		this.pendingOrders.put(order.getGarageHolder(), order);
		order.setEstimatedTime(currentTime.getUnmodifiableClockPlusExtraMinutes(assemblyLine.convertStandardOrderToJob(order)));
	}
	
	/**
	 * Method for adding an order to the pending orders list. It checks if the
	 * garageholder already has pending orders. If so the order is simply added
	 * to the corresponding arraylist. If not, the garageholders name is added
	 * as a key to the hashmap and the order is added to a new arraylist and
	 * this arraylist is added to pendingOrders.
	 * 
	 * The estimated time of completion is automatically set to the order.
	 * @param order
	 * 			The CustomOrder you want to add.
	 * @throws ImmutableException 
	 * 			If the order is an ImmutableOrder.
	 * 			
	 */
	public void addOrder(CustomOrder order, ImmutableClock currentTime) throws ImmutableException, NotImplementedException {
		this.pendingOrders.put(order.getGarageHolder(), order);
		order.setEstimatedTime(currentTime.getUnmodifiableClockPlusExtraMinutes(assemblyLine.convertCustomOrderToJob(order)));
	}

	@Override
	public void updateCompletedOrder(ImmutableClock estimatedTimeOfOrder) {
		// TODO Auto-generated method stub
		
	}
}
