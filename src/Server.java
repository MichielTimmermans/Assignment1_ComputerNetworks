import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
	// handler maken, moet runnable implementeren
	// nieuwe thread maken, en handler meegeven

	
	
	public static void main(String argv[]) throws Exception {
		ServerSocket servSocket = new ServerSocket(10000);
		while (true) {
			Socket connectionSocket = servSocket.accept();
			if(connectionSocket != null) {
				System.out.println("got request");
				Handler h = new Handler(connectionSocket);
				Thread thread = new Thread(h);
				thread.start();
			}
			//BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			//PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
			//String clientSentence = inFromClient.readLine();
			
			
		}
	} 
	
	
}