package uiTest;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import org.junit.Test;

import ui.runProgram;

public class TestRunProgram {

	@Test
	public void testMultipleLogins(){
		ByteArrayOutputStream myout = null;
		String s = System.lineSeparator();
		try{
			String input = "naam" +s + "garageholder" +s +"N" + s + "naam" + s;
			ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
			System.setIn(in);

			myout = new ByteArrayOutputStream();
			System.setOut(new PrintStream(myout));

			runProgram.main(new String[0]);
			
		} catch(NoSuchElementException e){ //dit moet omdat je het maar 2 keer uitvoert
			String output = myout.toString();
			
			//Test of hij niet 2x vraagt wat je rol is.
			assertEquals("Hello user, what's your name?" + s + "What's your role: manager, garageholder or worker?" + s + "You have no pending Orders" + s + s + "You have no completed Orders" + s + s + "Do you want to continue? Y/N" + s + "Hello user, what's your name?" + s + "You have no pending Orders" + s + s + "You have no completed Orders" + s + s + "Do you want to continue? Y/N" + s, output);
		}
	}
	
}
