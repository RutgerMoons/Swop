package domain.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.car.CarModel;
import domain.car.CarModelCatalogue;
import domain.car.CarModelCatalogueFiller;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.car.CustomCarModel;
import domain.car.CustomCarModelCatalogue;
import domain.car.CustomCarModelCatalogueFiller;
import domain.clock.Clock;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
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
import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserBook;
import domain.users.UserFactory;

public class Facade {

	private AssemblyLine assemblyLine;
	private AssemblyLineObserver assemblyLineObserver;
	private CarModelCatalogue carModelCatalogue;
	private Clock clock;
	private ClockObserver clockObserver;
	private CustomCarModelCatalogue customCarModelCatalogue;
	private Logger logger;

	private OrderBook orderBook;
	private PartPicker picker;


	private UserBook userBook;
	private UserFactory userFactory;

	public Facade(Set<BindingRestriction> bindingRestrictions, Set<OptionalRestriction> optionalRestrictions) {
		this.clock = new Clock();

		this.clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);

		this.assemblyLine = new AssemblyLine(clockObserver, clock.getUnmodifiableClock());
		this.assemblyLineObserver = new AssemblyLineObserver();
		this.assemblyLine.attachObserver(assemblyLineObserver);

		this.logger = new Logger(2, clockObserver, assemblyLineObserver);

