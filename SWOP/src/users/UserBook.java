package users;

import java.util.HashMap;

public class UserBook {
	
	private HashMap<String,User> userBook = new HashMap<String,User>();
	
	public UserBook(){
	}
	
	/**
	 * Returns the User Book
	 * 
	 * @return A HashMap containing the values in the user book
	 */
	public HashMap<String,User> getUserBook() {
		return userBook;
	}
	
	public void addUser(User user) throws NullPointerException, IllegalArgumentException {
		if(user == null) throw new NullPointerException("user can't be null");
		else if(userBook.containsKey(user.getName())) throw new IllegalArgumentException("user is already registered in the userBook");
		else{
			userBook.put(user.getName(), user);
		}
	}
}
