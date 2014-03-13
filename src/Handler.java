import java.io.*;
import java.net.*;

public class Handler implements Runnable {
	BufferedReader inFromClient;
	PrintWriter outToClient;
	String clientSentence;
	Socket socket;

	public Handler(Socket connectionSocket) throws Exception {
		
		this.socket = connectionSocket;
		System.out.println("handler made");
	}

	@Override
	public void run() {
		System.out.println("thread started");
		try { this.inFromClient = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		this.outToClient = new PrintWriter(
				socket.getOutputStream(), true); }
		catch (Exception e) {}
		
		System.out.println(inFromClient == null);
		while(true) {
			try {
				if(!((clientSentence = inFromClient.readLine()) == null)) {
					break;
				}
			} catch (IOException e) {}
						}
		String delimiters = "[ ]";
		System.out.println(clientSentence);
		String[] tokens = clientSentence.split(delimiters);
		int i = 0;
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}
		String URI = tokens[1].substring(tokens[1].indexOf("/")+1);
		
		try {
		if (tokens[0].equals("GET")) {
			get(URI);
		} else if (tokens[0].equals("HEAD")) {
			head(URI);
		} else if (tokens[0].equals("PUT")) {
			put(URI);
		} else if (tokens[0].equals("POST")) {
			post(URI);
		} else
			System.out.println("unknown error in decode method");
		socket.close();
		}
		catch(Exception e) {}
		
		outToClient.close();
	}

	// ////////////////////////////////HTTTP1.0//////////////////////////////
	public void get(String URI) throws Exception {
		String response;
		try {
			response = readFile("server/" + URI);

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
		}

		outToClient.println(response);
		outToClient.println("");
	}

	// ////////////////////////////////HTTTP1.0//////////////////////////////
	public void head(String URI) throws Exception {
		String response;
		try {
			
			response = readFile("server/" + URI).split("\n")[0];
			

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
			
		}
		outToClient.println(URI);
		outToClient.println(response);
		outToClient.println("");
	}
	
	////////////////////////HTTP1.0/////////////:
	public void post(String URI) throws Exception { //////////////exception handling
		String temp;
		String toPost = "";
		while((temp = inFromClient.readLine()) != null) {
			toPost = toPost + temp + "\n";
		}
		addToFile(URI,toPost);
	}
	
	public void put(String URI) throws Exception {///////////exception handling
		File file = new File(URI);
		if(file.exists() && file.isFile()) {
			post(URI);
		}
	}

	public String readFile(String location) throws Exception {
		FileReader fr = new FileReader(location);
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
	
	
	
	public boolean addToFile(String filepath, String text) throws Exception { ///////////////////////////////////////exception handling
		File file = new File(filepath);
		String[] pathparts = filepath.split("/");
		String partialpath = "";
		int i =0;
		while(i<pathparts.length-1) {
			partialpath = partialpath + pathparts[i];
			File temp = new File(partialpath);
			if(! (temp.exists() && temp.isDirectory())) {
				temp.mkdirs();
			}
			i++;
		}
		
		if (!(file.exists() && file.isFile())) {
			file.createNewFile();
		}
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println("text");
		writer.close();
		return true;
	}

}
