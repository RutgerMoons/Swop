import java.io.*;
import java.net.*;

class Client {
	
	public static String escapeSequence = "^]";
	
	public static void requestLoop(String s, Socket clientSocket) {
		try {
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			String outToServerString = s;
			outToServer.writeBytes(outToServerString + '\n');
		
			// Retrieve message from server
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			
			// close connection after first message passed to server if HTTP/1.0
			if (s.substring(s.length() - 1).equals("0")) {
				System.out.println("Session stopped");
				return;
			}
			
			while (true) {
				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				String sentence = inFromUser.readLine();
				
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				outToServerString = sentence;
				outToServer.writeBytes(outToServerString + '\n');
				
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				modifiedSentence = inFromServer.readLine();
				if (modifiedSentence.equals(escapeSequence)) {
					System.out.println("Session stopped");
					return;
				}
				System.out.println("FROM SERVER: " + modifiedSentence);
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
		 *  0: Command (GET, etc.)
		 *  1: URI
		 *  2: Port
		 *  3: Protocol
		 */
		String[] parsedCommand = sentence.split("\\s+");
		
		// URI in form: server/resource
		// split it:
		String[] uri = parsedCommand[1].split("/");
		
		// create socket to connect to the server
		Socket clientSocket = new Socket(uri[0], Integer.parseInt(parsedCommand[2]));
		
		// Connection should be properly set up at this point
		
		// Pass message to server
		requestLoop(parsedCommand[0] + " " + uri[1] + " " +  parsedCommand[3], clientSocket);
		clientSocket.close();
	}
}