import java.io.*;
import java.net.*;

class Client {
	public static void main(String argv[]) throws Exception {
		
				
		if(!checkValidity(argv)) {
			return;
		}
		
		String server = argv[1].split("/")[0];
		
		
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//Socket clientSocket = new Socket(server, 80);
		//PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		//String serverInput;
		
		get(argv[1], server);
		
		
		if(argv[0].equals("GET")) {
			get(argv[1], server);
			
		}
		
		
		
		
		/**
		else if (argv[0].equals("HEAD")) {
			head(argv[1], server);
		}
		
		else if (argv[0].equals("PUT")) {
			put(argv[1], server);
		}
		
		else if (argv[0].equals("POST")) {
			post(argv[1], server);
		}*/
		
		
			
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
			

	
		//clientSocket.close();
	}
	
	//////////////////////////////////ENKEL HTTP1.0//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void get(String URL, String server) throws Exception { ///////// exception catching nog fixen
		Socket clientSocket = new Socket(server, 80);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		outToServer.println("GET " + URL + " " + "HTTP/1.0");
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		clientSocket.close();
	}
	
	///////////////////GEEN HTTP1.1 versie nodig/////////////////////////////////////////////////////////////////
	public static void head(String URL, String server) throws Exception { /////// exception catching nog fixen
		Socket clientSocket = new Socket(server, 80);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		outToServer.println("HEAD " + URL + " " + "HTTP/1.0");
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		clientSocket.close();
	}
	
	////////////////////////ENKEL HTTP1.0////////////////////////////////////////////////////////
	public static void put(String URL, String server) throws Exception {////////////exception catching nog fixen
		Socket clientSocket = new Socket(server, 80);
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		
		outToServer.println("GET " + URL + " " + "HTTP/1.0");
		outToServer.println(""); 
		String serverInput;
		while(( serverInput = inFromServer.readLine()) != null) System.out.println(serverInput);
		clientSocket.close();
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

		if (!(argv[2].equals("HTTP/1.0") || argv[2].equals("HTTP/1.1"))) {
			System.out.println("Please use a valid HTTPVersion. Choose from: 1.0 or 1.1");
			System.out.println(argv[2]);
			return false;
		}
		return true;
	}

}
