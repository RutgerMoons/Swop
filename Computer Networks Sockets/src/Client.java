import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

class Client {
	//derp HEAD www.google.com/index.html 80 HTTP/1.1
	//derp HEAD localhost/index.html 4545 HTTP/1.1
	//derp GET localhost/index.html 4545 HTTP/1.1
	//derp GET localhost/derp.html 4545 HTTP/1.1
	
	/*
	 * Sends request to server
	 * 
	 * outToServer is already initialized
	 * requestString is given by user
	 * 
	 * HTTPCommand path/to/file.extension HTTPVersion
	 */
	public static void sendToServer(PrintWriter outToServer, String requestString) {
		outToServer.println(requestString + "\r\n");
		outToServer.println(System.lineSeparator());
		outToServer.flush();
	}
	
	/*
	 * prints response from server
	 * 
	 * inFromServer is already initialized
	 */
	public static void readAndPrintFromServer(BufferedReader inFromServer) throws IOException {
		//inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		System.out.println("FROM SERVER: ");
		String in;
		while ((in = inFromServer.readLine()) != null) {
			System.out.println(in);
			if (!inFromServer.ready()) {
				break;
			}
		}	
	}
	
	/*
	 * returns response from server as string
	 * 
	 * inFromServer is already initialized
	 */
	public static String readFileFromServer(BufferedReader inFromServer) throws IOException {
		StringBuilder sb = new StringBuilder();
		String in;
		while ((in = inFromServer.readLine()) != null) {
			sb.append(in);
			if (!inFromServer.ready()) {
				break;
			}
		}
		return sb.toString();
	}
	
	public static void handleRequest(String requestString, PrintWriter outToServer, BufferedReader inFromServer, Socket clientSocket) throws IOException {
		/*
		 * 0: HTTPCommand
		 * 1: pathToFile
		 * 2: HTTP protocol
		 */
		String[] parsedRequest = requestString.split("\\s+");
		switch (parsedRequest[0]) {
		case "GET":
			get(parsedRequest[1], outToServer, inFromServer, parsedRequest[2], clientSocket);
			break;
		case "HEAD":
			head(parsedRequest[1], outToServer, inFromServer, parsedRequest[2]);
			break;
		case "PUT":
			put(parsedRequest[1], outToServer, inFromServer, parsedRequest[2]);
			break;
		case "POST":
			post(parsedRequest[1], outToServer, inFromServer, parsedRequest[2]);
			break;
		default:
			sendToServer(outToServer, requestString);
			readAndPrintFromServer(inFromServer);
		}
		
	}

	// requests HTML page and finds all embedded images in that page
	public static void get(String pathToFile, PrintWriter outToServer, BufferedReader inFromServer, String protocol, Socket clientSocket) throws IOException {
		sendToServer(outToServer, "GET " + pathToFile + " " + protocol);
		String file = readFileFromServer(inFromServer);
		
		// get all images on the page
		String[] splitOnTags = file.split("<");
		for (String tag : splitOnTags) {
			if (tag.startsWith("img")) {
				// get the correct path, extension, ...
				String path = tag.split("\"")[1];
				String extension = path.split("\\.")[1];
				String pathWithoutExtension = path.split("\\.")[0];
				String[] pathWithoutExtensionSplit = pathWithoutExtension.split("/");
				String imageName = pathWithoutExtensionSplit[pathWithoutExtensionSplit.length - 1];
				
				// send request to server to get the image specified
				sendToServer(outToServer, "GET " + path + " " + protocol);
				
				// read the response from the server if image is found
				// and write to disk
				System.out.println("image: " + imageName + "." + extension + " is requested");
				String inputFromServer = readFileFromServer(inFromServer);
				System.out.println(inputFromServer);
				
				if (!inputFromServer.startsWith("error 404")) {
					ImageInputStream imageInput = ImageIO.createImageInputStream(clientSocket.getInputStream());
					BufferedImage img = ImageIO.read(imageInput);
					
					try {
						System.out.println("trying to write image to disk");
						ImageIO.write(img, extension, new File("src/foundImages/" + imageName));
						System.out.println("image: " + imageName + "." + extension + " is saved");
					} catch (IllegalArgumentException illegalArgumentException) {
						System.out.println("image was not valid");
					}
					
				}
			}
		}
	}
	
	public static void head(String pathToFile, PrintWriter outToServer, BufferedReader inFromServer, String protocol) throws IOException {
		sendToServer(outToServer, "HEAD " + pathToFile + " " + protocol);
		readAndPrintFromServer(inFromServer);
	}
	
	public static void put(String pathToFile, PrintWriter outToServer, BufferedReader inFromServer, String protocol) {
		
	}
	
	public static void post(String pathToFile, PrintWriter outToServer, BufferedReader inFromServer, String protocol) {
		
	}
	
	
	public static boolean isHTTP0(String protocol) {
		return (protocol.substring(protocol.length() - 1).equals("0"));
	}
	
	public static void requestLoop(BufferedReader inFromUser, String requestString, Socket clientSocket) {
		try {
			PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			handleRequest(requestString, outToServer, inFromServer, clientSocket);
			
			if (isHTTP0(requestString)) {
				System.out.println("Session stopped");
				clientSocket.close();
				return;
			}
			// HTTP/1.1: connection remains open until closed by server
			while (true) {
				String request = inFromUser.readLine();
				
				handleRequest(request, outToServer, inFromServer, clientSocket);
				
				// if protocol is HTTP/1.0
				// close connection after request is handled
				if (isHTTP0(request)) {
					System.out.println("Session stopped");
					clientSocket.close();
					break;
				}
			}
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String argv[]) throws Exception {
		// get input from user
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String sentence = inFromUser.readLine();
		
		/* parse command into individual parameters:
		 *  0: HTTP client (name of executable)
		 *  1: Command (GET, etc.)
		 *  2: URI
		 *  3: Port
		 *  4: Protocol
		 */
		String[] parsedCommand = sentence.split("\\s+");
		
		// URI in form: server/resource
		// split it:
		String[] uri = Parser.splitUri(parsedCommand[2]);
		
		// create socket to connect to the server
		Socket clientSocket = new Socket(uri[0], Integer.parseInt(parsedCommand[3]));
		
		// Connection should be properly set up at this point
		
		// Pass message to server
		// HTTPCommand path/file HTTPVersion
		String requestString = parsedCommand[1] + " " + uri[1] + " " +  parsedCommand[4];
		requestLoop(inFromUser, requestString, clientSocket);
		
		// clientSocket.close();
	}
}