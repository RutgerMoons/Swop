package domain.assembly.assemblyLine;

import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
/**
 * An interface representing an assembly line. An AssemblyLine 
 * is responsible for completing Jobs and advancing Jobs when a certain
 * Task of the Job is completed.
 */
public interface IAssemblyLine {

	
	/**
	 * Method for checking if the AssemblyLine can advance or certain tasks
	 * have to be finished first.
	 */
	public boolean canAdvance();
	
	/**
	 * Method for retrieving the IWorkBenches with unfinished Tasks.
	 * 
	 * @return	An unmodifiable list of IWorkBenches
	 */
	public List<IWorkBench> getBlockingWorkBenches();
	
	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return	An unmodifiable list of IWorkBenches
	 */
	public List<IWorkBench> getWorkBenches();
	
	/**
	 * Returns a powerset with all the VehicleOptions or sets 
	 * of VehicleOptions that occur in three or more pending Orders.
	 */
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders();

	/**
	 * Returns the current state of the AssemblyLine.
	 */
	public AssemblyLineState getState();

	/**
	 * Change the operational state of the AssemblyLine.
	 * 
	 * @param	state
	 * 			The state the assemblyLine has to be in
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when state is null
	 */
	public void setState(AssemblyLineState state);
	
	/**
	 * Get a list of responsibilities, which indicate the vehicles that the assemblyline can process.  
	 */
	public Set<VehicleSpecification> getResponsibilities();
}