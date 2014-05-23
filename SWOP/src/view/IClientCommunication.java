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

/**
 * An interface describing every function needed to implement to communicate
 * with the user.
 */
public interface IClientCommunication {

	/**
	 * Ask if the user wants to advance the AssemblyLine.
	 * 
	 * @return
	 * 			True if the string equals either "Y" (for yes) or "N" (for no).
	 */
	public boolean askAdvance();

	/**
	 * Ask the user if he wants to continue.
	 * 
	 * @return
	 * 			True if the string equals either "Y" (for yes) or "N" (for no).
	 */
	public boolean askContinue();

	/**
	 * Ask the user for a deadline.
	 * 
	 * @return 	
	 * 			The amount of minutes until the deadline is reached
	 */
	public int askDeadline();

	/**
	 * Ask if the user is finished.
	 * 
	 * @return
	 * 			True if the string equals either "Y" (for yes) or "N" (for no).
	 */
	public String askFinished();

	/**
	 * Lets the user choose the vehicle he wants to order.
	 * 
	 * @param 	catalogue
	 * 			A Set which contains Strings that represent the names of all possible vehicles that can be ordered.
	 */
	public String chooseModel(Set<String> catalogue);

	/**
	 * Lets the user choose an order out of all his pending/completed orders.
	 * 
	 * @param	pendingOrders
	 * 			The pending orders of the user
	 * 
	 * @param	completedOrders 
	 * 			The completed orders of the user
	 */
	public Optional<IOrder> chooseOrder(List<IOrder> pendingOrders,
			List<IOrder> completedOrders);

	/**
	 * Lets the user choose a VehicleOption when he is putting his Vehicle together.
	 * 
	 * @param	parts
	 * 			The list of VehicleOptions where the user can choose a VehicleOption from
	 * 			
	 */
	public Optional<VehicleOption> choosePart(List<VehicleOption> parts);

	/**
	 * Lets the user indicate which role he fulfills.
	 * 
	 * @return
	 * 			A String that represents which role the user fulfills.
	 */
	public String chooseRole();

	/**
	 * Lets the user choose the task he wants to perform.
	 * 
	 * @param 	tasksAtWorkbench
	 * 			ArrayList that contains Tasks. 
	 */
	public ITask chooseTask(List<ITask> tasksAtWorkbench);

	/**
	 * Lets the user indicate which workbench he's working at.
	 * 
	 * @param 	numberOfWorkBenches
	 * 			Integer that represents how many workbenches the user can choose from.
	 * 
	 * @param 	workbenches
	 * 			ArrayList that contains Strings. Each String represents the name of one of the workbenches.
	 * 
	 * @return
	 * 			A strictly positive integer, indicating which WorkBench it chooses. 
	 */
	public int chooseWorkBench(int numberOfWorkBenches,
			ArrayList<String> workbenches);

	/**
	 * Lets the user indicate how much time has passed.
	 * 
	 * @return
	 * 			An integer representing the elapsed time (in minutes).
	 */
	public int getElapsedTime();

	/**
	 * Presents the user with all of his possible use cases. The user
	 * chooses which use case he wants to execute.
	 * 
	 * @param	accessRights
	 * 			A list with all the user's access rights 
	 */
	public int getFlowControllerIndex(List<String> accessRights);

	/**
	 * Asks the user to enter an integer between zero and the given maxValue.
	 * If the answer isn't valid, the system informs the user and repeats the question,
	 * until a valid answer is given.
	 * 
	 * @param	maxValue
	 * 			The upper bound
	 */
	public int getIndex(int maxValue);

	/**
	 * Get the user's name.
	 * 
	 * @return
	 * 			A String that represents the user's name.
	 */
	public String getName();

	/**
	 * Lets the user indicate how many cars he wants to order.
	 * 
	 * @return
	 * 			A positive integer representing the quantity
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
	 * Logs the current user out.
	 */
	public void logout();

	/**
	 * Show the user the currently used Scheduling Algorithm's name and 
	 * the names of all the possible Scheduling Algorithms.
	 * 
	 * @param	current
	 * 			The scheduling algorithm currently used by the AssemblyLines
	 * 
	 * @param	possible
	 * 			A list with all the possible scheduling algorithms including the current 
	 * 			scheduling algorithm
	 */
	public void showAlgorithms(String current, List<String> possible);

	/**
	 * Notify the user that the scheduling algorithm was successfully switched.
	 * 
	 * @param	schedulingAlgorithmType
	 * 			The name of the scheduling algorithm the user has chosen
	 */
	public void showAlgorithmSwitched(String schedulingAlgorithmType);

	/**
	 * Notify the user that the scheduling algorithm was successfully switched using the given batch.
	 * 
	 * @param	schedulingAlgorithmType
	 * 			The name of the scheduling algorithm the user has chosen
	 * 
	 * @param	vehicleOptionsChosenForBatch
	 * 			The list of VehicleOptions who have priority in batch
	 */
	public void showAlgorithmSwitched(String schedulingAlgorithmType,
			List<VehicleOption> vehicleOptionsChosenForBatch);

	/**
	 * Show the user the AssemblyLine.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine needed to be shown		
	 */
	public void showAssemblyLine(IAssemblyLine assemblyLine);

	/**
	 * Show the user the Task he has chosen.
	 * 
	 * @param 	chosenTask
	 * 			The Task to be shown
	 */
	public void showChosenTask(ITask chosenTask);

	/**
	 * Show the user's completed orders.
	 * 
	 * @param 	completedOrders
	 * 			A list of IOrders
	 */
	public void showCompletedOrders(List<IOrder> completedOrders);

