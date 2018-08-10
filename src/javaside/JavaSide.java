/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaside;

import java.util.Scanner;
import javaside.Socket.TcpServerEventHandler;
import javaside.Socket.TcpServer;
import javaside.Socket.TcpClientEventHandler;
import javaside.Socket.TcpClient;

/**
 *
 * @author Abbas
 */
public class JavaSide {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int port = 123;
        System.out.println("Please Enter port of server:");
        Scanner scanner = new Scanner(System.in);
        port = Integer.parseInt(scanner.nextLine());
        
        TcpServer server = new TcpServer(port);
        
        System.out.println("TCP server start - port:" + port);

        // add event listener
        final TcpServer that_server = server;
        server.addEventHandler(new TcpServerEventHandler() {
            @Override
            public void onMessage(int client_id, String line) {
                System.out.println("* <" + client_id + "> " + line);
            }

            @Override
            public void onAccept(int client_id) {
                System.out.println("* <" + client_id + "> connection accepted");
                that_server.setReadInterval(100 + that_server.getClients().size() * 10);
            }

            @Override
            public void onClose(int client_id) {
                System.out.println("* <" + client_id + "> closed");
            }
        });

        // start TCP socket server
        server.listen();

        // broadcast
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        Scanner scanner = new Scanner(System.in);
                        String line = scanner.nextLine();
                        if (line != null&&!"".equals(line)) {
                            int data = Integer.parseInt(line);
                            if (that_server != null && that_server.getClients().size() > 0) {
                                that_server.getClients().forEach((_item) -> {
                                    _item.send(data);
                                });
                            }
                        }
                    } catch (InterruptedException | NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
