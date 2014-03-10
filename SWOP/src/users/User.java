package users;

import java.util.ArrayList;

import ui.*;

/**
 * Represents a single User. This is an abstract class, choose 1 of it's subclasses to instantiate. 
 *
 */
public abstract class User {

	private String name;
	/**
	 * Each user has a name. This name is given when a new User is constructed.
	 */
	public User(String name){
		this.setName(name);
	}

	/**
	 * Retrieving the name of the user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method for assigning a new name to the user.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Method for checking which handler is the right handler for the current user.
	 */
	public UseCaseHandler getRightHandler(ArrayList<UseCaseHandler> handlers){
		for (int i = 0; i < handlers.size(); i++) {
			if (handlers.get(i).mayUseThisHandler(this)) return handlers.get(i);
		}
		return null;
	}
}
