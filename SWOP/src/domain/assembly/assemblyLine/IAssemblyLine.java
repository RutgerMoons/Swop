package domain.assembly.assemblyLine;

import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.VehicleSpecification;
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
	 * Add a workbench to the assemblyLine.
	 * 
	 * @param 	bench
	 *          The workbench you want to add
	 * 
	 * @throws 	IllegalArgumentException
	 *          Thrown when the parameter is null
	 * 
	 * @throws	UnmodifiableException
	 * 			Thrown when the AssemblyLine is an unmodifiable one
	 */
	public void addWorkBench(IWorkBench bench);
	
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
	 * Get a list of responsibilities, which indicate the vehicles that the AssemblyLine can process.  
	 */
	public Set<VehicleSpecification> getResponsibilities();
	
	/**
	 * Returns an list containing all the pending StandardJobs of the AssemblyLine.
	 */
	public List<IJob> getStandardJobs();
	
	/**
	 * Method for asking the Scheduler to switch to the algorithm the given
	 * creator can create.
	 * 
	 * @param 	creator
	 *          Responsible for creating the correct SchedulingAlgorithm
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator);
}