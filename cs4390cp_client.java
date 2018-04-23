import java.io.*;
import java.util.*;
import java.net.*;

/**
 *	Main class for the chat client. Enables a user to chat with fellow users after connecting to a central server
 */
public class cs4390cp_client{
	static int port = 1;
	static Socket socket;
	static PrintWriter toServer;
	static BufferedReader fromServer;

	/**
	 *	Main method. Sets up a socket, probes for a valid server, and communicates with the server.
	 *	@args not used
	 */
	public static void main(String[] args) throws IOException{
		try{
			try{
				socket = new Socket("localhost", port);
			} catch (UnknownHostException e){
				System.out.println("ERROR: cs4390cp_client FLAG 0");
				System.exit(-1);
			} catch (IOException e){
				System.out.println("ERROR: cs4390cp_client FLAG 1");
				System.exit(-1);
			}

			toServer = new PrintWriter(socket.getOutputStream(), true);
			new serverRecieve().start();

			Scanner sc = new Scanner(System.in);

			System.out.println("Chat client has been set up successfully on port " + port + ". To exit, use \"ctrl\" + \"c\"");

			while(true){
				String temp = sc.nextLine();
				toServer.println(temp);
			}

		} finally{
			socket.close();
		}
	}

	/**
	 * Subclass that enables the chat client to recieve messages from the chat server on a seperate thread, enabling for concurrent user and server inputs.
	 */
	private static class serverRecieve extends Thread{

		/**
		 * run method run by the thread class upon calling of the extended start method.
		 */
		public void run(){
			try{
				fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while(true){
					String temp = fromServer.readLine();
					if(temp == null){
						try{
							wait();
						} catch (InterruptedException e){
							System.out.println("ERROR: serverSend FLAG 1");
						}
					} else{
						System.out.println(temp);
					}
				}
			} catch (IOException e){
				// System.out.println(e);
				System.out.printf("The server has been disconnected. The client is now closing.");
				System.exit(-1);
			}
		}
	}
}