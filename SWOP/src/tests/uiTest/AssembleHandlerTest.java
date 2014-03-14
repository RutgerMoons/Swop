package tests.uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import code.assembly.Action;
import code.assembly.AssemblyLine;
import code.assembly.Job;
import code.assembly.Task;
import code.assembly.WorkBench;
import code.car.Airco;
import code.car.Body;
import code.car.CarModel;
import code.car.Color;
import code.car.Engine;
import code.car.Gearbox;
import code.car.Seat;
import code.car.Wheel;
import code.clock.Clock;
import code.order.Order;
import code.ui.AssembleHandler;
import code.ui.UIFacade;
import code.ui.UserInterface;
import code.users.GarageHolder;
import code.users.Worker;

@RunWith(Parameterized.class)
public class AssembleHandlerTest {

	private UIFacade ui;
	private AssembleHandler handler;
	private AssemblyLine line;

	public AssembleHandlerTest(UIFacade ui) {
		this.ui = ui;
	}

	@Before
	public void initialize() {
		line = new AssemblyLine(new Clock());
		handler = new AssembleHandler(ui, line);
	}

	@Test
	public void TestMayUseHandler() {
		Worker worker = new Worker("Stef");
		GarageHolder holder = new GarageHolder("Rutger");
		assertTrue(handler.mayUseThisHandler(worker));
		assertFalse(handler.mayUseThisHandler(holder));
	}

	@Test
	public void TestUseCase() {

		CarModel model = new CarModel("Volkswagen", new Airco("manual"),
				new Body("sedan"), new Color("blue"), new Engine(
						"standard 2l 4 cilinders"), new Gearbox(
						"6 speed manual"), new Seat("leather black"),
				new Wheel("comfort"));
		Order order = new Order("Jef", model, 1);
		Job job = new Job(order);
		Task task = new Task("Paint");
		Action action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		job.addTask(task);

		String s = System.lineSeparator();
		Worker worker = new Worker("Stef");
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));

		String input = "1" + s + "1" + s + "ENTER" + s + "N" + s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new UserInterface();
		line = new AssemblyLine(new Clock());
		handler = new AssembleHandler(ui, line);

		WorkBench bench = line.getWorkbenches().get(0);
		bench.setCurrentJob(job);
		bench.chooseTasksOutOfJob();

		handler.executeUseCase(worker);
		String output = myout.toString();
		assertEquals(
				"Workbenches:"+ s + "1: car body"+s+"2: drivetrain" + s+ "3: accessories" + s + s + "What's the number of the workbench you're currently residing at?" + s+ "Tasks:" + s + "1.Paint" + s + s + "Which taskNumber do you choose?" + s+ "Your task: " + s+ "Paint" + s+ "Required actions: 1.Paint car blue" + s + s + "Press enter when you're finished" + s+ "All the tasks at this workbench are completed" + s + s+ "Do you want to continue? Y/N" + s,
				output);
	}
	

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new UserInterface() } });
	}
}
