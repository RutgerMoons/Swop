import java.io.*;
import java.net.*;

class ThreadedServer {
	public static void main(String argv[]) throws Exception {
		ServerSocket clientSocket = new ServerSocket(4545);
		System.out.println("Server is running. Awaiting requests.");
		while (true) {
			Socket connectionSocket = clientSocket.accept();
			if (connectionSocket != null) {
				Handler h = new Handler(connectionSocket);
				Thread thread = new Thread(h);
				thread.start();
			}
		}
	}
}

// hier moet ook eerst geparsed worden, hoe weet je anders welk protocol te gebruiken?
// maar de string wordt pas gelezen in de handler zelf :/
