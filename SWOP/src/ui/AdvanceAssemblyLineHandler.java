package ui;

import assembly.Task;
import assembly.WorkBench;
import users.GarageHolder;
import users.Manager;
import users.User;

public class AdvanceAssemblyLineHandler extends UseCaseHandler{

	public AdvanceAssemblyLineHandler(){
	}
	
	public boolean mayUseThisHandler(User user){
		if (user instanceof Manager) return true;
		else return false;
	}
	
	public void executeUseCase(User user){
		showAssemblyLine(user);
		advanceAssemblyLine(user);
	}

	private void showAssemblyLine(User user) {
		//TODO implement
	}
	
	private void advanceAssemblyLine(User user) {
		//TODO implement
	}

}
