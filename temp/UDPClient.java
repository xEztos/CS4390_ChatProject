/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author barab
 */
public class UDPClient {

    private final DatagramSocket socket;
    private final InetAddress server = InetAddress.getByName("localhost");
    private byte[] input, output;

    public UDPClient() throws Exception {
        socket = new DatagramSocket();
        input = new byte[1024];
        output = new byte[1024];
    }
    
    public void send(String s) throws Exception{
        DatagramPacket packet = pack(s); //pack the string into a packet
        //System.out.println(s);
        socket.send(packet); //send that packet to the server
        packet = clear(); //clean the packet for the reply
        socket.receive(packet); //keep the socket open and wait for reply, then store in packet
        System.out.println("-> " + new String(packet.getData())); //handle reply, I just print it out to the screen here
        
        
    }
    
    private DatagramPacket pack(String s){
        output = s.getBytes();
        return new DatagramPacket(output, output.length, server, 10250);
    }
    
    private DatagramPacket clear(){
        input = new byte[1024];
        return new DatagramPacket(input, input.length, server, 10250);
    }

    public void close() {
        socket.close();
    }

}
