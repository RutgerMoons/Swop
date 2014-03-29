package uiTest;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.IClientCommunication;
import ui.UseCaseFlowController;
import ui.UserFlowController;
import ui.ClientCommunication;
import facade.Facade;
import facade.IFacade;

@RunWith(Parameterized.class) 
public class UserHandlerTest {

	public IClientCommunication UIFacade;
	public IFacade facade;
//	public UserBook userbook;
	public ArrayList<UseCaseFlowController> handlers;
	public UserFlowController userH;
//	public AssemblyLine ass;
//	public Job job;

	public UserHandlerTest(IClientCommunication ui){
		this.UIFacade = ui;
	}

	@Before
	public void setup(){
		facade = new Facade();
		handlers = new ArrayList<UseCaseFlowController>();
		
	}
	
	@Test
	public void testLogin() {
		String s = System.lineSeparator();
		String input = "naam" + s + "manager" + s + "naam";
		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		ByteArrayOutputStream myout = new ByteArrayOutputStream();
		System.setOut(new PrintStream(myout));
		
		this.UIFacade = new ClientCommunication();
		assertNotNull(UIFacade);
		assertNotNull(facade);
		assertNotNull(handlers);
		this.userH = new UserFlowController(UIFacade, facade, handlers);
		
		userH.login();
		userH.login();
		
		String output = myout.toString();
		assertEquals("Hello user, what's your name?" + s + 
						"What's your role: manager, garageholder or worker?" + s +
						"Hello user, what's your name?" + s, output);
	}


//	@Test
//	public void testConstructor() {
//		userH = new UserHandler(uiFacade, userbook, handlers);
//		assertNotNull(userH);
//
//	}
//
//	@Test (expected = NullPointerException.class)
//	public void testConstructorFail1(){
//		UserHandler userH = new UserHandler(null, userbook, handlers);
//	}
//
//	@Test (expected = NullPointerException.class)
//	public void testConstructorFail2(){
//		UserHandler userH = new UserHandler(null, null, null);
//	}
//
//	@Test (expected = NullPointerException.class)
//	public void testConstructorFail3(){
//		UserHandler userH = new UserHandler(null, null, handlers);
//	}
//
//	@Test (expected = NullPointerException.class)
//	public void testConstructorFail4(){
//		UserHandler userH = new UserHandler(uiFacade, null, handlers);
//	}
//
//	@Test (expected = NullPointerException.class)
//	public void testConstructorFail5(){
//		UserHandler userH = new UserHandler(uiFacade, userbook, null);
//	}
//
//	@Test
//	public void testLogin1(){
//		try{
//			String s = System.lineSeparator();
//			String input = "naam" + s;
//			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//			System.setIn(in);
//
//			ByteArrayOutputStream myout = new ByteArrayOutputStream();
//			System.setOut(new PrintStream(myout));
//			GarageHolder user = new GarageHolder("naam");
//			userbook.addUser(user);
//			uiFacade = new UserInterface();
//			UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//
//			userH1.login();
//
//
//			String output = myout.toString();
//
//			assertEquals("Hello user, what's your name?" +s, output);
//
//		} finally{}
//	}
//
//	@Test
//	public void testLogin2(){
//		try{
//			String s = System.lineSeparator();
//			String input = "naam" +s + "garageholder" +s;
//			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//			System.setIn(in);
//
//			ByteArrayOutputStream myout = new ByteArrayOutputStream();
//			System.setOut(new PrintStream(myout));
//
//			uiFacade = new UserInterface();
//			UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//			userH1.login();
//
//
//			String output = myout.toString();
//
//			assertEquals("Hello user, what's your name?" + s +"What's your role: manager, garageholder or worker?" +s, output);
//
//		} finally{
//		}
//	}
//
//
//	@Test
//	public void testLogin3(){
//		try{
//			String s = System.lineSeparator();
//			String input = "naam" +s + "manager" +s;
//			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//			System.setIn(in);
//
//			ByteArrayOutputStream myout = new ByteArrayOutputStream();
//			System.setOut(new PrintStream(myout));
//
//			uiFacade = new UserInterface();
//			UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//			userH1.login();
//
//
//			String output = myout.toString();
//
//			assertEquals("Hello user, what's your name?" + s + "What's your role: manager, garageholder or worker?" +s, output);
//
//		} finally{
//		}
//	}
//
//
//	@Test
//	public void testLogin4(){
//		try{
//			String s = System.lineSeparator();
//			String input = "naam" +s + "worker" +s;
//			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//			System.setIn(in);
//
//			ByteArrayOutputStream myout = new ByteArrayOutputStream();
//			System.setOut(new PrintStream(myout));
//
//			uiFacade = new UserInterface();
//			UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//			userH1.login();
//
//
//			String output = myout.toString();
//
//			assertEquals("Hello user, what's your name?" +s +"What's your role: manager, garageholder or worker?" +s, output);
//
//		} finally{
//		}
//	}
//
//	@Test
//	public void logOut(){
//		String s = System.lineSeparator();
//		String input = "naam" +s;
//		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//		System.setIn(in);
//
//		ByteArrayOutputStream myout = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(myout));
//		GarageHolder user = new GarageHolder("naam");
//		userbook.addUser(user);
//		uiFacade = new UserInterface();
//		UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//
//		userH1.login();
//		assertNotNull(userH1.getCurrentUser());
//		userH1.logOut();
//		assertNull(userH1.getCurrentUser());
//	}
//
//	@Test
//	public void performDutiesTest1(){
//
//		String s = System.lineSeparator();
//		String input = "naam" +s + "manager" + s +"Y" + s + "Y"+s+ "20" + s + "Y" +s;
//		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//		System.setIn(in);
//
//		ByteArrayOutputStream myout = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(myout));
//
//		Clock clock = new Clock();
//		ass  = new AssemblyLine(clock);
//		CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
//		Order order = new Order("Luigi", model, 5);
//		job = new Job(order);
//		ass.addJob(job);
//		uiFacade = new UserInterface();
//		handlers.add(new AdvanceAssemblyLineHandler(uiFacade, ass,clock));
//
//		UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//		userH1.login();
//		userH1.performDuties();
//		String output = myout.toString();
//		assertEquals("Hello user, what's your name?"+s+
//				"What's your role: manager, garageholder or worker?"+s+
//				"Do you want advance the assemblyLine? Y/N"+s+"current assemblyline:"+s+
//				"-workbench 1: car body"+s+"-workbench 2: drivetrain"+s+
//				"-workbench 3: accessories"+s+s+"future assemblyline:"+s+
//				"-workbench 1: car body"+s+"-workbench 2: drivetrain"+s+
//				"-workbench 3: accessories"+s+s+"Do you want to continue? Y/N"+s+
//				"How much time has passed? (minutes, type a negative number if this is the start of the day)"+s+
//				"current assemblyline:"+s+"-workbench 1: car body"+s+
//				"-workbench 2: drivetrain"+s+"-workbench 3: accessories"+s+s+
//				"Press enter when you're finished" +s, output);
//
//	}
//
//	@Test (expected = IllegalArgumentException.class)
//	public void performDutiesTest2(){
//		String s = System.lineSeparator();
//		String input = "naam" +s + "worker" + s +"Y" + s + "Y"+s+ "20" + s + "Y" +s;
//		ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//		System.setIn(in);
//
//		ByteArrayOutputStream myout = new ByteArrayOutputStream();
//		System.setOut(new PrintStream(myout));
//
//		Clock clock = new Clock();
//		ass  = new AssemblyLine(clock);
//		CarModel model = new CarModel("it's me", new Airco("manual"), new Body("break"), new Color("red"), new Engine("bla"), new Gearbox("manual"), new Seat("vinyl grey"), new Wheel("comfort"));
//		Order order = new Order("Luigi", model, 5);
//		job = new Job(order);
//		ass.addJob(job);
//		uiFacade = new UserInterface();
//		handlers.add(new AdvanceAssemblyLineHandler(uiFacade, ass,clock));
//
//		UserHandler userH1 = new UserHandler(uiFacade, userbook, handlers);
//		userH1.login();
//		userH1.performDuties();
//
//	}
//
//
	@Parameterized.Parameters
	public static Collection<Object[]> instancesToTest() { 
		return Arrays.asList(
				new Object[][]{{new ClientCommunication()}});
	}
}
