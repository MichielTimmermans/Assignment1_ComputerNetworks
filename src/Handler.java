import java.io.*;
import java.net.*;

public class Handler implements Runnable {
	BufferedReader inFromClient;
	PrintWriter outToClient;
	String clientSentence;
	Socket socket;

	public Handler(Socket connectionSocket) throws Exception {
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));
		PrintWriter outToClient = new PrintWriter(
				connectionSocket.getOutputStream(), true);
		String clientSentence = inFromClient.readLine();
		this.socket = connectionSocket;

	}

	@Override
	public void run() {
		String delimiters = "[ ]";
		String[] tokens = clientSentence.split(delimiters);
		int i = 0;
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}

		if (tokens[0].equals("GET")) {
			get(tokens[1], this.outToClient);
		} else if (tokens[0].equals("HEAD")) {
			head(tokens[1], outToClient);
		} else if (tokens[0].equals("PUT")) {
			put(tokens[1], outToClient);
		} else if (tokens[0].equals("POST")) {
			post(tokens[1], outToClient);
		} else
			System.out.println("unknown error in decode method");
		socket.close();
		outToClient.close();
	}

	// ////////////////////////////////HTTTP1.0//////////////////////////////
	public void get(String URI, PrintWriter outToClient) throws Exception {
		String response;
		try {
			response = readFile(URI);

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
		}

		outToClient.println(response);
		outToClient.println("");
	}

	// ////////////////////////////////HTTTP1.0//////////////////////////////
	public void head(String URI, PrintWriter outToClient) throws Exception {
		String response;
		try {
			response = readFile(URI).split("\n")[0];

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
		}

		outToClient.println(response);
		outToClient.println("");
	}

	public String readFile(String location) throws Exception {
		FileReader fr = new FileReader("src/filte.txt");
		BufferedReader tr = new BufferedReader(fr);
		String text = "";
		String temp;
		while ((temp = tr.readLine()) != null) {
			text = text + temp + "\n";
		}
		return text;
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

		if (!(tokens[0].equals("GET") || tokens[0].equals("HEAD")
				|| tokens[0].equals("PUT") || tokens[0].equals("POST"))) {
			System.out
					.println("Please use a valid HTTPCommand, choose from: GET, HEAD, PUT or POST");
			System.out.println(tokens[0]);
			return false;
		}

		if (!(tokens[2].equals("HTTP/1.0") || tokens[2].equals("HTTP/1.1"))) {
			System.out
					.println("Please use a valid HTTPVersion. Choose from: 1.0 or 1.1");
			return false;
		}
		return true;
	}

}
