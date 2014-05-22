package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.NotImplementedException;
import domain.job.action.IAction;
import domain.job.task.ITask;
import domain.order.Delay;
import domain.order.order.IOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A text-based user interface to interact with the system.
 * 
 */
public class ClientCommunication implements IClientCommunication{

	private Scanner inputReader;
	public ClientCommunication() {
		this.inputReader = new Scanner(System.in);
	}

	@Override
	public boolean askAdvance() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N",
				expected).equals("Y");
	}

	@Override
	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected)
				.equals("Y");
	}

	@Override
	public int askDeadline() {
		int days = askNumber("How many days until the deadline is reached?");
		while(days<0){
			invalidAnswerPrompt();
			days = askNumber("How many days until the deadline is reached?");
		}
		int minutes = askNumber("How many minutes on that day until the deadline is reached?");
		while(minutes<0){
			invalidAnswerPrompt();
			minutes = askNumber("How many minutes on that day until the deadline is reached?");
		}

		return days*Clock.MINUTESINADAY + minutes;
	}

	@Override
	public String askFinished() {
		return askQuestion("Press enter when you're finished");
	}

	/**
	 * Ask a question for which the answer is expected to be an integer.
	 * 
	 * @param 	question
	 * 			Question needed to be asked
	 * 
	 * @return 	An integer which represents the users answer.
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
	 * Ask the question, give the answer of the user.
	 * 
	 * @param 	question
	 *          the question the user has to answer
	 * 
	 * @return 	answer of the user
	 */
	private String askQuestion(String question) {
		System.out.println(question);
		String answer = inputReader.nextLine();
		return answer;
	}

	/**
	 * Keep asking the same question, until a valid (expected) response is given
	 * by the user.
	 * 
	 * @param 	question
	 *          the question the user has to answer
	 * 
	 * @param 	expected
	 *          possible answers
	 *          
	 * @return 	answer of the user, this answer is an expected one
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

	@Override
	public Optional<IOrder> chooseOrder(List<IOrder> pendingOrders, List<IOrder> completedOrders) {
		ArrayList<String> orderString = new ArrayList<String>();
		orderString.add(0, "Pending Orders:" );
		int i = 1;
		for (IOrder order : pendingOrders) {
			orderString.add(i + "." + order.toString() );
			i++;
		}

		orderString.add("Completed Orders:");
		for (IOrder order : completedOrders) {
			orderString.add(i + "." + order.toString() );
			i++;
		}
		if(!pendingOrders.isEmpty() || !completedOrders.isEmpty()){
			show(orderString);
			if(askContinue()){

				int partNumber = askNumber("Which Order do you choose?")-1;
				if (partNumber >=0  && partNumber <= (pendingOrders.size() + completedOrders.size())) { 
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
		}else { 
			show(Arrays.asList("You have no pending or completed orders"));
		}
		return Optional.absent();
	}

	@Override
	public Optional<VehicleOption> choosePart(List<VehicleOption> parts) {
		ArrayList<String> partsString = new ArrayList<String>();
		partsString.add(0, "Possible parts:");

		int i = 1;
		for (VehicleOption part : parts) {
			partsString.add(i + "." + part.toString() );
			i++;
			if(i==parts.size()+1 && part.getType().isOptional()){
				partsString.add(i + ".Select Nothing" );
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

	@Override
	public String chooseRole() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("manager",
				"garageholder", "worker", "custom car shop manager"));
		return askQuestionLoop(
				"What's your role: manager, garageholder, worker or custom car shop manager?", expected);
	}

	@Override
	public ITask chooseTask(List<ITask> tasksAtWorkbench) {
		ArrayList<String> tasksString = new ArrayList<String>();
		tasksString.add(0, "Tasks:");
		int i = 1;
		for (ITask task : tasksAtWorkbench) {
			tasksString.add(i + "." + task.toString() );
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

	@Override
	public int getIndex(int maxValue) {
		int answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to " + Integer.toString(maxValue));
		while (answer <= 0 || answer > maxValue) {
			invalidAnswerPrompt();
			answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to" + Integer.toString(maxValue));
		}
		return answer;
	}

	@Override
	public String getName() {
		String answer = "";
		while ((answer = askQuestion("Hello user, what's your name?")) == null
				|| answer.isEmpty()) {
			invalidAnswerPrompt();
		}
		return answer;
	}

	@Override
	public int getQuantity() {
		int quantity = askNumber("How many cars do you want to order?");
		while (quantity <= 0) {
			invalidAnswerPrompt();
			quantity = askNumber("How many cars do you want to order?");
		}
		return quantity;
	}

	@Override
	public void invalidAnswerPrompt() {
		show(new ArrayList<String>(
				Arrays.asList("Sorry, that's not a valid response")));
	}


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
	}

	@Override
	public void showAlgorithms(String current, List<String> possible) {
		List<String> currentAlgorithm = new ArrayList<String>();
		currentAlgorithm.add("Current algorithm: " + current );
		this.show(currentAlgorithm);
		this.showAlgorithms(possible);
	}

	@Override
	public void showAlgorithmSwitched(String schedulingAlgorithmType) {
		show(Arrays.asList("Scheduling algorithm succesfully changed to: " + schedulingAlgorithmType));
	}

	@Override
	public void showAlgorithmSwitched(String schedulingAlgorithmType, List<VehicleOption> vehicleOptionsChosenForBatch) {
		String batchToString = "";
		for (VehicleOption o : vehicleOptionsChosenForBatch) {
			batchToString += o.toString() + ", ";
		}
		batchToString = batchToString.substring(0, batchToString.length() - 2);
		show(Arrays.asList("Scheduling algorithm succesfully changed to: " + schedulingAlgorithmType + 
				" with batch: " + batchToString));
	}

	@Override
	public void showAssemblyLine(IAssemblyLine assemblyLine) {
		ArrayList<String> assemblyLineStrings = new ArrayList<String>();
		assemblyLineStrings.add(0, "current assemblyline:");
		assemblyLineStrings.add(assemblyLine.getState().toString() );
		assemblyLineStrings.add("");

		List<IWorkBench> allWorkbenches = assemblyLine.getWorkBenches();

		for(IWorkBench workbench: allWorkbenches){
			assemblyLineStrings.add("Workbench: " + workbench.toString());
			assemblyLineStrings.add("Pending Tasks: ");
			assemblyLineStrings.addAll(getPendingTasks(workbench));
			assemblyLineStrings.add("Completed Tasks: ");
			assemblyLineStrings.addAll(getCompletedTasks(workbench));
			assemblyLineStrings.add("");
		}
		show(assemblyLineStrings);
	}

	private List<String> getCompletedTasks(IWorkBench workbench) {
		List<String> completedTaskList = new ArrayList<>();

		for(ITask task: workbench.getCurrentTasks()){
			if(task.isCompleted()){
				completedTaskList.add(task.toString() + ": completed" );
			}
		}

		return completedTaskList;
	}

	private List<String> getPendingTasks(IWorkBench workbench) {
		List<String> pendingTaskList = new ArrayList<>();

		for(ITask task: workbench.getCurrentTasks()){
			if(!task.isCompleted()){
				pendingTaskList.add(task.toString() + ": pending" );
			}
		}

		return pendingTaskList;
	}

	@Override
	public void showChosenTask(ITask chosenTask) {
		ArrayList<String> taskStrings = new ArrayList<String>();
		taskStrings.add("Your task: ");
		taskStrings.add(chosenTask.toString() );
		taskStrings.add("Required actions: ");
		int i = 1;
		for(IAction action: chosenTask.getActions()){
			taskStrings.add(i + ". " + action.toString() );
			i++;
		}

		show(taskStrings);
	}

	@Override
	public void showCompletedOrders(List<IOrder> completedOrders) {
		List<String> completedOrdersList = new ArrayList<String>();
		if (!completedOrders.isEmpty()) {
			completedOrdersList.add(0, "Your completed orders:" );
			for(IOrder order : completedOrders){
				completedOrdersList.add(order.toString() );
				completedOrdersList.add("\tcompletion time: " + order.getEstimatedTime().toString()  );
			}
			completedOrdersList.add("");
			show(completedOrdersList);
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no completed Orders")));
	}

	@Override
	public void showCustomOrder(ImmutableClock time) {
		show(new ArrayList<String>(Arrays.asList("Estimated completion time: " + time)));
	}

	@Override
	public IVehicle showSpecificCustomTasks(List<IVehicle> vehicles) {
		ArrayList<String> customString = new ArrayList<String>();
		customString.add(0, "Possible tasks:");
		int i = 1;
		for (IVehicle customTask : vehicles) {
			customString.add(i + "." + customTask.toString() );
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

	@Override
	public void showInvalidModel() {
		show(new ArrayList<String>(
				Arrays.asList("You created an invalid model, try again!")));
	}

	@Override
	public void showNoBatchesAvailable() {
		show(new ArrayList<String>(Arrays.asList("No batches available")));
	}

	@Override
	public void showOrder(int quantity, VehicleSpecification model, List<VehicleOption> chosenParts,
			ImmutableClock estimatedTime) {

		showOrder(quantity, model, chosenParts);
		show(Arrays.asList("Estimated time of completion :" + estimatedTime.toString() ));
	}

	@Override
	public void showOrder(int quantity, VehicleSpecification realModel, List<VehicleOption> chosenParts){
		show(new ArrayList<String>(Arrays.asList("Your order:", quantity + " "
				+ realModel.toString()  )));
		show(new ArrayList<String>(Arrays.asList("Your chosen parts:")));
		List<String> chosenPartsInString = new ArrayList<String>();
		for(VehicleOption option : chosenParts){
			chosenPartsInString.add(option.toString() );
		}
		show(chosenPartsInString);
	}

	@Override
	public void showOrderDetails(IOrder order) {
		List<String> orderDetails = new ArrayList<>();
		orderDetails.add("Orderdetails:");
		orderDetails.add(order.getQuantity() + " "
				+ order.getDescription());

		String carDetails = "Chosen vehicle options: ";
		for (VehicleOptionCategory category : order.getDescription()
				.getVehicleOptions().keySet()) {
			carDetails += order.getDescription().getVehicleOptions()
					.get(category)
					+ ", ";
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
		orderDetails.add("");
		show(orderDetails);
	}

	@Override
	public void showPendingOrders(List<IOrder> pendingOrders) {
		List<String> pendingOrdersList = new ArrayList<String>();
		if (!pendingOrders.isEmpty()) {
			pendingOrdersList.add(0, "Your pending orders:" );
			for(IOrder order : pendingOrders){
				pendingOrdersList.add(order.toString() );
				pendingOrdersList.add("\tEstimated completion time: " + order.getEstimatedTime().toString()  );
			}
			pendingOrdersList.add("");
			show(pendingOrdersList);
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no pending Orders")));
	}

	@Override
	public void showWorkBenchCompleted() {
		show(new ArrayList<String>(
				Arrays.asList("All the tasks at this workbench are completed")));
	}

	@Override
	public IAssemblyLine chooseAssemblyLine(List<IAssemblyLine> allAssemblyLines) {
		List<String> strings = new ArrayList<>();

		int i = 1;
		for(IAssemblyLine assemblyLine: allAssemblyLines){
			strings.add(i + ": " +assemblyLine.toString());
			i++;
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

	@Override
	public IWorkBench chooseWorkBench(List<IWorkBench> workbenches) {
		List<String> strings = new ArrayList<>();

		int i = 1;
		for(IWorkBench workbench: workbenches){
			strings.add(i + ": " +workbench.toString());
			i++;
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

	@Override
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
		if (customNumber >=0 && customNumber < customTasks.size()) {
			return customString.get(customNumber+1).substring(	
					customString.get(customNumber+1).indexOf(".") + 1);
		} else {
			invalidAnswerPrompt();
			return showCustomTasks(customTasks);
		}
	}

	@Override
	public void showAverageDays(int averageDays) {
		show(Arrays.asList("Average vehicles produced: " + averageDays));
	}

	@Override
	public void showMedianDays(int medianDays) {
		show(Arrays.asList("Median vehicles produced: " + medianDays));
	}

	@Override
	public void showDetailsDays(List<Integer> detailedDays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDays.size() + " days:");
		for(Integer day : detailedDays){
			details.add(day.toString());
		}
		show(details);

	}

	@Override
	public void showAverageDelays(int averageDelays) {
		show(Arrays.asList("Average delays: " + averageDelays));

	}

	@Override
	public void showMedianDelays(int medianDelays) {
		show(Arrays.asList("Median delays: " + medianDelays));

	}

	@Override
	public void showDetailedDelays(List<Delay> detailedDelays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDelays.size() + " delays:");
		for(Delay delay : detailedDelays){
			details.add(delay.toString() );
		}
		show(details);
	}

	@Override
	public void showAlgorithms(List<String> possible) {
		List<String> showAlgorithms = new ArrayList<String>();
		List<String> modifiedList = this.indexList(possible);
		showAlgorithms.add("All possible algorithms: " );
		showAlgorithms.addAll(modifiedList);
		this.show(showAlgorithms);

	}

	@Override
	public List<String> indexList(List<String> listToBeIndexed){
		List<String> finalResult = new ArrayList<String>();
		int index  = 1;
		for(String line: listToBeIndexed){
			finalResult.add(index + ": " + line );
			index++;
		}
		return finalResult;
	}

	@Override
	public void showBatches(Set<Set<VehicleOption>> batches) {
		List<String> sets = new ArrayList<String>();
		sets.add(0, "Possible Batches:");
		int i = 1;
		for (Iterator<Set<VehicleOption>> iterator = batches.iterator(); iterator.hasNext();) {
			Set<VehicleOption> s = (Set<VehicleOption>) iterator.next();
			String tmp = "";
			for (VehicleOption o : s) {
				tmp += o.toString() + ", ";
			}
			tmp = tmp.substring(0, tmp.length() - 2);
			sets.add(i, Integer.toString(i) + ". " + tmp);
			i++;
		}
		this.show(sets);
	}

	@Override
	public AssemblyLineState chooseStatus(List<AssemblyLineState> states, AssemblyLineState assemblyLineState) {
		showStatus(assemblyLineState);
		showPossibleStates(states);
		
		int customNumber = askNumber("Which operational state do you choose?") - 1;
		
		if (customNumber >0 && customNumber < states.size()) {
			return states.get(customNumber);
		} else {
			invalidAnswerPrompt();
			return chooseStatus(states, assemblyLineState);
		}
		
		
	}

	private void showPossibleStates(List<AssemblyLineState> states) {
		List<String> strings = new ArrayList<>();
		strings.add("Possible States");
		int i = 1;
		for(AssemblyLineState state: states){
			strings.add(i + ": " + state.toString());
			i++;
		}
		show(strings);
	}

	@Override
	public void showStatus(AssemblyLineState assemblyLineState) {
		List<String> strings = new ArrayList<String>();
		strings.add("Current status: " + assemblyLineState.toString());
		
		show(strings);
	}
}
