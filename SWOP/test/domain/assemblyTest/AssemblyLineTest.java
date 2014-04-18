package domain.assemblyTest;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import domain.assembly.AssemblyLine;
import domain.assembly.IWorkBench;
import domain.assembly.WorkBench;
import domain.car.CarModel;
import domain.car.CarModelSpecification;
import domain.car.CarOption;
import domain.car.CarOptionCategory;
import domain.clock.UnmodifiableClock;
import domain.exception.AlreadyInMapException;
import domain.exception.ImmutableException;
import domain.job.Action;
import domain.job.IAction;
import domain.job.ITask;
import domain.job.Task;
import domain.observer.ClockObserver;


public class AssemblyLineTest{

	private AssemblyLine line;
	private CarModel model;

	@Before
	public void initialize() {
		line = new AssemblyLine(new ClockObserver(), new UnmodifiableClock(0,240));

		Set<CarOption> parts = new HashSet<>();
		parts.add(new CarOption("sport", CarOptionCategory.BODY));
		CarModelSpecification template = new CarModelSpecification("model", parts, 60);
		model = new CarModel(template);

		try {
			model.addCarPart(new CarOption("manual", CarOptionCategory.AIRCO));
			model.addCarPart(new CarOption("sedan",  CarOptionCategory.BODY));
			model.addCarPart(new CarOption("red",  CarOptionCategory.COLOR));
			model.addCarPart(new CarOption("standard 2l 4 cilinders",  CarOptionCategory.ENGINE));
			model.addCarPart(new CarOption("6 speed manual",  CarOptionCategory.GEARBOX));
			model.addCarPart(new CarOption("leather black", CarOptionCategory.SEATS));
			model.addCarPart(new CarOption("comfort", CarOptionCategory.WHEEL));
		} catch (AlreadyInMapException e) {}
	}

	@Test
	public void TestConstructor() {
		assertNotNull(line);
		assertNotNull(line.getCurrentJobs());
		assertNotNull(line.getWorkbenches());
	}

	@Test(expected = IllegalArgumentException.class)
	public void TestInvalidConstructor(){
		new AssemblyLine(null, new UnmodifiableClock(0, 240));
	}

	@Test
	public void addWorkBenchTest(){
		Set<String> resp1 = new HashSet<String>();
		resp1.add("bla");
		resp1.add("bleu");
		IWorkBench workbench1 = new WorkBench(resp1, "workbench1");
		line.addWorkBench(workbench1);
		assertEquals(4, line.getWorkbenches().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void addWorkBenchTestInvalid(){
		line.addWorkBench(null);
	}

	@Test
	public void canAdvanceTestTrue(){
		//		UnmodifiableClock clock = new UnmodifiableClock(2,300);
		//		StandardOrder order = new StandardOrder("Luigi", this.model, 5, clock);
		Action action1 = new Action("paint");
		action1.setCompleted(true);
		Task task1 = new Task("task pait");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			//			line.convertStandardOrderToJob(order);
			line.getWorkbenches().get(0).setCurrentTasks(list);
			for(IWorkBench bench : line.getWorkbenches()){
				for(ITask task : bench.getCurrentTasks()){
					for(IAction action: task.getActions()){
						action.setCompleted(true);
					}
				}
			}
			assertTrue(line.canAdvance());
		} catch (ImmutableException e) {}
	}

	@Test
	public void canAdvanceTestFalse(){
		Action action1 = new Action("paint");
		action1.setCompleted(false);
		Task task1 = new Task("task pait");
		task1.addAction(action1);
		ArrayList<ITask> list = new ArrayList<ITask>();
		list.add(task1);
		try {
			line.getWorkbenches().get(0).setCurrentTasks(list);
		} catch (ImmutableException e) {		}
		assertFalse(line.canAdvance());
	}



}
