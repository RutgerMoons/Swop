import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Handler11 implements Runnable { 
	Socket connectionSocket;
	String escapeSequence = "^]";

	// connectionsSocket != null:
	// this is forced in ThreadedServer
	public Handler11(Socket socket) {
		this.connectionSocket = socket;
	}
	
	
	public void handleRequest(String clientSentence, BufferedReader inFromClient, DataOutputStream outToClient) {
		// 0: Command
		// 1: resource
		// 2: protocol
		String[] parsedCommand = clientSentence.split(" ");
		try {
			switch (parsedCommand[0]) {
			/*
			 * Requests a representation of the specified resource.
			 */
			case "GET":
				
				break;
			
			/*
			 * zie de Header op wikipedia
			 */
			case "HEAD":
				
				break;
			/*
			 * 	Requests that the enclosed entity be stored under the supplied URI.
			 *  If the URI refers to an already existing resource, it is modified;
			 *  if the URI does not point to an existing resource, then the server can create the resource with that URI
			 */
			case "PUT":
				System.out.println("Received PUT statement");
				outToClient.writeBytes("Please type your HTML-page");
				String html = inFromClient.readLine();
				PrintWriter outToFile = new PrintWriter(parsedCommand[1]);
				outToFile.write(html);
				break;
			
			/*
			 * Requests that the server accept the entity enclosed in the request as a new subordinate of the web resource identified by the URI
			 */
			case "POST":
				
				break;
			
			/*
			 * Malformed expression
			 */
			default:
				outToClient.writeBytes("Malformed expression.");
				break;
			}
		} catch (IOException e) {
			System.err.println("IOException occurred.");
		}
	}

	
	@Override
	public void run() {

		try {
			while (true) {
				
				//input from client
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String clientSentence = inFromClient.readLine();
				
				System.out.println("Received: " + clientSentence);
				
				// if protocol == HTTP/1.0 -> Close connection after sending response
				if (clientSentence.substring(clientSentence.length() - 1).equals("0")) {
					String capsSentence = clientSentence.toUpperCase() + '\n';
					outToClient.writeBytes(capsSentence);
					outToClient.close();
					return;
				} 
				
				// if client message == escapeSequence -> close connection
				if (clientSentence.equals(escapeSequence)) {
					outToClient.writeBytes(escapeSequence);
					outToClient.close();
					System.out.println("Received escape sequence: closing connection");
					return;
				}
				
				// process message and respond to client
				handleRequest(clientSentence, inFromClient, outToClient);
				String capsSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capsSentence);
			}
		} catch (IOException error1) {
			error1.printStackTrace();
		}	
	}
}
