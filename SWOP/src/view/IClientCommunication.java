package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.ImmutableClock;
import domain.job.task.ITask;
import domain.order.Delay;
import domain.order.order.IOrder;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;

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
	 * Ask the user for a deadline.
	 */

	public int askDeadline();

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

	/**
	 * Lets the user choose an order out of all his pending/completed orders.
	 */

	public Optional<IOrder> chooseOrder(List<IOrder> pendingOrders,
			List<IOrder> completedOrders);

	/**
	 * Lets the user choose a VehicleOption when he is putting his model together.
	 */

	public Optional<VehicleOption> choosePart(List<VehicleOption> parts);

	/**
	 * Let the user indicate which role he fulfills.
	 * @return
	 * 			A String that represents which role the user fulfills.
	 */

	public String chooseRole();

	/**
	 * Let the user indicate which task he wants to perform.
	 * @param tasksAtWorkbench
	 * 			ArrayList that contains Strings. Each String represents the description of one of the tasks.
	 * @return
	 * 			A strictly positive integer.
	 * 			The integer 'n' that is returned indicates the user chooses the n'th element in the given list.
	 */

	public ITask chooseTask(List<ITask> tasksAtWorkbench);

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
			ArrayList<String> workbenches);

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

	/**
	 * log the current user out
	 */

	public void logout();

	/**
	 * Show the user the currently used Scheduling Algorithm and all the possible Scheduling Algorithms
	 */
	public void showAlgorithms(String current, List<String> possible);

	/**
	 * Notify the user that the scheduling algorithm was successfully switched.
	 */
	public void showAlgorithmSwitched(String schedulingAlgorithmType);

	/**
	 * Notify the user that the scheduling algorithm was successfully switched using the given batch.
	 */
	public void showAlgorithmSwitched(String schedulingAlgorithmType,
			List<VehicleOption> vehicleOptionsChosenForBatch);

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

	public void showAssemblyLine(IAssemblyLine assemblyLine);

	/**
	 * Show the user which benches are keeping the assemblyline from advancing.
	 * @param notCompletedBenches
	 * 			A list of integers. Each integer represents the number of a workbench that is blocking the assemblyline.
	 */

	public void showBlockingBenches(
			ArrayList<Integer> notCompletedBenches);

	/**
	 * Show the user the task he has chosen.
	 * @param chosenTask
	 * 			A String that represents the task. 
	 * 			This String contains the task description, "Required actions:", and all the actions required.
	 * 			Each of these elements are separated by a comma.
	 */

	public void showChosenTask(ITask chosenTask);

	/**
	 * Show the user's completed orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity and the name of the model, separated by comma's.
	 */

	public void showCompletedOrders(List<IOrder> completedOrders);

	/**
	 * Show a custom order with the given estimated completion time.
	 */

	public void showCustomOrder(ImmutableClock time);

	/**
	 * Show the given custom tasks and let the user choose one.
	 */

	public IVehicle showSpecificCustomTasks(List<IVehicle> vehicles);

	/**
	 * Notify the user that the carModel he has put togehther is not a valid model.
	 */

	public void showInvalidModel();

	/**
	 * Shows the user there are no specification batches to switch the scheduling algorithm to.
	 */
	public void showNoBatchesAvailable();

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

	public void showOrder(int quantity, VehicleSpecification model,
			List<VehicleOption> chosenParts, ImmutableClock estimatedTime);

	public void showOrder(int quantity,
			VehicleSpecification realModel, List<VehicleOption> chosenParts);

	/**
	 * Show the details of the given order.
	 */

	public void showOrderDetails(IOrder order);

	/**
	 * Show the user's pending orders.
	 * @param pendingOrders
	 * 			An ArrayList of Strings.
	 * 			Each String in this ArrayList represents an order. 
	 * 			It contains the quantity, the name of the model and the estimated time, all separated by comma's.
	 */

	public void showPendingOrders(List<IOrder> pendingOrders);

	/**
	 * Notify the user that all the tasks at the workbench he's working on are completed.
	 */

	public void showWorkBenchCompleted();

	public IAssemblyLine chooseAssemblyLine(
			List<IAssemblyLine> allAssemblyLines);

	public IWorkBench chooseWorkBench(List<IWorkBench> workbenches);

	public String showCustomTasks(Set<String> customTasks);

	public void showAverageDays(int averageDays);

	public void showMedianDays(int medianDays);

	public void showDetailsDays(List<Integer> detailedDays);

	public void showAverageDelays(int averageDelays);

	public void showMedianDelays(int medianDelays);

	public void showDetailedDelays(List<Delay> detailedDelays);

	public void showAlgorithms(List<String> possible);

	public List<String> indexList(List<String> listToBeIndexed);

	public void showBatches(Set<Set<VehicleOption>> batches);

	public AssemblyLineState chooseStatus(
			List<AssemblyLineState> states, AssemblyLineState assemblyLineState);

	public void showStatus(AssemblyLineState assemblyLineState);

}