package domain.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.car.CarModel;
import domain.car.CarModelCatalogue;
import domain.car.CarModelCatalogueFiller;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.Clock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.NotImplementedException;
import domain.exception.RoleNotYetAssignedException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.ITask;
import domain.job.Task;
import domain.log.Logger;
import domain.observer.AssemblyLineObserver;
import domain.observer.ClockObserver;
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
	private CarModelCatalogue carModelCatalogue;
	private Clock clock;
	private ClockObserver clockObserver;
	private OrderBook orderBook;
	private UserBook userBook;
	private UserFactory userFactory;

	private AssemblyLineObserver assemblyLineObserver;
	private Logger logger;


	private PartPicker picker;
	
	public Facade(Set<BindingRestriction> bindingRestrictions, Set<OptionalRestriction> optionalRestrictions) {
		this.clock = new Clock();

		this.clockObserver = new ClockObserver();
		this.clock.attachObserver(clockObserver);
		
		this.assemblyLine = new AssemblyLine(clockObserver);
		this.assemblyLineObserver = new AssemblyLineObserver();
		this.assemblyLine.attachObserver(assemblyLineObserver);
		
		this.logger = new Logger(2, clockObserver, assemblyLineObserver);
		
		this.carModelCatalogue = new CarModelCatalogue();

		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		this.userFactory = new UserFactory();
		picker = new PartPicker(bindingRestrictions, optionalRestrictions);
		
		CarModelCatalogueFiller carModelFiller = new CarModelCatalogueFiller();
		for (CarModelSpecification model : carModelFiller.getInitialModels()) {
			carModelCatalogue.addModel(model);
		}
	}

	public void advanceAssemblyLine() throws IllegalStateException, ImmutableException, NoSuitableJobFoundException, NotImplementedException {
		assemblyLine.advance();
	}

	public void advanceClock(int time) {
		clock.advanceTime(time);

	}

	public boolean canAssemblyLineAdvance() {
		return assemblyLine.canAdvance();
	}


	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex, int taskIndex) throws IllegalStateException, ImmutableException, NoSuitableJobFoundException, NotImplementedException {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(workBenchIndex);

		Task task = (Task) workbench.getCurrentTasks().get(taskIndex);
		for (IAction action : task.getActions()) {
			Action act = (Action) action;
			act.setCompleted(true);
		}
		if (this.canAssemblyLineAdvance()) {
			this.advanceAssemblyLine();
		}
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

	public String processOrder(int quantity) throws ImmutableException, IllegalStateException, NotImplementedException {
		CarModel carModel = picker.getModel();
		
		if(!carModel.isValid())
			throw new IllegalStateException();
		StandardOrder order = new StandardOrder(userBook.getCurrentUser().getName(), carModel, quantity, clock.getUnmodifiableClock());
		this.orderBook.addOrder(order);
		return order.getEstimatedTime().toString();
	}

	public void startNewDay() {
		clock.startNewDay();

	}

	public void createNewModel(String realModel) {
		picker.setNewModel(carModelCatalogue.getCatalogue().get(realModel));
	}

	public Set<String> getCarPartTypes() {
		Set<String> types = new HashSet<>();
		for(CarOptionCategory type: CarOptionCategory.values()){
			types.add(type.toString());
			
		}
		return types;
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

	public List<String> getOrderDetails(String orderString) {
		IOrder chosenOrder=null;
		for(IOrder order: orderBook.getPendingOrders().values()){
			if (order.toString().equals(chosenOrder)) {
				chosenOrder = order;
			}
		}
		for(IOrder order: orderBook.getCompletedOrders().values()){
			if (order.toString().equals(chosenOrder)) {
				chosenOrder = order;
			}
		}
		
		List<String> orderDetails = new ArrayList<>();
		if(chosenOrder!=null){
			orderDetails.add(chosenOrder.getOrderTime().toString());
			try {
				orderDetails.add(chosenOrder.getDeadline().toString());
			} catch (NotImplementedException e) {
				orderDetails.add(chosenOrder.getEstimatedTime().toString());
			}
		}
		return orderDetails;
	}

}
