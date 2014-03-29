package facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import car.CarModel;
import exception.NoCompletedOrdersException;
import exception.NoPendingOrdersException;
import exception.RoleNotYetAssignedException;

public interface IFacade {

	public void advanceAssemblyLine() throws IllegalStateException;
	
	public void advanceClock(int time);
	
	public boolean canAssemblyLineAdvance();
	
	public void completeChosenTaskAtChosenWorkBench(int workBenchIndex, int taskIndex);
	
	public void createAndAddUser(String userName, String role);
	
	public List<String> getAccessRights();
	
	public String getAssemblyLineAsString();
	
	public ArrayList<Integer> getBlockingWorkBenches();
	
	public String getCarModelFromCatalogue(String carModelName) throws IllegalArgumentException;
	
	public Set<String> getCarModels();
	
	public ArrayList<String> getCompletedOrders() throws NoCompletedOrdersException;
	
	public String getFutureAssemblyLineAsString();
	
	public ArrayList<String> getPendingOrders() throws NoPendingOrdersException;
	
	public ArrayList<String> getTasksOfChosenWorkBench(int workBenchIndex);
	
	public ArrayList<String> getWorkBenchNames();
	
	/*
	 * login:
	 * 		vraag naam in ui, geef door aan IFacade,
	 * 		IFacade zoekt op:
	 * 			found: set user to found user
	 * 			not found: throw RoleNotYetAssignedException
	 * 				ui catcht vraag role op, geef door aan IFacade,
	 * 				IFacade krijgt de rol door en maakt nieuwe user aan en slaagt op in UserBook
	 * 
	 */
	public void login(String userName) throws RoleNotYetAssignedException;
	
	public void logout();
	
	/**
	 * creates a new order and returns the estimated time
	 */
	public int[] processOrder(String carModelName, int quantity);
	
	public void startNewDay();
}
