/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Networking.Test;

import Networking.Client.UDPClient;
import Networking.Server.UDPServer;
import java.util.Scanner;
/**
 *
 * @author barab
 */
public class UDPtest {
    public static void main(String[] args) throws Exception{
        UDPServer server = new UDPServer();
        UDPClient client = new UDPClient();
        Scanner sc = new Scanner(System.in);
        String s;
        new Thread(server).start();
        System.out.println("Server Starting...");
        while((s = sc.nextLine()).equals("exit") == false){
            client.send(s);
        }
        client.close();
        server.close();
    }
}
