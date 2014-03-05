package ui;

import users.User;

public abstract class UseCaseHandler {
	
	
	public UseCaseHandler(){
	}
	
	public abstract boolean mayUseThisHandler(User user);
	
	public abstract void executeUseCase(User user);
}
