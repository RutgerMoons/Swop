package domain.users;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import domain.exception.RoleNotYetAssignedException;

/**
 * Class that is responsible for keeping track of all the users in the system.
 */
public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	private User currentUser;
	private UserFactory factory;
	
	public UserBook(){
		factory = new UserFactory();
	}
	/**
	 * Returns the User Book
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public Map<String, User> getUserBook() {
		return Collections.unmodifiableMap(userBook);
	}
	
	
	/**
	 * Method for creating a new user given his name and role.
	 * @pre UserBook does not contain the combination of name and role.
	 */
	public void createUser(String userName, String role){
		this.factory.createUser(userName, role);
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
	
	/**
	 * Looks up the given name. If a user is found, it's returned,
	 * else a RoleNotYetAssignedException is thrown.
	 * 
	 * @param name
	 * 			The name of the user to look for
	 * 
	 * @throws RoleNotYetAssignedException
	 * 			Thrown when the given name has no role asigned to it yet
	 */
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
