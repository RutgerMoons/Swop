package domain.assembly.assemblyLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.UnmodifiableWorkBench;
import domain.exception.UnmodifiableException;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.vehicleOption.VehicleOption;

public class UnmodifiableAssemblyLine implements IAssemblyLine {

	private IAssemblyLine assemblyLine;
	
	public UnmodifiableAssemblyLine(IAssemblyLine assemblyLine2) {
		if (assemblyLine2 == null) {
			throw new IllegalArgumentException();
		}
		this.assemblyLine = assemblyLine2;
	}

	@Override
	public boolean canAdvance() {
		return this.assemblyLine.canAdvance();
	}

	@Override
	public List<IWorkBench> getBlockingWorkBenches() {
		List<IWorkBench> unmodifiables = new ArrayList<>();
		for(IWorkBench bench: assemblyLine.getBlockingWorkBenches()){
			unmodifiables.add(new UnmodifiableWorkBench(bench));
		}
		return Collections.unmodifiableList(unmodifiables);
	}

	@Override
	public List<IWorkBench> getWorkBenches() {
		List<IWorkBench> unmodifiables = new ArrayList<>();
		for(IWorkBench bench: assemblyLine.getWorkBenches()){
			unmodifiables.add(new UnmodifiableWorkBench(bench));
		}
		return Collections.unmodifiableList(unmodifiables);
	}

	@Override
	public Set<Set<VehicleOption>> getAllVehicleOptionsInPendingOrders() {
		return this.assemblyLine.getAllVehicleOptionsInPendingOrders();
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
