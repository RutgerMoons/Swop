package domain.users;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * A class representing the creation of a User based on a given name
 * and role. The newly created user is automatically granted his access rights.
 */
public class UserFactory {

	/**
	 * Method to create the User given a name and a role. 
	 * 
	 * @param	name
	 * 			The name of the new User
	 * 
	 * @param 	role
	 * 			The role of the new User
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one of the parameters or both are null 
	 * 			or userName is empty
	 */
	public User createUser(String userName, String role)
			throws IllegalArgumentException {
		if (role == null || userName == null || userName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		switch (role) {
		case "garageholder":
			return createGarageHolder(userName);
		case "manager":
			return createManager(userName);
		case "worker":
			return createWorker(userName);
		case "custom car shop manager":
			return createCustomManager(userName);
		default:
			throw new IllegalArgumentException();
		}
	}

	private User createCustomManager(String userName) {
		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.CUSTOMORDER}));
		return new User(userName, accessRights);
	}

	private User createGarageHolder(String userName) {
		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.ORDER, AccessRight.SHOWDETAILS }));
		return new User(userName, accessRights);
	}

	private User createManager(String userName) {
		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] {AccessRight.STATISTICS, AccessRight.SWITCH_SCHEDULING_ALGORITHM, AccessRight.SWITCH_OPERATIONAL_STATUS }));
		return new User(userName, accessRights);
	}

	private User createWorker(String userName) {
		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.ASSEMBLE, AccessRight.CHECKLINE }));
		return new User(userName, accessRights);
	}
}