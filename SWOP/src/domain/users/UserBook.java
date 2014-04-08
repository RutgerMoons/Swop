package domain.users;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
<<<<<<< HEAD:SWOP/src/users/UserBook.java
=======

import domain.exception.RoleNotYetAssignedException;
>>>>>>> origin/rutger:SWOP/src/domain/users/UserBook.java



/**
 * Class that is responsible for keeping track of all the users in the system.
 */
public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	private User currentUser;
	
	/**
	 * Returns the User Book
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public Map<String, User> getUserBook() {
		return Collections.unmodifiableMap(userBook);
	}
	
	/**
	 * Method for adding a new user to the hashmap. The method
	 * checks if the user is a valid user. When the user is already in the hashmap/userbook,
	 * nothing happens.
	 */
	public void addUser(User user) {
		if(user == null) throw new IllegalArgumentException("user can't be null");
		else if(userBook.containsKey(user.getName())) throw new IllegalArgumentException("user is already registered in the userBook");
		else{
			userBook.put(user.getName(), user);
		}
	}
	
	/**
	 * Returns the current user
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void login(String name) throws RoleNotYetAssignedException {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		if (!userBook.containsKey(name)) {
			throw new RoleNotYetAssignedException();
		}
		
		this.currentUser = userBook.get(name); 
	}
	
	/**
	 * logs the user out
	 */
	public void logout() {
		this.currentUser = null;
	}
	
}
