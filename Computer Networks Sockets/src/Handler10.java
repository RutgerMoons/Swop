import java.io.*;
import java.net.*;

public class Handler10 implements Runnable {
	Socket connectionSocket;
	String escapeSequence = "^]";
	
	// connectionsSocket != null:
	// this is forced in ThreadedServer
	public Handler10(Socket socket) {
		this.connectionSocket = socket;
	}

	@Override
	public void run() {

		try {
			while (true) {
				
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String clientSentence = inFromClient.readLine();
				
				System.out.println("Received: " + clientSentence);
				
				if (clientSentence.substring(clientSentence.length() - 1).equals("0")) {
					String capsSentence = clientSentence.toUpperCase() + '\n';
					outToClient.writeBytes(capsSentence);
					outToClient.close();
					return;
				} 
				if (clientSentence.equals(escapeSequence)) {
					outToClient.writeBytes(escapeSequence);
					outToClient.close();
					System.out.println("Received escape sequence: closing connection");
					return;
				}
				
				String capsSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capsSentence);
			}
		} catch (IOException error1) {
			error1.printStackTrace();
		}	
	}


}