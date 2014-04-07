package domain.observer;

import domain.log.Logger;

public class Observer {
		
	protected Logger logger;
	
	public Observer(Logger logger) {
		if (logger == null) {
			throw new NullPointerException();
		}
		this.logger = logger;
	}
	
}
