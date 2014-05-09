package domain.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import domain.assembly.AssemblyLineState;
import domain.assembly.IAssemblyLine;
import domain.assembly.IWorkBench;
import domain.clock.ImmutableClock;
import domain.company.Company;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.exception.UnmodifiableException;
import domain.job.ITask;
import domain.job.Task;
import domain.order.CustomOrder;
import domain.order.Delay;
import domain.order.IOrder;
import domain.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.users.AccessRight;
import domain.vehicle.CustomVehicle;
import domain.vehicle.IVehicle;
import domain.vehicle.IVehicleOption;
import domain.vehicle.Vehicle;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

/**
 * This class is the layer between the Domain model and the UI. The UI can only
 * call methods on this class to do someting in the domain model.
 * 
 */
public class Facade {

	private Company company;

	/**
	 * Create a new Facade. This initializes automatically all the necessary
	 * resources.
	 * 
	 * @param bindingRestrictions
	 *            The list of BindingRestrictions the facade has to deal with.
	 * @param optionalRestrictions
	 *            The list of OptionalRestrictions the facade has to deal with.
	 * */
	public Facade(Company company) {
		this.company = company;
	}

	/**
	 * Add a Part to the CarModel that is being built.
	 * 
	 * @param part
	 * 			The part you want to add to the model.
	 */
	public void addPartToModel(IVehicleOption part) {
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		company.addPartToModel(option);
	}

	/**
	 * Advance the clock
	 * 
	 * @param time
	 *            How much time the clock has to be advanced.
	 */
	public void advanceClock(int time) {
		this.company.advanceClock(time);
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
	 */
	public void completeChosenTaskAtChosenWorkBench(ITask task, ImmutableClock time) {
		Task modifiableTask = new Task(task.getTaskDescription());
		modifiableTask.setActions(task.getActions());
		this.company.completeChosenTaskAtChosenWorkBench(modifiableTask, time);
	}

	/**
	 * Create a new User and put it in the UserBook. The method automatically
	 * logs the newly created user in.
	 * 
	 * @param userName
	 *            The newly chosen userName.
	 * @param role
	 *            The role of the User.
	 */
	public void createAndAddUser(String userName, String role) {
		this.company.createAndAddUser(userName, role);
	}

	/**
	 * Create a new CarModel that has to be created from scratch.
	 * 
	 * @param realModel
	 *            The specification the PartPicker has to take into account when
	 *            creating the CarModel
	 *TODO Wrm vehicleSpeciication en niet IVehicleSpecification?
	 */
	public void createNewModel(VehicleSpecification realModel) {
		this.company.createNewModel(realModel);
	}

	/**
	 * Get the accessrights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return this.company.getAccessRights();
	}

	/**
	 * Get the workbenches which are blocking the AssemblyLine from advancing.
	 * @return
	 * 			A list of indexes of the workbenches that are blocking the AssemblyLine from advancing.
	 */
	public List<IWorkBench> getBlockingWorkBenches() {
		return ImmutableList.copyOf(company.getBlockingWorkBenches());
	}

	/**
	 * Get the CarModelSpecification from the catalogue.
	 * @param specificationName
	 * 			The name of the specification to retrieve the specification.
	 * @throws IllegalArgumentException
	 * 			If no CarModelSpecification is found with the given name.
	 */
	public VehicleSpecification getCarModelSpecificationFromCatalogue(String specificationName) {
		return company.getVehicleSpecification(specificationName);
	}

	/**
	 * Get a set of all the CarModelSpecifications that the Catalogue has.
	 */
	public Set<String> getCarModelSpecifications() {
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
	public Scheduler getCurrentSchedulingAlgorithm() {
		//TODO updaten na Rutger
		return this.assemblyLine.getCurrentSchedulingAlgorithm();
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
	public List<IVehicleOption> getParts(VehicleOptionCategory type) {
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
	 * Returns a list of all the possible scheduling algorithms as Strings.
	 */
	public List<String> getPossibleSchedulingAlgorithms() {
		return ImmutableList.copyOf(this.assemblyLine.getPossibleSchedulingAlgorithms());
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
		for(IVehicleOption option: model.getCarParts().values()){
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
		//TODO vragen aan rutger, moet dit in facade?
		return this.company.getAllCarOptionsInPendingOrders();
	}

	public List<IAssemblyLine> getAssemblyLines() {
		return Collections.unmodifiableList(company.getAssemblyLines());
	}
}
