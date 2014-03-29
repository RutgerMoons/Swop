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

	private Clock clock;
	private AssemblyLine assemblyLine;
	private CarPartCatalogue carPartCatalogue;
	private CarModelCatalogue carModelCatalogue;
	private OrderBook orderBook;
	private User user;
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
	public boolean canAssemblyLineAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Integer> getBlockingWorkBenches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void advanceAssemblyLine() {
		// TODO Auto-generated method stub

	}

	@Override
	public void advanceClock(int[] time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startNewDay() {
		// TODO Auto-generated method stub

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
	public String getCarModelFromCatalogue(String carModelName) throws IllegalArgumentException{
		for(String model : this.carModelCatalogue.getCatalogue().keySet()){
			if(model.equalsIgnoreCase(carModelName)){
				return model;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public int[] processOrder(String carModelName, int quantity) {
		CarModel carModel = this.carModelCatalogue.getCatalogue().get(carModelName);
		Order order = new Order(user.getName(), carModel, quantity);
		this.orderBook.addOrder(order);
		return order.getEstimatedTime();
	}

	@Override
	public void login(String userName) throws RoleNotYetAssignedException, IllegalArgumentException {
		if(userName == null) {
			throw new IllegalArgumentException();
		}
		if(!userBook.getUserBook().containsKey(userName)) {
			throw new RoleNotYetAssignedException();
		}

		this.user = userBook.getUserBook().get(userName);
	}

	@Override
	public void createAndAddUser(String userName, String role) throws IllegalArgumentException {
		this.user = userFactory.createUser(userName, role);
		this.userBook.addUser(this.user);
	}

	@Override
	public void logout() {
		this.user = null;
	}

	@Override
	public ArrayList<String> getPendingOrders() throws NoPendingOrdersException {
		ArrayList<String> pendingOrders = new ArrayList<String>();
		ArrayList<Order> orders = orderBook.getPendingOrders().get(user.getName());
		if(this.orderBook.getPendingOrders().containsKey(user) && !this.orderBook.getPendingOrders().get(user).isEmpty()){
			for (Order order : orders){
				pendingOrders.add(order.toString());
			}
			return pendingOrders;
		}
		else{
			throw new NoPendingOrdersException();
		}
	}

	@Override
	public ArrayList<String> getCompletedOrders() throws NoCompletedOrdersException {
		ArrayList<String> completedOrders = new ArrayList<String>();
		if(this.orderBook.getCompletedOrders().containsKey(user.getName())) {
			for(Order order: orderBook.getCompletedOrders().get(user.getName())){
				completedOrders.add(order.toString());
			}
			return completedOrders;
		}	
		else{
			throw new NoCompletedOrdersException();
		}
	}

	@Override
	public List<String> getAccessRights() {
		return this.user.getAccessRights();
	}

	@Override
	public Set<String> getCarModels() {
		return this.carModelCatalogue.getCatalogue().keySet();
	}



}
