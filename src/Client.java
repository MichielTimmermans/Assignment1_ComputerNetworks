import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client {

	static int portNumber = 10012;																		////initialise portNumber

	public static void main(String[] argv) throws Exception {										

									
		if (!checkValidity(argv)) {																	////  Check if Input arguments form a valid input with the method checkValidity()
			return;
		}

		String[] arg1 = argv[1].split("/");															////  Split the URI into the server and the relative URI
		String server = arg1[0];																	
		int i = 1;
		String URI = "";
		while (i < arg1.length) {
			URI = URI + "/" + arg1[i];
			i++;
		}

		if (argv[0].equals("GET")) {
			get(URI, server, argv[2]);

		}

		else if (argv[0].equals("HEAD")) {
			head(argv[1], server, argv[2]);
		}

		else if (argv[0].equals("PUT")) {
			put(argv[1], server, argv[2]);
		}

		else if (argv[0].equals("POST")) {

			post(argv[1], server, argv[2]);

		}

		else {																						//// You should never come here thanx to checkValidity()
			System.out.println("I don't know what went wrong.");
		}

		

	}

	
	public static void get(String URL, String server, String HTTPVersion) throws Exception {
		Socket clientSocket = new Socket(server, portNumber);										//// Initialize the needed readers and writers
		PrintWriter outToServer = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		outToServer.println("GET " + URL + " " + "HTTP/1.0");										//// Give the HTTP request to the server
		outToServer.println("");
		String serverInput;
		String page = "";
		while ((serverInput = inFromServer.readLine()) != null) {									//// Read the response from the server
			System.out.println(serverInput);
			page = page + serverInput;
		}
		String[] images = findImages(page);															//// Scan the page for images using the method findImages()
		int i = 0;
		while (i < images.length) {
			System.out.println("Retrieved image: " + images[i]);
			//getImage(images[i]);																	//// Fetch the image to disk using the method getImage() crashes sometimes
			i++;
		}
		if(HTTPVersion.equals("HTTP/1.1")) {
			getNewCommand();
		}
		
		outToServer.close();
		clientSocket.close();
		
		
	}
	
	public static void getNewCommand() throws Exception {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("new HTTPCommand: ");
		String command = inFromUser.readLine();
		System.out.println("new URI: ");
		String URI = inFromUser.readLine();
		System.out.println("new HTTPVersion: ");
		String version = inFromUser.readLine();
		String[] argumenten = {command, URI, version};
		main(argumenten);
		
	}
	
	public static void getImage(String URL) throws Exception {
		String server = URL.split("/")[0];															//// Get the server to connect to
		System.out.println(URL);
		Socket clientSocket = new Socket(server, portNumber);										//// Initialize readers and writers
		PrintWriter outToServer = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		outToServer.println("GET " + URL + " " + "HTTP/1.0");										//// Send request to server to fetch image
		outToServer.println("");
		String serverInput;
		String page = "";
		while ((serverInput = inFromServer.readLine()) != null) {									//// Read the response from the server
			System.out.println(serverInput);
			page = page + serverInput;
		}
		clientSocket.close();
		File file = new File("C:/clientImages/" + URL.split("/")[URL.split("/").length-1]);			//// Create empty file with the proper name and extension.
		file.createNewFile();
		PrintWriter writer = new PrintWriter(new FileWriter(file));									//// Write the image to the newly created file
		writer.println(page);
		writer.close();
	}

	
	public static void head(String URL, String server, String HTTPVersion) throws Exception { 
		Socket clientSocket = new Socket(server, portNumber);										//// Initialize readers and writers
		PrintWriter outToServer = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		outToServer.println("HEAD " + URL + " " + "HTTP/1.0");										//// Send HTTP request to the server
		outToServer.println("");
		String serverInput;
		while ((serverInput = inFromServer.readLine()) != null)										//// Read input from server
			System.out.println(serverInput);
		if(HTTPVersion.equals("HTTP/1.1")) {
			getNewCommand();
		}
		outToServer.close();
		clientSocket.close();
	}

	
	public static void put(String URL, String server, String HTTPVersion) throws Exception {
		Socket clientSocket = new Socket(server, portNumber);										//// Initialize readers and writers
		PrintWriter outToServer = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		String userInput = "";						
		String temp; 
		while(!((temp = inFromUser.readLine()).equals(""))) {										//// Get the input the user wishes to put at the requested URL
			userInput = userInput + temp + "\n";
		}
		int contentLength = userInput.length();
		outToServer.println("PUT " + URL + " " + "HTTP/1.0");										//// Send the full HTTP request to server
		outToServer.println("Content-Length: " + contentLength);
		outToServer.println(userInput);
		outToServer.println("");
		String serverInput;
		while ((serverInput = inFromServer.readLine()) != null)										//// Read server response
			System.out.println(serverInput);
		if(HTTPVersion.equals("HTTP/1.1")) {
			getNewCommand();
		}
		outToServer.close();
		clientSocket.close();
	}

	
	public static void post(String URL, String server, String HTTPVersion) throws Exception {
		Socket clientSocket = new Socket(server, portNumber);										//// Initialize readers and writers
		PrintWriter outToServer = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		String userInput = "";
		String temp; 
		while(!((temp = inFromUser.readLine()).equals(""))) {										//// Get the input the user wishes to post at the requested URL
			userInput = userInput + temp + "\n";
		}
		int contentLength = userInput.length();
		outToServer.println("POST " + URL + " " + "HTTP/1.0");										//// Send the full HTTP request to server
		outToServer.println("Content-Length: " + contentLength);
		outToServer.println(userInput);
		outToServer.println("");
		String serverInput;
		while ((serverInput = inFromServer.readLine()) != null)										//// Read server response
			System.out.println(serverInput);
		if(HTTPVersion.equals("HTTP/1.1")) {
			getNewCommand();
		}
		outToServer.close();
		clientSocket.close();
	}

	public static String[] findImages(String body) {
		String[] intermediate = body.split("<img");													//// Separate the webpage into different sections each containing 1 image, and starting with an image adress
		String[] imagecontainers = new String[intermediate.length - 1];
		int i = 1;
		while (i < intermediate.length) {
			imagecontainers[i - 1] = intermediate[i];
			i++;
		}
		i = 0;
		String temp;
		ArrayList<String> result = new ArrayList<String>();											//// Search for the src in the image tag and store the URI's in an ArrayList
		while (i < imagecontainers.length) {
			temp = imagecontainers[i].split(">")[0];
			if(temp.indexOf("src=\"")>=0) {
			temp = temp.substring(temp.indexOf("src=\""));
			temp = temp.split("\"")[0];
			result.add(temp); }
			i++;
		}
		String[] res = new String[result.size()];													//// Copy the ArrayList into an array
		i = 0;
		while (i < result.size()) {
			res[i] = result.get(i);
			i++;
		}

		return res;

	}

	public static boolean checkValidity(String argv[]) {

		if (argv.length != 3) {																		//// Check wether the input has three arguments
			System.out.println("Wrong format, use: COMMAND URI HTTP-VERSION");
			System.out.println("Example: 'GET www.google.com/index 1.0'");
			return false;
		}

		if (!(argv[0].equals("GET") || argv[0].equals("HEAD")										//// Check wether the first argument is one of the four supported commands
				|| argv[0].equals("PUT") || argv[0].equals("POST"))) {
			System.out
					.println("Please use a valid HTTPCommand, choose from: GET, HEAD, PUT or POST");
			System.out.println(argv[0]);
			return false;
		}

		if (!(argv[1].contains("/") && argv[1].contains("."))) {									//// Check if the second argument contains at least one / in order to be a valid URI
			System.out.println("Please use a valid URI");
			return false;
		}

		if (!(argv[2].equals("HTTP/1.0") || argv[2].equals("HTTP/1.1"))) {							//// Check if the third argument is a valid HTTP version
			System.out
					.println("Please use a valid HTTPVersion. Choose from: 1.0 or 1.1");
			System.out.println(argv[2]);
			return false;
		}
		return true;
	}

}