		this.carModelCatalogue = new CarModelCatalogue();
		this.customCarModelCatalogue = new CustomCarModelCatalogue();
		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		this.userFactory = new UserFactory();
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);

		CarModelCatalogueFiller carModelFiller = new CarModelCatalogueFiller();
		for (CarModelSpecification model : carModelFiller.getInitialModels()) {
			carModelCatalogue.addModel(model);
		}

		CustomCarModelCatalogueFiller customCarModelFiller = new CustomCarModelCatalogueFiller();
		Multimap<String, CustomCarModel> customModels = customCarModelFiller.getInitialModels();
		for(String model: customModels.keySet()){
			for(CustomCarModel customModel: customModels.get(model)){
				customCarModelCatalogue.addModel(model, customModel);
			}
		}
	}

	public void addPartToModel(String type, String part) {
		CarOptionCategory carOptionCategory = CarOptionCategory.valueOf(type);
		for(CarOption actualPart: picker.getModel().getSpecification().getCarParts().get(carOptionCategory)){
			if(actualPart.toString().equals(part))
				try {
					picker.getModel().addCarPart(actualPart);
				} catch (AlreadyInMapException e) { //ga nooit gebeuren omdat je 1x alle types overloopt
				}
		}
	}

	public void advanceAssemblyLine() throws ImmutableException, NoSuitableJobFoundException{
		assemblyLine.advance();
	}

	public void advanceClock(int time) {
		clock.advanceTime(time);

	}


	public boolean canAssemblyLineAdvance() {
		return assemblyLine.canAdvance();
	}

	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex, int taskIndex, int time) throws ImmutableException, NoSuitableJobFoundException {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(workBenchIndex);

		List<ITask> allTasks = workbench.getCurrentTasks();
		Iterator<ITask> iter = allTasks.iterator();
		while (iter.hasNext()) {
			ITask task = iter.next();
			if (task.isCompleted())
				iter.remove();
		}

		ITask task =  allTasks.get(taskIndex);
		for (IAction action : task.getActions()) {
			action.setCompleted(true);
		}

		if (this.canAssemblyLineAdvance()) {
			this.advanceAssemblyLine();
		}
		clock.advanceTime(time);
	}

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

	public void createNewModel(String realModel) {
		picker.setNewModel(carModelCatalogue.getCatalogue().get(realModel));
	}

	public List<AccessRight> getAccessRights() {
		return this.userBook.getCurrentUser().getAccessRights();
	}

	public String getAssemblyLineAsString() {
		return assemblyLine.toString();
	}

	public ArrayList<Integer> getBlockingWorkBenches() {
		return assemblyLine.getBlockingWorkBenches();
	}

	public String getCarModelFromCatalogue(String carModelName)
			throws IllegalArgumentException {
		for (String model : this.carModelCatalogue.getCatalogue().keySet()) {
			if (model.equalsIgnoreCase(carModelName)) {
				return model;
			}
		}
		throw new IllegalArgumentException();
	}

	public Set<String> getCarModels() {
		return this.carModelCatalogue.getCatalogue().keySet();
	}

	public Set<String> getCarPartTypes() {
		Set<String> types = new HashSet<>();
		for(CarOptionCategory type: CarOptionCategory.values()){
			types.add(type.toString());

		}
		return types;
	}

	public ArrayList<String> getCompletedOrders() {
		ArrayList<String> completedOrders = new ArrayList<String>();
		if (this.orderBook.getCompletedOrders().containsKey(
				userBook.getCurrentUser().getName())) {
			for (IOrder order : orderBook.getCompletedOrders().get(
					userBook.getCurrentUser().getName())) {
				completedOrders.add(order.toString());
			}
		}
		return completedOrders;
	}
	
	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public String getCurrentSchedulingAlgorithmAsString() {
		return this.assemblyLine.getCurrentSchedulingAlgorithmAsString();
	}

	public List<String> getCustomTasks() {
		List<String> tasks = new ArrayList<>();
		for(String model: customCarModelCatalogue.getCatalogue().keySet()){
			tasks.add(model);
		}
		return tasks;
	}

	public List<String> getOrderDetails(String orderString) {
		IOrder chosenOrder=null;
		for(IOrder order: orderBook.getPendingOrders().values()){
			if (order.toString().equals(orderString)) {
				chosenOrder = order;
			}
		}
		for(IOrder order: orderBook.getCompletedOrders().values()){
			if (order.toString().equals(orderString)) {
				chosenOrder = order;
			}
		}

		List<String> orderDetails = new ArrayList<>();
		if(chosenOrder!=null){
			orderDetails.add("Orderdetails:");
			orderDetails.add(chosenOrder.getQuantity() + " " + chosenOrder.getDescription());

			String carDetails = "Chosen carOptions: ";
			for( CarOptionCategory category: chosenOrder.getDescription().getCarParts().keySet() ){
				carDetails += chosenOrder.getDescription().getCarParts().get(category) + " ";
			}
			orderDetails.add(carDetails);

			orderDetails.add("Order Time: " + chosenOrder.getOrderTime().toString());
			try {
				orderDetails.add("(Expected) Completion Time: " + chosenOrder.getDeadline().toString());
			} catch (NotImplementedException e) {
				orderDetails.add("(Expected) Completion Time: " + chosenOrder.getEstimatedTime().toString());
			}
		}
		return orderDetails;
	}

	public Set<String> getParts(String type) {
		Set<String> parts = new HashSet<>();
		CarOptionCategory category = CarOptionCategory.valueOf(type);
		for(CarOption part: picker.getStillAvailableCarParts(category)){
			if(category.isOptional() || (picker.getModel().getForcedOptionalTypes().get(category)!=null && picker.getModel().getForcedOptionalTypes().get(category))){
				parts.add("Select nothing");
			}
			parts.add(part.toString());
		}
		return parts;
	}

	public ArrayList<String> getPendingOrders() {
		ArrayList<String> pendingOrders = new ArrayList<String>();
		Collection<IOrder> orders = orderBook
				.getPendingOrders().get(userBook.getCurrentUser().getName());
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

	public List<String> getSpecificCustomTasks(String taskDescription){
		List<String> tasks = new ArrayList<>();
		for(CustomCarModel model: customCarModelCatalogue.getCatalogue().get(taskDescription)){
			tasks.add(model.toString());
		}
		return tasks;
	}

	/**
	 * Get all of the available statistics.
	 * @return
	 * 		A List containing following statistics (in that order):
	 * 				average #cars produced / day
	 * 				median #cars produced / day
	 * 				#cars produced 2 days ago
	 * 				#cars produced 1 day ago
	 * 				average delay
	 * 				median delay
	 * 				second last delay
	 * 				last delay
	 */
	public List<String> getStatistics(){
		int detail = 2;		//aantal dagen waarvan gedetailleerde/exacte numbers voor moeten worden weeregegeven

		List<String> statistics = new ArrayList<String>();

		statistics.add("" + logger.averageDays());
		statistics.add("" + logger.medianDays());

		List<Integer> detailedDays = new ArrayList<Integer>(logger.getDetailedDays());
		while(detailedDays.size()<detail){ //als er geen statistics van genoeg dagen terug zijn: voeg 0 toe voor die dag(en)
			detailedDays.add(0,0);
		}
		for(int i = 0; i<detail ;i++){
			statistics.add("" + detailedDays.get(i));
		}

		statistics.add("" + logger.averageDelays());
		statistics.add("" + logger.medianDelays());

		List<Delay> detailedDelays = new ArrayList<Delay>(logger.getDetailedDelays());
		while(detailedDelays.size()<detail){ //als er niet genoeg delays zijn: voeg null toe voor die dag(en)
			detailedDelays.add(null);
		}
		for(int i = 0; i<detail ;i++){
			if(detailedDelays.get(i) == null){
				statistics.add("none");
			}
			else{
				statistics.add(detailedDelays.get(i).toString());
			}
		}



		return statistics;
	}

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

	public ArrayList<String> getWorkBenchNames() {
		ArrayList<String> workbenches = new ArrayList<String>();
		for (IWorkBench w : this.assemblyLine.getWorkbenches()) {
			workbenches.add(w.getWorkbenchName());
		}
		return workbenches;
	}

	public void login(String userName) throws RoleNotYetAssignedException,
	IllegalArgumentException {
		userBook.login(userName);
	}

	public void logout() {
		userBook.logout();
	}


	public String processCustomOrder(String model, String deadline) {
		CustomCarModel customModel = null;
		for(CustomCarModel custom: customCarModelCatalogue.getCatalogue().values()){
			if(custom.toString().equals(model)){
				customModel = custom;
			}
		}
		String[] split = deadline.split(",");
		int days = Integer.parseInt(split[0]);
		int hours = Integer.parseInt(split[1]);
		int minutes = Integer.parseInt(split[2]);
		minutes += hours*60;
		UnmodifiableClock deadlineClock = new UnmodifiableClock(days, minutes);
		CustomOrder order = new CustomOrder(userBook.getCurrentUser().getName(), customModel, 1, clock.getUnmodifiableClock(), deadlineClock);
		try {
			orderBook.addOrder(order, clock.getUnmodifiableClock());
		} catch (ImmutableException | NotImplementedException e) {
		}
		return order.getEstimatedTime().toString();
	}

	public String processOrder(int quantity) throws ImmutableException, IllegalStateException, NotImplementedException {
		CarModel carModel = picker.getModel();

		if(!carModel.isValid())
			throw new IllegalStateException();
		StandardOrder order = new StandardOrder(userBook.getCurrentUser().getName(), carModel, quantity, clock.getUnmodifiableClock());
		this.orderBook.addOrder(order, clock.getUnmodifiableClock());
		return order.getEstimatedTime().toString();
	}

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
	 * Switches the scheduling algorithm to use Batch with the given List of CarOptions.
	 */
	public void switchToBatch(List<CarOption> batch) {
		this.assemblyLine.switchToBatch(batch);
	}
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<CarOption>> getAllCarOptionsInPendingOrders() {
		return this.assemblyLine.getAllCarOptionsInPendingOrders();
	}
}
