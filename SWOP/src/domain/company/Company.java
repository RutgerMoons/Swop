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
import domain.exception.TimeToStartNewDayException;
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
	 * Create a new Company. It receives all its defining data structures to initialise everything properly.
	 * 
	 * @param 	bindingRestrictions
	 * 			List of BindingRestrictions necessary for the internal PartPicker
	 * 
	 * @param 	optionalRestrictions
	 * 			List of OptionalRestrictions necessary for the internal PartPicker
	 * 
	 * @param 	customCatalogue
	 * 			Catalogue for CustomOrders, this catalogue contains all the possible sorts of CustomOrders this Company can process
	 * 
	 * @param 	vehicleSpecificationCatalogue
	 * 			Catalogue for StandardOrders, this catalogue contains all the different possible kinds of StandardOrders that
	 * 			this Company can process
	 * 
	 * @param 	listOfAssemblyLines
	 * 			A list with all the AssemblyLines for this Company
	 * 
	 * @param 	clock
	 * 			A clock necessary for keeping track of time
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null
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
		this.log = new Logger(amountOfDetailedHistory, clock.getImmutableClock());
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
	 * 			The part you want to add to the model
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the part is null
	 */
	public void addPartToModel(VehicleOption part){
		if(part==null){
			throw new IllegalArgumentException();
		}
		this.partpicker.addCarPartToModel(part);
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
	 *          The Task in the Job on the Workbench
	 * 
	 * @param 	time
	 *          The time the clock has to be advanced
	 *          
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null
	 */
	public void completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, ITask task, ImmutableClock time){
		if(assemblyLine==null || workbench==null || task==null || time==null){
			throw new IllegalArgumentException();
		}
		try {
			int currentAmountOfMinutesAtScheduler = this.workloadDivider.completeChosenTaskAtChosenWorkBench(
																		assemblyLine, workbench, task, time);
			int difference = currentAmountOfMinutesAtScheduler - this.clock.getMinutes();
			if (difference > 0) {
				clock.advanceTime(difference);
			}
		} catch (TimeToStartNewDayException timeException) {
			this.startNewDay();
		}
		
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
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the arguments is null or is empty
	 */
	public void createAndAddUser(String userName, String role){
		if(userName==null || userName.isEmpty() || role==null || role.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.userbook.createUser(userName, role);
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
		if(realModel==null){
			throw new IllegalArgumentException();
		}
		this.partpicker.setNewModel(realModel);
	}

	/**
	 * Returns a list with the AccessRights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return Collections.unmodifiableList(this.userbook.getCurrentUser().getAccessRights());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 			The name of the specification needed to retrieve the VehicleSpecification from
	 * 			the VehicleSpecificationCatalogue
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the specificationName is null or is empty
	 */
	public VehicleSpecification getVehicleSpecificationFromCatalogue(String specificationName) {
		if(specificationName==null || specificationName.isEmpty()){
			throw new IllegalArgumentException();
		}
		return this.partpicker.getVehicleSpecificationCatalogue().getCatalogue().get(specificationName);
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
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the userName is null or is empty
	 */
	public void login(String userName) {
		if(userName==null || userName.isEmpty()){
			throw new IllegalArgumentException();
		}
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
		this.workloadDivider.startNewDay(this.clock.getImmutableClock());
	}

	/**
	 * Switches the scheduling algorithm based on the information
	 * given in the ScedulingAlgoritmeCreator object.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the creator is null
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		if(creator==null){
			throw new IllegalArgumentException();
		}
		this.workloadDivider.switchToSchedulingAlgorithm(creator);
	}

	/**
	 * Get all the AssemblyLines.
	 */
	public List<IAssemblyLine> getAssemblyLines() {
		return Collections.unmodifiableList(workloadDivider.getAssemblyLines());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 			The name of the specification. This is needed to retrieve 
	 * 			the associated VehicleSpecification
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the specificationName is null or is empty
	 */
	public VehicleSpecification getVehicleSpecification(String specificationName) {
		if(specificationName==null || specificationName.isEmpty()){
			throw new IllegalArgumentException();
		}
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
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the name is null or is empty
	 */
	public List<IOrder> getCompletedOrders(String name) {
		if(name==null || name.isEmpty()){
			throw new IllegalArgumentException();
		}
		return Collections.unmodifiableList(new ArrayList<IOrder>(orderbook.getCompletedOrders().get(name)));
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
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the type is null
	 */
	public List<VehicleOption> getStillAvailableVehicleOptions(VehicleOptionCategory type) {
		if(type==null){
			throw new IllegalArgumentException();
		}
		return Collections.unmodifiableList(partpicker.getStillAvailableCarParts(type));
	}

	/**
	 * Get a list of pending Orders of the User that is currently logged in.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the name is null or is empty
	 */
	public List<IOrder> getPendingOrders(String name) {
		if(name==null || name.isEmpty()){
			throw new IllegalArgumentException();
		}
		return Collections.unmodifiableList(new ArrayList<IOrder>(orderbook.getPendingOrders().get(name)));
	}

	/**
	 * Get the Custom tasks of a certain description.
	 * 
	 * @param 	taskDescription
	 * 			The description that matches the custom tasks		
	 * 	
	 * @throws	IllegalArgumentException
	 * 			Thrown when the taskDescription is null or is empty
	 */
	public Collection<CustomVehicle> getSpecificCustomTasks(String taskDescription) {
		if(taskDescription==null || taskDescription.isEmpty()){
			throw new IllegalArgumentException();
		}
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
	 * Get a list with how many Vehicles were produced for a certain amount of 
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
	 * Get the list with the latest Delays. The size of this list
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

	/**
	 * Get the current time.
	 */
	public ImmutableClock getImmutableClock() {
		return clock.getImmutableClock();
	}

	/**
	 * Method that adds the given Order to the OrderBook.
	 * 
	 * @param 	model
	 * 			The vehicle the user wants to order
	 * 
	 * @param	deadline
	 * 			The deadline of the custom order
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the order is null
	 */
	public ImmutableClock addOrder(CustomVehicle model, ImmutableClock deadline) {
		if(model==null || deadline == null){
			throw new IllegalArgumentException();
		}
		
		ImmutableClock time = clock.getImmutableClock().getImmutableClockPlusExtraMinutes(deadline.getTotalInMinutes());
		CustomOrder order = new CustomOrder(
				getCurrentUser(), model, 1,
				getImmutableClock(), time);
		
		orderbook.addOrder(order);
		return order.getEstimatedTime();
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
	public ImmutableClock processOrder(int quantity) {
		if(quantity<=0){
			throw new IllegalArgumentException();
		}
		Vehicle vehicle = partpicker.getModel();
		if (!vehicle.isValid())
			throw new IllegalStateException();

		StandardOrder order = new StandardOrder(userbook.getCurrentUser()
				.getName(), vehicle, quantity, clock.getImmutableClock());

		orderbook.addOrder(order);
		return order.getEstimatedTime();
	}

	/**
	 * Returns a powerset with all the VehicleOptions 
	 * or sets of VehicleOptions that occur in three or more pending Orders.
	 */
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.workloadDivider.getAllVehicleOptionsInPendingOrders());
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
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or more of the arguments are null
	 */
	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state, ImmutableClock clock) {
		if(assemblyLine == null || state == null || clock == null){
			throw new IllegalArgumentException();
		}
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
