package users;

import java.util.List;

/**
 * Represents a GarageHolder who can interact with the system and order new Cars to be produced.
 *
 */
public class GarageHolder extends User {

	public GarageHolder(String name, List<String> accessRights) {
		super(name, accessRights);
	}

}
