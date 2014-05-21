package domain.exception;

/**
 * A class representing an Exception thrown when a scheduling object 
 * doesn't have anything to schedule in the given time interval.
 */
public class NoMoreJobsInTimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
}
