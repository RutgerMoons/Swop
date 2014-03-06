import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class TestShizzle {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		try(BufferedReader br = new BufferedReader(new FileReader("src/index.html"))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        System.out.println(everything);
	        System.out.println(everything.length());
	    }
		
		
		
		
		
		
		
	}
}
