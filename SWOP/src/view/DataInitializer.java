package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import domain.assembly.assemblyLine.AssemblyLine;
import domain.assembly.assemblyLine.AssemblyLineState;
import domain.assembly.assemblyLine.IAssemblyLine;
import domain.assembly.workBench.IWorkBench;
import domain.assembly.workBench.WorkBench;
import domain.assembly.workBench.WorkBenchType;
import domain.clock.ImmutableClock;
import domain.facade.Facade;
import domain.job.task.ITask;
import domain.observer.observers.ClockObserver;
import domain.scheduling.schedulingAlgorithmCreator.SchedulingAlgorithmCreatorFifo;
import domain.vehicle.VehicleSpecification;
import domain.vehicle.catalogue.VehicleSpecificationCatalogue;
import domain.vehicle.vehicle.IVehicle;
import domain.vehicle.vehicleOption.VehicleOption;
import domain.vehicle.vehicleOption.VehicleOptionCategory;

/**
 * A class to initialize all data at the start of the program. 
 * It initializes the AssemblyLines and it simulates a production day. 
 */
public class DataInitializer {

	/**
	 * Initializes the AssemblyLines.
	 *  
	 * @param 	clockObserver
	 * 			The ClockObserver to attach to the AssemblyLines
	 * 
	 * @param 	clock
	 * 			The current time
	 * 
	 * @param 	catalogue
	 * 			The VehicleSpecificationCatalogue to retrieve the VehicleSpecification that the AssemblyLines can handle
	 */
	public List<AssemblyLine> getInitialAssemblyLines(ClockObserver clockObserver, ImmutableClock clock, VehicleSpecificationCatalogue catalogue) {
		List<AssemblyLine> assemblyLines = new ArrayList<AssemblyLine>();

		Map<WorkBenchType, Integer> timeAtWorkBench = new HashMap<WorkBenchType, Integer>();
		for(WorkBenchType type: WorkBenchType.values()){
			timeAtWorkBench.put(type, 60);
		}
		VehicleSpecification customSpecification = new VehicleSpecification("custom", new HashSet<VehicleOption>(), timeAtWorkBench, new HashSet<VehicleOption>());


		Set<VehicleSpecification> specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(customSpecification);
		AssemblyLine line1 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);


		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		specifications.add(customSpecification);
		AssemblyLine line2 = new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);

		specifications = new HashSet<>();
		specifications.add(catalogue.getCatalogue().get("model A"));
		specifications.add(catalogue.getCatalogue().get("model B"));
		specifications.add(catalogue.getCatalogue().get("model C"));
		specifications.add(catalogue.getCatalogue().get("model X"));
		specifications.add(catalogue.getCatalogue().get("model Y"));
		specifications.add(customSpecification);
		AssemblyLine line3= new AssemblyLine(clockObserver, clock, AssemblyLineState.OPERATIONAL, specifications);

		WorkBench body1 = new WorkBench( WorkBenchType.BODY);
		WorkBench body2 = new WorkBench( WorkBenchType.BODY);
		WorkBench body3 = new WorkBench( WorkBenchType.BODY);

		WorkBench drivetrain1 = new WorkBench( WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain2 = new WorkBench( WorkBenchType.DRIVETRAIN);
		WorkBench drivetrain3 = new WorkBench( WorkBenchType.DRIVETRAIN);

		WorkBench accessories1 = new WorkBench( WorkBenchType.ACCESSORIES);
		WorkBench accessories2 = new WorkBench( WorkBenchType.ACCESSORIES);
		WorkBench accessories3 = new WorkBench( WorkBenchType.ACCESSORIES);

		WorkBench cargo = new WorkBench( WorkBenchType.CARGO);

		WorkBench certificiation = new WorkBench( WorkBenchType.CERTIFICATION);

		line1.addWorkBench(body1);
		line1.addWorkBench(drivetrain1);
		line1.addWorkBench(accessories1);

		line2.addWorkBench(body2);
		line2.addWorkBench(drivetrain2);
		line2.addWorkBench(accessories2);

		line3.addWorkBench(body3);
		line3.addWorkBench(cargo);
		line3.addWorkBench(drivetrain3);
		line3.addWorkBench(accessories3);
		line3.addWorkBench(certificiation);

		assemblyLines.add(line1);
		assemblyLines.add(line2);
		assemblyLines.add(line3);

		line1.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		line2.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());
		line3.switchToSchedulingAlgorithm(new SchedulingAlgorithmCreatorFifo());

		return Collections.unmodifiableList(assemblyLines);
	}

	/**
	 * Initializes one production day.
	 * 
	 * @param 	facade
	 * 			Pass all calls through this facade to the domain
	 */
	public void initializeData(Facade facade){
		facade.createAndAddUser("jef", "garageholder");
		facade.login("jef");

		createNewVehicle(facade, "model A", 10);

		while(!facade.getAssemblyLines().get(0).getState().equals(AssemblyLineState.IDLE)
				|| !facade.getAssemblyLines().get(1).getState().equals(AssemblyLineState.IDLE)
				|| !facade.getAssemblyLines().get(2).getState().equals(AssemblyLineState.IDLE)){
			for(IAssemblyLine assemblyLine: facade.getAssemblyLines()){
				for(IWorkBench bench: assemblyLine.getWorkBenches()){
					for(ITask task: bench.getCurrentTasks()){
						facade.completeChosenTaskAtChosenWorkBench(assemblyLine, bench, task, new ImmutableClock(0, 0));
					}
				}
			}
		}

		facade.startNewDay();
		
		createNewVehicle(facade, "model A", 3);
		IVehicle vehicle = facade.getCustomOptions("spraying car bodies").get(0);
		facade.processCustomOrder(vehicle, new ImmutableClock(1, 1000));
		facade.processCustomOrder(vehicle, new ImmutableClock(2, 1000));
		facade.processCustomOrder(vehicle, new ImmutableClock(3, 1000));
		
		createNewVehicle(facade, "model B", 1);
		
		createNewVehicle(facade, "model C", 1);
		
		createNewVehicle(facade, "model X", 1);
	}


	private void createNewVehicle(Facade facade, String modelName, int quantity) {
		facade.createNewVehicle(facade.getVehicleSpecificationFromCatalogue(modelName));

		for(VehicleOption option: getOptions(facade)){
			facade.addPartToVehicle(option);
		}

		facade.processOrder(quantity);
	}

	private List<VehicleOption> getOptions(Facade facade){
		List<VehicleOption> options = new ArrayList<>();
		for (VehicleOptionCategory type : facade.getVehicleOptionCategory()) {
			List<VehicleOption> parts = facade.getRemainingVehicleOptions(type);
			if(parts!=null && !parts.isEmpty()){
				options.add(parts.get(0));
			}
		}
		return options;
	}
}
