package domain.scheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
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
import domain.vehicle.vehicleOption.VehicleOption;

public class WorkloadDivider implements ObservesOrderBook {

	private List<AssemblyLine> assemblyLines;
	
	public WorkloadDivider(	List<AssemblyLine> listOfAssemblyLines, OrderBookObserver orderBookObserver, AssemblyLineObserver assemblyLineObserver) {
		if (	listOfAssemblyLines == null || orderBookObserver == null || assemblyLineObserver == null) {
			throw new IllegalArgumentException();
		}
		this.assemblyLines = listOfAssemblyLines;
		orderBookObserver.attachLogger(this);
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
			for (VehicleOption part : order.getVehicleOptions()) {
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
		
		//1: filter operational assemblyLines
		//2: filter assemblyLines that can complete job
		//3: kies assemblyLine met laagste workload
		//4: schedule de job bij die assemblyLine
		//assemblyLine.schedule(job)
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
		return toReturn;
	}

	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state) {
		int index = assemblyLines.indexOf(assemblyLine);
		assemblyLines.get(index).setState(state);
	}
	
}
