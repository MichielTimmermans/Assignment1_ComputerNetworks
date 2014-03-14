
public class makeThingsRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String[] argC = new String[] {"HEAD", "localhost/index.txt", "HTTP/1.1"};
		//String[] argS = new String[] {""};
		//Server.main(argS);
		System.out.println("startclient");
		Client.main(argC);
		

	}

}
