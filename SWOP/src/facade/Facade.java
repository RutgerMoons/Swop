package facade;

import java.util.ArrayList;

import order.OrderBook;
import users.*;
import users.UserBook;
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
	private UserBook userBook;
	private User user;
	
	public Facade() {
		this.clock = new Clock();
		this.assemblyLine = new AssemblyLine(clock);
		this.carPartCatalogue = new CarPartCatalogue();
		this.carModelCatalogue = new CarModelCatalogue(carPartCatalogue);
		this.orderBook = new OrderBook(assemblyLine);
		this.userBook = new UserBook();
		
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
		if(userName == null || role == null) {
			throw new IllegalArgumentException();
		}
		//TODO: visitor pattern
		this.user = new Manager(userName);
		this.userBook.addUser(this.user);
		
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

}
