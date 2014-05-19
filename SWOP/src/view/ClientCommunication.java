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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#askAdvance()
	 */

	@Override
	public boolean askAdvance() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want advance the assemblyLine? Y/N",
				expected).equals("Y");
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#askContinue()
	 */

	@Override
	public boolean askContinue() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("Y", "N"));
		return askQuestionLoop("Do you want to continue? Y/N", expected)
				.equals("Y");
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#askDeadline()
	 */

	@Override
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#askFinished()
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseModel(java.util.Set)
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseOrder(java.util.List, java.util.List)
	 */

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
		}else { 
			show(Arrays.asList("You have no pending or completed orders"));
		}
		return Optional.absent();
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#choosePart(java.util.List)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseRole()
	 */

	@Override
	public String chooseRole() {
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("manager",
				"garageholder", "worker", "custom car shop manager"));
		return askQuestionLoop(
				"What's your role: manager, garageholder, worker or custom car shop manager?", expected);
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseTask(java.util.List)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseWorkBench(int, java.util.ArrayList)
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#getElapsedTime()
	 */

	@Override
	public int getElapsedTime() {
		return askNumber("How much time has passed? (minutes, type a negative number if this is the start of the day)");
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#getFlowControllerIndex(java.util.List)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#getIndex(int)
	 */
	@Override
	public int getIndex(int maxValue) {
		int answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to " + Integer.toString(maxValue));
		while (answer <= 0 || answer > maxValue) {
			invalidAnswerPrompt();
			answer = askNumber("Please enter an integer greater than 0 and lesser than or equal to" + Integer.toString(maxValue));
		}
		return answer;
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#getName()
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#getQuantity()
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#invalidAnswerPrompt()
	 */

	@Override
	public void invalidAnswerPrompt() {
		show(new ArrayList<String>(
				Arrays.asList("Sorry, that's not a valid response")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#invalidUserPrompt()
	 */

	@Override
	public void invalidUserPrompt() {
		show(new ArrayList<String>(Arrays.asList("You don't have any rights")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#logout()
	 */

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
	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAlgorithms(java.lang.String, java.util.List)
	 */
	@Override
	public void showAlgorithms(String current, List<String> possible) {
		List<String> currentAlgorithm = new ArrayList<String>();
		currentAlgorithm.add("Current algorithm: " + current );
		this.show(currentAlgorithm);
		this.showAlgorithms(possible);
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAlgorithmSwitched(java.lang.String)
	 */
	@Override
	public void showAlgorithmSwitched(String schedulingAlgorithmType) {
		show(Arrays.asList("Scheduling algorithm succesfully changed to: " + schedulingAlgorithmType));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAlgorithmSwitched(java.lang.String, java.util.List)
	 */
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAssemblyLine(domain.assembly.assemblyLine.IAssemblyLine)
	 */

	@Override
	public void showAssemblyLine(IAssemblyLine assemblyLine) {
		ArrayList<String> assemblyLineStrings = new ArrayList<String>();
		assemblyLineStrings.add(assemblyLine.getState().toString() );

		assemblyLineStrings.add(0, "current assemblyline:");

		List<IWorkBench> allWorkbenches = assemblyLine.getWorkBenches();

		for(IWorkBench workbench: allWorkbenches){
			assemblyLineStrings.add("Workbench: " + workbench.toString());
			assemblyLineStrings.add("Pending Tasks: ");
			assemblyLineStrings.addAll(getPendingTasks(workbench));
			assemblyLineStrings.add("Completed Tasks: ");
			assemblyLineStrings.addAll(getCompletedTasks(workbench));
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showBlockingBenches(java.util.ArrayList)
	 */

	@Override
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches) {
		show(new ArrayList<String>(
				Arrays.asList("AssemblyLine can't be advanced because of workbench "
						+ notCompletedBenches.toString())));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showChosenTask(domain.job.task.ITask)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showCompletedOrders(java.util.List)
	 */

	@Override
	public void showCompletedOrders(List<IOrder> completedOrders) {
		List<String> completedOrdersList = new ArrayList<String>();
		if (!completedOrders.isEmpty()) {
			completedOrdersList.add(0, "Your completed orders:" );
			for(IOrder order : completedOrders){
				completedOrdersList.add(order.toString() );
				completedOrdersList.add("\tcompletion time: " + order.getEstimatedTime().toString()  );
			}
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no completed Orders")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showCustomOrder(domain.clock.ImmutableClock)
	 */

	@Override
	public void showCustomOrder(ImmutableClock time) {
		show(new ArrayList<String>(Arrays.asList("Estimated completion time: " + time)));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showSpecificCustomTasks(java.util.List)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showInvalidModel()
	 */

	@Override
	public void showInvalidModel() {
		show(new ArrayList<String>(
				Arrays.asList("You created an invalid model, try again!")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showNoBatchesAvailable()
	 */
	@Override
	public void showNoBatchesAvailable() {
		show(new ArrayList<String>(Arrays.asList("No batches available")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showOrder(int, domain.vehicle.VehicleSpecification, java.util.List, domain.clock.ImmutableClock)
	 */

	@Override
	public void showOrder(int quantity, VehicleSpecification model, List<VehicleOption> chosenParts,
			ImmutableClock estimatedTime) {

		showOrder(quantity, model, chosenParts);
		show(Arrays.asList("Estimated time of completion :" + estimatedTime.toString() ));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showOrder(int, domain.vehicle.VehicleSpecification, java.util.List)
	 */
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
	/* (non-Javadoc)
	 * @see view.IClientCommunication#showOrderDetails(domain.order.order.IOrder)
	 */

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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showPendingOrders(java.util.List)
	 */

	@Override
	public void showPendingOrders(List<IOrder> pendingOrders) {
		List<String> pendingOrdersList = new ArrayList<String>();
		if (!pendingOrders.isEmpty()) {
			pendingOrdersList.add(0, "Your pending orders:" );
			for(IOrder order : pendingOrders){
				pendingOrdersList.add(order.toString() );
				pendingOrdersList.add("\tEstimated completion time: " + order.getEstimatedTime().toString()  );
			}
		} else
			show(new ArrayList<String>(
					Arrays.asList("You have no pending Orders")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showWorkBenchCompleted()
	 */

	@Override
	public void showWorkBenchCompleted() {
		show(new ArrayList<String>(
				Arrays.asList("All the tasks at this workbench are completed")));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseAssemblyLine(java.util.List)
	 */
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseWorkBench(java.util.List)
	 */
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showCustomTasks(java.util.Set)
	 */
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
			return customString.get(customNumber+1).substring(	//TODO check of het werkt
					customString.get(customNumber+1).indexOf(".") + 1);
		} else {
			invalidAnswerPrompt();
			return showCustomTasks(customTasks);
		}
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAverageDays(int)
	 */
	@Override
	public void showAverageDays(int averageDays) {
		show(Arrays.asList("Average vehicles produced: " + averageDays));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showMedianDays(int)
	 */
	@Override
	public void showMedianDays(int medianDays) {
		show(Arrays.asList("Median vehicles produced: " + medianDays));
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showDetailsDays(java.util.List)
	 */
	@Override
	public void showDetailsDays(List<Integer> detailedDays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDays.size() + " days:");
		for(Integer day : detailedDays){
			details.add(day.toString());
		}
		show(details);

	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAverageDelays(int)
	 */
	@Override
	public void showAverageDelays(int averageDelays) {
		show(Arrays.asList("Average delays: " + averageDelays));

	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showMedianDelays(int)
	 */
	@Override
	public void showMedianDelays(int medianDelays) {
		show(Arrays.asList("Median delays: " + medianDelays));

	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showDetailedDelays(java.util.List)
	 */
	@Override
	public void showDetailedDelays(List<Delay> detailedDelays) {
		List<String> details = new ArrayList<>();
		details.add("Details of the last " + detailedDelays.size() + " delays:");
		for(Delay delay : detailedDelays){
			details.add(delay.toString() );
		}
		show(details);
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showAlgorithms(java.util.List)
	 */
	@Override
	public void showAlgorithms(List<String> possible) {
		List<String> showAlgorithms = new ArrayList<String>();
		List<String> modifiedList = this.indexList(possible);
		showAlgorithms.add("All possible algorithms: " );
		showAlgorithms.addAll(modifiedList);
		this.show(showAlgorithms);

	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#indexList(java.util.List)
	 */
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showBatches(java.util.Set)
	 */
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

	/* (non-Javadoc)
	 * @see view.IClientCommunication#chooseStatus(java.util.List, domain.assembly.assemblyLine.AssemblyLineState)
	 */
	@Override
	public AssemblyLineState chooseStatus(List<AssemblyLineState> states, AssemblyLineState assemblyLineState) {
		showStatus(assemblyLineState);
		
		int customNumber = askNumber("Which operational state do you choose?") - 1;
		
		if (customNumber >0 && customNumber < states.size()) {
			return states.get(customNumber);
		} else {
			invalidAnswerPrompt();
			return chooseStatus(states, assemblyLineState);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see view.IClientCommunication#showStatus(domain.assembly.assemblyLine.AssemblyLineState)
	 */
	@Override
	public void showStatus(AssemblyLineState assemblyLineState) {
		AssemblyLineState[] states = AssemblyLineState.values();
		List<String> strings = new ArrayList<String>();
		strings.add(0, "Possible States:");
		int i = 1;
		for(AssemblyLineState state: states){
			strings.add(i + ": " + state.toString());
			i++;
		}
		strings.add("Current status: " + assemblyLineState.toString());
		
		show(strings);
	}
}
