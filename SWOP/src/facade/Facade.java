package facade;

import java.util.ArrayList;
import java.util.List;

import order.OrderBook;
import users.*;
import assembly.AssemblyLine;
import car.CarModel;
import car.CarModelCatalogue;
import car.CarModelCatalogueFiller;
import car.CarPartCatalogue;
import car.CarPartCatalogueFiller;
import clock.Clock;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getTasksOfChosenWorkBench(int workBenchIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex,
			int taskIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CarModel getCarModelFromCatalogue(String carModelName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] processOrder(String userName, String carModelName, int quantity) {
		// TODO Auto-generated method stub
		return null;
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
	public ArrayList<String> getPendingOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getCompletedOrders() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getAccessRights() {
		return this.user.getAccessRights();
	}

}
