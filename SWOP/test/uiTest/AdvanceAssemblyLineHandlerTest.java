package uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import order.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import textuitester.TextUITester;
import ui.AdvanceAssemblyLineFlowController;
import ui.IClientCommunication;
import ui.ClientCommunication;
import ui.UseCaseFlowController;
import ui.UserFlowController;
import users.GarageHolder;
import users.Manager;
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

import facade.Facade;
import facade.IFacade;

@RunWith(Parameterized.class)
public class AdvanceAssemblyLineHandlerTest {
	public IClientCommunication communication;
	public AdvanceAssemblyLineFlowController advAss;
	public IJob job;
	public IFacade facade;

	public AdvanceAssemblyLineHandlerTest(IClientCommunication ui) {
		communication = ui;
	}

	@Before
	public void setup() {

		facade = new Facade();
		advAss = new AdvanceAssemblyLineFlowController("Advance assemblyline",
				communication, facade);

		CarModel model = new CarModel("it's me", new Airco("manual"), new Body(
				"break"), new Color("red"), new Engine("bla"), new Gearbox(
				"manual"), new Seat("vinyl grey"), new Wheel("comfort"));
		Order order = new Order("Luigi", model, 5);
		job = new Job(order);

	}

	@Test
	public void testConstructor() {
		assertNotNull(advAss);
	}

	/*
	 * @Test public void showCurrentAssTest(){ try{ String s =
	 * System.lineSeparator(); final ByteArrayOutputStream myout = new
	 * ByteArrayOutputStream(); System.setOut(new PrintStream(myout));
	 * advAss.showCurrentAssemblyLine();
	 * 
	 * String output = myout.toString();
	 * 
	 * assertEquals("current assemblyline:" + s +"-workbench 1: car body" + s +
	 * "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s,
	 * output);
	 * 
	 * } finally { } }
	 * 
	 * @Test public void showFutureAssTest(){ try{ String s =
	 * System.lineSeparator(); final ByteArrayOutputStream myout = new
	 * ByteArrayOutputStream(); System.setOut(new PrintStream(myout));
	 * 
	 * advAss.showFutureAssemblyLine(); String output = myout.toString();
	 * 
	 * assertEquals("future assemblyline:" + s + "-workbench 1: car body" + s +
	 * "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s,
	 * output);
	 * 
	 * } finally { } }
	 * 
	 * @Test public void advAssLineTest2(){ try{ String s =
	 * System.lineSeparator(); String input = "-200" +s + "Y" + s;
	 * ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
	 * System.setIn(in);
	 * 
	 * 
	 * ByteArrayOutputStream myout = new ByteArrayOutputStream();
	 * System.setOut(new PrintStream(myout));
	 * 
	 * facade.processOrder("Polo", 5);
	 * 
	 * 
	 * advAss.advanceAssemblyLine(); String output = myout.toString();
	 * 
	 * assertEquals(
	 * "How much time has passed? (minutes, type a negative number if this is the start of the day)"
	 * + s +"current assemblyline:" + s + "-workbench 1: car body" + s +
	 * "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s +
	 * "Press enter when you're finished" + s, output);
	 * 
	 * } finally { } }
	 * 
	 * 
	 * @Test public void advAssLineTest4(){ String s = System.lineSeparator();
	 * String input = "200" +s + "Y" + s; ByteArrayInputStream in = new
	 * ByteArrayInputStream(input.getBytes()); System.setIn(in);
	 * 
	 * 
	 * ByteArrayOutputStream myout = new ByteArrayOutputStream();
	 * System.setOut(new PrintStream(myout));
	 * 
	 * advAss.advanceAssemblyLine(); String output = myout.toString();
	 * 
	 * assertEquals(
	 * "How much time has passed? (minutes, type a negative number if this is the start of the day)"
	 * + s + "You can't advance the assemblyline, because there are no orders."
	 * + s +"current assemblyline:" + s + "-workbench 1: car body" + s +
	 * "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s +
	 * "Press enter when you're finished" + s, output); }
	 */
	/*
	 * @Test public void useCaseTest() { try { String s =
	 * System.lineSeparator(); String input = "200" + s + "Y" + s + "Y" + s +
	 * "200" + s + "Y" + s; ByteArrayInputStream in = new
	 * ByteArrayInputStream(input.getBytes()); System.setIn(in);
	 * 
	 * ByteArrayOutputStream myout = new ByteArrayOutputStream();
	 * System.setOut(new PrintStream(myout));
	 * 
	 * advAss.executeUseCase(); String output = myout.toString();
	 * 
	 * assertEquals( "Do you want advance the assemblyLine? Y/N" + s +
	 * "Sorry, that's not a valid response" + s + s +
	 * "Do you want advance the assemblyLine? Y/N" + s + "current assemblyline:"
	 * + s + "-workbench 1: car body" + s + "-workbench 2: drivetrain" + s +
	 * "-workbench 3: accessories" + s + s + "future assemblyline:" + s +
	 * "-workbench 1: car body" + s + "-workbench 2: drivetrain" + s +
	 * "-workbench 3: accessories" + s + s + "Do you want to continue? Y/N" + s
	 * +
	 * "How much time has passed? (minutes, type a negative number if this is the start of the day)"
	 * + s + "current assemblyline:" + s + "-workbench 1: car body" + s +
	 * "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s +
	 * "Press enter when you're finished" + s, output);
	 * 
	 * } finally { } }
	 */
	@Test
	public void test() {
		String path = System.getProperty("java.class.path");
		TextUITester tester = new TextUITester(
				"java -cp " + path + " ui.AssemAssist"); //C:\\Users\\Stef\\workspace\\Swop\\bin\\
		// UserFlowController controller = new UserFlowController(communication,
		// facade, new ArrayList<UseCaseFlowController>(Arrays.asList(advAss)));
		// controller.login();
		TextUITester abc = new TextUITester("java ui.AssemAssist");
		String s = System.lineSeparator();
		tester.expectLine("Hello user, what's your name?");
		tester.sendLine("Mario");
		tester.expectLine("What's your role: manager, garageholder or worker?");
		tester.sendLine("manager");
		tester.expectLine("Options:");
		tester.expectLine("1: Advance assemblyline");
		tester.expectLine("What do you want to perform?");
		tester.sendLine("1");
		tester.expectLine("Do you want advance the assemblyLine? Y/N");
		tester.sendLine("Y");

		tester.expectLine("current assemblyline:");
		tester.expectLine("-workbench 1: car body");
		tester.expectLine("-workbench 2: drivetrain");
		tester.expectLine("-workbench 3: accessories");
		tester.expectLine("");
		tester.expectLine("future assemblyline:");
		tester.expectLine("-workbench 1: car body");
		tester.expectLine("-workbench 2: drivetrain");
		tester.expectLine("-workbench 3: accessories");
		tester.expectLine("");
		tester.expectLine("Do you want to continue? Y/N");

		tester.sendLine("Y");

		tester.expectLine("How much time has passed? (minutes, type a negative number if this is the start of the day)");
		tester.sendLine("60");

		tester.expectLine("You can't advance the assemblyline, because there are no orders.");
		tester.expectLine("current assemblyline:");
		tester.expectLine("-workbench 1: car body");
		tester.expectLine("-workbench 2: drivetrain");
		tester.expectLine("-workbench 3: accessories");
		tester.expectLine("");
		tester.expectLine("Press enter when you're finished");
		tester.kill();
	}

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() {
		return Arrays.asList(new Object[][] { { new ClientCommunication() } });
	}

}
