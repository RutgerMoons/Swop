package users;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ui.UseCaseFlowController;

/**
 * Represents a single User. This is an abstract class, choose 1 of it's subclasses to instantiate. 
 *
 */
public abstract class User {

	private String name;
	private ArrayList<String> accessRights;
	/**
	 * Each user has a name. This name is given when a new User is constructed.
	 * 
	 * @throws IllegalArgumentException
	 * 			if name==null or isEmpty
	 */
	public User(String name, List<String> accessRights){
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
	 * @throws IllegalArgumentException
	 * 			if name==null || isEmpty.
	 */
	public void setName(String name) {
		if(name==null || name.isEmpty())
			throw new IllegalArgumentException();
		this.name = name;
	}
	
	/**
	 * returns the access rights
	 */
	public List<String> getAccessRights() {
		return Collections.unmodifiableList(accessRights);
	}
}
