package domain.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.RoleNotYetAssignedException;
import domain.job.task.ITask;
import domain.log.Logger;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.ClockObserver;
import domain.observer.observers.OrderBookObserver;
import domain.order.Delay;
import domain.order.OrderBook;
import domain.order.order.CustomOrder;
import domain.order.order.IOrder;
import domain.order.order.StandardOrder;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.scheduling.WorkloadDivider;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.users.AccessRight;
import domain.users.UserBook;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.CustomVehicleCatalogue;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.CustomVehicle;
import domain.vehicle.vehicle.Vehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class representing a company. Everything that defines a company has this class as an attribute, direct or indirect.
 * Direct attributes are for example: an UserBook, an OrderBook, a PartPicker but also a CustomVehicleCatalogue, a Logger, a Clock,
 * a WorkloadDivider etc.
 */
public class Company {

	private UserBook userbook;
	private OrderBook orderbook;
	private PartPicker partpicker;
	private CustomVehicleCatalogue customCatalogue;
	private Logger log;
	private Clock clock;
	private WorkloadDivider workloadDivider;
	private Set<BindingRestriction> bindingRestrictions;
	private Set<OptionalRestriction> optionalRestrictions;
	private int amountOfDetailedHistory;

	/**
	 * Create a new Company. It receives all its defining datastructures to initialise everything properly. TODO veranderen later
	 * 
	 * @param 	bindingRestrictions
	 * 			List of BindingRestrictions necessary for the internal PartPicker
	 * 
	 * @param 	optionalRestrictions
	 * 			List of OptionalRestrictions necessary for the internal PartPicker
	 * 
	 * @param 	customCatalogue
	 * 			Catalogue for CustomOrders, this catalogue contains all the possible sorts of CustomOrders this Company can process.
	 * 
	 * @param 	vehicleSpecificationCatalogue
	 * 			Catalogue for StandardOrders, this catalogue contains all the different possible kinds of StandardOrders that
	 * 			this Company can process.
	 * 
	 * @param 	listOfAssemblyLines
	 * 			A list with all the AssemblyLines for this Company.
	 * 
	 * @param 	clock
	 * 			A clock necessary for keeping track of time.s
	 */
	public Company(Set<BindingRestriction> bindingRestrictions, Set<OptionalRestriction> optionalRestrictions, 
			CustomVehicleCatalogue customCatalogue, VehicleSpecificationCatalogue vehicleSpecificationCatalogue, 
			List<AssemblyLine> listOfAssemblyLines, Clock clock){
		if (bindingRestrictions == null || optionalRestrictions == null || customCatalogue == null || 
				vehicleSpecificationCatalogue == null || listOfAssemblyLines == null || clock==null){
			throw new IllegalArgumentException();
		}
		this.userbook = new UserBook();
		this.orderbook = new OrderBook();
		amountOfDetailedHistory = 2;
		this.log = new Logger(amountOfDetailedHistory);
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		assemblyLineObserver.attachLogger(log);
		assemblyLineObserver.attachLogger(orderbook);
		this.clock = clock;
		ClockObserver clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);
		clockObserver.attachLogger(log);
		this.bindingRestrictions = bindingRestrictions;
		this.optionalRestrictions = optionalRestrictions;
		this.customCatalogue = customCatalogue;
		this.partpicker = new PartPicker(vehicleSpecificationCatalogue, this.bindingRestrictions, this.optionalRestrictions);
		OrderBookObserver orderBookObserver = new OrderBookObserver();
		this.workloadDivider = new WorkloadDivider(listOfAssemblyLines, orderBookObserver, assemblyLineObserver);
		orderbook.attachObserver(orderBookObserver);
	}

	/**
	 * Add a Part to the Vehicle that is being built.
	 * 
	 * @param 	part
	 * 			The part you want to add to the model.
	 */
	public void addPartToModel(VehicleOption part){
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		this.partpicker.getModel().addVehicleOption(option);
	}

	/**
	 * Complete the task the user has chosen to complete. The method
	 * automatically advances the AssemblyLine if it can advance.
	 * 
	 * @param 	assemblyLine
	 * 			The assemblyLine on which a certain task of a job is assembled
	 * 
	 * @param 	workBench
	 *          The workbench on which a certain task of a job is assembled
	 * 
	 * @param 	task
	 *          The Task in the Job on the Workbench.
	 * 
	 * @param 	time
	 *          The time the clock has to be advanced.
	 */
	public void completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, ITask task, ImmutableClock time){
		this.workloadDivider.completeChosenTaskAtChosenWorkBench(assemblyLine, workbench, task);

		clock.advanceTime(time.getTotalInMinutes());
	}

	/**
	 * Create a new User and put it in the UserBook. The method automatically
	 * logs the newly created user in.
	 * 
	 * @param 	userName
	 *          The newly chosen userName
	 * 
	 * @param 	role
	 *          The role of the User
	 */
	public void createAndAddUser(String userName, String role){
		this.userbook.createUser(userName, role);
	}

	/**
	 * Create a new Vehicle that has to be created from scratch.
	 * 
	 * @param 	realModel
	 *          The specification the PartPicker has to take into account when
	 *          creating the Vehicle
	 */
	public void createNewVehicle(VehicleSpecification realModel) {
		this.partpicker.setNewModel(realModel);
	}

	/**
	 * Returns an unmodifiable list with the AccessRights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return Collections.unmodifiableList(this.userbook.getCurrentUser().getAccessRights());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 			The name of the specification needed to retrieve the VehicleSpecification from
	 * 			the VehicleSpecificationCatalogue.
	 */
	public VehicleSpecification getVehicleSpecificationFromCatalogue(String specificationName) {
		return this.partpicker.getCatalogue().getCatalogue().get(specificationName);
	}

	/**
	 * Returns the name of the currently used Scheduling Algorithm.
	 */
	public String getCurrentSchedulingAlgorithm() {
		return this.workloadDivider.getCurrentSchedulingAlgorithm();
	}

	/**
	 * Login with a userName.
	 * 
	 * @param 	userName
	 * 			The name of the user
	 * 
	 * @throws 	RoleNotYetAssignedException
	 * 			Thrown when it's the first time that this user logs in
	 */
	public void login(String userName) throws RoleNotYetAssignedException{
		this.userbook.login(userName);
	}

	/**
	 * Log the current user out.
	 */
	public void logout(){
		this.userbook.logout();
	}

	/**
	 * Advance the clock to the next day.
	 */
	public void startNewDay(){
		this.clock.startNewDay();
	}

	/**
	 * Switches the scheduling algorithm based on the information
	 * given in the ScedulingAlgoritmeCreator object.
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.workloadDivider.switchToSchedulingAlgorithm(creator);
	}

	/**
	 * Returns a list of unmodifiable AssemblyLines.
	 */
	public List<IAssemblyLine> getAssemblyLines() {
		return Collections.unmodifiableList(workloadDivider.getAssemblyLines());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 			The name of the specification. This is needed to retrieve 
	 * 			the associated VehicleSpecification.
	 */
	public VehicleSpecification getVehicleSpecification(String specificationName) {
		return partpicker.getSpecification(specificationName);
	}

	/**
	 * Get a set of all the VehicleSpecifications that the VehicleSpecificationCatalogue has.
	 */
	public Set<String> getVehicleSpecifications() {
		return Collections.unmodifiableSet(partpicker.getVehicleSpecifications());
	}

	/**
	 * Returns the name of the current User.
	 */
	public String getCurrentUser() {
		return userbook.getCurrentUser().getName();
	}

	/**
	 * Get a list of the completed Orders of the User that is currently logged in. 
	 */
	public Collection<IOrder> getCompletedOrders(String name) {
		return Collections.unmodifiableCollection(orderbook.getCompletedOrders().get(name));
	}

	/**
	 * Get a list of available CustomTasks from the CustomVehicleCatalogue. 
	 */
	public Set<String> getCustomTasksDescription() {
		return Collections.unmodifiableSet(customCatalogue.getCatalogue().keySet());
	}

	/**
	 * Get the still available VehicleOptions for the model that is being built. 
	 * 
	 * @param 	type
	 * 			The type of the VehicleOption that has to be selected 
	 */
	public List<VehicleOption> getStillAvailableCarParts(VehicleOptionCategory type) {
		return Collections.unmodifiableList(partpicker.getStillAvailableCarParts(type));
	}

	/**
	 * Get a list of pending Orders of the User that is currently logged in.
	 */
	public Collection<IOrder> getPendingOrders(String name) {
		return Collections.unmodifiableCollection(orderbook.getPendingOrders().get(name));
	}

	public Collection<CustomVehicle> getSpecificCustomTasks(String taskDescription) {
		return Collections.unmodifiableCollection(customCatalogue.getCatalogue().get(taskDescription));
	}

	/**
	 * Returns the average amount of Vehicles produced.
	 */
	public int getAverageDays() {
		return log.averageDays();
	}

	/**
	 * Returns the median amount of Vehicles produced.
	 */
	public int getMedianDays(){
		return log.medianDays();
	}

	/**
	 * Returns an unmodifiable list with how many Vehicles were produced for a certain amount of 
	 * last days. This amount is decided by amountOfDetailedHistory, an 
	 * attribute of the company.
	 */
	public List<Integer> getDetailedDays(){
		List<Integer> detailedList = new ArrayList<>();
		detailedList.addAll(log.getDetailedDays());
		if(detailedList.size() < this.amountOfDetailedHistory){
			for(int i = detailedList.size();i<this.amountOfDetailedHistory; i++){
				detailedList.add(0);
			}
		}
		return Collections.unmodifiableList(detailedList);
	}

	/**
	 * Returns the average amount of Delays when Vehicles were produced.
	 */
	public int getAverageDelays(){
		return log.averageDelays();
	}

	/**
	 * Returns the median amount of Delays when Vehicles were produced.
	 */
	public int getMedianDelays(){
		return log.medianDelays();
	}

	/**
	 * Returns an unmodifiable list with the lastest Delays. The size of this list
	 * is decided by amountOfDetailedHistory, an attribute of the company.
	 */
	public List<Delay> getDetailedDelays(){
		List<Delay> detailedList = new ArrayList<>();
		detailedList.addAll(log.getDetailedDelays());
		if(detailedList.size() < this.amountOfDetailedHistory){
			for(int i = detailedList.size();i<this.amountOfDetailedHistory; i++){
				detailedList.add(new Delay(new ImmutableClock(0,0), new ImmutableClock(0,0)));
			}
		}
		return Collections.unmodifiableList(detailedList);
	}

	public ImmutableClock getUnmodifiableClock() {
		return clock.getImmutableClock();
	}

	/**
	 * Method that adds the given Order to the OrderBook.
	 * 
	 * @param 	order
	 * 			Order that needs to be added to the OrderBook.s
	 */
	public void addOrder(CustomOrder order) {
		orderbook.addOrder(order, getUnmodifiableClock());
	}

	/**
	 * Create and schedule a standard Order.
	 * 
	 * @param 	quantity
	 * 			The amount of vehicles the user wants to order
	 */
	public ImmutableClock processOrder(int quantity) {
		Vehicle vehicle = partpicker.getModel();
		if (!vehicle.isValid())
			throw new IllegalStateException();

		StandardOrder order = new StandardOrder(userbook.getCurrentUser()
				.getName(), vehicle, quantity, clock.getImmutableClock());

		orderbook.addOrder(order, clock.getImmutableClock());
		return order.getEstimatedTime();
	}

	/**
	 * Returns a immutable powerset with all the VehicleOptions 
	 * or sets of VehicleOptions that occur in three or more pending Orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.workloadDivider.getAllCarOptionsInPendingOrders());
	}

	/**
	 * Changes the State of a given AssemblyLine to the given AssemblyLineState.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine which state needs to be changed by the user
	 * 
	 * @param 	state
	 * 			This AssemblyLineState will be the new state of the given AssemblyLine
	 * 
	 * @param 	clock
	 * 			The current time 
	 */
	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state, ImmutableClock clock) {
		this.clock.advanceTime(clock.getTotalInMinutes());
		if(state.equals(AssemblyLineState.MAINTENANCE)){
			ClockObserver observer = new ClockObserver();
			this.clock.attachObserver(observer);
			workloadDivider.changeState(assemblyLine, state, observer, this.clock.getImmutableClock());
		} else {
			workloadDivider.changeState(assemblyLine, state);
		}
	}
}
