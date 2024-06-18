package org.umcs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    
    ServerSocket serverSocket;
    Map<String, ServerHandler> clients = new HashMap<>();

    public void start(int port)  {
        try {
            serverSocket = new ServerSocket(port); //ServerSocket creates a socket to be attached to a port.
            System.out.println( "Server started" + "\n"  );
            while (true) {
                Socket clientSocket = serverSocket.accept();
            ".accept()" is used to wait. Stops program until it detects a connection from the client.
                System.out.println( "Client connected" );
                ServerHandler serverHandler = new ServerHandler(clientSocket, this);
                Thread thread = new Thread(serverHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String message, ServerHandler serverHandler){
        for(ServerHandler client : clients.values()){
            if(!client.equals(serverHandler)) client.send(message, serverHandler);
        }
    }
    public void disconnect(String message, ServerHandler serverHandler){
        clients.remove(serverHandler.username);
        for(ServerHandler client : clients.values()){
            if(!client.equals(serverHandler)) client.send(message, serverHandler);
        }
    }
}


package org.umcs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class ServerHandler implements  Runnable{
    
    private final Server server;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Socket clientSocket;
    String username;

    public ServerHandler(Socket clientSocket, Server server)  {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()
                    ));
            writer = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {

            String rawMessage;
            while((rawMessage = reader.readLine())!= null){
                if (rawMessage.equals("EXIT")) {
                    server.disconnect("left a chat", this);
                    break;
                }else server.broadcast(rawMessage,this);
            }
            clientSocket.close();
            System.out.println( "Client disconnected" );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message, ServerHandler serverHandler){
        writer.println(serverHandler.username + " "+ message);
    }

}
