package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import car.CarModel;
import order.Order;
import assembly.AssemblyLine;
import assembly.Task;
import assembly.WorkBench;


public class UserInterface implements UIFacade{
	
	private Scanner inputReader;
	
	/**
	 * Ask the question, give the answer of the user
	 * 
	 * @param question the question the user has to answer
	 * @return answer of the user
	 */
	private String askQuestion(String question){
		System.out.println(question);
		inputReader = new Scanner(System.in);
		String answer = inputReader.nextLine();
		//inputReader.close();
		return answer;
	}
	
	/**
	 * Keep asking the same question, until a valid (expected) response is given by the user 
	 * note: inputReader is only constructed once
	 * 
	 * @param question the question the user has to answer
	 * @param expected possible answers
	 * @return answer of the user, this answer is an expected one
	 */
	private String askQuestionLoop(String question, ArrayList<String> expected) {
		inputReader = new Scanner(System.in);
		while (true) {
			System.out.println(question);
			String answer = inputReader.nextLine();
			if (answer != null && expected.contains(answer)) {
				//inputReader.close();
				return answer;
			} 
			invalidAnswerPrompt();
		}
	}
	
	@Override
	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected).equals("Y");
	}
	
	/**
	 * User presses enter to indicate he's finished
	 * It doesn't matter if there's something typed
	 */
	public String askFinished(){
		return askQuestion("Press enter when you're finished");
	}
	
	public boolean askAdvance(){
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N", expected).equals("Y");
	}
	
	public int askNumber(String question) {
		while (true) {
			try {
				return Integer.parseInt(askQuestion(question));
			}
			catch (NumberFormatException n) {
			}
		}
	}
	
	public void invalidAnswerPrompt(){
		show(new ArrayList<String>(Arrays.asList("Sorry, that's not a valid response")));
	}
	
	public void invalidUserPrompt(){
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}
	
	private void show(ArrayList<String> message){
		for (int i = 0; i < message.size(); i++) {
			System.out.println(message.get(i));
		}
		System.out.println("");
	}
	
	/**
	 * 
	 */
	@Override
	public void showPendingOrders(ArrayList<Order> pendingOrders) {
		ArrayList<String> ordersInString = new ArrayList<String>();
		String orderInString;
		
		ordersInString.add("Your pending orders:");
		
		if(pendingOrders != null){
			for (int i = 0; i < pendingOrders.size(); i++) {
				orderInString = pendingOrders.get(i).getQuantity() + 
								" " + 
								pendingOrders.get(i).getDescription() + 
								" Estimated completion time: " + 
								pendingOrders.get(i).getEstimatedTime()[0] + 
								" days and " + 
								pendingOrders.get(i).getEstimatedTime()[1]/60 + 
								" hours and " + 
								pendingOrders.get(i).getEstimatedTime()[1]%60 + 
								" minutes";
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
				orderInString = completedOrders.get(i).getQuantity() + " " + 
								completedOrders.get(i).getDescription();
				ordersInString.add(orderInString);
			}
			
			show(ordersInString);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no completed Orders")));
		
	}

	@Override
	public void showOrder(int quantity, CarModel model, int[] estimatedTime) {
		if(estimatedTime[0] == -1) {
			show(new ArrayList<String>(Arrays.asList("Your order:",quantity + " " + model)));
		} 
		else {
			show(new ArrayList<String>(Arrays.asList("Your order:",	quantity + " " + 
																	model + " Estimated completion time: day " + 
																	estimatedTime[0] + " " + 
																	estimatedTime[1]/60 + "h" + 
																	estimatedTime[1]%60)));
		}
		
	}
	
	public void showWorkBenchCompleted(){
		show(new ArrayList<String>(Arrays.asList("All the tasks at this workbench are completed")));
	}
	
	public void showAssemblyLine(AssemblyLine assemblyline, String tense){
		ArrayList<String> assemblyLineString = new ArrayList<String>();
		assemblyLineString.add(tense + " assemblyline:");
		
		WorkBench workbench;
		String completed;
		for (int i = 0; i < assemblyline.getWorkbenches().size(); i++) {
			workbench = assemblyline.getWorkbenches().get(i);
			assemblyLineString.add("-workbench " + (i+1) + ": " + assemblyline.getWorkbenches().get(i).getWorkbenchName());
			for (int j = 0; j < workbench.getCurrentTasks().size(); j++) {
				if(workbench.getCurrentTasks().get(j).isCompleted()) {
					completed = "completed";
				}
				else {
					completed = "not completed";
				}
				assemblyLineString.add("  *" + workbench.getCurrentTasks().get(j).getTaskDescription() + ": " + completed);
			}
		}
		show(assemblyLineString);
	}
	
	public void showChosenTask(Task task){
		ArrayList<String> chosenTask = new ArrayList<String>();
		chosenTask.add("Your task: " + task.getTaskDescription());
		chosenTask.add("Required actions:");
		for (int i = 0; i < task.getActions().size(); i++) {
					chosenTask.add((i+1) + "." + 
					task.getActions().get(i).getDescription());
		}
		show(chosenTask);
	}
	
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches){
		show(new ArrayList<String>(Arrays.asList(	"AssemblyLine can't be advanced because of workbench " + 
													notCompletedBenches.toString())));
	}
	
	/**
	 * ask the name of the user and return this
	 */
	public String getName(){
		return askQuestion("Hello user, what's your name?");
	}
	
	/**
	 * ask the amount of cars to order, until the response is a positive integer
	 */
	@Override
	public int getQuantity() {
		int quantity = Integer.parseInt(askQuestion("How many cars do you want to order?"));
		while (quantity<=0) {
			invalidAnswerPrompt();
			quantity = Integer.parseInt(askQuestion("How many cars do you want to order?"));
		}
		return quantity;
	}
	
	/**
	 * ask the user how much time has passed and return this if it is an integer
	 * a negative integer -> new day
	 * 0 or positive integer -> amount of minutes passed
	 */
	public int getElapsedTime(){
		return askNumber("How much time has passed? (minutes)");
	}
	
	/**
	 * user has to indicate which role he fulfills  
	 */
	public String chooseRole(){
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("manager", "garageholder", "worker"));
		return askQuestionLoop("What's your role: manager, garageholder or worker?", expected);
	}
	
	/**
	 * returns the number of the workbench at which the user is currently residing
	 * 
	 * @param The amount of workbenches in the assemblyline
	 * this is necessary to validate the user input
	 */
	public int chooseWorkBench(int numberOfWorkBenches, AssemblyLine assemblyline){
		ArrayList<String> workBenchNames = new ArrayList<>();
		int i = 1;
		for (WorkBench w : assemblyline.getWorkbenches()) {
			workBenchNames.add(i + ": " + w.getWorkbenchName());
			i++;
		}
		show(workBenchNames);
		
		int numberWorkbench = (askNumber("What's the number of the workbench you're currently residing at?"));
		if (numberWorkbench >= 1  && numberWorkbench <= numberOfWorkBenches) {
			return numberOfWorkBenches;
		} else {
			invalidAnswerPrompt();
			return chooseWorkBench(numberOfWorkBenches, assemblyline);
		}
	}
	
	/**
	 * the user indicates which tasks he wants to perform
	 * 
	 * @param The tasks at the user's current workbench
	 */
	public int chooseTask(ArrayList<Task> tasks){
		ArrayList<String> tasksInStrings = new ArrayList<String>();
		
		tasksInStrings.add("Tasks:");
		for (int i = 0; i <tasks.size(); i++) {
					tasksInStrings.add((i+1) + ". " + 
					tasks.get(i).getTaskDescription());
		}
		
		show(tasksInStrings);
		int taskNumber = askNumber("Which taskNumber do you choose?");
		if (taskNumber >= 1  && taskNumber <= taskNumber) {
			return taskNumber;
		} else {
			invalidAnswerPrompt();
			return chooseWorkBench(taskNumber, assemblyLine);
		}	
	}
	
	/**
	 * the user has to indicate which model to order
	 */
	@Override
	public String chooseModel(Set<String> catalogue) {
		ArrayList<String> catalogueInString = new ArrayList<String>();
		
		catalogueInString.add("Possible models:");
		for(String key:catalogue){
			catalogueInString.add(key);
		}
		show(catalogueInString);
		
		return askQuestionLoop("Which model do you want to order?", catalogueInString);
	}
}

 

