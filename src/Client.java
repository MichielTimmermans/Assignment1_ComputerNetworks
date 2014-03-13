import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client {
	
	static int portNumber = 10000;
	public static void main(String[] argc) throws Exception {
		
		String argv[] = {"HEAD", "localhost/index.txt", "HTTP/1.0"}	;
		if(!checkValidity(argv)) {
			return;
		}
		
		String[] arg1 = argv[1].split("/"); 
		String server = arg1[0];
		int i = 1;
		String URI = "";
		while(i<arg1.length) {
			URI = URI + "/" + arg1[i];
			i++;
		}
		
		//String server = argv[1].split("/")[0];
		
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//Socket clientSocket = new Socket(server, 80);
		//PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		//String serverInput;
		
		
		
		
		if(argv[0].equals("GET")) {
			get(URI, server);
			
		}
		
		
		
		
		
		else if (argv[0].equals("HEAD")) {
			head(argv[1], server);
		}
		
		else if (argv[0].equals("PUT")) {
			put(argv[1], server);
		}
		
		else if (argv[0].equals("POST")) {
			
			post(argv[1], server);
						
		}
		
		else {
			System.out.println("I don't know what went wrong.");
		}
		
		
			
			/**String sentence = inFromUser.readLine();
			outToServer.println(sentence);
			outToServer.println("");
			boolean gotResponse = false;
			String webPage = "";
			while (true) {
				serverInput = "" + inFromServer.readLine();
				if (!(serverInput == null || serverInput.equals("null"))) {
					webPage = webPage + serverInput;
					System.out.println("FROM SERVER: " + serverInput);
					gotResponse = true;
				} else {
					if (gotResponse) {
						break;
					}
				}

			} */
			

	
		
	}
	
	//////////////////////////////////ENKEL HTTP1.0//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void get(String URL, String server) throws Exception { ///////// exception catching nog fixen
		////////////////////////////////image fetching
		
		Socket clientSocket = new Socket(server, portNumber);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		outToServer.println("GET " + URL + " " + "HTTP/1.0");
		outToServer.println(""); 
		String serverInput;
		String page = "";
		while(( serverInput = inFromServer.readLine()) != null) {
			System.out.println(serverInput);
			page = page + serverInput;
		}
		String[] images = findImages(page);
		int i =0; 
		while(i<images.length){
			System.out.println("Retrieved image: " + images[i]);
			i++;   ////////////////////writetofile
		}
		outToServer.close();
		clientSocket.close();
	}
	
	///////////////////GEEN HTTP1.1 versie nodig/////////////////////////////////////////////////////////////////
	public static void head(String URL, String server) throws Exception { /////// exception catching nog fixen
		Socket clientSocket = new Socket(server, portNumber);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		outToServer.println("HEAD " + URL + " " + "HTTP/1.0");
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		outToServer.close();
		clientSocket.close();
	}
	
	////////////////////////ENKEL HTTP1.0////////////////////////////////////////////////////////
	public static void put(String URL, String server) throws Exception {////////////exception catching nog fixen
		Socket clientSocket = new Socket(server, portNumber);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String userInput = inFromUser.readLine();   //////////////////////////supports only one line
		int contentLength = userInput.length();
		outToServer.println("PUT " + URL + " " + "HTTP/1.0"); 
		outToServer.println("Content-Length: " + contentLength);
		outToServer.println(userInput);
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		outToServer.close();
		clientSocket.close();
	}
	
		////////////////////////ENKEL HTTP1.0////////////////////////////////////////////////////////
	public static void post(String URL, String server) throws Exception {////////////exception catching nog fixen
		Socket clientSocket = new Socket(server, portNumber);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String userInput = inFromUser.readLine(); ///////////////////supports only one line
		int contentLength = userInput.length();
		outToServer.println("POST " + URL + " " + "HTTP/1.0"); 
		outToServer.println("Content-Length: " + contentLength);
		outToServer.println(userInput);
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		outToServer.close();
		clientSocket.close();
	}
	
	public static String[] findImages(String body) {
		String[] intermediate = body.split("<img");
		String[] imagecontainers = new String[intermediate.length-1];
		int i = 1;
		while(i<intermediate.length) {
			imagecontainers[i-1] = intermediate[i];
			i++;
		}
		i=0;
		String temp;
		ArrayList<String> result= new ArrayList<String>();
		while(i<imagecontainers.length) {
			temp = imagecontainers[i].split(">")[0];
			temp = temp.substring(temp.indexOf("src=\""));
			temp = temp.split("\"")[0];	
			result.add(temp);
			i++;
		}
		String[] res = new String[result.size()];
		i=0;
		while(i<result.size()) {
			res[i] = result.get(i);
			i++;
		}
		
		return res;
		
		
	}

	public static boolean checkValidity(String argv[]) {

		
		if (argv.length != 3) {
			System.out.println("Wrong format, use: COMMAND URI HTTP-VERSION");
			System.out.println("Example: 'GET www.google.com/index 1.0'");
			return false;
		}

		
		if (!(argv[0].equals("GET") || argv[0].equals("HEAD") || argv[0].equals("PUT") || argv[0].equals("POST"))) {
			System.out.println("Please use a valid HTTPCommand, choose from: GET, HEAD, PUT or POST");
			System.out.println(argv[0]);
			return false;
		}
		
		if(!(argv[1].contains("/") && argv[1].contains("."))) {
			System.out.println("Please use a valid URI");
			return false;
		}

		if (!(argv[2].equals("HTTP/1.0") || argv[2].equals("HTTP/1.1"))) {
			System.out.println("Please use a valid HTTPVersion. Choose from: 1.0 or 1.1");
			System.out.println(argv[2]);
			return false;
		}
		return true;
	}

}
