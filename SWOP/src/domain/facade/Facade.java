package domain.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import view.CustomVehicleCatalogueFiller;
import view.VehicleSpecificationCatalogueFiller;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableCollection.Builder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.UnmodifiableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.job.IAction;
import domain.job.ITask;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.order.CustomOrder;
import domain.order.Delay;
import domain.order.IOrder;
import domain.order.OrderBook;
import domain.order.StandardOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.scheduling.Scheduler;
import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserBook;
import domain.users.UserFactory;
import domain.vehicle.CustomVehicle;
import domain.vehicle.CustomVehicleCatalogue;
import domain.vehicle.IVehicleOption;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.VehicleSpecificationCatalogue;

/**
 * This class is the layer between the Domain model and the UI. The UI can only
 * call methods on this class to do someting in the domain model.
 * 
 */
public class Facade {

	private AssemblyLine assemblyLine;
	private AssemblyLineObserver assemblyLineObserver;
	private VehicleSpecificationCatalogue vehicleSpecificationCatalogue;
	private Clock clock;
	private ClockObserver clockObserver;
	private CustomVehicleCatalogue customVehicleCatalogue;
	private Logger logger;

	private OrderBook orderBook;
	private PartPicker picker;

	private UserBook userBook;
	private UserFactory userFactory;

	/**
	 * Create a new Facade. This initializes automatically all the necessary
	 * resources.
	 * 
	 * @param bindingRestrictions
	 *            The list of BindingRestrictions the facade has to deal with.
	 * @param optionalRestrictions
	 *            The list of OptionalRestrictions the facade has to deal with.
	 * */
	public Facade(Set<BindingRestriction> bindingRestrictions,
			Set<OptionalRestriction> optionalRestrictions) {
		this.clock = new Clock();

		this.clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);

		this.assemblyLine = new AssemblyLine(clockObserver,
				clock.getUnmodifiableClock());
		this.assemblyLineObserver = new AssemblyLineObserver();
		this.assemblyLine.attachObserver(assemblyLineObserver);

		this.logger = new Logger(2, clockObserver, assemblyLineObserver);

