package domain.exception;

/**
 * Class representing an Exception thrown when an SchedulingAlgorithm of an AssemblyLine didn't find
 * a suitable Job.
 */
public class NoSuitableJobFoundException extends IllegalStateException {

	private static final long serialVersionUID = -4069064046713112271L;

}
