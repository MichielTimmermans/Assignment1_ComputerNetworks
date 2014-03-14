import java.io.*;
import java.net.*;

public class Handler implements Runnable {
	BufferedReader inFromClient;																		//// Declare readers and writers
	PrintWriter outToClient;
	String clientSentence;
	Socket socket;

	public Handler(Socket connectionSocket) throws Exception {
		
		this.socket = connectionSocket;																	//// Set socket as global variable
		
	}

	@Override
	public void run() {																					//// run() method required to be able to start as thread
		try { this.inFromClient = new BufferedReader(new InputStreamReader(								//// try-catch statement to initialize readers and writers because run() cant throw exceptions
				socket.getInputStream()));
		this.outToClient = new PrintWriter(
				socket.getOutputStream(), true); }
		catch (Exception e) {}
		
		
		while(true) {																					//// get the first line of the HTTP request in order to determine what to do
			try {
				if(!((clientSentence = inFromClient.readLine()) == null)) {
					break;
				}
			} catch (IOException e) {}
						}
		
		String delimiters = "[ ]";																		//// Split the firstline into HTTPCommand , URI and HTTPVersion
		String[] tokens = clientSentence.split(delimiters);
		int i = 0;
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}
		String URI = tokens[1].substring(tokens[1].indexOf("/")+1);										//// URI = everything that comes after the server (first occurence of /)
		
		try {
		if (tokens[0].equals("GET")) {																	//// Choose the correct command to execute
			get(URI);
		} else if (tokens[0].equals("HEAD")) {
			head(URI);
		} else if (tokens[0].equals("PUT")) {
			put(URI);
		} else if (tokens[0].equals("POST")) {
			post(URI);
		} else
			System.out.println("unknown error in decode method");
		
		if(!tokens[2].equals("HTTP1.1")) {
			socket.close();																				//// Close the socket after executing the command if HTTPVersion is not HTTP/1.1, else wait for new request
		}
		else run();
		}
		catch(Exception e) {}
		
		outToClient.close();
	}

	
	public void get(String URI) throws Exception {
		String response;
		try {
			response = readFile(URI);														//// Reads the requested file

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
		}

		outToClient.println(response);																	//// Send the content of the requested file to the client
		outToClient.println("");
	}

	
	public void head(String URI) throws Exception {
		String response;
		try {
			
			response = readFile(URI).split("\n")[0];										//// Reads the requested file and selects the first line
			

		} catch (Exception esc) {
			response = "No such file exists, please provide a valid URI.";
			
		}
		
		outToClient.println(response);																	//// Sends the first line of the requested file to the client
		outToClient.println("");
	}
	
	
	public void post(String URI) throws Exception { 									
		String temp;
		String toPost = "";
		while((temp = inFromClient.readLine()) != null) {								//// concatenates all lines from the post body request on separate lines, in a single string
			toPost = toPost + temp + "\n";
			if(temp.equals("")) {
				break;
			}
		}
		addToFile(URI,toPost);															//// writes the string to the file using the method addToFile()
	}
	
	public void put(String URI) throws Exception {										//// Posts the file if it already exists
		File file = new File(URI);
		if(file.exists() && file.isFile()) {
			post(URI);
		}
	}

	public String readFile(String location) throws Exception {
		location = "C:/Server/" + location.split("/")[location.split("/").length-1];
		FileReader fr = new FileReader(location);										//// Initialize filereaders
		BufferedReader tr = new BufferedReader(fr);
		String text = "";
		String temp;
		while ((temp = tr.readLine()) != null) {										//// Adds all lines of the file to a string
			text = text + temp + "\n";
		}
		return text;
	}

	public boolean checkValidity(String sentence) {

		String delimiters = "[ ]";																			//// Checks wheter the first line of the command has three parts
		String[] tokens = sentence.split(delimiters);
		if (tokens.length != 3) {
			System.out.println("Wrong format, use: COMMAND URI HTTP-VERSION");
			System.out.println("Example: 'GET /index HTTP/1.0'");
			return false;
		}

		int i = 0;																							//// Delete all spaces from the parts of the first line
		while (i < 3) {
			tokens[i] = tokens[i].replaceAll("\\s+", "");
			i++;
		}
			
		if (!(tokens[0].equals("GET") || tokens[0].equals("HEAD")											//// Checks wether the first part of the line is a valid HTTPCommand
				|| tokens[0].equals("PUT") || tokens[0].equals("POST"))) {
			System.out
					.println("Please use a valid HTTPCommand, choose from: GET, HEAD, PUT or POST");
			System.out.println(tokens[0]);
			return false;
		}

		
		if (!(tokens[1].contains("/") && tokens[1].contains("."))) {										//// Check if the second argument contains at least one / in order to be a valid URI
			System.out.println("Please use a valid URI");
			return false;
		}
		
		if (!(tokens[2].equals("HTTP/1.0") || tokens[2].equals("HTTP/1.1"))) {								//// Checks wether the third part of the line is a valid HTTPVersion
			System.out
					.println("Please use a valid HTTPVersion. Choose from: HTTP/1.0 or HTTP/1.1");
			return false;
		}
		return true;
	}
	
	
	
	public boolean addToFile(String filepath, String text) throws Exception {
		filepath = "C:/Server/" + filepath.split("/")[filepath.split("/").length-1];
		File file = new File(filepath);
		String[] pathparts = filepath.split("/");
		String partialpath = "";
		int i =0;
		boolean mapMade;
		while(i<pathparts.length-1) {														//// Checks wether the requested path exists, and creates it if it doesn't exist already
			partialpath = partialpath + pathparts[i];
			File temp = new File(partialpath);
			if(! (temp.exists() && temp.isDirectory())) {
				mapMade = temp.mkdirs();
				System.out.println(mapMade);
			}
			i++;
		}
		
		if (!(file.exists() && file.isFile())) {											//// Creates the new, empty file if it doesn't exist yet
			file.createNewFile();
		}
		String oldFile = readFile(filepath);												//// Reads the file to be written to and saves it in a string
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		writer.println(oldFile + "\n" + text);														//// Replace the old content of the file by the old content, followed by the new content
		writer.close();
		return true;
	}

}
