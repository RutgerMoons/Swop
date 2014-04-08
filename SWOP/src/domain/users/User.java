package domain.users;

import java.util.Collections;
import java.util.List;

/**
 * Represents a single User. This is an abstract class, choose 1 of it's
 * subclasses to instantiate.
 * 
 */
public class User {

	private final String name;
	private List<AccessRight> accessRights;

	/**
	 * Each user has a name. This name is given when a new User is constructed.
	 * 
	 * @throws IllegalArgumentException
	 *             if name == null or isEmpty or AccessRights == null
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
	 * Retrieving the name of the user.
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the access rights
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
