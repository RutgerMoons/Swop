package ui;


/**
 * 
 * Defines the program flow for a specific use case.
 *
 */
public abstract class UseCaseFlowController {
	
	private final String accessRight;
	
	public UseCaseFlowController(String accessRight){
		if (accessRight == null) {
			throw new NullPointerException();
		}
		this.accessRight = accessRight;
	}
	
	/**
	 * Execute the use case.
	 * @param user
	 * 			primary actor in this use case
	 */
	public abstract void executeUseCase();

	public String getAccessRight() {
		return accessRight;
	}
	
}
