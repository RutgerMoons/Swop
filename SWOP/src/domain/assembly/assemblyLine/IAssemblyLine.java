package domain.assembly.assemblyLine;

import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.vehicle.VehicleSpecification;
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
	public List<IWorkBench> getBlockingWorkBenches();
	
	/**
	 * Get the IWorkBenches that are assigned to this AssemblyLine.
	 * 
	 * @return	A list of IWorkBenches
	 */
	public List<IWorkBench> getWorkBenches();
	
	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders();

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
