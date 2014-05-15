package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.exception.UnmodifiableException;
import domain.job.job.IJob;
import domain.scheduling.Scheduler;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;
//TODO doc
public class UnmodifiableAssemblyLine implements IAssemblyLine {

	private AssemblyLine assemblyLine;
	
	public UnmodifiableAssemblyLine(AssemblyLine assemblyLine) {
		if (assemblyLine == null) {
			throw new IllegalArgumentException();
		}
		this.assemblyLine = assemblyLine;
	}

	@Override
	public boolean canAdvance() {
		return this.assemblyLine.canAdvance();
	}

	@Override
	public ArrayList<IWorkBench> getBlockingWorkBenches() {
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
	public Set<Set<VehicleOption>> getAllCarOptionsInPendingOrders() {
		return this.assemblyLine.getAllCarOptionsInPendingOrders();
	}

	@Override
	public AssemblyLineState getState() {
		return this.assemblyLine.getState();
	}

	@Override
	public void setState(AssemblyLineState state) {
		throw new UnmodifiableException();
	}

	@Override
	public String toString(){
		return assemblyLine.toString();
	}

	@Override
	public Set<VehicleSpecification> getResponsibilities() {
		return assemblyLine.getResponsibilities();
	}
	
	@Override
	public boolean equals(Object line){
		return assemblyLine.equals(line);
	}
}