package order;

import java.util.List;

import assembly.AssemblyLine;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;

/**
 * this class will be used to keep record of two kinds of orders. these two
 * kinds of orders are the pending orders and the completed orders. this class
 * will be initialized upon creation.
 * 
 * 
 */
public class OrderBook {
	/**
	 * Two hashmaps for differentiating the completed from the pending orders.
	 * The key references to the name of the garageholder. The value references
	 * to all the orders of this garageholder.
	 */
	// private HashMap<String, ArrayList<Order>> pendingOrders;
	// private HashMap<String, ArrayList<Order>> completedOrders;
	private Multimap<String, Order> pendingOrders;
	private Multimap<String, Order> completedOrders;
	private AssemblyLine assemblyLine;

	public OrderBook(AssemblyLine assemblyLine) {
		initializeBook();
		this.assemblyLine = assemblyLine;
	}

	/**
	 * Method for retrieving all the pending orders. An Immutable MultiMap will be returned.
	 */
	public Multimap<String, Order> getPendingOrders() {
		return new ImmutableListMultimap.Builder<String, Order>().putAll(pendingOrders).build();
	}


	/**
	 * Method for retrieving all the completed orders. An Immutable MultiMap will be returned.
	 */
	public Multimap<String, Order> getCompletedOrders() {
		return new ImmutableListMultimap.Builder<String, Order>().putAll(completedOrders).build();
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
	public void updateOrderBook(Order order) {
		if (order == null) {
			throw new IllegalArgumentException();
		}

		List<Order> pending = (List<Order>) this.pendingOrders.get(order.getGarageHolder());
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
	 * @param order
	 */
	public void addOrder(Order order) {

		this.pendingOrders.put(order.getGarageHolder(), order);

		assemblyLine.addMultipleJobs(assemblyLine.convertOrderToJob(order));
		assemblyLine.calculateEstimatedTime(order);
	}
}
