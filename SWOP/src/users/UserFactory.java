package users;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * This class is used for creating all users based on name and role.
 * The user is automatically granted his access rights.
 *
 */
public class UserFactory {
	
	/**
	 * Uses name and role to create a user of the correct type with the correct access rights
	 */
	public User createUser(String userName, String role) throws IllegalArgumentException {
		if (role == null) {
			throw new IllegalArgumentException();
		}
		switch (role) {
		case "garageholder" : return createGarageHolder(userName);
		case "manager" : return createManager(userName);
		case "worker" : return createWorker(userName);
		default : throw new IllegalArgumentException();
		}
	}

	private User createGarageHolder(String userName) {
		if (userName == null) {
			throw new IllegalArgumentException();
		}
		ArrayList<String> accessRights = new ArrayList<String>(Arrays.asList(new String[] {"Order"}));
		return new GarageHolder(userName, accessRights);
	}

	private User createManager(String userName) {
		if (userName == null) {
			throw new IllegalArgumentException();
		}
		ArrayList<String> accessRights = new ArrayList<String>(Arrays.asList(new String[] {"Advance assemblyline"}));
		return new Manager(userName, accessRights);
	}
	
	private User createWorker(String userName) {
		if (userName == null) {
			throw new IllegalArgumentException();
		}
		ArrayList<String> accessRights = new ArrayList<String>(Arrays.asList(new String[] {"Complete assembly tasks"}));
		return new Worker(userName, accessRights);
	}

	
	
	
}
