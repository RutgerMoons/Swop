package ui;

import java.util.ArrayList;
import java.util.Set;

import assembly.Task;
import order.*;

public interface UIFacade {
	
	public String getName();
	
	public String getRole();
	
	public String getModel(Set<String> catalogue);
	
	public String askFinished();
	
	public int getQuantity();
	
	public int getWorkBench();
	
	public int chooseTask(ArrayList<Task> tasks);
	
	public boolean askContinue();
	
	public void invalidAnswerPrompt();
	
	public void invalidUserPrompt();
	
	public void showPendingOrders(ArrayList<Order> pendingOrders);
	
	public void showCompletedOrders(ArrayList<Order> completedOrders);
	
	public void showWorkBenchCompleted();
	
	//estimatedTime kan -1 zijn!! --> dan moet de estimatedTime niet geprint worden 
	public void showOrder(int quantity, String model, int estimatedTime);
}
