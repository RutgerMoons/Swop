package domain.users;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * This class is used for creating all users based on name and role. The user is
 * automatically granted his access rights.
 * 
 */
public class UserFactory {

	/**
	 * Uses name and role to create a user of the correct type with the correct
	 * access rights
	 */
	public User createUser(String userName, String role)
			throws IllegalArgumentException {
		if (role == null) {
			throw new IllegalArgumentException();
		}
		switch (role) {
		case "garageholder":
			return createGarageHolder(userName);
		case "manager":
			return createManager(userName);
		case "worker":
			return createWorker(userName);
		default:
			throw new IllegalArgumentException();
		}
	}

	private User createGarageHolder(String userName) {

		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.ORDER }));
		return new User(userName, accessRights);
	}

	private User createManager(String userName) {

		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.ADVANCE }));
		return new User(userName, accessRights);
	}

	private User createWorker(String userName) {

		ArrayList<AccessRight> accessRights = new ArrayList<AccessRight>(
				Arrays.asList(new AccessRight[] { AccessRight.ASSEMBLE }));
		return new User(userName, accessRights);
	}

}
