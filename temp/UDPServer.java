/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author barab
 *
 */
public class UDPServer implements Runnable {

    private DatagramSocket socket;
    private byte[] input, output;
    private boolean runserver;

    public UDPServer() throws Exception {
        socket = new DatagramSocket(10250);
        input = new byte[1024];
        output = "pong".getBytes();
        runserver = true;
    }

    @Override
    public void run() {
        DatagramPacket packet;
        try {
            while (runserver) {
                packet = new DatagramPacket(input, input.length);
                socket.receive(packet);
                handleRecieve(packet);
                handleSend(packet);
            }
        } catch (IOException E) {

        }
    }

    private void handleRecieve(DatagramPacket packet) {
        String packetData = new String(packet.getData());
        //do whatever you need with the data, store it somewhere or something, run other methods, etc.
        
        //once your done with that, preload the output
        output = ("Hi I'm the server and I recieved: " + packetData).getBytes();
    }

    private void handleSend(DatagramPacket packet) throws IOException{
        InetAddress client = packet.getAddress();
        int clientPort = packet.getPort();
        //the datagram packet can hold any byte array, it doesn't have to be a string
        //It can even hold your own objects as long as you implement a tobytearray method
        DatagramPacket out = new DatagramPacket(output, output.length, client, clientPort);
        socket.send(out);
    }

    public void close() {
        runserver = false;
        socket.close();
    }

    
}
