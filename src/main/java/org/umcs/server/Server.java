package org.umcs.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    ServerSocket serverSocket;
    Map<String,ClientHandler> listClients = new HashMap<>();

    public void start(int port) throws IOException {

        serverSocket = new ServerSocket(port); //ServerSocket creates a socket to be attached to a port.
        System.out.println(ColorANSI.BLUE + "Server started"+ "\n" + ColorANSI.RESET);
        while (true) {
            Socket clientSocket = serverSocket.accept();
//            ".accept()" is used to wait. Stops program until it detects a connection from the client.
            System.out.println(ColorANSI.BLUE + "Client connected" + ColorANSI.RESET);
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            Thread thread = new Thread(clientHandler);
            thread.start();

        }
    }
    public void broadcast(String message, ClientHandler clientHandler){
        for(ClientHandler client : listClients.values()){
            if(!client.equals(clientHandler)) client.send(message,clientHandler);
        }
    }

}
