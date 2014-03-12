import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TestShizzle {
	public static boolean isHTTP0(String protocol) {
		return (protocol.substring(protocol.length() - 1).equals("0"));
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		String t = "GET /index.html HTTP/1.0";
		System.out.println(isHTTP0(t));
		
		
		
		
		
		
		
	}
}
