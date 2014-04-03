package users;

import java.util.List;


/**
 * Represents a Worker that can work at a WorkBench and complete actions to construct a car. 
 *
 */
public class Worker extends User {

	public Worker(String name, List<AccessRight> accessRights) {
		super(name, accessRights);
	}
	
}
