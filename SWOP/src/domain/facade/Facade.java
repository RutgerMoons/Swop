package domain.facade;

import java.util.ArrayList;
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
import domain.car.CarOptionCategogry;
import domain.clock.Clock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.exception.RoleNotYetAssignedException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.ITask;
import domain.job.Task;
import domain.order.OrderBook;
import domain.order.StandardOrder;
import domain.restriction.PartPicker;
import domain.users.AccessRight;
import domain.users.User;
import domain.users.UserBook;
import domain.users.UserFactory;

public class Facade {

	private AssemblyLine assemblyLine;
	private CarModelCatalogue carModelCatalogue;
	private Clock clock;
	private OrderBook orderBook;
	private UserBook userBook;
	private UserFactory userFactory;
	private PartPicker picker;
	
	public Facade() {
		this.clock = new Clock();
		this.assemblyLine = new AssemblyLine(clock);
		this.carModelCatalogue = new CarModelCatalogue();
		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		this.userFactory = new UserFactory();
		picker = new PartPicker();
		
		CarModelCatalogueFiller carModelFiller = new CarModelCatalogueFiller();
		for (CarModelSpecification model : carModelFiller.getInitialModels()) {
			carModelCatalogue.addModel(model);
		}
	}

	public void advanceAssemblyLine() throws IllegalStateException {
		assemblyLine.advance();
	}

	public void advanceClock(int time) {
		clock.advanceTime(time);

	}

	public boolean canAssemblyLineAdvance() {
		return assemblyLine.canAdvance();
	}

	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex,
			int taskIndex) {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(
				workBenchIndex);
		Task task = (Task) workbench.getCurrentTasks().get(taskIndex);
		for (IAction action : task.getActions()) {
			Action act = (Action) action;
			act.setCompleted(true);
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
			for (StandardOrder order : orderBook.getCompletedOrders().get(
					userBook.getCurrentUser().getName())) {
				completedOrders.add(order.toString());
			}
		}
		return completedOrders;
	}

	public String getFutureAssemblyLineAsString() {
		return assemblyLine.getFutureAssemblyLine().toString();
	}

	public ArrayList<String> getPendingOrders() {
		ArrayList<String> pendingOrders = new ArrayList<String>();
		List<StandardOrder> orders = (List<StandardOrder>) orderBook
				.getPendingOrders().get(userBook.getCurrentUser().getName());
		if (this.orderBook.getPendingOrders().containsKey(
				userBook.getCurrentUser().getName())
				&& !this.orderBook.getPendingOrders()
						.get(userBook.getCurrentUser().getName()).isEmpty()) {
			for (StandardOrder order : orders) {
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

	public String processOrder(String carModelName, int quantity) throws ImmutableException {
		CarModel carModel = picker.getModel();
		
		StandardOrder order = new StandardOrder(userBook.getCurrentUser().getName(), carModel, quantity);
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
		for(CarOptionCategogry type: CarOptionCategogry.values()){
			types.add(type.toString());
		}
		return types;
	}

	public Set<String> getParts(String type) {
		Set<String> parts = new HashSet<>();
		for(CarOption part: picker.getStillAvailableCarParts(CarOptionCategogry.valueOf(type))){
			parts.add(part.toString());
		}
		return parts;
	}

	public void addPartToModel(String type, String part) {
		CarOptionCategogry carOptionCategogry = CarOptionCategogry.valueOf(type);
		for(CarOption actualPart: picker.getModel().getSpecification().getCarParts().get(carOptionCategogry)){
			if(actualPart.toString().equals(part))
				try {
					picker.getModel().addCarPart(actualPart);
				} catch (AlreadyInMapException e) { //ga nooit gebeuren omdat je 1x alle types overloopt
				}
		}
	}

}
