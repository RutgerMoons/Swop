package domain.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.assemblyLine.MaintenanceTimeManager;
import domain.assembly.assemblyLine.UnmodifiableAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.clock.ImmutableClock;
import domain.exception.TimeToStartNewDayException;
import domain.job.action.Action;
import domain.job.action.IAction;
import domain.job.job.IJob;
import domain.job.job.Job;
import domain.job.task.ITask;
import domain.job.task.Task;
import domain.observer.observers.AssemblyLineObserver;
import domain.observer.observers.AssemblyLineStateObserver;
import domain.observer.observers.ClockObserver;
import domain.observer.observers.OrderBookObserver;
import domain.observer.observes.ObservesAssemblyLineState;
import domain.observer.observes.ObservesOrderBook;
import domain.order.order.IOrder;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.vehicleOption.VehicleOption;
/**
 * A class representing a mechanism to divide the incoming Orders over the AssemblyLines. 
 * It tries to evenly divide the workload over the different AssemblyLines.
 */
public class WorkloadDivider implements ObservesOrderBook, ObservesAssemblyLineState {

	private List<AssemblyLine> assemblyLines;

	/**
	 * Creates a workload divider.
	 * 
	 * @param 	listOfAssemblyLines
	 * 			A list of AssemblyLines the WorkloadDivider will be working with
	 * 
	 * @param 	orderBookObserver
	 * 			An OrderBookObserver. It will notify the WorkloadDivider each time there's a new Order
	 * 
	 * @param 	assemblyLineObserver
	 * 			An AssemblyLineObserver. It is used to attach all the AssemblyLines
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or more parameters is null
	 * 
	 */
	public WorkloadDivider(	List<AssemblyLine> listOfAssemblyLines, OrderBookObserver orderBookObserver, AssemblyLineObserver assemblyLineObserver) {
		if (	listOfAssemblyLines == null || orderBookObserver == null || assemblyLineObserver == null) {
			throw new IllegalArgumentException();
		}
		orderBookObserver.attachLogger(this);
		this.assemblyLines = listOfAssemblyLines;
		AssemblyLineStateObserver assemblyLineStateObserver = new AssemblyLineStateObserver();
		assemblyLineStateObserver.attachLogger(this);
		// attach the assemblyLineObserver to all assemblyLines
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			assemblyLine.attachObserver(assemblyLineObserver);
			assemblyLine.attachObserver(assemblyLineStateObserver);
		}
	}

	/**
	 * Returns the name of the current SchedulingAlgorithm used by every AssemblyLine.
	 */
	public String getCurrentSchedulingAlgorithm() {
		if (this.assemblyLines.size() <= 0) {
			return "There are no assemblyLines at the moment.";
		} else {
			return this.assemblyLines.get(0).getCurrentSchedulingAlgorithm();
		}
	}

	/**
	 * Lets all the AssemblyLines switch their SchedulingAlgorithms.
	 * 
	 * @param 	creator
	 * 			The creator representing the SchedulingAlgorithm that will be used by all the AssemblyLines.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
	 */
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		if (creator == null){
			throw new IllegalArgumentException();
		}
		else {
			for (AssemblyLine assemblyLine : this.assemblyLines) {
				assemblyLine.switchToSchedulingAlgorithm(creator);
			}	
		}
	}

	/**
	 * Returns a list with all the IAssemblyLines.
	 */
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

	/**
	 * Returns all the operational AssemblyLines. 
	 */
	public List<IAssemblyLine> getOperationalUnmodifiableAssemblyLines() {
		List<AssemblyLine> operational = getAssemblyLines(AssemblyLineState.OPERATIONAL);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : operational) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	/**
	 * Returns all the AssemblyLines who are in maintenance.
	 */
	public List<IAssemblyLine> getMaintenanceUnmodifiableAssemblyLines() {
		List<AssemblyLine> maintenance = getAssemblyLines(AssemblyLineState.MAINTENANCE);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : maintenance) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	/**
	 * Returns all the broken AssemblyLines in a list.
	 */
	public List<IAssemblyLine> getBrokenUnmodifiableAssemblyLines() {
		List<AssemblyLine> broken = getAssemblyLines(AssemblyLineState.BROKEN);
		ArrayList<IAssemblyLine> unmodifiable = new ArrayList<>();
		for (AssemblyLine assemblyLine : broken) {
			unmodifiable.add(new UnmodifiableAssemblyLine(assemblyLine));
		}
		return Collections.unmodifiableList(unmodifiable);
	}

	/**
	 * This method converts an order to a list of Jobs, 1 for each Vehicle and sets
	 * the minimal index for each Job. This index refers to the first WorkBench that is
	 * needed to complete the Job. The method returns a list of Jobs.
	 * 
	 * @param 	order
	 *          The Order that needs to be converted to a list of jobs.
	 * 
	 * @throws 	IllegalArgumentException
	 *       	Thrown when the given parameter is null
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
	 * This method receives an Order, let another method convert it to Jobs
	 * then distributes these Jobs amongst its AssemblyLines.
	 * 
	 * @param	order 
	 * 			The Order needed to be converted into jobs
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when the parameter is null
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
		//1: filter operational assemblyLines
		//2: filter assemblyLines that can complete job
		//3: kies assemblyLine met laagste workload
		//4: schedule de job bij die assemblyLine
		//assemblyLine.schedule(job)
		//5: kijken of de assemblyline stilstaat (can advancen)
		AssemblyLine scheduleHere = null;
		for(AssemblyLine line: assemblyLines){
			//Checken: Operational && kan job verwerken.
			if(line.getState().equals(AssemblyLineState.OPERATIONAL) && canBeHandled(job, line) ){
				//kijken naar workload
				if(scheduleHere!=null && line.getStandardJobs().size()<scheduleHere.getStandardJobs().size()){
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

	private boolean canBeHandled(IJob job, AssemblyLine line) {
		return line.getResponsibilities().contains(job.getVehicleSpecification());
	}

	/**
	 * Matches the given AssemblyLine to one of its own. If a match is found, 
	 * the request is passed on.
	 * 
	 * @param	assemblyLine 
	 * 			The AssemblyLine needed to be matched
	 * 
	 * @param	workbench
	 * 			The WorkBench at which the given Task is completed
	 * 
	 * @param	task
	 * 			The Task that has been completed
	 * 
	 * @param	elapsed
	 * 			A representation of how much time it took to complete the given Task
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or more of the given parameters is null
	 * 
	 * @throws	TimeToStartNewDayException
	 * 			Thrown when all the assemblyLines are not Operational or Idle
	 */
	public int completeChosenTaskAtChosenWorkBench(IAssemblyLine assemblyLine, IWorkBench workbench, ITask task, ImmutableClock elapsed) throws TimeToStartNewDayException {
		if(assemblyLine == null || workbench == null || task == null || elapsed == null){
			throw new IllegalArgumentException();
		}
		else{
			for (AssemblyLine line : this.assemblyLines) {
				if (line.equals(assemblyLine)) {
					int timeOfInternalClock = line.completeChosenTaskAtChosenWorkBench(workbench, task, elapsed);
					boolean allAssemblyLinesNotOperational = true;
					for (AssemblyLine a : this.assemblyLines) {
						if (a.getState() == AssemblyLineState.OPERATIONAL || a.getState() == AssemblyLineState.IDLE) {
							allAssemblyLinesNotOperational = false;
						}
					}
					if (allAssemblyLinesNotOperational) {
						throw new TimeToStartNewDayException();
					}
					return timeOfInternalClock;
				}
			}
			throw new IllegalStateException();
		}
	}

	/**
	 * Returns a powerset with all the VehicleOptions or sets of 
	 * VehicleOptions that occur in three or more pending Orders.
	 */
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders() {
		HashSet<VehicleOption> set = new HashSet<>();
		ArrayList<IJob> jobs = new ArrayList<>();
		for (AssemblyLine assemblyLine : this.assemblyLines) {
			jobs.addAll(assemblyLine.getStandardJobs());
		}

		// get all the VehicleOptions that occur in the pending orders
		for (IJob job : jobs) {
			for (VehicleOption o : job.getVehicleOptions()) {
				set.add(o);
			}
		}

		// get all the VehicleOptions that occur in the pending orders 3 or more times
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

		// get all the sets of VehicleOptions that occur in the pending orders 3 or more times
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

	/**
	 * Method to change the AssemblyLineState of the given AssemblyLine to the given 
	 * AssemblyLineState.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine that needs to have its state changed
	 * 
	 * @param 	state
	 * 			The AssemblyLineState that an AssemblyLine will be having
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or both parameters are null
	 */
	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state){
		if (assemblyLine == null || state == null){
			throw new IllegalArgumentException();
		}
		else{
			for(AssemblyLine line: assemblyLines){
				if(assemblyLine.equals(line)){
					line.setState(state);
				}
			}
		}
	}

	/**
	 * Method to change the AssemblyLineState of the given AssemblyLine to the given 
	 * AssemblyLineState when the given AssemblyLineState is maintenance. A MaintenanceTimeManager will
	 * be constructed to deal with the maintenance.
	 * 
	 * @param 	assemblyLine
	 * 			The AssemblyLine that needs to have its state changed
	 * 
	 * @param 	state
	 * 			The AssemblyLineState that an AssemblyLine will be having
	 * 
	 * @param	observer
	 * 			The ClockObserver that needs to be attached on the MaintenanceTimeManager that will be created
	 * 
	 * @param 	clock
	 * 			The snapshot of the current time.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown when one or both parameters are null
	 */
	public void changeState(IAssemblyLine assemblyLine, AssemblyLineState state, ClockObserver observer, ImmutableClock clock) {
		Optional<AssemblyLine> finalLine = getModifiableAssemblyLine(assemblyLine);

		if(finalLine.isPresent()){
			MaintenanceTimeManager manager = new MaintenanceTimeManager(finalLine.get(), clock);
			observer.attachLogger(manager);
			changeState(assemblyLine, state);
		}
	}

	private Optional<AssemblyLine> getModifiableAssemblyLine(IAssemblyLine assemblyLine){
		for(AssemblyLine line: assemblyLines){
			if(assemblyLine.equals(line)){
				return Optional.fromNullable(line);
			}
		}
		return Optional.absent();
	}

	/**
	 * Get the IWorkBenches which are blocking the AssemblyLine from advancing.

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

	@Override
	public void updateAssemblylineState(AssemblyLineState previousState, AssemblyLineState currentState) {
		if(!(previousState.equals(AssemblyLineState.BROKEN) && !currentState.equals(AssemblyLineState.OPERATIONAL)) &&
				!(previousState.equals(AssemblyLineState.MAINTENANCE) && !currentState.equals(AssemblyLineState.OPERATIONAL))){
			List<IJob> jobs = new ArrayList<>();
			for(AssemblyLine assemblyLine: this.assemblyLines){
				jobs.addAll(assemblyLine.removeUnscheduledJobs());
			}
			for(IJob job: jobs){
				divide(job);
			}
		}
	}

}
