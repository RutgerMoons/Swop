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
public class ClientCommunication implements IClientCommunication {

	private Scanner inputReader;

	public ClientCommunication() {
		this.inputReader = new Scanner(System.in);
	}

	/**
	 * Ask if the user wants to advance the assemblyline.
	 * 
	 * @return A String that equals either "Y" (for yes) or "N" (for no).
	 */
	@Override
	public boolean askAdvance() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N",
				expected).equals("Y");
	}

	/**
	 * Ask the user if he wants to continue.
	 * 
	 * @return A String that equals either "Y" (for yes) or "N" (for no).
	 */
	@Override
	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected)
				.equals("Y");
	}

	/**
	 * User presses enter to indicate he's finished It doesn't matter if there's
	 * something typed
	 */
	@Override
	public String askFinished() {
		return askQuestion("Press enter when you're finished");
	}

	/**
	 * Ask a question for which the answer is expected to be an integer.
	 * 
	 * @param question
	 * @return An integer which represents the users's answer.
	 */
	private int askNumber(String question) {
		while (true) {
			try {
				return Integer.parseInt(askQuestion(question));
			} catch (NumberFormatException n) {
			}
		}
	}

	/**
	 * Ask the question, give the answer of the user
	 * 
	 * @param question
	 *            the question the user has to answer
	 * @return answer of the user
	 */
	private String askQuestion(String question) {
		System.out.println(question);
		String answer = inputReader.nextLine();
		return answer;
	}

	/**
	 * Keep asking the same question, until a valid (expected) response is given
	 * by the user note: inputReader is only constructed once
	 * 
	 * @param question
	 *            the question the user has to answer
	 * @param expected
	 *            possible answers
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
	 * the user has to indicate which model to order
	 */
	@Override
	public String chooseModel(Set<String> catalogue) {
		ArrayList<String> catalogueInString = new ArrayList<String>();

		catalogueInString.add("Possible models:");
		for (String key : catalogue) {
			catalogueInString.add(key);
		}
		show(catalogueInString);

		return askQuestionLoop("Which model do you want to order?",
				catalogueInString);
	}

	/**
	 * user has to indicate which role he fulfills
	 */
	@Override
	public String chooseRole() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("manager",
				"garageholder", "worker", "custom car shop manager"));
		return askQuestionLoop(
				"What's your role: manager, garageholder, worker or custom car shop manager?", expected);
	}

	/**
	 * the user indicates which tasks he wants to perform
	 * 
	 * @param The
	 *            tasks at the user's current workbench
	 */
	@Override
	public int chooseTask(ArrayList<String> tasks) {
		ArrayList<String> tasksString = new ArrayList<String>();
		tasksString.add(0, "Tasks:");
		int i = 1;
		for (String task : tasks) {
			tasksString.add(i + "." + task);
			i++;
		}
		show(tasksString);

		int taskNumber = askNumber("Which taskNumber do you choose?");
		if (taskNumber >= 1 && taskNumber <= tasks.size()) {
			return taskNumber;
		} else {
			invalidAnswerPrompt();
			return chooseTask(tasks);
		}
	}

	/**
	 * returns the number of the workbench at which the user is currently
	 * residing
	 * 
	 * @param The
	 *            amount of workbenches in the assemblyline this is necessary to
	 *            validate the user input
	 */
	@Override
	public int chooseWorkBench(int numberOfWorkBenches,
			ArrayList<String> workbenches) {
		ArrayList<String> workBenchNames = new ArrayList<>();
		workBenchNames.add("Workbenches:");
		int i = 1;
		for (String workbench : workbenches) {
			workBenchNames.add(i + ": " + workbench);
			i++;
		}
		show(workBenchNames);
		int numberWorkbench = (askNumber("What's the number of the workbench you're currently residing at?"));
		if (numberWorkbench >= 1 && numberWorkbench <= numberOfWorkBenches) {
			return numberWorkbench;
		} else {
			invalidAnswerPrompt();
			return chooseWorkBench(numberOfWorkBenches, workbenches);
		}
	}

	/**
	 * ask the user how much time has passed and return this if it is an integer
	 * a negative integer -> new day 0 or positive integer -> amount of minutes
	 * passed
	 */
	@Override
	public int getElapsedTime() {
		return askNumber("How much time has passed? (minutes, type a negative number if this is the start of the day)");
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

	/**
	 * ask the name of the user and return this
	 */
	@Override
	public String getName() {
		String answer = "";
		while ((answer = askQuestion("Hello user, what's your name?")) == null
				|| answer.isEmpty()) {
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
		while (quantity <= 0) {
			invalidAnswerPrompt();
			quantity = askNumber("How many cars do you want to order?");
		}
		return quantity;
	}

	/**
	 * Notify the user that the answer he has given is not a valid answer.
	 */
	@Override
	public void invalidAnswerPrompt() {
		show(new ArrayList<String>(
				Arrays.asList("Sorry, that's not a valid response")));
	}

	/**
	 * Notify the user that the answer he has given is not a valid user.
	 */
	@Override
	public void invalidUserPrompt() {
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}

	@Override
	public void logout() {
		System.out.println("Session finished correctly.");
	}

	/**
	 * Show message to the users.
	 * 
	 * @param message
	 *            The message that has to be shown to the users.
	 */
	private void show(List<String> message) {
		for (int i = 0; i < message.size(); i++) {
			System.out.println(message.get(i));
		}
		System.out.println("");
	}

	/**
	 * Show the user the assemblyline.
	 * 
	 * @param assemblyline
	 *            A String representing the assemblyline. The String contains
	 *            all the workbenches in the assemblyline and all the tasks at
	 *            each workbench (with indication whether the task has been
	 *            completed), each of them separated by a comma.
	 * @param tense
	 *            String that indicates whether the other parameter is a current
	 *            or future assemblyline.
	 */
	@Override
	public void showAssemblyLine(String assemblyline, String tense) {
		ArrayList<String> assemblyLineStrings = new ArrayList<String>(
				Arrays.asList(assemblyline.split(",")));
		assemblyLineStrings.add(0, tense + " assemblyline:");
		show(assemblyLineStrings);
	}

	/**
	 * Show the user which benches are keeping the assemblyline from advancing.
	 * 
	 * @param notCompletedBenches
	 *            A list of integers. Each integer represents the number of a
	 *            workbench that is blocking the assemblyline.
	 */
	@Override
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches) {
		show(new ArrayList<String>(
				Arrays.asList("AssemblyLine can't be advanced because of workbench "
						+ notCompletedBenches.toString())));
	}

	/**
	 * Show the user the task he has chosen.
	 * 
	 * @param task
	 *            A String that represents the task. This String contains the
	 *            task description, "Required actions:", and all the actions
	 *            required. Each of these elements are separated by a comma.
	 */
	@Override
	public void showChosenTask(String task) {
		ArrayList<String> taskStrings = new ArrayList<String>(
				Arrays.asList(task.split(",")));
		taskStrings.add(0, "Your task: ");
		show(taskStrings);
	}

	/**
	 * Show the user's completed orders.
	 * 
	 * @param pendingOrders
	 *            An ArrayList of Strings. Each String in this ArrayList
	 *            represents an order. It contains the quantity and the name of
	 *            the model, separated by comma's.
	 */
	@Override
	public void showCompletedOrders(ArrayList<String> completedOrders) {
		if (!completedOrders.isEmpty()) {
			completedOrders.add(0, "Your completed orders:");
			show(completedOrders);
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no completed Orders")));
	}

	/**
	 * Show the user the order he is about to place.
	 * 
	 * @param quantity
	 *            An integer representing the quantity of cars the user is about
	 *            to order.
	 * @param carModel
	 *            A String representing the name of the carmodel the user is
	 *            about to order.
	 * @param estimatedTime
	 *            The estimated completion time, represented by two integers:
	 *            the day and the time (in minutes). If the estimated completion
	 *            time == -1, the completion time can't be shown.
	 */
	@Override
	public void showOrder(int quantity, String model, List<String> chosenParts,
			String estimatedTime) {
		if (model == null || estimatedTime == null) {
			throw new IllegalArgumentException();
		}
		if(estimatedTime == ""){
			show(new ArrayList<String>(Arrays.asList("Your order:", quantity + " "
					+ model )));
			show(new ArrayList<String>(Arrays.asList("Your chosen parts:")));
			show(chosenParts);
		}
		else{
			show(new ArrayList<String>(Arrays.asList("Your order:", quantity + " "
					+ model + " Estimated completion time: " + estimatedTime)));
			show(new ArrayList<String>(Arrays.asList("Your chosen parts:")));
			show(chosenParts);
		}
	}

	/**
	 * Show the user his pending orders.
	 * 
	 * @param pendingOrders
	 *            An ArrayList of Strings. Each String in this ArrayList
	 *            represents an order. It contains the quantity, the name of the
	 *            model and the estimated time, all separated by comma's.
	 */
	@Override
	public void showPendingOrders(ArrayList<String> pendingOrders) {
		if (!pendingOrders.isEmpty()) {
			pendingOrders.add(0, "Your pending orders:");
			show(pendingOrders);
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no pending Orders")));
	}

	/**
	 * Notify the user that all the tasks at the workbench he's working on are
	 * completed.
	 */
	@Override
	public void showWorkBenchCompleted() {
		show(new ArrayList<String>(
				Arrays.asList("All the tasks at this workbench are completed")));
	}

	@Override
	public String choosePart(Set<String> parts) {
		ArrayList<String> partsString = new ArrayList<String>();
		partsString.add(0, "Possible parts:");
		int i = 1;
		for (String part : parts) {
			partsString.add(i + "." + part);
			i++;
		}
		show(partsString);

		int partNumber = askNumber("Which Part Number do you choose?");
		if (partNumber >= 1 && partNumber <= parts.size()) {
			return partsString.get(partNumber).substring(
					partsString.get(partNumber).indexOf(".") + 1);
		} else {
			invalidAnswerPrompt();
			return choosePart(parts);
		}

	}

	@Override
	public void showInvalidModel() {
		show(new ArrayList<String>(
				Arrays.asList("You created an invalid model, try again!")));
	}

	@Override
	public String chooseOrder(List<String> pendingOrders, List<String> completedOrders) {
		ArrayList<String> orderString = new ArrayList<String>();
		orderString.add(0, "Pending Orders:");
		int i = 1;
		for (String order : pendingOrders) {
			orderString.add(i + "." + order);
			i++;
		}

		orderString.add("Completed Orders:");
		for (String order : completedOrders) {
			orderString.add(i + "." + order);
			i++;
		}


		if(!pendingOrders.isEmpty() || !completedOrders.isEmpty()){
			show(orderString);
			if(!askContinue()){
				return "No Order";
			}
			int partNumber = askNumber("Which Order do you choose?");
			if (partNumber >= 1 && partNumber <= (orderString.size() - 2)) { //de -2 zijn de lijnen Pending Orders: en Completed Orders:
				int indexOfCompletedOrders = orderString.indexOf("Completed Orders:");
				if (partNumber == indexOfCompletedOrders) {
					partNumber++;
				}

				return orderString.get(partNumber).substring(orderString.get(partNumber).indexOf(".") + 1);
			} else {
				invalidAnswerPrompt();
				return chooseOrder(pendingOrders, completedOrders);
			}
		}else{
			orderString.add("There are no pending or completed orders, so you can't choose an order");
			show(orderString);
			return "No Order";
		}
	}

	@Override
	public void showOrderDetails(List<String> orderDetails) {
		show(orderDetails);
	}

	@Override
	public String showCustomTasks(List<String> tasks) {
		ArrayList<String> customString = new ArrayList<String>();
		customString.add(0, "Possible tasks:");
		int i = 1;
		for (String customTask : tasks) {
			customString.add(i + "." + customTask);
			i++;
		}
		show(customString);

		int customNumber = askNumber("Which Task do you choose?");
		if (customNumber >= 1 && customNumber <= tasks.size()) {
			return customString.get(customNumber).substring(
					customString.get(customNumber).indexOf(".") + 1);
		} else {
			invalidAnswerPrompt();
			return showCustomTasks(tasks);
		}
	}

	@Override
	public String askDeadline() {
		while (true) {
			show(new ArrayList<String>(Arrays.asList("What is the deadline? (Days,Hours,Minutes   untill completion)")));
			String answer = inputReader.nextLine();
			if (answer != null && isValidDeadline(answer)) {
				return answer;
			}
			invalidAnswerPrompt();
		}
	}
	private boolean isValidDeadline(String answer) {
		String[] split = answer.split(",");
		if(split.length!=3){
			return false;
		}
		try{
			Integer.parseInt(split[0]);
			Integer.parseInt(split[1]);
			Integer.parseInt(split[2]);
		}catch(NumberFormatException e){
			return false;
		}

		return true;
	}

	@Override
	public void showCustomOrder(String time) {
		show(new ArrayList<String>(Arrays.asList("Estimated completion time: " + time)));
	}

	@Override
	public void showStatistics(List<String> statistics){
		List<String> statisticsToShow = new ArrayList<String>();
		statisticsToShow.add("Statistics:");
		statisticsToShow.add("*average number of cars produced in a working day: " + statistics.get(0));
		statisticsToShow.add("*median number of cars produced in a working day: " + statistics.get(1));
		statisticsToShow.add("*number of cars produced in the last two days:     2 days ago:" + statistics.get(2) + "     1 day ago:" + statistics.get(3));
		statisticsToShow.add("*average delay: " + statistics.get(4));
		statisticsToShow.add("*median delay: " + statistics.get(5));
		statisticsToShow.add("*two last delays:     second last:" + statistics.get(6) + "     last:" + statistics.get(7));
		show(statisticsToShow);
	}
}
