package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.Optional;

import domain.assembly.IAssemblyLine;
import domain.assembly.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.job.IAction;
import domain.job.ITask;
import domain.order.Delay;
import domain.order.IOrder;
import domain.vehicle.IVehicle;
import domain.vehicle.IVehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

/**
 * A text-based user interface to interact with the system.
 * 
 */
public class ClientCommunication{

	private Scanner inputReader;
	private final String LINESEPARATOR = System.lineSeparator();
	public ClientCommunication() {
		this.inputReader = new Scanner(System.in);
	}

	/**
	 * Ask if the user wants to advance the assemblyline.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */

	public boolean askAdvance() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N",
				expected).equals("Y");
	}

	/**
	 * Ask the user if he wants to continue.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */

	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected)
				.equals("Y");
	}

	/**
	 * Ask the user for a deadline.
	 */

	public int askDeadline() {
		int days = askNumber("How many days until the deadline is reached?");
		while(days<0){
			invalidAnswerPrompt();
			days = askNumber("How many days until the deadline is reached?");
		}
		int minutes = askNumber("How many minutes on that day untill the deadline is reached?");
		while(minutes<0){
			invalidAnswerPrompt();
			minutes = askNumber("How many minutes on that day untill the deadline is reached?");
		}

		return days*Clock.MINUTESINADAY + minutes;
	}

	/**
	 * Ask if the user is finished.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */

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
	 * Choose the car model the user wants to order.
	 * @param catalogue
	 * 			A Set which contains Strings that represent the names of all possible car models that can be ordered.
	 * @return
	 */

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
	 * Lets the user choose an order out of all his pending/completed orders.
	 */

	public Optional<IOrder> chooseOrder(List<IOrder> pendingOrders, List<IOrder> completedOrders) {
		ArrayList<String> orderString = new ArrayList<String>();
		orderString.add(0, "Pending Orders:" + LINESEPARATOR);
		int i = 1;
		for (IOrder order : pendingOrders) {
			orderString.add(i + "." + order.toString() + LINESEPARATOR);
			i++;
		}

		orderString.add("Completed Orders:");
		for (IOrder order : completedOrders) {
			orderString.add(i + "." + order.toString() + LINESEPARATOR);
			i++;
		}
		if(!pendingOrders.isEmpty() || !completedOrders.isEmpty()){
			show(orderString);
			if(askContinue()){

				int partNumber = askNumber("Which Order do you choose?")-1;
				if (partNumber >0  && partNumber < (orderString.size() - 2)) { 
					if(partNumber<pendingOrders.size()){
						return Optional.fromNullable(pendingOrders.get(partNumber));
					}else {
						partNumber = partNumber - pendingOrders.size();
						return Optional.fromNullable(completedOrders.get(partNumber));
					}
				} else {
					invalidAnswerPrompt();
					return chooseOrder(pendingOrders, completedOrders);
				}
			}
		}
		return Optional.absent();
	}

	/**
	 * Lets the user choose a CarOption when he is putting his model together.
	 */

	public Optional<IVehicleOption> choosePart(List<IVehicleOption> parts) {
		ArrayList<String> partsString = new ArrayList<String>();
		partsString.add(0, "Possible parts:");

		int i = 1;
		for (IVehicleOption part : parts) {
			partsString.add(i + "." + part.toString() + LINESEPARATOR);
			i++;
			if(i==parts.size() && part.getType().isOptional()){
				partsString.add(i + ". Select Nothing" + LINESEPARATOR);
			}
		}


		show(partsString);


		int partNumber = askNumber("Which Part Number do you choose?")-1;
		if (partNumber >=0  && partNumber < parts.size()) {
			return Optional.fromNullable(parts.get(partNumber));
		}else if(partNumber == parts.size()){
			return Optional.absent();
		}else {
			invalidAnswerPrompt();
			return choosePart(parts);
		}

	}

	/**
	 * Let the user indicate which role he fulfills.
	 * @return
	 * 			A String that represents which role the user fulfills.
	 */

	public String chooseRole() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("manager",
				"garageholder", "worker", "custom car shop manager"));
		return askQuestionLoop(
				"What's your role: manager, garageholder, worker or custom car shop manager?", expected);
	}

	/**
	 * Let the user indicate which task he wants to perform.
	 * @param tasksAtWorkbench
	 * 			ArrayList that contains Strings. Each String represents the description of one of the tasks.
	 * @return
	 * 			A strictly positive integer.
	 * 			The integer 'n' that is returned indicates the user chooses the n'th element in the given list.
	 */

	public ITask chooseTask(List<ITask> tasksAtWorkbench) {
		ArrayList<String> tasksString = new ArrayList<String>();
		tasksString.add(0, "Tasks:");
		int i = 1;
		for (ITask task : tasksAtWorkbench) {
			tasksString.add(i + "." + task.toString() + LINESEPARATOR);
			i++;
		}
		show(tasksString);

		int taskNumber = askNumber("Which taskNumber do you choose?") -1;
		if (taskNumber >= 0 && taskNumber < tasksAtWorkbench.size()) {
			return tasksAtWorkbench.get(taskNumber);
		} else {
			invalidAnswerPrompt();
			return chooseTask(tasksAtWorkbench);
		}


	}

	/**
	 * Let the user indicate which workbench he's working at.
	 * @param numberOfWorkbenches
	 * 			Integer that represents how many workbenches the user can choose from.
	 * @param workbenches
	 * 			ArrayList that contains Strings. Each String represents the name of one of the workbenches.
	 * @return
	 * 			A strictly positive integer. 
	 * 			The integer 'n' that is returned indicates the user chooses the n'th element in the given list.
	 */

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
	 * Let the user indicate how much time has passed.
	 * @return
	 * 			An integer representing the elapsed time (in minutes).
	 */

	public int getElapsedTime() {
		return askNumber("How much time has passed? (minutes, type a negative number if this is the start of the day)");
	}

	/**
	 * presents the user with all of his possible use cases 
	 * and the user indicates which use case to perform
	 */

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
	 * Asks the user to enter an integer 0 < integer <= maxValue
	 * If the answer isn't valid, the system informs the user and repeats the question,
	 * until a valid answer is given.
	 */
	public int getIndex(int maxValue) {
		int answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to " + Integer.toString(maxValue));
		while (answer <= 0 || answer > maxValue) {
			invalidAnswerPrompt();
			answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to" + Integer.toString(maxValue));
		}
		return answer;
	}

	/**
	 * Get the users' name.
	 * @return
	 * 			A String that represents the user's name.
	 */

	public String getName() {
		String answer = "";
		while ((answer = askQuestion("Hello user, what's your name?")) == null
				|| answer.isEmpty()) {
			invalidAnswerPrompt();
		}
		return answer;
	}

	/**
	 * Let the user indicate how many cars he wants to order.
	 * @return
	 * 			a positive integer representing the quantity
	 */

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

	public void invalidAnswerPrompt() {
		show(new ArrayList<String>(
				Arrays.asList("Sorry, that's not a valid response")));
	}

	/**
	 * Notify the user that the answer he has given is not a valid user.
	 */

	public void invalidUserPrompt() {
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}

	/**
	 * log the current user out
	 */

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
	}
	/**
	 * Show the user the currently used Scheduling Algorithm and all the possible Scheduling Algorithms
	 */
	public void showAlgorithms(String current, ArrayList<String> possible) {
		possible.add(0, "current algorithm: " + current + "\n");
		show(possible);
	}

	/**
	 * Notify the user that the scheduling algorithm was succesfully switched.
	 */
	public void showAlgorithmSwitched(String type) {
		show(Arrays.asList("Scheduling algorithm succesfully changed to: " + type));
	}

	/**
	 * Notify the user that the scheduling algorithm was succesfully switched using the given batch.
	 */
	public void showAlgorithmSwitched(String type, String batch) {
		show(Arrays.asList("Scheduling algorithm succesfully changed to: " + type + 
				" with batch: " + batch));
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

	public void showAssemblyLine(IAssemblyLine assemblyLine) {
		ArrayList<String> assemblyLineStrings = new ArrayList<String>();
		assemblyLineStrings.add("STATE: " + assemblyLine.getState() + LINESEPARATOR);

		assemblyLineStrings.add(0, "current assemblyline:");

		List<IWorkBench> allWorkbenches = assemblyLine.getWorkbenches();

		for(IWorkBench workbench: allWorkbenches){
			assemblyLineStrings.addAll(getPendingTasks(workbench));
			assemblyLineStrings.addAll(getCompletedTasks(workbench));
		}
		show(assemblyLineStrings);
	}

	private List<String> getCompletedTasks(IWorkBench workbench) {
		List<String> completedTaskList = new ArrayList<>();

		for(ITask task: workbench.getCurrentTasks()){
			if(task.isCompleted()){
				completedTaskList.add(task.toString() + ": completed" + LINESEPARATOR);
			}
		}

		return completedTaskList;
	}

	private List<String> getPendingTasks(IWorkBench workbench) {
		List<String> pendingTaskList = new ArrayList<>();

		for(ITask task: workbench.getCurrentTasks()){
			if(!task.isCompleted()){
				pendingTaskList.add(task.toString() + ": pending" + LINESEPARATOR);
			}
		}

		return pendingTaskList;
	}

	/**
	 * Shows batches with index
	 */
	public void showBatches(ArrayList<String> batches) {
		show(batches);
	}

	/**
	 * Show the user which benches are keeping the assemblyline from advancing.
	 * @param notCompletedBenches
	 * 			A list of integers. Each integer represents the number of a workbench that is blocking the assemblyline.
	 */

	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches) {
		show(new ArrayList<String>(
				Arrays.asList("AssemblyLine can't be advanced because of workbench "
						+ notCompletedBenches.toString())));
	}

	/**
	 * Show the user the task he has chosen.
	 * @param chosenTask
	 * 			A String that represents the task. 
	 * 			This String contains the task description, "Required actions:", and all the actions required.
	 * 			Each of these elements are separated by a comma.
	 */

	public void showChosenTask(ITask chosenTask) {
		ArrayList<String> taskStrings = new ArrayList<String>();
		taskStrings.add("Your task: ");
		taskStrings.add(chosenTask.toString() + LINESEPARATOR);
		taskStrings.add("Required actions: "+ LINESEPARATOR);
		int i = 1;
		for(IAction action: chosenTask.getActions()){
			taskStrings.add(i + ". " + action.toString() + LINESEPARATOR);
			i++;
		}

		show(taskStrings);
	}

	/**
	 * Show the user's completed orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity and the name of the model, separated by comma's.
	 */

	public void showCompletedOrders(List<IOrder> completedOrders) {
		List<String> completedOrdersList = new ArrayList<String>();
		if (!completedOrders.isEmpty()) {
			completedOrdersList.add(0, "Your completed orders:" + LINESEPARATOR);
			for(IOrder order : completedOrders){
				completedOrdersList.add(order.toString() + LINESEPARATOR);
				completedOrdersList.add("\tcompletion time: " + order.getEstimatedTime().toString() + LINESEPARATOR );
			}
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no completed Orders")));
	}

	/**
	 * Show a custom order with the given estimated completion time.
	 */

	public void showCustomOrder(ImmutableClock time) {
		show(new ArrayList<String>(Arrays.asList("Estimated completion time: " + time)));
	}

	/**
	 * Show the given custom tasks and let the user choose one.
	 */

	public IVehicle showSpecificCustomTasks(List<IVehicle> vehicles) {
		ArrayList<String> customString = new ArrayList<String>();
		customString.add(0, "Possible tasks:");
		int i = 1;
		for (IVehicle customTask : vehicles) {
			customString.add(i + "." + customTask.toString() + LINESEPARATOR);
			i++;
		}
		show(customString);


		int customNumber = askNumber("Which Task do you choose?")-1;
		if (customNumber >= 0 && customNumber < vehicles.size()) {
			return vehicles.get(customNumber);
		} else {
			invalidAnswerPrompt();
			return showSpecificCustomTasks(vehicles);
		}
	}

	/**
	 * Notify the user that the carModel he has put togehther is not a valid model.
	 */

	public void showInvalidModel() {
		show(new ArrayList<String>(
				Arrays.asList("You created an invalid model, try again!")));
	}

	/**
	 * Shows the user there are no specification batches to switch the scheduling algorithm to.
	 */
	public void showNoBatchesAvailable() {
		show(new ArrayList<String>(Arrays.asList("No batches available")));
	}

	/**
	 * Show the user the order he is about to place.
	 * @param quantity
	 * 			An integer representing the quantity of cars the user is about to order.
	 * @param realModel 
	 * @param chosenParts
	 * 			A String representing the name of the vehicleModel the user is about to order.
	 * @param estimatedTime
	 * 			The estimated completion time, represented by two integers: the day and the time (in minutes).
	 * 			If the estimated completion time == -1, the completion time can't be shown.
	 */

	public void showOrder(int quantity, VehicleSpecification model, List<IVehicleOption> chosenParts,
			ImmutableClock estimatedTime) {

		showOrder(quantity, model, chosenParts);
		show(Arrays.asList("Estimated time of completion :" + estimatedTime.toString() + LINESEPARATOR));
	}

	public void showOrder(int quantity, VehicleSpecification realModel, List<IVehicleOption> chosenParts){
		show(new ArrayList<String>(Arrays.asList("Your order:", quantity + " "
				+ realModel.toString() + LINESEPARATOR )));
		show(new ArrayList<String>(Arrays.asList("Your chosen parts:")));
		List<String> chosenPartsInString = new ArrayList<String>();
		for(IVehicleOption option : chosenParts){
			chosenPartsInString.add(option.toString() + LINESEPARATOR);
		}
		show(chosenPartsInString);
	}
	/**
	 * Show the details of the given order.
	 */

	public void showOrderDetails(IOrder order) {
		List<String> orderDetails = new ArrayList<>();
		orderDetails.add("Orderdetails:");
		orderDetails.add(order.getQuantity() + " "
				+ order.getDescription());

		String carDetails = "Chosen carOptions: ";
		for (VehicleOptionCategory category : order.getDescription()
				.getVehicleOptions().keySet()) {
			carDetails += order.getDescription().getVehicleOptions()
					.get(category)
					+ " ";
		}
		orderDetails.add(carDetails);

		orderDetails.add("Order Time: "
				+ order.getOrderTime().toString());
		try {
			orderDetails.add("(Expected) Completion Time: "
					+ order.getDeadline().toString());
		} catch (NotImplementedException e) {
			orderDetails.add("(Expected) Completion Time: "
					+ order.getEstimatedTime().toString());
		}
		show(orderDetails);
	}

	/**
	 * Show the user's pending orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity, the name of the model and the estimated time, all separated by comma's.
	 */

	public void showPendingOrders(List<IOrder> pendingOrders) {
		List<String> pendingOrdersList = new ArrayList<String>();
		if (!pendingOrders.isEmpty()) {
			pendingOrdersList.add(0, "Your pending orders:" + LINESEPARATOR);
			for(IOrder order : pendingOrders){
				pendingOrdersList.add(order.toString() + LINESEPARATOR);
				pendingOrdersList.add("\tEstimated completion time: " + order.getEstimatedTime().toString() + LINESEPARATOR );
			}
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no pending Orders")));
	}

	/**
	 * Show the given statistics to the user.
	 */

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

	/**
	 * Notify the user that all the tasks at the workbench he's working on are completed.
	 */

	public void showWorkBenchCompleted() {
		show(new ArrayList<String>(
				Arrays.asList("All the tasks at this workbench are completed")));
	}

	public IAssemblyLine chooseAssemblyLine(List<IAssemblyLine> allAssemblyLines) {
		List<String> strings = new ArrayList<>();

		for(IAssemblyLine assemblyLine: allAssemblyLines){
			strings.add(assemblyLine.toString() + LINESEPARATOR);
		}

		show(strings);
		int index = askNumber("Which AssemblyLine do you choose?") -1;
		if(index>=0 && index<allAssemblyLines.size()){
			return allAssemblyLines.get(index);
		}else{
			invalidAnswerPrompt();
			return chooseAssemblyLine(allAssemblyLines);
		}

	}

	public IWorkBench chooseWorkBench(List<IWorkBench> workbenches) {
		List<String> strings = new ArrayList<>();

		for(IWorkBench workbench: workbenches){
			strings.add(workbench.toString() + LINESEPARATOR);
		}

		show(strings);
		int index = askNumber("Which WorkBench do you choose?") -1;
		if(index>=0 && index<workbenches.size()){
			return workbenches.get(index);
		}else{
			invalidAnswerPrompt();
			return chooseWorkBench(workbenches);
		}
	}

	public String showCustomTasks(Set<String> customTasks) {
		ArrayList<String> customString = new ArrayList<String>();
		customString.add(0, "Possible tasks:");
		int i = 1;
		for (String customTask : customTasks) {
			customString.add(i + "." + customTask);
			i++;
		}
		show(customString);

		int customNumber = askNumber("Which Task do you choose?") -1;
		if (customNumber >0 && customNumber < customTasks.size()) {
			return customString.get(customNumber).substring(	//TODO check of het werkt
					customString.get(customNumber).indexOf(".") + 1);
		} else {
			invalidAnswerPrompt();
			return showCustomTasks(customTasks);
		}
	}

	public void showAverageDays(int averageDays) {
		show(Arrays.asList("Average vehicles produced: " + averageDays));
	}

	public void showMedianDays(int medianDays) {
		show(Arrays.asList("Median vehicles produced: " + medianDays));
	}

	public void showDetailsDays(List<Integer> detailedDays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDays.size() + " days:");
		for(Integer day : detailedDays){
			details.add(day + LINESEPARATOR);
		}
		show(details);
		
	}

	public void showAverageDelays(int averageDelays) {
		show(Arrays.asList("Average delays: " + averageDelays));
		
	}

	public void showMedianDelays(int medianDelays) {
		show(Arrays.asList("Median delays: " + medianDelays));
		
	}

	public void showDetailedDelays(List<Delay> detailedDelays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDelays.size() + " delays:");
		for(Delay delay : detailedDelays){
			details.add(delay.toString() + LINESEPARATOR);
		}
		show(details);
	}
}
