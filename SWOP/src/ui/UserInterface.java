package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import car.CarModel;
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
	public void showPendingOrders(ArrayList<String> pendingOrders) {
		if(pendingOrders != null){
			pendingOrders.add(0,"Your pending orders:");
			show(pendingOrders);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no pending Orders")));
	}

	@Override
	public void showCompletedOrders(ArrayList<String> completedOrders) {
		if(completedOrders != null){
			completedOrders.add(0,"Your completed orders:");
			show(completedOrders);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no completed Orders")));
	}

	@Override
	public void showOrder(int quantity, String model, int[] estimatedTime) {
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
	
	public void showAssemblyLine(String assemblyline, String tense){
		ArrayList<String> assemblyLineStrings = new ArrayList<String>(Arrays.asList(assemblyline.split(",")));
		assemblyLineStrings.add(0,tense + " assemblyline:");
		show(assemblyLineStrings);
	}
	
	public void showChosenTask(String task){
		ArrayList <String> taskStrings = new ArrayList<String>(Arrays.asList(task.split(",")));
		taskStrings.add(0,"Your task: ");
		show(taskStrings);
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
		return askNumber("How much time has passed? (minutes, type a negative number if this is the start of the day)");
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
	public int chooseWorkBench(int numberOfWorkBenches, ArrayList<String> workbenches){
		ArrayList<String> workBenchNames = new ArrayList<>();
		workBenchNames.add("Workbenches:");
		int i = 1;
		for (String workbench : workbenches) {
			workBenchNames.add(i + ": " + workbench);
			i++;
		}
		show(workBenchNames);
		
		int numberWorkbench = (askNumber("What's the number of the workbench you're currently residing at?"));
		if (numberWorkbench >= 1  && numberWorkbench <= numberOfWorkBenches) {
			return numberWorkbench;
		} else {
			invalidAnswerPrompt();
			return chooseWorkBench(numberOfWorkBenches, workbenches);
		}
	}
	
	/**
	 * the user indicates which tasks he wants to perform
	 * 
	 * @param The tasks at the user's current workbench
	 */
	public int chooseTask(ArrayList<String> tasks){
		ArrayList<String> tasksString = new ArrayList<String>();
		tasksString.add(0,"Tasks:");
		int i = 1;
		for(String task : tasks){
			tasksString.add(i+"."+task);
			i++;
		}
		show(tasksString);
		
		int taskNumber = askNumber("Which taskNumber do you choose?");
		if (taskNumber >= 1  && taskNumber <= tasks.size()) {
			return taskNumber;
		} else {
			invalidAnswerPrompt();
			return chooseTask(tasks);
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

 

