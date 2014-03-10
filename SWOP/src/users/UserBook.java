package users;

import java.util.HashMap;
/**
 * Class that is responsible for keeping track of all the users in the system.
 */
public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	
	/**
	 * Returns the User Book
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public HashMap<String,User> getUserBook() {
		return userBook;
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
}
