package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * A text-based user interface to interact with the system.
 *
 */
public class ClientCommunication implements IClientCommunication{
	
	private Scanner inputReader;
	
	public ClientCommunication() {
		this.inputReader = new Scanner(System.in);
	}
	
	/**
	 * Ask the question, give the answer of the user
	 * 
	 * @param question the question the user has to answer
	 * @return answer of the user
	 */
	private String askQuestion(String question){
		System.out.println(question);
		String answer = inputReader.nextLine();
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
		while (true) {
			System.out.println(question);
			String answer = inputReader.nextLine();
			if (answer != null && expected.contains(answer)) {
				return answer;
			} 
			invalidAnswerPrompt();
		}
	}
	  
	/**
	 * Ask a question for which the answer is expected to be an integer.
	 * @param question
	 * @return
	 * 			An integer which represents the users's answer.
	 */
	private int askNumber(String question) {
		while (true) {
			try {
				return Integer.parseInt(askQuestion(question));
			}
			catch (NumberFormatException n) {
			}
		}
	}
	
	/**
	 * Ask the user if he wants to continue.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */
	@Override
	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected).equals("Y");
	}
	
	/**
	 * User presses enter to indicate he's finished
	 * It doesn't matter if there's something typed
	 */
	@Override
	public String askFinished(){
		return askQuestion("Press enter when you're finished");
	}
	
	/**
	 * Ask if the user wants to advance the assemblyline.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */
	@Override
	public boolean askAdvance(){
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N", expected).equals("Y");
	}
	
	/**
	 * Notify the user that the answer he has given is not a valid answer.
	 */
	@Override
	public void invalidAnswerPrompt(){
		show(new ArrayList<String>(Arrays.asList("Sorry, that's not a valid response")));
	}
	
	/**
	 * Notify the user that the answer he has given is not a valid user.
	 */
	@Override
	public void invalidUserPrompt(){
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}
	
	/**
	 * Show message to the users.
	 * @param message
	 * 			The message that has to be shown to the users.
	 */
	private void show(ArrayList<String> message){
		for (int i = 0; i < message.size(); i++) {
			System.out.println(message.get(i));
		}
		System.out.println("");
	}
	
	/**
	 * Show the user his pending orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity, the name of the model and the estimated time, all separated by comma's.
	 */
	@Override
	public void showPendingOrders(ArrayList<String> pendingOrders) {
		if(!pendingOrders.isEmpty()){
			pendingOrders.add(0,"Your pending orders:");
			show(pendingOrders);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no pending Orders")));
	}

	/**
	 * Show the user's completed orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity and the name of the model, separated by comma's.
	 */
	@Override
	public void showCompletedOrders(ArrayList<String> completedOrders) {
		if(!completedOrders.isEmpty()){
			completedOrders.add(0,"Your completed orders:");
			show(completedOrders);
		}
		else show(new ArrayList<String>(Arrays.asList("You have no completed Orders")));
	}

	/**
	 * Show the user the order he is about to place.
	 * @param quantity
	 * 			An integer representing the quantity of cars the user is about to order.
	 * @param carModel
	 * 			A String representing the name of the carmodel the user is about to order.
	 * @param estimatedTime
	 * 			The estimated completion time, represented by two integers: the day and the time (in minutes).
	 * 			If the estimated completion time == -1, the completion time can't be shown.
	 */
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
	
	/**
	 * Notify the user that all the tasks at the workbench he's working on are completed.
	 */
	@Override
	public void showWorkBenchCompleted(){
		show(new ArrayList<String>(Arrays.asList("All the tasks at this workbench are completed")));
	}
	
	/**
	 * Show the user the assemblyline.
	 * @param assemblyline
	 * 			A String representing the assemblyline.
	 * 			The String contains all the workbenches in the assemblyline 
	 * 			and all the tasks at each workbench (with indication whether the task has been completed), 
	 * 			each of them separated by a comma.
	 * @param tense
	 * 			String that indicates whether the other parameter is a current or future assemblyline.
	 */
	@Override
	public void showAssemblyLine(String assemblyline, String tense){
		ArrayList<String> assemblyLineStrings = new ArrayList<String>(Arrays.asList(assemblyline.split(",")));
		assemblyLineStrings.add(0,tense + " assemblyline:");
		show(assemblyLineStrings);
	}
	
	/**
	 * Show the user the task he has chosen.
	 * @param task
	 * 			A String that represents the task. 
	 * 			This String contains the task description, "Required actions:", and all the actions required.
	 * 			Each of these elements are separated by a comma.
	 */
	@Override
	public void showChosenTask(String task){
		ArrayList <String> taskStrings = new ArrayList<String>(Arrays.asList(task.split(",")));
		taskStrings.add(0,"Your task: ");
		show(taskStrings);
	}
	
	/**
	 * Show the user which benches are keeping the assemblyline from advancing.
	 * @param notCompletedBenches
	 * 			A list of integers. Each integer represents the number of a workbench that is blocking the assemblyline.
	 */
	@Override
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches){
		show(new ArrayList<String>(Arrays.asList(	"AssemblyLine can't be advanced because of workbench " + 
													notCompletedBenches.toString())));
	}
	
	/**
	 * ask the name of the user and return this
	 */
	@Override
	public String getName(){
		String answer = "";
		while((answer= askQuestion("Hello user, what's your name?"))==null || answer.isEmpty()){
			invalidAnswerPrompt();
		}
		return answer;
	}
	
	/**
	 * Ask the amount of cars to order, until the response is a positive integer
	 */
	@Override
	public int getQuantity() {
		int quantity = askNumber("How many cars do you want to order?");
		while (quantity<=0) {
			invalidAnswerPrompt();
			quantity = askNumber("How many cars do you want to order?");
		}
		return quantity;
	}
	
	/**
	 * ask the user how much time has passed and return this if it is an integer
	 * a negative integer -> new day
	 * 0 or positive integer -> amount of minutes passed
	 */
	@Override
	public int getElapsedTime(){
		return askNumber("How much time has passed? (minutes, type a negative number if this is the start of the day)");
	}
	
	/**
	 * user has to indicate which role he fulfills  
	 */
	@Override
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
	@Override
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
	@Override
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

	@Override
	public int getFlowControllerIndex(List<String> accessRights) {
		System.out.println("Options:");
		int i = 1;
		for (String accessRight : accessRights) {
			System.out.println(i + ": " + accessRight);
			i++;
		}
		
		int index = askNumber("What do you want to perform?");
		while (index < 1 || index > accessRights.size()) {
			invalidAnswerPrompt();
			index = askNumber("What do you want to perform?");
		}
		return index;
	}

	@Override
	public void logout() {
		System.out.println("Session finished correctly.");		
	}
}

 

