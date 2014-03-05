package ui;

import java.util.ArrayList;

import car.Catalogue;
import order.Order;
import order.OrderBook;
import users.GarageHolder;
import users.User;

public class OrderHandler extends UseCaseHandler{
	
	private OrderBook orderBook;
	private UIFacade UIFacade;
	private Catalogue catalogue;
	
	public OrderHandler (UIFacade UIFacade, OrderBook orderBook, Catalogue catalogue){
		this.UIFacade = UIFacade;
		this.orderBook = orderBook;
		this.catalogue = catalogue;
	}
		
	public boolean mayUseThisHandler(User user){
		if (user instanceof GarageHolder) return true;
		else return false;
	}
	
	public void executeUseCase(User user){
		showOrders(user);
		Order order = placeNewOrder(user);
		if(order != null)
			showNewOrder(user, order);
	}
	
	private void showOrders(User user){
		ArrayList<Order> pendingOrders;
		ArrayList<Order> completedOrders;
		
		if(this.orderBook.getPendingOrders().containsKey(user.getName()) && !this.orderBook.getPendingOrders().get(user.getName()).isEmpty()) 
			pendingOrders = this.orderBook.getPendingOrders().get(user.getName());
		else pendingOrders = null;
		
		if(this.orderBook.getCompletedOrders().containsKey(user.getName()))
			completedOrders = this.orderBook.getCompletedOrders().get(user.getName());
		else completedOrders = null;
		
		this.UIFacade.showPendingOrders(pendingOrders);
		this.UIFacade.showCompletedOrders(completedOrders);
		
	}
	
	private Order placeNewOrder(User user){
		boolean wantToOrder = this.UIFacade.askContinue();
		if(!wantToOrder) 
			return null;
		else{
			String model = UIFacade.getModel(catalogue.getCatalogue().keySet());
			int quantity = UIFacade.getQuantity();
			UIFacade.showOrder(quantity, model, -1);
			boolean confirm = UIFacade.askContinue();
			if(!confirm){
				executeUseCase(user);
				return null;
			}
			else{
				Order order = new Order(user.getName(), model, quantity);
				orderBook.addOrder(order);
				return order;
			}
		}
	}
	
	private void showNewOrder(User user, Order order){
		UIFacade.showOrder(order.getQuantity(),order.getDescription(),order.getEstimatedTime());
	}
}