	/**
	 * Show a custom order with the given estimated completion time.
	 */
	public void showCustomOrder(ImmutableClock time);

	/**
	 * Show the given custom tasks and lets the user choose one.
	 * 
	 * @param	vehicles
	 * 			The list with all the possible custom tasks that can be performed on a Vehicle
	 */
	public IVehicle showSpecificCustomTasks(List<IVehicle> vehicles);

	/**
	 * Notify the user that the Vehicle he has put together is not a valid model.
	 */
	public void showInvalidModel();

	/**
	 * Show the user there are no specification batches to switch the scheduling algorithm to.
	 */
	public void showNoBatchesAvailable();

	/**
	 * Show the user the order he is about to place.
	 * 
	 * @param 	quantity
	 * 			An integer representing the quantity of cars the user is about to order
	 * 
	 * @param 	model
	 * 			The VehicleSpecification the user is about to order 
	 * 
	 * @param 	chosenParts
	 * 			A list with all parts the user has chosen
	 *
	 * @param 	estimatedTime
	 * 			The estimated completion time, represented by two integers: the day and the time (in minutes).
	 * 			If the estimated completion time == -1, the completion time can't be shown.
	 */
	public void showOrder(int quantity, VehicleSpecification model,
			List<VehicleOption> chosenParts, ImmutableClock estimatedTime);

	/**
	 * Show the user everything he has chosen.
	 * 
	 * @param 	quantity
	 * 			An integer representing the quantity of cars the user is about to order
	 * 
	 * @param 	realModel
	 * 			The VehicleSpecification the user is about to order 
	 * 
	 * @param 	chosenParts
	 * 			A list with all the parts the user has chosen
	 */
	public void showOrder(int quantity, VehicleSpecification realModel,
			List<VehicleOption> chosenParts);

	/**
	 * Show the details of the given order.
	 */
	public void showOrderDetails(IOrder order);

	/**
	 * Show the user's pending orders.
	 * 
	 * @param 	pendingOrders
	 * 			Show the user's pending orders 
	 */
	public void showPendingOrders(List<IOrder> pendingOrders);

	/**
	 * Notify the user that all the tasks at the workbench he's working on are completed.
	 */
	public void showWorkBenchCompleted();

	/**
	 * Lets the user choose an AssemblyLine out of a list of AssemblyLines.
	 * 
	 * @param 	allAssemblyLines
	 * 			List with all the available assemblyLines.
	 */
	public IAssemblyLine chooseAssemblyLine(List<IAssemblyLine> allAssemblyLines);

	/**
	 * Lets the user choose a WorkBench out of a list of WorkBenches.
	 * 
	 * @param 	workbenches
	 * 			List with all the available workbenches
	 */
	public IWorkBench chooseWorkBench(List<IWorkBench> workbenches);

	/**
	 * Shows all the Tasks that can be applied for a CustomOrder.
	 * 
	 * @param 	customTasks
	 * 			Set with the names of all Tasks 
	 */
	public String showCustomTasks(Set<String> customTasks);

	/**
	 * Show the average of produced Vehicles in a day.
	 * 
	 * @param 	averageDays
	 * 			The value 
	 */
	public void showAverageDays(int averageDays);

	/**
	 * Show the median of produced Vehicles in a day.
	 * 
	 * @param 	medianDays
	 * 			The value
	 */
	public void showMedianDays(int medianDays);

	/**
	 * Show some days in details.
	 * 
	 * @param 	detailedDays
	 * 			A list with all the details, meaning the value for each day
	 */
	public void showDetailsDays(List<Integer> detailedDays);

	/**
	 * Show the average of Delays.
	 * 
	 * @param 	averageDelays
	 * 			The value 
	 */
	public void showAverageDelays(int averageDelays);

	/**
	 * Show the median of Delays.
	 * 
	 * @param 	medianDelays
	 * 			The value
	 */
	public void showMedianDelays(int medianDelays);

	/**
	 * Show the latest Delays in detail.
	 * 
	 * @param 	detailedDelays
	 * 			A list with all the Delays
	 */
	public void showDetailedDelays(List<Delay> detailedDelays);

	/**
	 * Show all possible SchedulingAlgorithms.
	 * 
	 * @param 	possible
	 * 			A list with the names of the possible Scheduling Algorithms
	 */
	public void showAlgorithms(List<String> possible);

	/**
	 * Method to index a given list.
	 * 
	 * @param 	listToBeIndexed
	 * 			List that needs to be indexed
	 * 
	 * @return	returns the same list but then indexed
	 */
	public List<String> indexList(List<String> listToBeIndexed);

	/**
	 * Show all the options for SchedulingAlgorithmBatch.
	 * 
	 * @param 	batches
	 * 			A powerset with all the possible sets of VehicleOptions
	 */
	public void showBatches(Set<Set<VehicleOption>> batches);

	/**
	 * Lets the user choose the new AssemblyLineState for an AssemblyLine.
	 * 
	 * @param 	states
	 * 			The possible states
	 * 
	 * @param 	assemblyLineState
	 * 			The current state of an AssemblyLine
	 */
	public AssemblyLineState chooseStatus(List<AssemblyLineState> states,
			AssemblyLineState assemblyLineState);

	/**
	 * Show the current state of the AssemblyLine.
	 * 
	 * @param 	assemblyLineState
	 * 			The state of the AssemblyLine
	 */
	public void showStatus(AssemblyLineState assemblyLineState);
}