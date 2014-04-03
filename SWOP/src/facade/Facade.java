package facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import order.Order;
import order.OrderBook;
import users.User;
import users.UserBook;
import users.UserFactory;
import assembly.Action;
import assembly.AssemblyLine;
import assembly.IAction;
import assembly.ITask;
import assembly.IWorkBench;
import assembly.Task;
import car.CarModel;
import car.CarModelCatalogue;
import car.CarModelCatalogueFiller;
import car.CarPartCatalogue;
import car.CarPartCatalogueFiller;
import clock.Clock;
import exception.NoCompletedOrdersException;
import exception.NoPendingOrdersException;
import exception.RoleNotYetAssignedException;

public class Facade implements IFacade {

	private AssemblyLine assemblyLine;
	private CarModelCatalogue carModelCatalogue;
	private CarPartCatalogue carPartCatalogue;
	private Clock clock;
	private OrderBook orderBook;
	private UserBook userBook;
	private UserFactory userFactory;

	public Facade() {
		this.clock = new Clock();
		this.assemblyLine = new AssemblyLine(clock);
		this.carPartCatalogue = new CarPartCatalogue();
		this.carModelCatalogue = new CarModelCatalogue(carPartCatalogue);
		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		this.userFactory = new UserFactory();

		CarPartCatalogueFiller carPartFiller = new CarPartCatalogueFiller(carPartCatalogue);
		carPartFiller.initializeCarParts();

		CarModelCatalogueFiller carModelFiller = new CarModelCatalogueFiller();
		for (CarModel carmodel : carModelFiller.getInitialModels()) {
			carModelCatalogue.addModel(carmodel);
		}
	}

	@Override
	public void advanceAssemblyLine() throws IllegalStateException {
		assemblyLine.advance();
	}

	@Override
	public void advanceClock(int time) {
		clock.advanceTime(time);

	}

	@Override
	public boolean canAssemblyLineAdvance() {
		return assemblyLine.canAdvance();
	}

	@Override
	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex,
			int taskIndex) {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(workBenchIndex);
		Task task = (Task) workbench.getCurrentTasks().get(taskIndex);
		for(IAction action: task.getActions()){
			Action act = (Action) action; 
			act.setCompleted(true);
		}
	}

	@Override
	public void createAndAddUser(String userName, String role) throws IllegalArgumentException {
		User currentUser = userFactory.createUser(userName, role);
		this.userBook.addUser(currentUser);
		try {
			this.userBook.login(userName);
		} catch (RoleNotYetAssignedException r) {
			System.err.println("Something went wrong at login, this shouldn't happen.");
		}
	}

	@Override
	public List<String> getAccessRights() {
		return this.userBook.getCurrentUser().getAccessRights();
	}
	
	@Override
	public String getAssemblyLineAsString() {
		return assemblyLine.toString();
	}


	@Override
	public ArrayList<Integer> getBlockingWorkBenches() {
		return assemblyLine.getBlockingWorkBenches();
	}

	@Override
	public String getCarModelFromCatalogue(String carModelName) throws IllegalArgumentException{
		for(String model : this.carModelCatalogue.getCatalogue().keySet()){
			if(model.equalsIgnoreCase(carModelName)){
				return model;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Set<String> getCarModels() {
		return this.carModelCatalogue.getCatalogue().keySet();
	}

	@Override
	public ArrayList<String> getCompletedOrders() {
		ArrayList<String> completedOrders = new ArrayList<String>();
		if(this.orderBook.getCompletedOrders().containsKey(userBook.getCurrentUser().getName())) {
			for(Order order: orderBook.getCompletedOrders().get(userBook.getCurrentUser().getName())){
				completedOrders.add(order.toString());
			}
		}	
		return completedOrders;
	}

	@Override
	public String getFutureAssemblyLineAsString() {
		return assemblyLine.getFutureAssemblyLine().toString();
	}
	
	@Override
	public ArrayList<String> getPendingOrders() {
		ArrayList<String> pendingOrders = new ArrayList<String>();
		List<Order> orders = (List<Order>) orderBook.getPendingOrders().get(userBook.getCurrentUser().getName());
		if(this.orderBook.getPendingOrders().containsKey(userBook.getCurrentUser().getName()) 
				&& !this.orderBook.getPendingOrders().get(userBook.getCurrentUser().getName()).isEmpty()){
			for (Order order : orders){
				pendingOrders.add(order.toString());
			}
		}
		return pendingOrders;

	}

	@Override
	public ArrayList<String> getTasksOfChosenWorkBench(int workBenchIndex) {
		IWorkBench workbench = this.assemblyLine.getWorkbenches().get(workBenchIndex);
		ArrayList<String> tasks = new ArrayList<String>();
		for(ITask task : workbench.getCurrentTasks()){
			if(!task.isCompleted()){
				tasks.add(task.toString());
			}
		}
		return tasks;
	}

	@Override
	public ArrayList<String> getWorkBenchNames() {
		ArrayList<String> workbenches = new ArrayList<String>();
		for(IWorkBench w : this.assemblyLine.getWorkbenches()){
			workbenches.add(w.getWorkbenchName());
		}
		return workbenches;
	}

	@Override
	public void login(String userName) throws RoleNotYetAssignedException, IllegalArgumentException {
		userBook.login(userName);
	}

	@Override
	public void logout() {
		userBook.logout();
	}

	@Override
	public int[] processOrder(String carModelName, int quantity) {
		CarModel carModel = this.carModelCatalogue.getCatalogue().get(carModelName);
		Order order = new Order(userBook.getCurrentUser().getName(), carModel, quantity);
		this.orderBook.addOrder(order);
		return order.getEstimatedTime();
	}

	@Override
	public void startNewDay() {
		clock.startNewDay();

	}

}
