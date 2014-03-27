package facade;

import java.util.ArrayList;

import car.CarModel;
import exception.RoleNotYetAssignedException;

public class Facade implements IFacade {

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
	public void login(String userName) throws RoleNotYetAssignedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAndAddUser(String userName, String role) {
		// TODO Auto-generated method stub
		
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
