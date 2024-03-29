package domain.users;

import java.util.Collections;
import java.util.List;

/**
 * A class representing a single User. A User has a name and a list of AccessRights. 
 */
public class User {

	private final String name;
	private List<AccessRight> accessRights;

	/**
	 * Each user has a name. This name is given when a new User is constructed. 
	 * A user might also have some AccessRights it can use to perform some use case.
	 * 
	 * @param	name
	 * 			The name of the new User
	 * 
	 * @param 	accessRights
	 * 			List of AccessRights for the user 
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when name is null or empty or thrown when accessRights is null
	 */
	public User(String name, List<AccessRight> accessRights)
			throws IllegalArgumentException {
		if (name == null || name.isEmpty() || accessRights == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.accessRights = accessRights;
	}

	/**
	 * Returns the name of the user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a list of the AccessRights.
	 */
	public List<AccessRight> getAccessRights() {
		return Collections.unmodifiableList(accessRights);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessRights.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (!accessRights.equals(other.accessRights))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}
}