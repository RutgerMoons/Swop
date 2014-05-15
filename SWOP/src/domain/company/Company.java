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
 * 
 * 
 *
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
	 * Add a Part to the CarModel that is being built.
	 * 
	 * @param 	part
	 * 				The part you want to add to the model.
	 */
	public void addPartToModel(VehicleOption part){
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		this.partpicker.getModel().addCarPart(option);
	}
	
	/**
	 * Complete the task the user has chosen to complete. The method
	 * automatically advances the AssemblyLine if it can advance.
	 * 
	 * @param 	assemblyLine
	 * 			  The assemblyLine on which a certain task of a job is assembled
	 * 
	 * @param 	workBench
	 *            The workbench on which a certain task of a job is assembled
	 * 
	 * @param 	task
	 *            The Task in the Job on the Workbench.
	 * 
	 * @param 	time
	 *            The time the clock has to be advanced.
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
	 *            The newly chosen userName
	 * 
	 * @param 	role
	 *            The role of the User
	 */
	public void createAndAddUser(String userName, String role){
		this.userbook.createUser(userName, role);
	}

	/**
	 * Create a new Vehicle that has to be created from scratch.
	 * 
	 * @param 	realModel
	 *            The specification the PartPicker has to take into account when
	 *            creating the Vehicle
	 */
	public void createNewVehicle(VehicleSpecification realModel) {
		this.partpicker.setNewModel(realModel);
	}

	/**
	 * Get the AccessRights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return Collections.unmodifiableList(this.userbook.getCurrentUser().getAccessRights());
	}

	/**
	 * Get the VehicleSpecification from the VehicleSpecificationCatalogue.
	 * 
	 * @param 	specificationName
	 * 				The name of the specification needed to retrieve the VehicleSpecification from
	 * 				the VehicleSpecificationCatalogue.
	 */
	public VehicleSpecification getVehicleSpecificationFromCatalogue(String specificationName) {
		return this.partpicker.getCatalogue().getCatalogue().get(specificationName);
	}
	
	public String getCurrentSchedulingAlgorithm() {
		return this.workloadDivider.getCurrentSchedulingAlgorithm();
	}

	public void login(String userName) throws RoleNotYetAssignedException{
		this.userbook.login(userName);
	}

	public void logout(){
		this.userbook.logout();
	}

	public void startNewDay(){
		this.clock.startNewDay();
	}

	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		this.workloadDivider.switchToSchedulingAlgorithm(creator);
	}

	public List<IAssemblyLine> getAssemblyLines() {
		return Collections.unmodifiableList(workloadDivider.getAssemblyLines());
	}

	/**
	 * Get the workbenches which are blocking the AssemblyLine from advancing.
	 * @return
	 * 			A list of indexes of the workbenches that are blocking the AssemblyLine from advancing.
	 */
	public List<IWorkBench> getBlockingWorkBenches(IAssemblyLine assemblyLine) {
		// workloadDivider returnt een lijst van UnmodifiableWorkbenches
		return Collections.unmodifiableList(workloadDivider.getBlockingWorkBenches(assemblyLine));
	}

	public VehicleSpecification getVehicleSpecification(String specificationName) {
		return partpicker.getSpecification(specificationName);
	}

	public Set<String> getVehicleSpecifications() {
		return Collections.unmodifiableSet(partpicker.getVehicleSpecifications());
	}

	public String getCurrentUser() {
		return userbook.getCurrentUser().getName();
	}

	public Collection<IOrder> getCompletedOrders(String name) {
		return Collections.unmodifiableCollection(orderbook.getCompletedOrders().get(name));
	}

	public Set<String> getCustomTasksDescription() {
		return Collections.unmodifiableSet(customCatalogue.getCatalogue().keySet());
	}

	public List<VehicleOption> getStillAvailableCarParts(VehicleOptionCategory type) {
		return Collections.unmodifiableList(partpicker.getStillAvailableCarParts(type));
	}

	public Collection<IOrder> getPendingOrders(String name) {
		return Collections.unmodifiableCollection(orderbook.getPendingOrders().get(name));
	}

	public Collection<CustomVehicle> getSpecificCustomTasks(String taskDescription) {
		return Collections.unmodifiableCollection(customCatalogue.getCatalogue().get(taskDescription));
	}

	public int getAverageDays() {
		return log.averageDays();
	}

	public int getMedianDays(){
		return log.medianDays();
	}

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

	public int getAverageDelays(){
		return log.averageDelays();
	}

	public int getMedianDelays(){
		return log.medianDelays();
	}

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

	public void addOrder(CustomOrder order) {
		orderbook.addOrder(order, getUnmodifiableClock());
	}

	public ImmutableClock processOrder(int quantity) {
		Vehicle vehicle = partpicker.getModel();
		if (!vehicle.isValid())
			throw new IllegalStateException();

		StandardOrder order = new StandardOrder(userbook.getCurrentUser()
				.getName(), vehicle, quantity, clock.getImmutableClock());

		orderbook.addOrder(order, clock.getImmutableClock());
		return order.getEstimatedTime();
	}

	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return Collections.unmodifiableSet(this.workloadDivider.getAllCarOptionsInPendingOrders());
	}

	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state) {
		workloadDivider.changeState(assemblyLine, state);
	}
}
