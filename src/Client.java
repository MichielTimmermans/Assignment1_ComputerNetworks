import java.io.*;
import java.net.*;
	 
	public class Client {
	    
		
		
		public static void main(String[] args) throws IOException {
	        //String hostName = args[0];
	        //int portNumber = Integer.parseInt(args[1]);
	    	String hostName = "www.google.com";
	    	int portNumber = 80;
	    	//System.out.println("Please provide the hostname and portnumber you wish to connect to:");
	    	//System.out.println("Format: hostname portnumber");
	        try (
	        	
	        		
	            Socket echoSocket = new Socket(hostName, portNumber);
	        	
	            PrintWriter out =
	                new PrintWriter(echoSocket.getOutputStream(), true);
	            BufferedReader in =
	                new BufferedReader(
	                    new InputStreamReader(echoSocket.getInputStream()));
	            BufferedReader stdIn =
	                new BufferedReader(
	                    new InputStreamReader(System.in))
	        ) {	                          
                String userInput, serverInput;
	            while (true) {
	            	userInput = stdIn.readLine();
	            	System.out.println("got userinput: " + userInput);
	            	String delimiters = "[ ]";
	                String[] tokens = userInput.split(delimiters);
	                if(tokens.length != 3) {
	                	System.out.println("Wrong format, use: COMMAND URI HTTP-VERSION");
	                }
	                out.println(userInput);
	                System.out.println("sent to google");  
	                System.out.println(in.readLine());
	                System.out.println(in.readLine());
	                System.out.println(in.readLine());
	                System.out.println(in.readLine());
	                System.out.println(in.readLine());
	                System.out.println(in.readLine());
	                
	                
	            }
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                hostName);
	            System.exit(1);
	        } 
	    }
	}
	

