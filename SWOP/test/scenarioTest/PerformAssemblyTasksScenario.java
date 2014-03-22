package scenarioTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import order.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.AssembleHandler;
import ui.UIFacade;
import ui.UserInterface;
import users.Worker;
import assembly.Action;
import assembly.AssemblyLine;
import assembly.Job;
import assembly.Task;
import assembly.WorkBench;
import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import clock.Clock;

import com.google.common.base.Optional;
/**
 * Assumption user is already correctly logged in.
 *
 */
@RunWith(Parameterized.class)
public class PerformAssemblyTasksScenario {
	
	private UIFacade ui;
	private AssembleHandler handler;
	private AssemblyLine line;
	private Job job;
	private Order order;
	private Worker worker;

	public PerformAssemblyTasksScenario(UIFacade ui) {
		this.ui = ui;
	}

	@Before
	public void initialize() {
		line = new AssemblyLine(new Clock());
		handler = new AssembleHandler(ui, line);
		worker = new Worker("Mario");
		CarModel model = new CarModel("Volkswagen", new Airco("manual"),
				new Body("sedan"), new Color("blue"), new Engine(
						"standard 2l 4 cilinders"), new Gearbox(
						"6 speed manual"), new Seat("leather black"),
				new Wheel("comfort"));
		order = new Order("Jef", model, 1);
		job = new Job(order);
		Task task = new Task("Paint");
		Action action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		job.addTask(task);

		
	}
	
	@Test
	public void PerformUseCase(){
		String s = System.lineSeparator();
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		String input = "1" // The workbench the worker is residing at, step 2
						+ s 
						+ "1" // The worker selects a task, step 4
						+ s 
						+ "ENTER" // Pressed by worker when task is finished, step 6
						+ s 
						+ "Y" // Worker chooses to continue, step 4 again
						+ s
						+"1"
						+s
						+"N";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new UserInterface();
		line = new AssemblyLine(new Clock());
		handler = new AssembleHandler(ui, line);
		
		WorkBench bench = line.getWorkbenches().get(0);
		bench.setCurrentJob(Optional.fromNullable(job));
		bench.chooseTasksOutOfJob();

		handler.executeUseCase(worker);
		String output = myout.toString();
		assertEquals(
				"Workbenches:"+ s //Status of the workbenches, step 1
				+ "1: car body" + s 
				+ "2: drivetrain" + s 
				+ "3: accessories" + s + s 
				+ "What's the number of the workbench you're currently residing at?" + s //Step 1
				+ "Tasks:" + s // Showing of the pending tasks, step 3 
				+ "1.Paint" + s + s 
				+ "Which taskNumber do you choose?" + s
				+ "Your task: " + s //Step 5, system shows information about the chosen task
				+ "Paint" + s
				+ "Required actions: 1.Paint car blue" + s + s 
				+ "Press enter when you're finished" + s 
				+ "All the tasks at this workbench are completed" + s + s // step 7, updated vieuw of the workbench
				+ "Do you want to continue? Y/N" + s
				+ "Workbenches:"+ s //Status of the workbenches, step 1
				+ "1: car body" + s 
				+ "2: drivetrain" + s 
				+ "3: accessories" + s + s 
				+ "What's the number of the workbench you're currently residing at?" + s
				+ "All the tasks at this workbench are completed"+s+s
				+ "Do you want to continue? Y/N" +s, // step 8
				output);
	}
	
	
	
	@Test
	public void PerformUseCaseAlternateFlow(){
		String s = System.lineSeparator();
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		String input = "1" // The workbench the worker is residing at, step 2
						+ s 
						+ "1" // The worker selects a task, step 4
						+ s 
						+ "ENTER" // Pressed by worker when task is finished, step 6
						+ s 
						+ "N" // Worker does not choose to continue, step 8a
						+ s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new UserInterface();
		line = new AssemblyLine(new Clock());
		handler = new AssembleHandler(ui, line);
		
		WorkBench bench = line.getWorkbenches().get(0);
		bench.setCurrentJob(Optional.fromNullable(job));
		bench.chooseTasksOutOfJob();

		handler.executeUseCase(worker);
		String output = myout.toString();
		assertEquals(
				"Workbenches:"+ s //Status of the workbenches, step 1
				+ "1: car body" + s 
				+ "2: drivetrain" + s 
				+ "3: accessories" + s + s 
				+ "What's the number of the workbench you're currently residing at?" + s //Step 1
				+ "Tasks:" + s // Showing of the pending tasks, step 3 
				+ "1.Paint" + s + s 
				+ "Which taskNumber do you choose?" + s
				+ "Your task: " + s //Step 5, system shows information about the chosen task
				+ "Paint" + s
				+ "Required actions: 1.Paint car blue" + s + s 
				+ "Press enter when you're finished" + s 
				+ "All the tasks at this workbench are completed" + s + s // step 7, updated vieuw of the workbench
				+ "Do you want to continue? Y/N" + s, // step 8
				output);
		// flow stops here
	}
	


	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new UserInterface() } });
	}
}
