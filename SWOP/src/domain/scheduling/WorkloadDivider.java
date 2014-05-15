package domain.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.exception.UnmodifiableException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.OrderBookObserver;
import domain.observer.observes.ObservesOrderBook;
import domain.order.IOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.vehicleOption.VehicleOption;

public class WorkloadDivider implements ObservesOrderBook {

	private List<AssemblyLine> assemblyLines;

	public WorkloadDivider(	List<AssemblyLine> listOfAssemblyLines, OrderBookObserver orderBookObserver, AssemblyLineObserver assemblyLineObserver) {
		if (	listOfAssemblyLines == null || orderBookObserver == null || assemblyLineObserver == null) {
			throw new IllegalArgumentException();
		}
		orderBookObserver.attachLogger(this);
		this.assemblyLines = listOfAssemblyLines;
		// attach the assemblyLineObserver to all assemblyLines
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			assemblyLine.attachObserver(assemblyLineObserver);
		}
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
			assemblyLines.add(assemblyLine);
		}
		return Collections.unmodifiableList(assemblyLines);
	}

	private List<AssemblyLine> getAssemblyLines(AssemblyLineState state) {
		ArrayList<AssemblyLine> operational = new ArrayList<>();
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			if (assemblyLine.getState() == state) {
				operational.add(assemblyLine);
			}
		}
		return Collections.unmodifiableList(operational);
	}

	public List<IAssemblyLine> getOperationalUnmodifiableAssemblyLines() {
		List<AssemblyLine> operational = getAssemblyLines(AssemblyLineState.OPERATIONAL);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : operational) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	public List<IAssemblyLine> getMaintenanceUnmodifiableAssemblyLines() {
		List<AssemblyLine> maintenance = getAssemblyLines(AssemblyLineState.MAINTENANCE);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : maintenance) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	public List<IAssemblyLine> getBrokenUnmodifiableAssemblyLines() {
		List<AssemblyLine> broken = getAssemblyLines(AssemblyLineState.BROKEN);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : broken) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
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
			for (VehicleOption part : order.getVehicleOptions()) {
				ITask task = new Task(part.getTaskDescription());
				IAction action = new Action(part.getActionDescription());
				task.addAction(action);
				job.addTask(task);
			}
			jobs.add(job);
		}
		return Collections.unmodifiableList(jobs);
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

		//1: filter operational assemblyLines
		//2: filter assemblyLines that can complete job
		//3: kies assemblyLine met laagste workload
		//4: schedule de job bij die assemblyLine
		//assemblyLine.schedule(job)
		//5: kijken of de assemblyline stilstaat (can advancen)
		AssemblyLine scheduleHere = null;
		for(AssemblyLine line: assemblyLines){
			//Checken: Operational && kan job verwerken.
			if(line.getState().equals(AssemblyLineState.OPERATIONAL) && job.canBeHandled(line.getResponsibilities()) ){
				//kijken naar workload
				if(scheduleHere!=null && scheduleHere.getCurrentJobs().size()<line.getCurrentJobs().size()){
					scheduleHere = line;
				}else if(scheduleHere==null){
					scheduleHere = line;
				}
			}
		}
		if(scheduleHere!=null){
			scheduleHere.schedule(job);
			if(scheduleHere.canAdvance()){
				scheduleHere.advance();
			}
		}
	}

	/**
	 * Matches the given assemblyLine to one of its own. If a match is found, 
	 * the request is passed on.
	 * 
	 * @param	assemblyLine to be matched
	 */
	public void completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, ITask task){
		for (AssemblyLine line : this.assemblyLines) {
			if (line.equals(assemblyLine)) {
				line.completeChosenTaskAtChosenWorkBench(workbench, task);
				break;
			}
		}
	}

	/**
	 * returns a powerset with all the CarOptions or sets of CarOptions that occur in three or more pending orders.
	 */
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		HashSet<VehicleOption> set = new HashSet<>();
		ArrayList<IJob> jobs = new ArrayList<>();
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			jobs.addAll(assemblyLine.getStandardJobs());
		}

		// get all the CarOptions that occur in the pending orders
		for (IJob job : jobs) {
			for (VehicleOption o : job.getVehicleOptions()) {
				set.add(o);
			}
		}

		// get all the CarOptions that occur in the pending orders 3 or more times
		HashSet<VehicleOption> threeOrMoreTimes = new HashSet<>();
		for (VehicleOption option : set) {
			int counter = 0;
			for (IJob job : jobs) {
				if (job.getOrder().getDescription().getVehicleOptions().values().contains(option)) {
					counter++;
				}
			}
			if (counter >= 3) {
				threeOrMoreTimes.add(option);
			}
		} 

		// get all the sets of CarOptions that occur in the pending orders 3 or more times
		Set<Set<VehicleOption>> toReturn = new HashSet<Set<VehicleOption>>();
		Set<Set<VehicleOption>> powerSet = Sets.powerSet(threeOrMoreTimes);
		for (Set<VehicleOption> subset : powerSet) {
			if (subset.size() <= 0) {
				continue;
			}
			int counter = 0;
			for (IJob job : jobs) {
				if (job.getOrder().getDescription().getVehicleOptions().values().containsAll(subset)) {
					counter++;
				}
			}
			if (counter >= 3) {
				toReturn.add(subset);
			}
		}
		return Collections.unmodifiableSet(toReturn);
	}

	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state) {
		for(AssemblyLine line: assemblyLines){
			if(assemblyLine.equals(line)){
				line.setState(state);
				if(!line.getState().equals(AssemblyLineState.OPERATIONAL)){
					List<IJob> jobs = line.removeUnscheduledJobs();
					for(IJob job: jobs){
						divide(job);
					}
				}
			}
		}
	}

	/**
	 * Get the workbenches which are blocking the AssemblyLine from advancing.
	 * @return
	 * 			A list of indexes of the workbenches that are blocking the AssemblyLine from advancing.
	 */
	public List<IWorkBench> getBlockingWorkBenches(IAssemblyLine assemblyLine) {
		for (AssemblyLine line : this.assemblyLines) {
			if (line.equals(assemblyLine)) {
				return Collections.unmodifiableList(line.getBlockingWorkBenches());
			}
		}
		return new ArrayList<>();
	}

}
