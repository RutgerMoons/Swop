package ui;

import users.User;

/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseHandler {
	
	/**
	 * Indicates if the user is authorized to be part of the use case.
	 * @param user
	 * 			The user of which we want to get to know if he's authorized.
	 * @return
	 * 			A boolean indicating if the user is authorized.
	 */
	public abstract boolean mayUseThisHandler(User user);
	
	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	public abstract void executeUseCase(User user);
}
