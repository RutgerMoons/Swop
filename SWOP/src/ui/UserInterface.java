package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import order.Order;
import assembly.AssemblyLine;
import assembly.Task;
import assembly.WorkBench;


public class UserInterface implements UIFacade{
	
	private Scanner inputReader;

	private String askQuestion(String question){
		System.out.println(question);
		inputReader = new Scanner(System.in);
		String answer = inputReader.nextLine();
		return answer;
	}
	
	private void show(ArrayList<String> message){
		for (int i = 0; i < message.size(); i++) {
			System.out.println(message.get(i));
		}
		System.out.println("");
	}
	
	@Override
	public boolean askContinue() {
		String answer = askQuestion("Do you want to continue? Y/N");
		if(answer.equals("Y")){
			return true;
		}
		else if(answer == "N"){
			return false;
		}
		else return false;
	}
	
	public String getName(){
		return askQuestion("Hello user, what's your name?");
	}
	
	public String getRole(){
		return askQuestion("What's your role: manager, garageholder or worker?");
	}
	
	@Override
	public String getModel(Set<String> catalogue) {
		ArrayList<String> catalogueInString = new ArrayList<String>();
		
		catalogueInString.add("Possible models:");
		for(String key:catalogue){
			catalogueInString.add(key);
		}
		show(catalogueInString);
		
		return askQuestion("Which model do you want to order?");
	}

	@Override
	public int getQuantity() {
		return Integer.parseInt(askQuestion("How many cars do you want to order?"));
	}

	public void invalidAnswerPrompt(){
		show(new ArrayList<String>(Arrays.asList("Sorry, that's not a valid response")));
	}
	
	public void invalidUserPrompt(){
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}
	
	@Override
	public void showPendingOrders(ArrayList<Order> pendingOrders) {
		ArrayList<String> ordersInString = new ArrayList<String>();
		String orderInString;
		
		ordersInString.add("Your pending orders:");
		
		if(pendingOrders != null){
			for (int i = 0; i < pendingOrders.size(); i++) {
				orderInString = pendingOrders.get(i).getQuantity() + " " + pendingOrders.get(i).getDescription() + " Estimated completion time:" + pendingOrders.get(i).getEstimatedTime()[0] + " days and " + pendingOrders.get(i).getEstimatedTime()[1]/60 + " hours and " + pendingOrders.get(i).getEstimatedTime()[1]%60 + " minutes";
				ordersInString.add(orderInString);
			}
			show(ordersInString);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no pending Orders")));
		
	}

	@Override
	public void showCompletedOrders(ArrayList<Order> completedOrders) {
		ArrayList<String> ordersInString = new ArrayList<String>();
		String orderInString;
		
		ordersInString.add("Your completed orders:");
		
		if(completedOrders != null){
			for (int i = 0; i < completedOrders.size(); i++) {
				orderInString = completedOrders.get(i).getQuantity() + " " + completedOrders.get(i).getDescription();
				ordersInString.add(orderInString);
			}
			
			show(ordersInString);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no completed Orders")));
		
	}

	@Override
	public void showOrder(int quantity, String model, int[] estimatedTime) {
		if(estimatedTime[0] == -1)
			show(new ArrayList<String>(Arrays.asList("Your order:",quantity + " " + model)));
		else show(new ArrayList<String>(Arrays.asList("Your order:",quantity + " " + model + " Estimated completion time: " + estimatedTime[0] + " days and " + estimatedTime[1]/60 + " hours and " + estimatedTime[1]%60 + " minutes")));
		
	}
	
	public int getWorkBench(){
		return Integer.parseInt(askQuestion("What's the number of the workbench you're currently residing at?"));
	}
	
	public int chooseTask(ArrayList<Task> tasks){
		ArrayList<String> tasksInStrings = new ArrayList<String>();
		
		tasksInStrings.add("Tasks:");
		for (int i = 0; i <tasks.size(); i++) {
			tasksInStrings.add(i + ". " + tasks.get(i).getTaskDescription());
		}
		
		show(tasksInStrings);
		
		return Integer.parseInt(askQuestion("Which taskNumber do you choose?"));		
	}
	
	public void showChosenTask(Task task){
		ArrayList<String> chosenTask = new ArrayList<String>();
		chosenTask.add("Your task: " + task.getTaskDescription());
		chosenTask.add("Required actions:");
		for (int i = 0; i < task.getActions().size(); i++) {
			chosenTask.add((i+1) + "." + task.getActions().get(i).getDescription());
		}
	}
	
	public String askFinished(){
		return askQuestion("Type 'done' when you're finished");
	}
	
	public void showWorkBenchCompleted(){
		show(new ArrayList<String>(Arrays.asList("All the tasks at this workbench are completed")));
	}
	
	public void showAssemblyLine(AssemblyLine assemblyline, String tense){
		ArrayList<String> assemblyLineString = new ArrayList<String>();
		assemblyLineString.add(tense + "assemblyline:");
		
		WorkBench workbench;
		String completed;
		for (int i = 0; i < assemblyline.getWorkbenches().size(); i++) {
			workbench = assemblyline.getWorkbenches().get(i);
			assemblyLineString.add("-workbench " + (i+1) + ":");
			for (int j = 0; j < workbench.getCurrentTasks().size(); j++) {
				if(workbench.getCurrentTasks().get(i).isCompleted())
					completed = "completed";
				else
					completed = "not completed";
				assemblyLineString.add("  *" + workbench.getCurrentTasks().get(i).getTaskDescription() + ": " + completed);
			}
		}
	}
	
	public int getElapsedTime(){
		return Integer.parseInt(askQuestion("How much time has passed? (minutes)"));
	}
	
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches){
		show(new ArrayList<String>(Arrays.asList("AssemblyLine can't be advanced because of workbench " + notCompletedBenches.toString())));
	}
}