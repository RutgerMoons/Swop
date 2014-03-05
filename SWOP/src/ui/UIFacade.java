package ui;

import java.util.ArrayList;
import java.util.Set;

import order.*;

public interface UIFacade {
	
	public String getName();
	
	public String getRole();
	
	public String getModel(Set<String> catalogue);
	
	public int getQuantity();
	
	public boolean askContinue();
	
	public void invalidAnswerPrompt();
	
	public void invalidUserPrompt();
	
	public void showPendingOrders(ArrayList<Order> pendingOrders);
	
	public void showCompletedOrders(ArrayList<Order> completedOrders);
	
	//estimatedTime kan null zijn!! --> dan moet de estimatedTime niet geprint worden 
	public void showOrder(int quantity, String model, int estimatedTime);
}
