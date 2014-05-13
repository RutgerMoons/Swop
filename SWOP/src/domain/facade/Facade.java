package domain.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.exception.UnmodifiableException;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.order.CustomOrder;
import domain.order.Delay;
import domain.order.IOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.users.AccessRight;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * This class is the layer between the Domain model and the UI. The UI can only
 * call methods on this class to do something in the domain model.
 * 
 */
public class Facade {

	private Company company;

	/**
	 * Create a new Facade. This initializes automatically all the necessary
	 * resources.
	 *
	 * @param	company
	 * 				The instantiation of a Company object to which the facade communicates everything to
	 * 
	 * @throws 	IllegalArgumentException
	 * 				Thrown when the given company is null
	 * */
	public Facade(Company company) {
		if (company == null) {
			throw new IllegalArgumentException();
		}
		this.company = company;
	}

	/**
	 * Add a Part to the VehicleModel that is being ordered
	 * 
	 * @param part
	 * 			The VehicleOption you want to add to the model
	 */
	public void addPartToModel(VehicleOption part) {
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		company.addPartToModel(option);
	}

	/**
	 * Communicate to the company instance that at a given assemblyLine at a given workbench 
	 * a given task is completed in a given time.
	 * 
	 * @param 	assemblyLine
	 * 				The assemblyLine at which the worker stands
	 * 
	 * @param 	workbench
	 *           	The workbench where the given task is completed
	 *           
	 * @param 	task
	 *            	The task that is completed by the worker
	 *            
	 * @param 	time
	 *            	The amount of minutes needed to complete the task
	 */
	public void completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, 
			ITask task, ImmutableClock time) {
		Task modifiableTask = new Task(task.getTaskDescription());
		modifiableTask.setActions(task.getActions());
		
		this.company.completeChosenTaskAtChosenWorkBench(assemblyLine, workbench, modifiableTask, time);
	}

	/**
	 * Create a new User and put it in the UserBook. The method automatically
	 * logs the newly created user in.
	 * 
	 * @param 	userName
	 *            The newly chosen userName
	 *            
	 * @param 	role
	 *            The role of the User
	 */
	public void createAndAddUser(String userName, String role) {
		this.company.createAndAddUser(userName, role);
	}

	/**
	 * Create a new Vehicle that has to be created from scratch.
	 * 
	 * @param 	realModel
	 *            The specification the PartPicker has to take into account when
	 *            creating the Vehicle
	 */
	public void createNewVehicle(VehicleSpecification realModel) {
		this.company.createNewVehicle(realModel);
	}

	/**
	 * Get the access rights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return this.company.getAccessRights();
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue
	 * 
	 * @param 	specificationName
	 * 				The name of the specification. This is needed to retrieve 
	 * 				the associated VehicleSpecification.
	 */
	public VehicleSpecification getVehicleSpecificationFromCatalogue(String specificationName) {
		return company.getVehicleSpecification(specificationName);
	}

	/**
	 * Get a set of all the CarModelSpecifications that the Catalogue has.
	 */
	public Set<String> getVehicleSpecifications() {
		return ImmutableSet.copyOf(company.getVehicleSpecifications());
	}

	/**
	 * Get a set off all the CarPartTypes that are available.
	 */
	public List<VehicleOptionCategory> getCarPartTypes() {
		return ImmutableList.copyOf(Arrays.asList(VehicleOptionCategory.values()));
	}

	/**
	 * Get a list of the completed orders of the user that is currently logged in. 
	 */
	public List<IOrder> getCompletedOrders() {
		String name = company.getCurrentUser();
		return ImmutableList.copyOf(company.getCompletedOrders(name));
	}

	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public String getCurrentSchedulingAlgorithm() {
		return company.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Get a list of available CustomTasks from the catalogue. 
	 * @return
	 */
	public Set<String> getCustomTasks() {
		return ImmutableSet.copyOf(company.getCustomTasksDescription());
	}

	/**
	 * Get the still available Parts for the model that is being built. 
	 * @param type
	 * 			The type of the parts that has to be selected. 
	 */
	public List<VehicleOption> getParts(VehicleOptionCategory type) {
		return ImmutableList.copyOf(company.getStillAvailableCarParts(type));
	}

	/**
	 * Get a list of pending orders of the user that is currently logged in.
	 */
	public List<IOrder> getPendingOrders() {
		String name = company.getCurrentUser();
		return ImmutableList.copyOf(company.getPendingOrders(name));
	}

	/**
	 * Get a list of specific Custom Tasks on the basis of the taskDescription.
	 * The taskDescription is like "spraying car bodies" or "installing custom seats"
	 * 
	 */
	public List<IVehicle> getSpecificCustomTasks(String taskDescription) {
		List<IVehicle> tasks = new ArrayList<>();
		for (CustomVehicle model : company.getSpecificCustomTasks(taskDescription)) {
			tasks.add(model);
		}
		return ImmutableList.copyOf(tasks);
	}
	
	public int getAverageDays(){
		return company.getAverageDays();
	}
	
	public int getMedianDays(){
		return company.getMedianDays();
	}
	
	public List<Integer> getDetailedDays(){
		return company.getDetailedDays();
	}
	
	public int getAverageDelays(){
		return company.getAverageDelays();
	}
	
	public int getMedianDelays(){
		return company.getMedianDelays();
	}
	
	public List<Delay> getDetailedDelays(){
		return company.getDetailedDelays();
	}


	/**
	 * Login with a userName.
	 * @param userName
	 * 			The name of the user.
	 * @throws RoleNotYetAssignedException
	 * 			If it's the first time that this user logs in.
	 */
	public void login(String userName) throws RoleNotYetAssignedException {
		this.company.login(userName);
	}

	/**
	 * Log the current user out.
	 */
	public void logout() {
		this.company.logout();
	}

	/**
	 * Create and schedule a CustomOrder.
	 * 
	 * @param model
	 * 			The name of the CustomCarModel that has to be ordered.
	 * @param deadline
	 * 			The deadline of the CustomOrder
	 */
	public ImmutableClock processCustomOrder(IVehicle model, ImmutableClock deadline) throws IllegalArgumentException{
		CustomVehicle vehicle = new CustomVehicle();
		for(VehicleOption option: model.getVehicleOptions().values()){
			vehicle.addCarPart(option);
		}

		CustomOrder order = new CustomOrder(
				company.getCurrentUser(), vehicle, 1,
				company.getUnmodifiableClock(), deadline);
		company.addOrder(order);
		return order.getEstimatedTime();
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
	public ImmutableClock processOrder(int quantity) throws	IllegalStateException{
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
	 * given in the ScedulingAlgoritmeCreator object.
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.company.switchToSchedulingAlgorithm(creator);
	}

	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return ImmutableSet.copyOf(this.company.getAllCarOptionsInPendingOrders());
	}

	public List<IAssemblyLine> getAssemblyLines() {
		return Collections.unmodifiableList(company.getAssemblyLines());
	}

	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state) {
		company.changeState(assemblyLine, state);
	}

	public List<AssemblyLineState> getAssemblyLineStates() {
		return ImmutableList.copyOf(Arrays.asList(AssemblyLineState.values()));
	}
}
