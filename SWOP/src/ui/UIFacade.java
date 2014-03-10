package ui;

import java.util.ArrayList;
import java.util.Set;

import car.CarModel;
import assembly.AssemblyLine;
import assembly.Task;
import order.*;

public interface UIFacade {
	
	public String getName();
	
	public String chooseRole();
	
	public String chooseModel(Set<String> catalogue);
	
	public String askFinished();
	
	public int getQuantity();
	
	public int chooseWorkBench(int numberOfWorkbenches);
	
	public int chooseTask(ArrayList<Task> tasks);
	
	public void showChosenTask(Task task);
	
	public boolean askContinue();
	
	public void invalidAnswerPrompt();
	
	public void invalidUserPrompt();
	
	public void showPendingOrders(ArrayList<Order> pendingOrders);
	
	public void showCompletedOrders(ArrayList<Order> completedOrders);
	
	public void showWorkBenchCompleted();
	
	//estimatedTime kan -1 zijn!! --> dan moet de estimatedTime niet geprint worden 
	public void showOrder(int quantity, CarModel carModel, int[] estimatedTime);
	
	public void showAssemblyLine(AssemblyLine assemblyline, String tense);
	
	public int getElapsedTime();
	
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches);
	
	public boolean askAdvance();
}
