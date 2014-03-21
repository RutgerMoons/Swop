package code.users;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


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
		return copy(userBook);
	}
	
	/**
	 * Get a deep clone of a HashMap<String,User>.
	 * @param map
	 * 			The original data.
	 * @return
	 * 			A HashMap<String,User>.
	 */
	public HashMap<String, User> copy(HashMap<String, User> map){
		HashMap<String, User> copy = new HashMap<>();
		
		Set<Entry<String, User>> dataSet = map.entrySet();
		for(Entry<String, User> entry: dataSet){
			copy.put(entry.getKey(), entry.getValue());
		}
		return copy;
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
