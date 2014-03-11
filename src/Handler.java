import java.io.*;
import java.net.*;

public class Handler implements Runnable {

	public Handler(Socket connectionSocket) throws Exception {
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream(), true);
		String clientSentence = inFromClient.readLine();

	}
	
	public void decode(String sentence, PrintWriter outToClient) {
		String delimiters = "[ ]";
		String[] tokens = sentence.split(delimiters);
		int i = 0;
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}
		
		if(tokens[0].equals("GET")) {
			GET(tokens[1], outToClient);
		}
		else if(tokens[0].equals("HEAD")) { 
			HEAD(tokens[1], outToClient);
		}
		else if(tokens[0].equals("PUT")) {
			PUT(tokens[1], outToClient);
		}
		else if(tokens[0].equals("POST")) {
			POST(tokens[1], outToClient);
		}
		else System.out.println("unknown error in decode method");
	}
	
	public void GET(String URI, PrintWriter outToClient) {
		
	}
	
	public boolean checkValidity(String sentence) {

		String delimiters = "[ ]";
		String[] tokens = sentence.split(delimiters);
		if (tokens.length != 3) {
			System.out.println("Wrong format, use: COMMAND URI HTTP-VERSION");
			System.out.println("Example: 'GET /index 1.0'");
			return false;
		}

		int i = 0;
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}

		if (!(tokens[0].equals("GET") || tokens[0].equals("HEAD") || tokens[0].equals("PUT") || tokens[0].equals("POST"))) {
			System.out.println("Please use a valid HTTPCommand, choose from: GET, HEAD, PUT or POST");
			System.out.println(tokens[0]);
			return false;
		}

		if (!(tokens[2].equals("HTTP/1.0") || tokens[2].equals("HTTP/1.1"))) {
			System.out.println("Please use a valid HTTPVersion. Choose from: 1.0 or 1.1");
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

	