		this.vehicleSpecificationCatalogue = new VehicleSpecificationCatalogue();
		this.customVehicleCatalogue = new CustomVehicleCatalogue();
		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		this.userFactory = new UserFactory();
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);

		VehicleSpecificationCatalogueFiller carModelFiller = new VehicleSpecificationCatalogueFiller();
		for (VehicleSpecification model : carModelFiller.getInitialModels()) {
			vehicleSpecificationCatalogue.addModel(model);
		}

		CustomVehicleCatalogueFiller customCarModelFiller = new CustomVehicleCatalogueFiller();
		Multimap<String, CustomVehicle> customModels = customCarModelFiller
				.getInitialModels();
		for (String model : customModels.keySet()) {
			for (CustomVehicle customModel : customModels.get(model)) {
				customVehicleCatalogue.addModel(model, customModel);
			}
		}
	}

	/**
	 * Add a Part to the CarModel that is being built.
	 * 
	 * @param part
	 * 			The part you want to add to the model.
	 */
	public void addPartToModel(IVehicleOption part) {
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		try {
			picker.getModel().addCarPart(option);
		} catch (AlreadyInMapException e) {
		}
	}

	/**
	 * Advance the assemblyline.
	 * 
	 * @throws UnmodifiableException
	 *             If an order on one of the workbenches is an ImmutableOrder
	 * @throws NoSuitableJobFoundException
	 *             If there are no suitable jobs to be put on the assemblyline.
	 */
	public void advanceAssemblyLine() throws UnmodifiableException,
			NoSuitableJobFoundException {
		assemblyLine.advance();
	}

	/**
	 * Advance the clock
	 * 
	 * @param time
	 *            How much time the clock has to be advanced.
	 */
	public void advanceClock(int time) {
		clock.advanceTime(time);

	}

	/**
	 * Check if the assemblyline can advance.
	 */
	public boolean canAssemblyLineAdvance() {
		return assemblyLine.canAdvance();
	}

	/**
	 * Complete the task the user has chosen to complete. The method
	 * automatically advances the AssemblyLine if it can advance.
	 * 
	 * @param workBenchIndex
	 *            The index of the workbench the job from which the task has to
	 *            be completed is on.
	 * @param taskIndex
	 *            The index of the Task in the Job on the Workbench.
	 * @param time
	 *            The time the clock has to be advanced.
	 * @throws UnmodifiableException
	 *             If the Task in the job is an ImmutableTask.
	 * @throws NoSuitableJobFoundException
	 *             If there are no suitable jobs to be put on the assemblyline.
	 */
	public void completeChosenTaskAtChosenWorkBench(ITask task, int time) throws UnmodifiableException,
			NoSuitableJobFoundException {

		for (IAction action : task.getActions()) {
			action.setCompleted(true);
		}

		if (this.canAssemblyLineAdvance()) {
			this.advanceAssemblyLine();
		}
		clock.advanceTime(time);
	}

	/**
	 * Create a new User and put it in the UserBook. The method automatically
	 * logs the newly created user in.
	 * 
	 * @param userName
	 *            The newly chosen userName.
	 * @param role
	 *            The role of the User.
	 * @throws IllegalArgumentException
	 *             If an illegal username or role has been inserted.
	 */
	public void createAndAddUser(String userName, String role)
			throws IllegalArgumentException {
		User currentUser = userFactory.createUser(userName, role);
		this.userBook.addUser(currentUser);
		try {
			this.userBook.login(userName);
		} catch (RoleNotYetAssignedException r) {
			System.err
					.println("Something went wrong at login, this shouldn't happen.");
		}
	}

	/**
	 * Create a new CarModel that has to be created from scratch.
	 * 
	 * @param realModel
	 *            The specification the PartPicker has to take into account when
	 *            creating the CarModel
	 */
	public void createNewModel(VehicleSpecification realModel) {
		picker.setNewModel(realModel);
	}

	/**
	 * Get the accessrights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return this.userBook.getCurrentUser().getAccessRights();
	}

	/**
	 * Get the AssemblyLine as a String.
	 */
	public String getAssemblyLineAsString() {
		return assemblyLine.toString();
	}

	/**
	 * Get the workbenches which are blocking the AssemblyLine from advancing.
	 * @return
	 * 			A list of indexes of the workbenches that are blocking the AssemblyLine from advancing.
	 */
	public List<Integer> getBlockingWorkBenches() {
		return assemblyLine.getBlockingWorkBenches();
	}

	/**
	 * Get the CarModelSpecification from the catalogue.
	 * @param specificationName
	 * 			The name of the specification to retrieve the specification.
	 * @throws IllegalArgumentException
	 * 			If no CarModelSpecification is found with the given name.
	 */
	public VehicleSpecification getCarModelSpecificationFromCatalogue(String specificationName) {
		return vehicleSpecificationCatalogue.getCatalogue().get(specificationName);
	}

	/**
	 * Get a set of all the CarModelSpecifications that the Catalogue has.
	 */
	public Set<String> getCarModelSpecifications() {
		return this.vehicleSpecificationCatalogue.getCatalogue().keySet();
	}

	/**
	 * Get a set off all the CarPartTypes that are available.
	 */
	public List<VehicleOptionCategory> getCarPartTypes() {
		return Arrays.asList(VehicleOptionCategory.values());
	}

	/**
	 * Get a list of the completed orders of the user that is currently logged in. 
	 */
	public List<IOrder> getCompletedOrders() {
		return ImmutableList.copyOf(orderBook.getCompletedOrders().get(userBook.getCurrentUser().getName()));
	}

	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public Scheduler getCurrentSchedulingAlgorithmAsString() {
		return this.assemblyLine.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get a list of available CustomTasks from the catalogue. 
	 * @return
	 */
	public Set<String> getCustomTasks() {
		return customVehicleCatalogue.getCatalogue().keySet();
	}

	/**
	 * Get a list of details of an order.
	 * @param orderString
	 * 			The string representing the order.
	 */
	public List<String> getOrderDetails(String orderString) {
		IOrder chosenOrder = null;
		for (IOrder order : orderBook.getPendingOrders().values()) {
			if (order.toString().equals(orderString)) {
				chosenOrder = order;
			}
		}
		for (IOrder order : orderBook.getCompletedOrders().values()) {
			if (order.toString().equals(orderString)) {
				chosenOrder = order;
			}
		}

		List<String> orderDetails = new ArrayList<>();
		if (chosenOrder != null) {
			orderDetails.add("Orderdetails:");
			orderDetails.add(chosenOrder.getQuantity() + " "
					+ chosenOrder.getDescription());

			String carDetails = "Chosen carOptions: ";
			for (VehicleOptionCategory category : chosenOrder.getDescription()
					.getCarParts().keySet()) {
				carDetails += chosenOrder.getDescription().getCarParts()
						.get(category)
						+ " ";
			}
			orderDetails.add(carDetails);

			orderDetails.add("Order Time: "
					+ chosenOrder.getOrderTime().toString());
			try {
				orderDetails.add("(Expected) Completion Time: "
						+ chosenOrder.getDeadline().toString());
			} catch (NotImplementedException e) {
				orderDetails.add("(Expected) Completion Time: "
						+ chosenOrder.getEstimatedTime().toString());
			}
		}
		return orderDetails;
	}

	/**
	 * Get the still available Parts for the model that is being built. 
	 * @param type
	 * 			The type of the parts that has to be selected. 
	 */
	public Set<VehicleOption> getParts(VehicleOptionCategory type) {
		return ImmutableSet.copyOf(picker.getStillAvailableCarParts(type));
	}
	
	/**
	 * Get a list of pending orders of the user that is currently logged in.
	 */
	public ArrayList<String> getPendingOrders() {
		ArrayList<String> pendingOrders = new ArrayList<String>();
		Collection<IOrder> orders = orderBook.getPendingOrders().get(
				userBook.getCurrentUser().getName());
		if (this.orderBook.getPendingOrders().containsKey(
				userBook.getCurrentUser().getName())
				&& !this.orderBook.getPendingOrders()
						.get(userBook.getCurrentUser().getName()).isEmpty()) {
			for (IOrder order : orders) {
				pendingOrders.add(order.toString());
			}
		}
		return pendingOrders;

	}

	/**
	 * Returns a list of all the possible scheduling algorithms as Strings.
	 */
	public ArrayList<String> getPossibleSchedulingAlgorithms() {
		return this.assemblyLine.getPossibleSchedulingAlgorithms();
	}

	/**
	 * Get a list of specific Custom Tasks on the basis of the taskDescription.
	 * The taskDescription is like "spraying car bodies" or "installing custom seats"
	 * 
	 */
	public List<String> getSpecificCustomTasks(String taskDescription) {
		List<String> tasks = new ArrayList<>();
		for (CustomVehicle model : customVehicleCatalogue.getCatalogue().get(
				taskDescription)) {
			tasks.add(model.toString());
		}
		return tasks;
	}

	/**
	 * Get all of the available statistics.
	 * 
	 * @return A List containing following statistics (in that order): average
	 *         #cars produced / day median #cars produced / day #cars produced 2
	 *         days ago #cars produced 1 day ago average delay median delay
	 *         second last delay last delay
	 */
	public List<String> getStatistics() {
		int detail = 2; // aantal dagen waarvan gedetailleerde/exacte numbers
						// voor moeten worden weeregegeven

		List<String> statistics = new ArrayList<String>();

		statistics.add("" + logger.averageDays());
		statistics.add("" + logger.medianDays());

		List<Integer> detailedDays = new ArrayList<Integer>(
				logger.getDetailedDays());
		while (detailedDays.size() < detail) { // als er geen statistics van
												// genoeg dagen terug zijn: voeg
												// 0 toe voor die dag(en)
			detailedDays.add(0, 0);
		}
		for (int i = 0; i < detail; i++) {
			statistics.add("" + detailedDays.get(i));
		}

		statistics.add("" + logger.averageDelays());
		statistics.add("" + logger.medianDelays());

		List<Delay> detailedDelays = new ArrayList<Delay>(
				logger.getDetailedDelays());
		while (detailedDelays.size() < detail) { // als er niet genoeg delays
													// zijn: voeg null toe voor
													// die dag(en)
			detailedDelays.add(null);
		}
		for (int i = 0; i < detail; i++) {
			if (detailedDelays.get(i) == null) {
				statistics.add("none");
			} else {
				statistics.add(detailedDelays.get(i).toString());
			}
		}

		return statistics;
	}

	/**
	 * Get a list of tasks of a Workbench, specified by the workBenchIndex.
	 */
	public ArrayList<String> getTasksOfChosenWorkBench(int workBenchIndex) {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(
				workBenchIndex);
		ArrayList<String> tasks = new ArrayList<String>();
		for (ITask task : workbench.getCurrentTasks()) {
			if (!task.isCompleted()) {
				tasks.add(task.toString());
			}
		}
		return tasks;
	}

	/**
	 * Get the names of the WorkBenches.
	 */
	public ArrayList<String> getWorkBenchNames() {
		ArrayList<String> workbenches = new ArrayList<String>();
		for (IWorkBench w : this.assemblyLine.getWorkbenches()) {
			workbenches.add(w.getWorkbenchName());
		}
		return workbenches;
	}
	
	/**
	 * Login with a userName.
	 * @param userName
	 * 			The name of the user.
	 * @throws RoleNotYetAssignedException
	 * 			If it's the first time that this user logs in.
	 */
	public void login(String userName) throws RoleNotYetAssignedException {
		userBook.login(userName);
	}

	/**
	 * Log the current user out.
	 */
	public void logout() {
		userBook.logout();
	}

	/**
	 * Create and schedule a CustomOrder.
	 * 
	 * @param model
	 * 			The name of the CustomCarModel that has to be ordered.
	 * @param deadline
	 * 			The deadline of the CustomOrder
	 */
	public String processCustomOrder(String model, String deadline) throws IllegalArgumentException{
		CustomVehicle customModel = null;
		for (CustomVehicle custom : customVehicleCatalogue.getCatalogue()
				.values()) {
			if (custom.toString().equals(model)) {
				customModel = custom;
			}
		}
		if(customModel==null){
			throw new IllegalArgumentException();
		}
		String[] split = deadline.split(",");
		int days = Integer.parseInt(split[0]);
		int hours = Integer.parseInt(split[1]);
		int minutes = Integer.parseInt(split[2]);
		minutes += hours * 60;
		ImmutableClock deadlineClock = new ImmutableClock(days, minutes);
		CustomOrder order = new CustomOrder(
				userBook.getCurrentUser().getName(), customModel, 1,
				clock.getUnmodifiableClock(), deadlineClock);
		try {
			orderBook.addOrder(order, clock.getUnmodifiableClock());
		} catch (UnmodifiableException | NotImplementedException e) {
		}
		return order.getEstimatedTime().toString();
	}

	/**
	 * Create and schedule a standard order.
	 * @param quantity
	 * 			The amount of cars you want to order.
	 * @throws UnmodifiableException
	 * 			This is never thrown here.
	 * @throws IllegalStateException
	 * 			If the CarModel isn't a valid model.
	 * @throws NotImplementedException
	 * 			This is never thrown here.
	 */
	public String processOrder(int quantity) throws UnmodifiableException,
			IllegalStateException, NotImplementedException {
		Vehicle vehicle = picker.getModel();

		if (!vehicle.isValid())
			throw new IllegalStateException();
		StandardOrder order = new StandardOrder(userBook.getCurrentUser()
				.getName(), vehicle, quantity, clock.getUnmodifiableClock());
		this.orderBook.addOrder(order, clock.getUnmodifiableClock());
		return order.getEstimatedTime().toString();
	}

	/**
	 * Advance the clock to the next day.
	 */
	public void startNewDay() {
		clock.startNewDay();
	}

	/**
	 * Switches the scheduling algorithm to use Fifo.
	 */
	public void switchToFifo() {
		this.assemblyLine.switchToFifo();
	}

	/**
	 * Switches the scheduling algorithm to use Batch with the given List of
	 * CarOptions.
	 */
	public void switchToBatch(List<VehicleOption> batch) {
		this.assemblyLine.switchToBatch(batch);
	}
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return this.assemblyLine.getAllCarOptionsInPendingOrders();
	}
}
