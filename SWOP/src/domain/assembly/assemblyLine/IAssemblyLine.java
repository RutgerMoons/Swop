package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.job.job.IJob;
import domain.scheduling.Scheduler;
import domain.vehicle.vehicleOption.VehicleOption;

public interface IAssemblyLine {

	
	/**
	 * Method for checking if the assemblyLine can advance or certain tasks
	 * have to be finished first.
	 */
	public boolean canAdvance();
	
	/**
	 * Method for retrieving the workbenches with unfinished tasks.
	 */
	public ArrayList<Integer> getBlockingWorkBenches();
	
	/**
	 * Get all the pending jobs for this AssemblyLine
	 * 
	 * @return 	A list representing the current jobs
	 */
	public List<IJob> getCurrentJobs();
	
	/**
	 * Returns the currently used Scheduling Algorithm Type as String
	 */
	public Scheduler getCurrentScheduler();
	
	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return	A list of IWorkBenches
	 */
	public List<IWorkBench> getWorkbenches();
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders();

	/**
	 * 
	 * @return	the state of the AssemblyLine
	 */
	public AssemblyLineState getState();

	/**
	 * Change the operational state of the assemblyLine
	 * 
	 * @param	state
	 * 			The state the assemblyLine has to be in
	 */
	public void setState(AssemblyLineState state);
}
