package users;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	
	/**
	 * Returns the User Book
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public HashMap<String,User> getUserBook() {
		return clone(userBook);
	}
	
	/**
	 * Get a deep clone of a HashMap<String,User>.
	 * @param map
	 * 			The original data.
	 * @return
	 * 			A HashMap<String,User>.
	 */
	public HashMap<String, User> clone(HashMap<String, User> map){
		HashMap<String, User> clone = new HashMap<>();
		
		Set<Entry<String, User>> dataSet = map.entrySet();
		for(Entry<String, User> entry: dataSet){
			clone.put(entry.getKey(), entry.getValue());
		}
		return clone;
	}
	
	/**
	 * Add a User to the UserBook.
	 * @param user
	 * 			The User you want to add.
	 */
	public void addUser(User user) {
		if(user == null) throw new IllegalArgumentException("user can't be null");
		else if(userBook.containsKey(user.getName())) throw new IllegalArgumentException("user is already registered in the userBook");
		else{
			userBook.put(user.getName(), user);
		}
	}
	
	
}
