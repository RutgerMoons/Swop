package domain.observer;

import java.util.ArrayList;

import domain.clock.UnmodifiableClock;

public class AssemblyLineObserver {

	private ArrayList<LogsAssemblyLine> loggers;
	
	public AssemblyLineObserver() {
		this.loggers = new ArrayList<LogsAssemblyLine>();
	}
	
	public void attachLogger(LogsAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.add(logger);
	}
	
	public void detachLogger(LogsAssemblyLine logger) {
		if (logger == null) {
			throw new IllegalArgumentException();
		}
		loggers.remove(logger);
	}
	
	public void updateCompletedOrder(UnmodifiableClock estimatedTimeOfOrder) {
		for (LogsAssemblyLine logger : this.loggers) {
			logger.updateCompletedOrder(estimatedTimeOfOrder);
		}
	}
}
