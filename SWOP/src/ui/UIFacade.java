package ui;

import java.util.ArrayList;
import java.util.Set;

import assembly.AssemblyLine;
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
	
	public void showChosenTask(Task task);
	
	public boolean askContinue();
	
	public void invalidAnswerPrompt();
	
	public void invalidUserPrompt();
	
	public void showPendingOrders(ArrayList<Order> pendingOrders);
	
	public void showCompletedOrders(ArrayList<Order> completedOrders);
	
	public void showWorkBenchCompleted();
	
	//estimatedTime kan -1 zijn!! --> dan moet de estimatedTime niet geprint worden 
	public void showOrder(int quantity, String model, int[] estimatedTime);
	
	public void showAssemblyLine(AssemblyLine assemblyline, String tense);
	
	public int getElapsedTime();
	
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches);
}
