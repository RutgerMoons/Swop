package domain.users;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import domain.exception.RoleNotYetAssignedException;

/**
 * A class representing a book with all the Users. It's responsible for keeping track of all the Users in the system.
 */
public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	private User currentUser;
	private UserFactory factory;
	
	public UserBook(){
		factory = new UserFactory();
	}
	
	/**
	 * Returns the User Book.
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public Map<String, User> getUserBook() {
		return Collections.unmodifiableMap(userBook);
	}
	
	
	/**
	 * Method for creating a new user given his name and role.
	 * 
	 * @param	userName
	 * 			The user's name
	 * 
	 * @param	role
	 * 			The role the user performs
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the user is already in the UserBook
	 */
	public void createUser(String userName, String role){
		User user = this.factory.createUser(userName, role);
		addUser(user);
	}
	
	/**
	 * Method for adding a new user to the UserBook. The method
	 * checks if the user is a valid user.
	 * 
	 * @param	user
	 * 			The user you want to register to the UserBook
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when user is null or the user is already in the UserBook
	 */
	public void addUser(User user) {
		if(user == null){
			throw new IllegalArgumentException("user can't be null");
		}
		else if(userBook.containsKey(user.getName())){
			throw new IllegalArgumentException("user is already registered in the userBook");
		}
		else{
			userBook.put(user.getName(), user);
		}
	}
	
	/**
	 * Returns the current user.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when there is no user logged in
	 */
	public User getCurrentUser() {
		if(currentUser == null){
			throw new IllegalArgumentException();
		}
		return currentUser;
	}
	
	/**
	 * Looks up the given name. If a User is found, it's returned,
	 * else a RoleNotYetAssignedException is thrown.
	 * 
	 * @param 	name
	 * 			The name of the user to look for
	 * 
	 * @throws 	RoleNotYetAssignedException
	 * 			Thrown when the given name has no role assigned to it yet
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the name is null
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
	 * Logs the User out.
	 */
	public void logout() {
		this.currentUser = null;
	}
}