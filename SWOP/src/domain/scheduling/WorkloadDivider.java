package domain.scheduling;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observes.ObservesOrderBook;
import domain.order.IOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.vehicleOption.IVehicleOption;

public class WorkloadDivider implements ObservesOrderBook {

	private ArrayList<AssemblyLine> assemblyLines;
	
	public WorkloadDivider(ArrayList<AssemblyLine> assemblyLines) {
		if (assemblyLines == null) {
			throw new IllegalArgumentException();
		}
		this.assemblyLines = assemblyLines;
	}
	
	public String getCurrentSchedulingAlgorithm() {
		if (this.assemblyLines.size() <= 0) {
			return "There are no assemblyLines at the moment.";
		} else {
			return this.assemblyLines.get(0).getCurrentSchedulingAlgorithm();
		}
	}
	
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			assemblyLine.switchToSchedulingAlgorithm(creator);
		}	
	}
	
	public List<IAssemblyLine> getAssemblyLines() {
		ArrayList<IAssemblyLine> assemblyLines = new ArrayList<>();
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			assemblyLines.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return assemblyLines;
	}
	
	private ArrayList<AssemblyLine> getAssemblyLines(AssemblyLineState state) {
		ArrayList<AssemblyLine> operational = new ArrayList<>();
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			if (assemblyLine.getState() == state) {
				operational.add(assemblyLine);
			}
		}
		return operational;
	}
	
	public List<IAssemblyLine> getOperationalUnmodifiableAssemblyLines() {
		ArrayList<AssemblyLine> operational = getAssemblyLines(AssemblyLineState.OPERATIONAL);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : operational) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return unmodifiable;
	}
	
	public List<IAssemblyLine> getMaintenanceUnmodifiableAssemblyLines() {
		ArrayList<AssemblyLine> maintenance = getAssemblyLines(AssemblyLineState.MAINTENANCE);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : maintenance) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return unmodifiable;
	}
	
	public List<IAssemblyLine> getBrokenUnmodifiableAssemblyLines() {
		ArrayList<AssemblyLine> broken = getAssemblyLines(AssemblyLineState.BROKEN);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : broken) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return unmodifiable;
	}
	
	/**
	 * This method converts an order to a list of Jobs, 1 for each car and sets
	 * the minimal index for each job. This index refers to the first workbench that is
	 * needed to complete the job. The method returns a list of Jobs.
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
	private List<IJob> convertOrderToJobs(IOrder order) {
		List<IJob> jobs = new ArrayList<>();
		for (int i = 0; i < order.getQuantity(); i++) {
			IJob job = new Job(order);
			for (IVehicleOption part : order.getVehicleOptions()) {
				ITask task = new Task(part.getTaskDescription());
				IAction action = new Action(part.getActionDescription());
				task.addAction(action);
				job.addTask(task);
			}
			jobs.add(job);
		}
		return new ImmutableList.Builder<IJob>().addAll(jobs).build();
	}
	
	/**
	 * This method receives an order, let another method convert it to jobs
	 * then distributes these jobs amongst its AssemblyLines.
	 * 
	 * @param	order to be converted into jobs
	 */
	@Override
	public void processNewOrder(IOrder order) {
		if (order == null) {
			throw new IllegalArgumentException();
		}
		List<IJob> jobs = convertOrderToJobs(order);
		for (IJob job : jobs) {
			divide(job);
		}
	}
	
	private void divide(IJob job) {
		//TODO:
		//kies juiste assemblyLine
		//assemblyLine.schedule(job)
	}
	
	
	
	
}
