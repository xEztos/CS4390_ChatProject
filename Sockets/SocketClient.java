// By Greg Ozbirn, University of Texas at Dallas
// Adapted from example at Sun website: 
// http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
// 11/07/07

import java.io.*;
import java.util.Scanner;
import java.net.*;

public class SocketClient
{
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	
	public void communicate()
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your name: ");
		String name = sc.nextLine();

		//Send data over socket
		out.println(name);

		//Receive text from server
		try
		{
			String line = in.readLine();
			System.out.println("Text received: " + line);
		} 
		catch (IOException e)
		{
			System.out.println("Read failed");
			System.exit(1);
		}
	}
  
	public void listenSocket(String host, int port)
	{
		//Create socket connection
		try
		{
		 socket = new Socket(host, port);
		 out = new PrintWriter(socket.getOutputStream(), true);
		 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} 
		catch (UnknownHostException e) 
		{
		 System.out.println("Unknown host");
		 System.exit(1);
		} 
		catch (IOException e) 
		{
		 System.out.println("No I/O");
		 System.exit(1);
		}
	}

	public static void main(String[] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage:  client hostname port");
			System.exit(1);
		}

		SocketClient client = new SocketClient();

		String host = args[0];
		int port = Integer.valueOf(args[1]);
		client.listenSocket(host, port);
		client.communicate();
	}
}
