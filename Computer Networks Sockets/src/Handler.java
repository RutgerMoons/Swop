import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;


public class Handler implements Runnable { 
	Socket connectionSocket;
	String escapeSequence = "^]";

	// connectionsSocket != null:
	// this is forced in ThreadedServer
	public Handler(Socket socket) {
		this.connectionSocket = socket;
	}
	
	public void writeToClient(PrintWriter outToClient, String message) {
		outToClient.println(message + "\r\n");
		outToClient.println(System.lineSeparator());
		outToClient.flush();
	}
	
	public String readFromClient(BufferedReader inFromClient) throws IOException {
		return inFromClient.readLine();
	}
	
	public boolean isHTTP0(String protocol) {
		return (protocol.substring(protocol.length() - 1).equals("0"));
	}
	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public String getContentLengthAsString(String pathToFile) throws FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(new FileReader("src" + pathToFile))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        return(Integer.toString(everything.length()));
	    }
	}
	
	public String getContentFromFile(String pathToFile) throws FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(new FileReader("src" + pathToFile))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
 
	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        return sb.toString();
	    }
	}
	
	public void getImage(String path, Socket connectionSocket, DataOutputStream dout, PrintWriter outToClient) {
		try {
			
			try {	
				BufferedImage img = ImageIO.read(new File(path));
				writeToClient(outToClient, "Sending image");
				String extension = path.split("\\.")[1];
				ImageIO.write(img,extension,dout);
				
				dout.writeUTF("\r\n");
				dout.writeUTF(System.lineSeparator());
				dout.flush();
				
				//dout.close(); // also closes socket
			} catch (FileNotFoundException f) {
				writeToClient(outToClient, "error 404: File not found");
			}
		}
		catch (IOException i) {
			System.out.println("Requested image not found");
			
			writeToClient(outToClient, "error 404: File not found");
			
		}
	}
	
	/*
	 * date
	 * content type
	 * content length
	 */
	public void handleHead(String protocol, String pathToFile, PrintWriter outToClient) throws IOException {
		String toWrite;
		try {
			toWrite = 		protocol + 
							" 200 OK\n" + 
							"Date: " + getDate() + "\n" + 
							"Content Type: text/html; charset=UTF-8\n"+
							"Content length: " + getContentLengthAsString(pathToFile);
			writeToClient(outToClient, toWrite);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			writeToClient(outToClient, "error 404: File not found");
		}
	}
	
	public void handleGet(String protocol, String pathToFile, PrintWriter outToClient, DataOutputStream dout) throws IOException {
		String toWrite;
		String extension = pathToFile.split("\\.")[1];
		if (extension.equals("html")) {
			try {
				toWrite = 		protocol + " " +
								getContentFromFile(pathToFile);
				writeToClient(outToClient, toWrite);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				writeToClient(outToClient, "error 404: File not found");
			}
		}
		else {
			//image
			getImage("src/" + pathToFile, connectionSocket, dout, outToClient);
		}
	}
	
	public void handleRequest(String clientSentence, BufferedReader inFromClient, PrintWriter outToClient, DataOutputStream dout) {

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
				handleGet(parsedCommand[2], parsedCommand[1], outToClient, dout);
				if (isHTTP0(parsedCommand[2])) {
					connectionSocket.close();
				}
				break;
			
			/*
			 * zie de Header op wikipedia
			 */
			case "HEAD":
				handleHead(parsedCommand[2], parsedCommand[1], outToClient);
				if (isHTTP0(parsedCommand[2])) {
					connectionSocket.close();
				}
				break;
			/*
			 * 	Requests that the enclosed entity be stored under the supplied URI.
			 *  If the URI refers to an already existing resource, it is modified;
			 *  if the URI does not point to an existing resource, then the server can create the resource with that URI
			 */
			case "PUT":
				System.out.println("Received PUT statement");
				//outToClient.writeBytes("Please type your HTML-page");
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
				writeToClient(outToClient, "Error 400: Bad Request");
				break;
			}
		} catch (IOException e) {
			System.err.println("IOException occurred.");
		}
	}

	
	@Override
	public void run() {

		try {
			while (!connectionSocket.isClosed()) { //
				// prepare streams
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream());
				DataOutputStream dout = new DataOutputStream(connectionSocket.getOutputStream());
				
				// read input from user
				String clientSentence = readFromClient(inFromClient);
				System.out.println("Received: " + clientSentence);
				
				// if client message == escapeSequence -> close connection
//				if (clientSentence.equals(escapeSequence)) {
//					System.out.println("Received escape sequence: closing connection");
//					connectionSocket.close();
//					return; 
//				}
				
				// process message and respond to client
				handleRequest(clientSentence, inFromClient, outToClient, dout);
			}
		} catch (IOException error1) {
			error1.printStackTrace();
		}	
	}
}
