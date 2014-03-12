package ui;

import java.util.ArrayList;
import java.util.Set;

import car.CarModel;
import assembly.AssemblyLine;
import assembly.Task;

public interface UIFacade {
	
	public String getName();
	
	public String chooseRole();
	
	public String chooseModel(Set<String> catalogue);
	
	public String askFinished();
	
	public int getQuantity();
	
	public int chooseWorkBench(int numberOfWorkbenches, ArrayList<String> workbenches);
	
	public int chooseTask(ArrayList<String> tasks);
	
	public void showChosenTask(String task);
	
	public boolean askContinue();
	
	public void invalidAnswerPrompt();
	
	public void invalidUserPrompt();
	
	public void showPendingOrders(ArrayList<String> pendingOrders);
	
	public void showCompletedOrders(ArrayList<String> completedOrders);
	
	public void showWorkBenchCompleted();
	
	//estimatedTime kan -1 zijn!! --> dan moet de estimatedTime niet geprint worden 
	public void showOrder(int quantity, String carModel, int[] estimatedTime);
	
	public void showAssemblyLine(String assemblyline, String tense);
	
	public int getElapsedTime();
	
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches);
	
	public boolean askAdvance();
}
