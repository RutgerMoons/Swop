package order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class OrderBook {
	/**
	 * String = naam van de garagehoulder
	 * ArrayList<Order> : alle orders besteld door deze garagehoulder
	 */
	private HashMap<String, ArrayList<Order>> pendingOrders;
	private HashMap<String, ArrayList<Order>> completedOrders;

	public OrderBook(){
		initializeBook();
	}

	/*
	 * Geen clone() van pendingOrders want java doet moeilijk.
	 */
	public HashMap<String, ArrayList<Order>> getPendingOrders() {
		return clone(pendingOrders);
	}


	public HashMap<String, ArrayList<Order>> clone(HashMap<String, ArrayList<Order>> map){
		HashMap<String, ArrayList<Order>> newMap = new HashMap<String,ArrayList<Order>>();
		Set<Entry<String,ArrayList<Order>>> set1 = map.entrySet();
		//newMap.putAll(map);

		for (Entry<String, ArrayList<Order>> entry : set1){
			ArrayList<Order> test = new ArrayList<Order>();
			for(Order ord : entry.getValue()){
				test.add(ord);
				//newMap.put(entry.getKey(), test);
			}
			newMap.put(entry.getKey(),test);
//			for(Order ord : m){
//				newMap.get(entry.getKey()).add(ord);
//			}

		}

		return newMap;
	}

	public HashMap<String, ArrayList<Order>> getCompletedOrders() {
		return clone(completedOrders);
	}

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

	public void initializeBook(){
		pendingOrders = new HashMap<String, ArrayList<Order>>();
		completedOrders = new HashMap<String, ArrayList<Order>>();
	}

	// mss wordt in de arraylist geen orders maar description van orders geadd.. Not sure anymore
	public void addOrder(Order order){
		if(!this.pendingOrders.containsKey(order.getGarageHolder())){
			ArrayList<Order> firstPendingOrder = new ArrayList<Order>();
			firstPendingOrder.add(order);
			this.pendingOrders.put(order.getGarageHolder(),firstPendingOrder);
		}
		else {
			this.pendingOrders.get(order.getGarageHolder()).add(order);
		}
	}
}
