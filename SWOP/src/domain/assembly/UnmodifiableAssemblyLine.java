package domain.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.exception.NoSuitableJobFoundException;
import domain.exception.UnmodifiableException;
import domain.job.IJob;
import domain.order.CustomOrder;
import domain.order.StandardOrder;
import domain.scheduling.Scheduler;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;
import domain.vehicle.IVehicleOption;

public class UnmodifiableAssemblyLine implements IAssemblyLine {

	private AssemblyLine assemblyLine;
	
	public UnmodifiableAssemblyLine(AssemblyLine assemblyLine) {
		if (assemblyLine == null) {
			throw new IllegalArgumentException();
		}
		this.assemblyLine = assemblyLine;
	}
	
	@Override
	public void addWorkBench(IWorkBench bench) {
		throw new UnmodifiableException();	
	}

	@Override
	public void advance() throws UnmodifiableException,	NoSuitableJobFoundException {
		throw new UnmodifiableException();	
	}

	@Override
	public boolean canAdvance() {
		return this.assemblyLine.canAdvance();
	}

	@Override
	public int convertCustomOrderToJob(CustomOrder order) throws UnmodifiableException {
		throw new UnmodifiableException();	
	}

	@Override
	public int convertStandardOrderToJob(StandardOrder order) throws UnmodifiableException {
		throw new UnmodifiableException();	
	}

	@Override
	public ArrayList<Integer> getBlockingWorkBenches() {
		return this.assemblyLine.getBlockingWorkBenches();
	}

	@Override
	public List<IJob> getCurrentJobs() {
		return this.assemblyLine.getCurrentJobs();
	}

	@Override
	public Scheduler getCurrentScheduler() {
		return this.assemblyLine.getCurrentScheduler();
	}

	@Override
	public List<IWorkBench> getWorkbenches() {
		return this.assemblyLine.getWorkbenches();
	}

	@Override
	public void switchToSchedulingAlgorithm(SchedulingAlgorithmCreator creator) {
		throw new UnmodifiableException();	
	}

	@Override
	public Set<Set<IVehicleOption>> getAllCarOptionsInPendingOrders() {
		return this.assemblyLine.getAllCarOptionsInPendingOrders();
	}

	@Override
	public AssemblyLineState getState() {
		return this.assemblyLine.getState();
	}

}
