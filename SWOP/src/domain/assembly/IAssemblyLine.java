package domain.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.clock.ImmutableClock;
import domain.exception.NoSuitableJobFoundException;
import domain.exception.UnmodifiableException;
import domain.job.IJob;
import domain.observer.AssemblyLineObserver;
import domain.order.CustomOrder;
import domain.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.vehicle.VehicleOption;

public interface IAssemblyLine {

	/**
	 * Add a workbench to the assemblyLine.
	 * 
	 * @param bench
	 *            The workbench you want to add.
	 * @throws IllegalArgumentException
	 *             Thrown when the parameter is null.
	 */
	public void addWorkBench(IWorkBench bench);
	
	/**
	 * This method advances the workbenches if all the workbenches are
	 * completed. It shifts the jobs to it's next workstation.
	 * It notifies its observers when an order is completed.
	 * 
	 * @throws NoSuitableJobFoundException
	 * 		Thrown when no job can be scheduled by the scheduler.
	 *  
	 * @throws UnmodifiableException 
	 * 		Thrown when an IOrder has no deadline yet.
	 * 
	 * @throws IllegalStateException
	 * 		Thrown when the assemblyLine cannot advance.
	 */
	public void advance() throws UnmodifiableException, NoSuitableJobFoundException;
	
	/**
	 * Method for checking if the assemblyLine can advance or certain tasks
	 * have to be finished first.
	 */
	public boolean canAdvance();
	
	/**
	 * This method converts an CustomOrder to a list of Jobs, 1 for each car. 
	 * The method returns the estimated time of completion for the order.
	 * 
	 * @param order
	 *            The order that needs to be converted to a list of jobs.
	 *             
	 * @throws UnmodifiableException 
	 * 		Thrown when an IOrder has no deadline yet.
	 * 
	 * @throws IllegalArgumentException
	 *       Thrown when the given parameter is null
	 */
	public int convertCustomOrderToJob(CustomOrder order) throws UnmodifiableException;
	
	/**
	 * This method converts an StandardOrder to a list of Jobs, 1 for each car.
	 * The method returns the estimated time of completion for the order.
	 * 
	 *             
	 * @throws UnmodifiableException 
	 * 		Thrown when an IOrder has no deadline yet.
	 * 
	 * @throws IllegalArgumentException
	 *       Thrown when the given parameter is null
	 */
	public int convertStandardOrderToJob(StandardOrder order) throws UnmodifiableException;
	
	/**
	 * Method for retrieving the workbenches with unfinished tasks.
	 */
	public ArrayList<Integer> getBlockingWorkBenches();
	
	/**
	 * Get all the pending jobs for this AssemblyLine.
	 * 
	 * @return A list representing the current jobs.
	 */
	public List<IJob> getCurrentJobs();
	
	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public Scheduler getCurrentSchedulingAlgorithm();
	
	/**
	 * Returns a list of all the possible scheduling algorithms as Strings.
	 */
	public ArrayList<String> getPossibleSchedulingAlgorithms();
	
	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return A list of IWorkBenches.
	 */
	public List<IWorkBench> getWorkbenches();
	
	
	/**
	 * The observer will be added to the notify list and is subscribed for
	 * every notification.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void attachObserver(AssemblyLineObserver observer);
	
	/**
	 * The observer will be no longer subscribed and will not be notified for future notifications.
	 * 
	 * @throws IllegalArgumentException
	 * 		Thrown when the parameter is null.
	 */
	public void detachObserver(AssemblyLineObserver observer);
	
	/**
	 * Method that notifies all the subscribers when an order is completed and sends the current
	 * time to every subscriber.
	 */
	public void notifyObserverCompleteOrder(ImmutableClock aClock);
	
	/**
	 * Method for asking the scheduler to switch to Fifo algorithm.
	 */
	public void switchToFifo();
	
	/**
	 * Method for asking the scheduler to switch to batch algorithm with as key
	 * the given list of CarOptions.
	 */
	public void switchToBatch(List<VehicleOption> vehicleOptions);
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders();
}
