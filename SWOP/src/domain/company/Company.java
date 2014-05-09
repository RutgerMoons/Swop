package domain.company;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import domain.assembly.IAssemblyLine;
import domain.assembly.IWorkBench;
import domain.clock.Clock;
import domain.clock.ImmutableClock;
import domain.exception.RoleNotYetAssignedException;
import domain.job.IAction;
import domain.job.ITask;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
import domain.order.Delay;
import domain.order.IOrder;
import domain.order.OrderBook;
import domain.restriction.BindingRestriction;
import domain.restriction.OptionalRestriction;
import domain.restriction.PartPicker;
import domain.scheduling.WorkloadDivider;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.users.AccessRight;
import domain.users.UserBook;
import domain.vehicle.CustomVehicle;
import domain.vehicle.CustomVehicleCatalogue;
import domain.vehicle.IVehicleOption;
import domain.vehicle.VehicleOption;
import domain.vehicle.VehicleOptionCategory;
import domain.vehicle.VehicleSpecification;

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

	public Company(Set<BindingRestriction> bindingRestrictions, Set<OptionalRestriction> optionalRestrictions, CustomVehicleCatalogue customCatalogue){
		if (bindingRestrictions == null || optionalRestrictions == null || customCatalogue == null){
			throw new IllegalArgumentException();
		}
		this.userbook = new UserBook();
		this.orderbook = new OrderBook();
		amountOfDetailedHistory = 2;
		this.log = new Logger(amountOfDetailedHistory);
		AssemblyLineObserver assemblyLineObserver = new AssemblyLineObserver();
		assemblyLineObserver.attachLogger(log);
		assemblyLineObserver.attachLogger(orderbook);
		this.clock = new Clock();
		ClockObserver clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);
		clockObserver.attachLogger(log);
		this.bindingRestrictions = bindingRestrictions;
		this.optionalRestrictions = optionalRestrictions;
		this.customCatalogue = customCatalogue;
		this.partpicker = new PartPicker(this.bindingRestrictions, this.optionalRestrictions);
		
	}
	
	/**
	 * Add a Part to the CarModel that is being built.
	 * 
	 * @param part
	 * 			The part you want to add to the model.
	 */
	public void addPartToModel(IVehicleOption part){
		VehicleOption option = new VehicleOption(part.getDescription(), part.getType());
		this.partpicker.getModel().addCarPart(option);
	}

	/**
	 * Advance the clock
	 * 
	 * @param time
	 *            How much time the clock has to be advanced.
	 */
	public void advanceClock(int time){
		this.clock.advanceTime(time);
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
	public void completeChosenTaskAtChosenWorkBench(ITask task, ImmutableClock time){
		for (IAction action : task.getActions()) {
			action.setCompleted(true);
		}
		this.workloadDivider.checkIfCanAdvanceOneAssemblyLine()
		clock.advanceTime();
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
	public void createAndAddUser(String userName, String role){
		this.userbook.createUser(userName, role);
	}
	
	/**
	 * Create a new CarModel that has to be created from scratch.
	 * 
	 * @param realModel
	 *            The specification the PartPicker has to take into account when
	 *            creating the CarModel
	 */
	public void createNewModel(VehicleSpecification realModel) {
		this.partpicker.setNewModel(realModel);
	}
	
	/**
	 * Get the accessrights of the User that is currently logged in.
	 */
	public List<AccessRight> getAccessRights() {
		return ImmutableList.copyOf(this.userbook.getCurrentUser().getAccessRights());
	}
	
	
	//TODO getBlockingWorkbenches() nog toevoegen
	
	/**
	 * Get the CarModelSpecification from the catalogue.
	 * @param specificationName
	 * 			The name of the specification to retrieve the specification.
	 * @throws IllegalArgumentException
	 * 			If no CarModelSpecification is found with the given name.
	 */
	public VehicleSpecification getCarModelSpecificationFromCatalogue(String specificationName) {
		return this.partpicker.getCatalogue().get(specificationName);
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
	
	public void switchToDifferentAlgoritm(SchedulingAlgorithmCreator creator){
		
	}

	public List<IAssemblyLine> getAssemblyLines() {
		return workloadDivider.getAssemblyLines();
	}

	public List<IWorkBench> getBlockingWorkBenches() {
		// TODO Auto-generated method stub
		return null;
	}

	public VehicleSpecification getVehicleSpecification(String specificationName) {
		return partpicker.getSpecification(specificationName);
	}

	public Set<String> getVehicleSpecifications() {
		return partpicker.getVehicleSpecifications();
	}

	public String getCurrentUser() {
		return userbook.getCurrentUser().getName();
	}

	public Collection<IOrder> getCompletedOrders(String name) {
		return orderbook.getCompletedOrders().get(name);
	}

	public Set<String> getCustomTasksDescription() {
		return customCatalogue.getCatalogue().keySet();
	}

	public List<IVehicleOption> getStillAvailableCarParts(VehicleOptionCategory type) {
		return partpicker.getStillAvailableCarParts(type);
	}

	public Collection<IOrder> getPendingOrders(String name) {
		return orderbook.getPendingOrders().get(name);
	}

	public Collection<CustomVehicle> getSpecificCustomTasks(String taskDescription) {
		return customCatalogue.getCatalogue().get(taskDescription);
	}

	public int getAverageDays() {
		return log.averageDays();
	}
	
	public int getMedianDays(){
		return log.medianDays();
	}
	
	public List<Integer> getDetailedDays(){
		List<Integer> detailedList = log.getDetailedDays();
		if(detailedList.size() < this.amountOfDetailedHistory){
			for(int i = detailedList.size();i<this.amountOfDetailedHistory; i++){
				detailedList.add(0);
			}
		}
		return detailedList;
	}
	
	public int getAverageDelays(){
		return log.averageDelays();
	}
	
	public int getMedianDelays(){
		return log.medianDelays();
	}
	
	public List<Delay> getDetailedDelays(){
		List<Delay> detailedList = log.getDetailedDelays();
		if(detailedList.size() < this.amountOfDetailedHistory){
			for(int i = detailedList.size();i<this.amountOfDetailedHistory; i++){
				detailedList.add(new Delay(new ImmutableClock(0,0), new ImmutableClock(0,0)));
			}
		}
		return detailedList;
	}
}
