import java.io.*;
import java.net.*;

class Client {
	//derp HEAD www.google.com/index.html 80 HTTP/1.1
	//derp HEAD localhost/index.html 4545 HTTP/1.0
	
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
	
	public static boolean isHTTP0(String protocol) {
		return (protocol.substring(protocol.length() - 1).equals("0"));
	}
	
	public static void requestLoop(BufferedReader inFromUser, String requestString, Socket clientSocket) {
		try {
			// send request to server
			PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream());
			sendToServer(outToServer, requestString);
			
			// wait for response from server
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			readAndPrintFromServer(inFromServer);
			
			// close connection after first message passed to server if HTTP/1.0
			if (isHTTP0(requestString)) {
				System.out.println("Session stopped");
				clientSocket.close();
				return;
			}
			
			// HTTP/1.1: connection remains open until closed by server
			while (true) {
				String request = inFromUser.readLine();
				System.out.println(request);
				
				// request should be:
				// HTTPCommand path/to/file.extension HTTPVersion
				sendToServer(outToServer, request);
				readAndPrintFromServer(inFromServer);
				
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