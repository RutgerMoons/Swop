package uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import order.Order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import car.Airco;
import car.Body;
import car.CarModel;
import car.Color;
import car.Engine;
import car.Gearbox;
import car.Seat;
import car.Wheel;
import clock.Clock;

import assembly.AssemblyLine;
import assembly.Job;

import ui.AdvanceAssemblyLineHandler;
import ui.UIFacade;
import ui.UserInterface;
import users.GarageHolder;
import users.Manager;

@RunWith(Parameterized.class) 
public class AdvanceAssemblyLineHandlerTest {
	public UIFacade uiFacade;
	public AdvanceAssemblyLineHandler advAss;
	public AssemblyLine assembly;
	public Job job;

	public AdvanceAssemblyLineHandlerTest(UIFacade ui){
		uiFacade = ui;
	}

	@Before
	public void setup(){
		Clock clock = new Clock();
		assembly = new AssemblyLine(clock);
		advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
		CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
		Order order = new Order("Luigi", model, 5);
		job = new Job(order);
	}

	@Test
	public void testConstructor() {
		assertNotNull(advAss);
	}

	@Test
	public void testMayUseThisHandler(){
		Manager man = new Manager("Mario");
		GarageHolder garage = new GarageHolder("Luigi");
		assertTrue(advAss.mayUseThisHandler(man));
		assertFalse(advAss.mayUseThisHandler(garage));
	}

	@Test
	public void executeUseCaseTes(){
//		Manager man = new Manager("Mario");
//		advAss.executeUseCase(man);
		
	}

	@Test
	public void showCurrentAssTest(){
		try{
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			advAss.showCurrentAssemblyLine();

			String output = myout.toString();

			assertEquals("current assemblyline:\r\n" +"-workbench 1: car body\r\n" + "-workbench 2: drivetrain\r\n"+"-workbench 3: accessories\r\n\r\n", output);

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	@Test
	public void showFutureAssTest(){
		try{
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			
			assembly.addJob(job);
			advAss.showFutureAssemblyLine();
			String output = myout.toString();

			assertEquals("future assemblyline:\r\n-workbench 1: car body\r\n-workbench 2: drivetrain\r\n-workbench 3: accessories\r\n\r\n", output);

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	
	@Test
	public void advAssLineTest1(){
		try{
			String s = System.lineSeparator();
			String input = "200";
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			assembly.addJob(job);
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			in.read("\n".getBytes());
			in.reset();
			advAss.advanceAssemblyLine();
			String output = myout.toString();

			assertEquals("future assemblyline:\r\n-workbench 1: car body\r\n-workbench 2: drivetrain\r\n-workbench 3: accessories\r\n\r\n", output);

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() {
		return Arrays.asList(
				new Object[][]{{new UserInterface()}});
	}

}
