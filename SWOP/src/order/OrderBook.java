package order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import assembly.AssemblyLine;

/**
 * this class will be used to keep record of two kinds of orders.
 * these two kinds of orders are the pending orders and the completed
 * orders.
 * this class will be initialized upon creation.
 *  
 *
 */
public class OrderBook {
	/**
	 * Two hashmaps for differentiating the completed from the 
	 * pending orders. 
	 * The key references to the name of the garageholder.
	 * The value references to all the orders of this garageholder. 
	 */
	private HashMap<String, ArrayList<Order>> pendingOrders;
	private HashMap<String, ArrayList<Order>> completedOrders;
	private AssemblyLine assemblyLine;

	public OrderBook(AssemblyLine assemblyLine){
		initializeBook();
		this.assemblyLine = assemblyLine;
	}


	/**
	 * Method for retrieving all the pending orders.
	 * A clone of the hashmap will be returned.
	 */
	public HashMap<String, ArrayList<Order>> getPendingOrders() {
		return clone(pendingOrders);
	}


	/**
	 * Method for deep cloning a given HashMap. 
	 */
	public HashMap<String, ArrayList<Order>> clone(HashMap<String, ArrayList<Order>> map){
		HashMap<String, ArrayList<Order>> newMap = new HashMap<String,ArrayList<Order>>();
		Set<Entry<String,ArrayList<Order>>> set1 = map.entrySet();
		for (Entry<String, ArrayList<Order>> entry : set1){
			ArrayList<Order> test = new ArrayList<Order>();
			for(Order ord : entry.getValue()){
				test.add(ord);
			}
			newMap.put(entry.getKey(),test);
		}
		return newMap;
	}

	/**
	 * Method for retrieving all the completed orders.
	 * A clone of the hashmap will be returned.
	 */
	public HashMap<String, ArrayList<Order>> getCompletedOrders() {
		return clone(completedOrders);
	}

	/**
	 * Method for updating the OrderBook.
	 * A given order needs to be moved from pendingOrders
	 * to completedOrders. The method checks if the associated
	 * garageholder already has completed orders. If so, the 
	 * order is simply added to the corresponding arrayList.
	 * If not the name of the garageholder is added as key and the 
	 * order is added to a new arrayList.
	 * @param order
	 */
	public void updateOrderBook(Order order){
		if(order == null){
			throw new IllegalArgumentException();
		}

		ArrayList<Order> pending = this.pendingOrders.get(order.getGarageHolder());
		if(pending.contains(order)){
			pending.remove(order);
			if(!this.completedOrders.containsKey(order.getGarageHolder())){
				ArrayList<Order> firstCompletedOrder = new ArrayList<Order>();
				firstCompletedOrder.add(order);
				this.completedOrders.put(order.getGarageHolder(),firstCompletedOrder);
			}
			else{
				this.completedOrders.get(order.getGarageHolder()).add(0, order);
			}
		}
	}

	/**
	 * Method for initializing OrderBook. It initializes
	 * both hashmaps : pendingOrders and completedOrders.
	 */
	public void initializeBook(){
		pendingOrders = new HashMap<String, ArrayList<Order>>();
		completedOrders = new HashMap<String, ArrayList<Order>>();
	}

	/**
	 * Method for adding an order to the pending orders list.
	 * It checks if the garageholder already  has pending orders.
	 * If so the order is simply added to the corresponding arraylist.
	 * If not, the garageholders name is added as a key to the hashmap and
	 * the order is added to a new arraylist and this arraylist is added
	 * to pendingOrders.
	 * @param order
	 */
	public void addOrder(Order order){
		if(!this.pendingOrders.containsKey(order.getGarageHolder())){
			ArrayList<Order> firstPendingOrder = new ArrayList<Order>();
			firstPendingOrder.add(order);
			this.pendingOrders.put(order.getGarageHolder(),firstPendingOrder);
		}
		else {
			this.pendingOrders.get(order.getGarageHolder()).add(order);
		}
		assemblyLine.getCurrentJobs().addAll(assemblyLine.convertOrderToJob(order));
		assemblyLine.calculateEstimatedTime(order);
	}
}
