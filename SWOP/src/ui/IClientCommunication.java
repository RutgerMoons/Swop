package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**TODO: fix deze documentatie
 * Interface for the userinterface. Any userinterface that is attached to this program has to implement all these methods somehow.
 *
 */
public interface IClientCommunication {
	
	/**
	 * Ask if the user wants to advance the assemblyline.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */
	public boolean askAdvance();
	
	/**
	 * Ask the user if he wants to continue.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */
	public boolean askContinue();
	
	/**
	 * Ask if the user is finished.
	 * @return
	 * 			A String that equals either "Y" (for yes) or "N" (for no).
	 */
	public String askFinished();
	
	/**
	 * Choose the car model the user wants to order.
	 * @param catalogue
	 * 			A Set which contains Strings that represent the names of all possible car models that can be ordered.
	 * @return
	 */
	public String chooseModel(Set<String> catalogue);
	
	public String choosePart(Set<String> parts);
	
	/**
	 * Let the user indicate which role he fulfills.
	 * @return
	 * 			A String that represents which role the user fulfills.
	 */
	public String chooseRole();
	
	/**
	 * Let the user indicate which task he wants to perform.
	 * @param tasks
	 * 			ArrayList that contains Strings. Each String represents the description of one of the tasks.
	 * @return
	 * 			A strictly positive integer.
	 * 			The integer 'n' that is returned indicates the user chooses the n'th element in the given list.
	 */
	public int chooseTask(ArrayList<String> tasks);
	
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
	public int chooseWorkBench(int numberOfWorkbenches, ArrayList<String> workbenches);
	
	/**
	 * Let the user indicate how much time has passed.
	 * @return
	 * 			An integer representing the elapsed time (in minutes).
	 */
	public int getElapsedTime();
	
	/**
	 * presents the user with all of his possible use cases 
	 * and the user indicates which use case to perform
	 */
	public int getFlowControllerIndex(List<String> accessRights);
	
	/**
	 * Asks the user to enter an integer 0 < integer <= maxValue
	 * If the answer isn't valid, the system informs the user and repeats the question,
	 * until a valid answer is given.
	 */
	public int getIndex(int maxValue);
	
	/**
	 * Get the users' name.
	 * @return
	 * 			A String that represents the user's name.
	 */
	public String getName();
	
	/**
	 * Let the user indicate how many cars he wants to order.
	 * @return
	 * 			a positive integer representing the quantity
	 */
	public int getQuantity();
	
	/**
	 * Notify the user that the answer he has given is not a valid answer.
	 */
	public void invalidAnswerPrompt();
	
	/**
	 * Notify the user that the answer he has given is not a valid user.
	 */
	public void invalidUserPrompt();
	
	public void logout();
	
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
	public void showAssemblyLine(String assemblyline, String tense);
	
	/**
	 * Show the user the currently used Scheduling Algorithm and all the possible Scheduling Algorithms
	 */
	public void showAlgorithms(String currentAlgorithm, ArrayList<String> algorithms);
	
	/**
	 * Show the user which benches are keeping the assemblyline from advancing.
	 * @param notCompletedBenches
	 * 			A list of integers. Each integer represents the number of a workbench that is blocking the assemblyline.
	 */
	public void showBlockingBenches(ArrayList<Integer> notCompletedBenches);
	
	/**
	 * Show the user the task he has chosen.
	 * @param task
	 * 			A String that represents the task. 
	 * 			This String contains the task description, "Required actions:", and all the actions required.
	 * 			Each of these elements are separated by a comma.
	 */
	public void showChosenTask(String task);
	
	/**
	 * Show the user's completed orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity and the name of the model, separated by comma's.
	 */
	public void showCompletedOrders(ArrayList<String> completedOrders);
	
	//estimatedTime kan -1 zijn!! --> dan moet de estimatedTime niet geprint worden 
	/**
	 * Show the user the order he is about to place.
	 * @param quantity
	 * 			An integer representing the quantity of cars the user is about to order.
	 * @param realModel 
	 * @param chosenParts
	 * 			A String representing the name of the carmodel the user is about to order.
	 * @param estimatedTime
	 * 			The estimated completion time, represented by two integers: the day and the time (in minutes).
	 * 			If the estimated completion time == -1, the completion time can't be shown.
	 */
	public void showOrder(int quantity, String realModel, List<String> chosenParts, String estimatedTime);
	
	/**
	 * Show the user's pending orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity, the name of the model and the estimated time, all separated by comma's.
	 */
	public void showPendingOrders(ArrayList<String> pendingOrders);

	/**
	 * Notify the user that all the tasks at the workbench he's working on are completed.
	 */
	public void showWorkBenchCompleted();

	public void showInvalidModel();
	
	public String chooseOrder(List<String> pendingOrders, List<String> completedOrders);

	public void showOrderDetails(List<String> orderDetails);

	public String showCustomTasks(List<String> tasks);

	public String askDeadline();
	
	public void showAlgorithmSwitched(String type);

	public void showCustomOrder(String time);
	
	public void showStatistics(List<String> statistics);
	
	
}
