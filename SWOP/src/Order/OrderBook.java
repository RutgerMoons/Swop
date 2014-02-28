package Order;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderBook {
	private HashMap<String, ArrayList<Order>> pendingOrders;
	private HashMap<String, ArrayList<Order>> completedOrders;

	public OrderBook(){
		setPendingOrders(new HashMap<String, ArrayList<Order>>());
		setCompletedOrders(new HashMap<String, ArrayList<Order>>());
	}

	public HashMap<String, ArrayList<Order>> getPendingOrders() {
		return pendingOrders;
	}

	private void setPendingOrders(HashMap<String, ArrayList<Order>> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}

	public HashMap<String, ArrayList<Order>> getCompletedOrders() {
		return completedOrders;
	}

	private void setCompletedOrders(HashMap<String, ArrayList<Order>> completedOrders) {
		this.completedOrders = completedOrders;
	}
	
	public void updateOrderBook(Order order){
		if(order != null && pendingOrders.get(order.getGarageHolder()).contains(order)){
			pendingOrders.get(order.getGarageHolder()).remove(order);
			completedOrders.get(order.getGarageHolder()).add(0, order);
		}
	}
}
