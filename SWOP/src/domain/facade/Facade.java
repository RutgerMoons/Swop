package domain.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.exception.RoleNotYetAssignedException;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.order.Delay;
import domain.order.order.IOrder;
import domain.order.order.UnmodifiableOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicle.UnmodifiableVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing the layer between the Domain model and the controllers. The controllers can only
 * call methods in this class to do something in the domain model.
 */
public class Facade {

	private Company company;

	/**
	 * Create a new Facade. This initializes automatically all the necessary
	 * resources.
	 *
	 * @param	company
	 * 			The instantiation of a Company object to which the facade communicates everything to
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the given company is null
	 * */
	public Facade(Company company) {
		if (company == null) {
			throw new IllegalArgumentException();
		}
		this.company = company;
	}

	/**
	 * Add a Part to the Vehicle that is being ordered.
	 * 
	 * @param 	part
	 * 			The VehicleOption you want to add to the model
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the part is null
	 */
	public void addPartToVehicle(VehicleOption part) {
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		company.addPartToModel(option);
	}

	/**
	 * Communicate to the company that at a given AssemblyLine at a given WorkBench 
	 * a given Task is completed in a given time.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine at which the worker stands
	 * 
	 * @param 	workbench
	 *           The WorkBench where the given Task is completed
	 *           
	 * @param 	task
	 *          The Task that is completed by the worker
	 *            
	 * @param 	time
	 *          The amount of minutes needed to complete the Task
	 *          
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null
	 */
	public void completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, 
			ITask task, ImmutableClock time) {
		Task modifiableTask = new Task(task.getTaskDescription());
		modifiableTask.setActions(task.getActions());

		this.company.completeChosenTaskAtChosenWorkBench(assemblyLine, workbench, modifiableTask, time);
	}

	/**
	 * Create a new User and put it in the UserBook. The method does not automatically
	 * log the newly created user in.
	 * 
	 * @param 	userName
	 *          The newly chosen userName
	 *            
	 * @param 	role
	 *          The role of the User
	 *          
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null or is empty
	 */
	public void createAndAddUser(String userName, String role) {
		this.company.createAndAddUser(userName, role);
	}

	/**
	 * Create a new Vehicle that has to be created from scratch.
	 * 
	 * @param 	realModel
	 *          The specification the PartPicker has to take into account when
	 *          creating the Vehicle
	 *          
	 * @throws	IllegalArgumentException
	 * 			Thrown when realModel is null
	 */
	public void createNewVehicle(VehicleSpecification realModel) {
		this.company.createNewVehicle(realModel);
	}

	/**
	 * Get the access rights of the User that is currently logged in.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when there is no user logged in
	 */
	public List<AccessRight> getAccessRights() {
		return Collections.unmodifiableList(this.company.getAccessRights());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 			The name of the specification. This is needed to retrieve 
	 * 			the associated VehicleSpecification.
	 *
	 * @throws	IllegalArgumentException
	 * 			Thrown when the specificationName is null or is empty
	 */
	public VehicleSpecification getVehicleSpecificationFromCatalogue(String specificationName) {
		return company.getVehicleSpecification(specificationName);
	}

	/**
	 * Get a set of all the VehicleSpecifications that the VehicleSpecificationCatalogue has.
	 */
	public Set<String> getVehicleSpecifications() {
		return Collections.unmodifiableSet(company.getVehicleSpecifications());
	}

	/**
	 * Get a list of all the VehicleOptionCategories that are available.
	 */
	public List<VehicleOptionCategory> getVehicleOptionCategory() {
		return Collections.unmodifiableList(Arrays.asList(VehicleOptionCategory.values()));
	}

	/**
	 * Get a list of the completed Orders of the User that is currently logged in.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when there is no user logged in
	 */
	public List<IOrder> getCompletedOrders() {
		String name = company.getCurrentUser();
		List<IOrder> unmodifiables = new ArrayList<>();
		for(IOrder order: company.getCompletedOrders(name)){
			unmodifiables.add(new UnmodifiableOrder(order));
		}
		return Collections.unmodifiableList(unmodifiables);
	}

	/**
	 * Returns the name of the currently used Scheduling Algorithm.
	 */
	public String getCurrentSchedulingAlgorithm() {
		return company.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get a list of available CustomTasks from the CustomVehicleCatalogue. 
	 */
	public Set<String> getCustomTasks() {
		return Collections.unmodifiableSet(company.getCustomTasksDescription());
	}

	/**
	 * Get the still available VehicleOptions for the model that is being built. 
	 * 
	 * @param 	type
	 * 			The type of the VehicleOption that has to be selected 
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the type is null
	 */
	public List<VehicleOption> getRemainingVehicleOptions(VehicleOptionCategory type) {
		return Collections.unmodifiableList(company.getStillAvailableVehicleOptions(type));
	}

	/**
	 * Get a list of pending Orders of the User that is currently logged in.
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when there is no user logged in
	 */
	public List<IOrder> getPendingOrders() {
		String name = company.getCurrentUser();
		List<IOrder> unmodifiables = new ArrayList<>();
		for(IOrder order: company.getPendingOrders(name)){
			unmodifiables.add(new UnmodifiableOrder(order));
		}
		return Collections.unmodifiableList(unmodifiables);
	}

	/**
	 * Get a list of Vehicles based on the given optionDescription.
	 * 
	 * 
	 * @param	optionDescription
	 * 			The optionDescription can for example be "spraying car bodies" or "installing custom seats"
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the taskDescription is null or is empty
	 */
	public List<IVehicle> getCustomOptions(String optionDescription) {
		List<IVehicle> tasks = new ArrayList<>();
		for (CustomVehicle model : company.getSpecificCustomTasks(optionDescription)) {
			tasks.add(new UnmodifiableVehicle(model));
		}
		return Collections.unmodifiableList(tasks);
	}

	/**
	 * Returns the average amount of Vehicles produced.
	 */
	public int getAverageDays(){
		return company.getAverageDays();
	}

	/**
	 * Returns the median amount of Vehicles produced.
	 */
	public int getMedianDays(){
		return company.getMedianDays();
	}

	/**
	 * Returns an list with how many Vehicles were produced for the last x days. This amount
	 * x is decided when the company is initialised.
	 */
	public List<Integer> getDetailedDays(){
		return Collections.unmodifiableList(company.getDetailedDays());
	}

	/**
	 * Returns the average amount of Delays when Vehicles were produced.
	 */
	public int getAverageDelays(){
		return company.getAverageDelays();
	}

	/**
	 * Returns the median amount of Delays when Vehicles were produced.
	 */
	public int getMedianDelays(){
		return company.getMedianDelays();
	}

	/**
	 * Returns an list with the latest Delays. The size of this list 
	 * is decided when the company is initialised.
	 */
	public List<Delay> getDetailedDelays(){
		return Collections.unmodifiableList(company.getDetailedDelays());
	}

	/**
	 * Login with a UserName.
	 * 
	 * @param 	userName
	 * 			The name of the user
	 * 
	 * @throws 	RoleNotYetAssignedException
	 * 			Thrown when it's the first time that this user logs in
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the userName is null or is empty
	 */
	public void login(String userName) {
		this.company.login(userName);
	}

	/**
	 * Log the current user out.
	 */
	public void logout() {
		this.company.logout();
	}

	/**
	 * Create and schedule a custom Order.
	 * 
	 * @param 	model
	 * 			The name of the CustomVehicle that has to be ordered
	 * 
	 * @param 	deadline
	 * 			The deadline of the CustomOrder
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null
	 */
	public ImmutableClock processCustomOrder(IVehicle model, ImmutableClock deadline){
		CustomVehicle vehicle = new CustomVehicle();
		for(VehicleOption option: model.getVehicleOptions().values()){
			vehicle.addVehicleOption(option);
		}
		return company.addOrder(vehicle, deadline);
	}

	/**
	 * Create and schedule a standard Order.
	 * 
	 * @param 	quantity
	 * 			The amount of vehicles the user wants to order
	 * 
	 * @throws 	IllegalArgumentException
	 * 			Thrown when the quantity is less than or equal to zero
	 */
	public ImmutableClock processOrder(int quantity){
		return company.processOrder(quantity);
	}

	/**
	 * Advance the clock to the next day.
	 */
	public void startNewDay() {
		this.company.startNewDay();
	}

	/**
	 * Switches the scheduling algorithm based on the information
	 * given in the SchedulingAlgorithmCreator object.
	 * 
	 * @param	creator
	 * 			The SchedulingAlgorithCreator of the SchedulingAlgorithm that has to be switched to
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the creator is null
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.company.switchToSchedulingAlgorithm(creator);
	}

	/**
	 * Returns a powerset with all the VehicleOptions 
	 * or sets of VehicleOptions that occur in three or more pending Orders.
	 */
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.company.getAllVehicleOptionsInPendingOrders());
	}

	/**
	 * Returns a list of AssemblyLines.
	 */
	public List<IAssemblyLine> getAssemblyLines() {
		ArrayList<IAssemblyLine> unmodifiables = new ArrayList<>();
		for (IAssemblyLine assemblyLine : company.getAssemblyLines()) {
			unmodifiables.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiables);
	}

	/**
	 * Changes the State of a given AssemblyLine to the given AssemblyLineState.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine which state needs to be changed
	 * 
	 * @param 	state
	 * 			This AssemblyLineState will be the new state of the given AssemblyLine
	 * 
	 * @param 	clock
	 * 			The time that has passed
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or more of the arguments are null
	 */
	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state, ImmutableClock clock) {
		company.changeState(assemblyLine, state, clock);
	}

	/**
	 * Returns a list of the AssemblyLineStates that can be chosen.
	 * The states are: Operational, Maintenance and Broken.
	 */
	public List<AssemblyLineState> getAssemblyLineStates() {
		List<AssemblyLineState> list = new ArrayList<AssemblyLineState>(Arrays.asList(AssemblyLineState.values()));
		list.remove(AssemblyLineState.IDLE);
		list.remove(AssemblyLineState.FINISHED);
		return Collections.unmodifiableList(list);
	}
}
