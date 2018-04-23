import java.net.*;
import java.util.*;
import java.io.*;

/**
 *	Main class for the chat server.
 */
public class cs4390cp_server{
	static int port = 1;
	// ArrayList<String> names = new ArrayList<String>();
	// ArrayList<ServerSocket> ss;
	static ArrayList<PrintWriter> pw = new ArrayList<PrintWriter>();

	/**
	 *	Main metod of the server. This method sets up a socket, and attempts to continuously accept any incomming connection requests by a client
	 * @param args not used
	 */
	public static void main(String[] args) throws IOException{
		ServerSocket socket = null;
		try{
			socket = new ServerSocket(port);
			System.out.printf("ServerSocket successfully created on port %d%n", port);
		} catch (IOException e){
			System.out.println("ERROR FLAG 5");
			System.exit(-1);
		}

		try{
			while(true){
				new ClientComm(socket.accept()).start();
			}
		} finally{
			try{
				socket.close();
			} catch (Exception e){
				System.out.println("ERROR: FLAG 0");
				System.exit(-1);
			}
		}
	}

	/**
	 *	subclass of the client communication that is paired with a single connected chat client.
	 */
	private static class ClientComm extends Thread{
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		ClientComm(Socket socket){
			this.socket = socket;
		}

		/**
		 * The run method invoked by the start() of the extended thread class. This method will read what a client is communicating to this server program and tell all the other connected clients what this specific clientComm is associated with is saying.
		 */
		public void run(){
			try{
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);

				pw.add(output);	// adds a client PrintWriter into an arrayList of printwriters

				while(true){
					String temp = input.readLine();
					String ret = String.format("[%s:%d] - %s",socket.getLocalAddress().toString(),socket.getPort(),temp);
					System.out.println(ret);

					if(temp == null){
						String message = String.format("[%s:%d] has left the server", socket.getLocalAddress().toString(), socket.getPort());
						System.out.println(message);
						broadcast(message);
						return;
						// try{
						// 	wait();
						// } catch (InterruptedException e){
						// 	System.out.println("ERROR: FLAG 2");
						// }
						
					// } else if(temp.equalsIgnoreCase("exit")){
					// 	return;
					} else if(pw.size() == 1){
						output.printf("SERVER - There is nobody else on the server.");
					}else{
						for(int i = 0; i < pw.size(); i++){

							PrintWriter tempW = pw.get(i);
							if(tempW != output){
									tempW.println(ret);
							}
						}
					}
				}
			} catch(IOException e){	// client forcefully (ctrl + c) disconnects
				// String message = String.format("[%s:%d] has left the server%n", socket.getLocalAddress().toString(), socket.getPort());
				// System.out.print(message);
				// broadcast(message);
				System.out.println("ERROR: FLAG 1");
				return;
				// System.exit(-1);
			} finally{
				// System.out.println("finally");
				pw.remove(output);
				try{
					socket.close();
				} catch( IOException e){
					System.out.println("ERROR: FLAG 4");
				}
			}
		}

		/**
		 * A method used by the server to broadcast a message to all the users connected to this server
		 * @param s the message in the form of a string to broadcast
		 */
		public void broadcast(String s){
			for(int i = 0; i < pw.size(); i++){
				PrintWriter tempW = pw.get(i);
				tempW.println(s);
			}
		}
	}
}

