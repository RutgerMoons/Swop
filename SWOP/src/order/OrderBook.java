package order;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {
	/**
	 * String = naam van de garagehoulder
	 * ArrayList<Order> : alle orders besteld door deze garagehoulder
	 */
	private static HashMap<String, ArrayList<Order>> pendingOrders;
	private static HashMap<String, ArrayList<Order>> completedOrders;


	/*
	 * Geen clone() van pendingOrders want java doet moeilijk.
	 */
	public static HashMap<String, ArrayList<Order>> getPendingOrders() {
		return pendingOrders;
	}


	public static HashMap<String, ArrayList<Order>> getCompletedOrders() {
		return completedOrders;
	}

	// Ik denk dat de prof in de les zei dat van die lange oproepen gelijk hieronder
	// niet goed waren... Hoe veranderen?
	public static void updateOrderBook(Order order){
		if(order == null){
			throw new IllegalArgumentException();
		}
		
		ArrayList<Order> pending = getPendingOrders().get(order.getGarageHolder());
		if(pending.contains(order)){
			pending.remove(order);
			if(!getCompletedOrders().containsKey(order.getGarageHolder())){
				ArrayList<Order> firstCompletedOrder = new ArrayList<Order>();
				firstCompletedOrder.add(order);
				getCompletedOrders().put(order.getGarageHolder(),firstCompletedOrder);
			}
			else{
				getCompletedOrders().get(order.getGarageHolder()).add(0, order);
			}
		}
	}

	public static void initializeBook(){
		pendingOrders = new HashMap<String, ArrayList<Order>>();
		completedOrders = new HashMap<String, ArrayList<Order>>();
	}

	// mss wordt in de arraylist geen orders maar description van orders geadd.. Not sure anymore
	public static void addOrder(Order order){
		if(!OrderBook.getPendingOrders().containsKey(order.getGarageHolder())){
			ArrayList<Order> firstPendingOrder = new ArrayList<Order>();
			firstPendingOrder.add(order);
			OrderBook.getPendingOrders().put(order.getGarageHolder(),firstPendingOrder);
		}
		else {
			OrderBook.getPendingOrders().get(order.getGarageHolder()).add(order);
		}
	}
}
