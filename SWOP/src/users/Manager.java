package users;

import java.util.List;

/**
 * Represents a manager of an AssemblyLine, who can interact with the system. 
 *
 */
public class Manager extends User {

	public Manager(String name, List<AccessRight> accessRights) {
		super(name, accessRights);
	}



	
}
