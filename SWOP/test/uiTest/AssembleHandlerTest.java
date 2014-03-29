package uiTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import ui.AssembleFlowController;
import ui.IClientCommunication;
import ui.ClientCommunication;
import users.GarageHolder;
import users.Worker;
import assembly.Action;
import assembly.AssemblyLine;
import assembly.IJob;
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

@RunWith(Parameterized.class)
public class AssembleHandlerTest {

	private IClientCommunication ui;
	private AssembleFlowController handler;
	private AssemblyLine line;

	public AssembleHandlerTest(IClientCommunication ui) {
		this.ui = ui;
	}

	@Before
	public void initialize() {
		line = new AssemblyLine(new Clock());
		handler = new AssembleFlowController(ui, line);
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
		IJob job = new Job(order);
		Task task = new Task("Paint");
		Action action = new Action("Paint car blue");
		action.setCompleted(false);
		task.addAction(action);
		((Job) job).addTask(task);

		String s = System.lineSeparator();
		Worker worker = new Worker("Stef");
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));

		String input = "1" + s + "1" + s + "ENTER" + s + "N" + s;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ui = new ClientCommunication();
		line = new AssemblyLine(new Clock());
		handler = new AssembleFlowController(ui, line);

		WorkBench bench = (WorkBench) line.getWorkbenches().get(0);
		bench.setCurrentJob(Optional.fromNullable(job));
		bench.chooseTasksOutOfJob();

		handler.executeUseCase(worker);
		String output = myout.toString();
		assertEquals(
				"Workbenches:"+ s + "1: car body"+s+"2: drivetrain" + s+ "3: accessories" + s + s + "What's the number of the workbench you're currently residing at?" + s+ "Tasks:" + s + "1.Paint" + s + s + "Which taskNumber do you choose?" + s+ "Your task: " + s+ "Paint" + s+ "Required actions: 1.Paint car blue" + s + s + "Press enter when you're finished" + s+ "All the tasks at this workbench are completed" + s + s+ "Do you want to continue? Y/N" + s,
				output);
	}
	

	@Parameterized.Parameters
	public static Collection<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}
}
