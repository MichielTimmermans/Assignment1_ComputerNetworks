import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
	
	public static void main(String argv[]) throws Exception {
		ServerSocket servSocket = new ServerSocket(10012);															//// Start up server
		try {
			
			while (true) {
			Socket connectionSocket = servSocket.accept();															//// Await incoming connection		
			if(connectionSocket != null) {																			//// On incoming connection, start a new thread and give a connected socket to the Handler, which will take care of the request.																	
				Handler h = new Handler(connectionSocket);
				Thread thread = new Thread(h);
				thread.start();
			}
		}	
				
			
		}
		
		finally  {
			servSocket.close();																						//// Make sure the socket gets closed in case of a crash,....
		}
	} 
	
	
}