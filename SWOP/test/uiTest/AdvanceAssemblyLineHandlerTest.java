package uiTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ui.UIFacade;
import ui.UserInterface;

@RunWith(value = Parameterized.class) 
public class AdvanceAssemblyLineHandlerTest {
	public UIFacade uiFacade;

	public AdvanceAssemblyLineHandlerTest(UIFacade ui){
		uiFacade = ui;
	}
	@Test
	public void testExecuteUseCase() {
		//TODO 
	}

	@Parameterized.Parameters
	public static List<Object> instancesToTest() {
		return Arrays.asList(
				new Object[]{new UserInterface()});
	}

}
