package ui;

import java.util.ArrayList;

import order.Order;
import order.OrderBook;

import users.GarageHolder;

public class GarageHolderInterface {
	private GarageHolder name;
	
	public GarageHolderInterface (GarageHolder name){
		this.name=name;
	}
	
	// interpreter pattern
	public void showOrderBook(){
		System.out.println("Checking in OrderBook for earlier orders.");
		ArrayList<Order> vervuldeOrders = OrderBook.getCompletedOrders().get(name.getName());
		if(vervuldeOrders != null){
			System.out.println("These orders have been completed:");
			int teller = 0;
			for(Order ord : vervuldeOrders){
				System.out.println(teller + " : " + ord.getDescription());
			}
		}
		else{
			System.out.println("None of your orders have been completed yet.");
		}
		
		ArrayList<Order> pending = OrderBook.getPendingOrders().get(name.getName());
		if(pending !=null){
			System.out.println("These orders have been ordered by you:");
			int teller = 0;
			for(Order ord : pending){
				System.out.println(teller + " : " + ord.getDescription());
			}
		}
		else{
			System.out.println("You have no pending orders.");
		}
	}

}
