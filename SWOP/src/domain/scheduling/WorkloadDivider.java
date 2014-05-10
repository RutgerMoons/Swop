package domain.scheduling;

import java.util.ArrayList;
import java.util.List;

import domain.assembly.AssemblyLine;
import domain.assembly.IAssemblyLine;
import domain.assembly.UnmodifiableAssemblyLine;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreator;

public class WorkloadDivider {

	private ArrayList<AssemblyLine> assemblyLines;
	
	public WorkloadDivider() {
		//TODO: init assemblyLines
	}
	
	public String getCurrentSchedulingAlgorithm() {
		if (this.assemblyLines.size() <= 0) {
			return "No scheduling algorithm is used yet.";
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
}
