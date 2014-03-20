package tests.uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Optional;

import code.assembly.Action;
import code.assembly.AssemblyLine;
import code.assembly.Job;
import code.assembly.Task;
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
import code.ui.AdvanceAssemblyLineHandler;
import code.ui.UIFacade;
import code.ui.UserInterface;
import code.users.GarageHolder;
import code.users.Manager;

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
			String s = System.lineSeparator();
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			advAss.showCurrentAssemblyLine();

			String output = myout.toString();

			assertEquals("current assemblyline:" + s +"-workbench 1: car body" + s + "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s, output);

		} finally {
		}
	}

	@Test
	public void showFutureAssTest(){
		try{
			String s = System.lineSeparator();
			final ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			
			assembly.addJob(job);
			advAss.showFutureAssemblyLine();
			String output = myout.toString();

			assertEquals("future assemblyline:" + s + "-workbench 1: car body" + s + 
						"-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s, output);

		} finally {
		}
	}

	
	@Test
	public void advAssLineTest1(){
		try{
			String s = System.lineSeparator();
			String input = "200" +s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new UserInterface();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
			
			assembly.addJob(job);
			
			advAss.advanceAssemblyLine();
			String output = myout.toString();

			assertEquals("How much time has passed? (minutes, type a negative number if this is the start of the day)"
					+ s +"current assemblyline:" + s + "-workbench 1: car body" + s + "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
					+ "Press enter when you're finished" + s, output);

		} finally {
		} 
	}
	
	@Test
	public void advAssLineTest2(){
		try{
			String s = System.lineSeparator();
			String input = "200" + s + "Y" + s + "200" + s + "Y" + s + "200" + s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new UserInterface();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			
			CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
			Order order = new Order("Luigi", model, 5);
			Job job = new Job(order);
			Action action = new Action("action");
			action.setCompleted(false);
			Task task = new Task("Paint");
			task.addAction(action);
			job.addTask(task);
			
			assembly.addJob(job);
			assembly.getWorkbenches().get(0).setCurrentJob(Optional.fromNullable(job));
			assembly.getWorkbenches().get(0).chooseTasksOutOfJob();
			advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
			
			advAss.advanceAssemblyLine();
			advAss.advanceAssemblyLine();
			//advAss.advanceAssemblyLine();
			
			String output = myout.toString();

			assertEquals("AssemblyLine can't be advanced because of workbench [1]" + s + s
					+ "current assemblyline:" + s 
					+ "-workbench 1: car body" + s 
					+ "  *Paint: not completed" + s
					+ "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
					+ "Press enter when you're finished" + s 
					+ "AssemblyLine can't be advanced because of workbench [1]" + s + s
					+ "current assemblyline:" + s 
					+ "-workbench 1: car body" + s 
					+ "  *Paint: not completed" + s
					+ "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
					+ "Press enter when you're finished" + s, output);

		} finally {
		} 
	}
	
	@Test
	public void advAssLineTest3(){
		try{
			String s = System.lineSeparator();
			String input = "-200" +s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new UserInterface();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
			
			assembly.addJob(job);
			
			advAss.advanceAssemblyLine();
			String output = myout.toString();

			assertEquals("How much time has passed? (minutes, type a negative number if this is the start of the day)"
					+ s +"current assemblyline:" + s + "-workbench 1: car body" + s + "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
					+ "Press enter when you're finished" + s, output);

		} finally {
		} 
	}
	
	
	@Test
	public void advAssLineTest4(){
		String s = System.lineSeparator();
		String input = "200" +s + "Y" + s;
		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		
		
		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		
		uiFacade = new UserInterface();
		Clock clock = new Clock();
		assembly = new AssemblyLine(clock);
		advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
		
		
		advAss.advanceAssemblyLine();
		String output = myout.toString();

		assertEquals("How much time has passed? (minutes, type a negative number if this is the start of the day)"
				+ s + "You can't advance the assemblyline, because there are no orders."
				+ s +"current assemblyline:" + s + "-workbench 1: car body" + s + "-workbench 2: drivetrain" + s + "-workbench 3: accessories" + s + s
				+ "Press enter when you're finished" + s, output);
	}
	@Test
	public void useCaseTest(){
		try{
			String s = System.lineSeparator(); 
			String input = "200" + s + "Y" + s + "Y" + s + "200" + s + "Y" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);
			
			
			ByteArrayOutputStream myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));
			
			uiFacade = new UserInterface();
			Clock clock = new Clock();
			assembly = new AssemblyLine(clock);
			advAss = new AdvanceAssemblyLineHandler(uiFacade, assembly, clock);
			
			assembly.addJob(job);
			Manager manager = new Manager("name");
			advAss.executeUseCase(manager);
			String output = myout.toString();

			assertEquals("Do you want advance the assemblyLine? Y/N" + s
							+ "Sorry, that's not a valid response" + s + s
							+ "Do you want advance the assemblyLine? Y/N" + s
							+ "current assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "future assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "Do you want to continue? Y/N" + s
							+ "How much time has passed? (minutes, type a negative number if this is the start of the day)" + s
							+ "current assemblyline:" + s 
							+ "-workbench 1: car body" + s 
							+ "-workbench 2: drivetrain" + s 
							+ "-workbench 3: accessories" + s + s
							+ "Press enter when you're finished" + s, output);

		} finally {
		} 
	}
	
	

	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() { 
		return Arrays.asList(
				new Object[][]{{new UserInterface()}});
	}
	
	
}
