package ui;

import order.OrderBook;
import users.GarageHolder;
import users.User;
import users.UserBook;
import users.Worker;
import assembly.AssemblyLine;
import assembly.Task;
import assembly.WorkBench;
import car.Catalogue;
import clock.Clock;

public class AssembleHandler extends UseCaseHandler{
	
	public AssembleHandler(){
		
	}

	public boolean mayUseThisHandler(User user){
		if (user instanceof Worker) return true;
		else return false;
	}
	
	public void executeUseCase(User user){
		WorkBench workBench = chooseWorkBench(user);
		Task task = chooseTask(user,workBench);
		endTask(user,task);
	}
	
	private WorkBench chooseWorkBench(User user){
		return null;
	}
	
	private Task chooseTask(User user, WorkBench workbench){
		return null;
	}
	
	private void endTask(User user, Task task){
		
	}
	
}