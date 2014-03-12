import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String argv[]) throws Exception {
		ServerSocket clientSocket = new ServerSocket(6789);
		while (true) {
			Socket connectionSocket = clientSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			String capsSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capsSentence);
		}
	}
}