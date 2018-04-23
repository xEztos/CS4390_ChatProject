import java.net.*;
import java.util.*;
import java.io.*;

public class cs4390cp_server{
	static int port = 1;
	// ArrayList<String> names = new ArrayList<String>();
	// ArrayList<ServerSocket> ss;
	static ArrayList<PrintWriter> pw = new ArrayList<PrintWriter>();

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

	private static class ClientComm extends Thread{
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		ClientComm(Socket socket){
			this.socket = socket;
		}

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

		public void broadcast(String s){
			for(int i = 0; i < pw.size(); i++){
				PrintWriter tempW = pw.get(i);
				tempW.println(s);
			}
		}
	}
}

