package org.umcs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class ClientHandler implements  Runnable{
    Server server;
    BufferedReader reader;
    PrintWriter writer;
    String username;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.server = server;
        reader = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()
                ));
        writer = new PrintWriter(clientSocket.getOutputStream(),true);
    }

    @Override
    public void run() {
        try {
            clientRegistration();
            String rawMessage;
            while((rawMessage = reader.readLine())!= null){
                server.broadcast(rawMessage,this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message, ClientHandler clientHandler){
        writer.println(ColorANSI.BLUE +
                clientHandler.username + " " +
                ColorANSI.RESET +
                message);
    }


    public void clientRegistration() throws IOException {
        writer.println();
        writer.println(ColorANSI.BLUE + "Welcome to the server ^_^" );
        getUsername();
        writer.println(ColorANSI.BLUE + "Thanks ^-^" );
        writer.println("Now you can chat with other users ^.^");
        writer.println("Let's go!" + ColorANSI.RESET);
        writer.println();
    }

    public void getUsername() throws IOException {
        writer.println("Write your username so that other users know that you have connected to the server o_o"
                + ColorANSI.RESET);
        username = reader.readLine();

        boolean uniqueUsername = false;
        while(!uniqueUsername) {
            boolean isUnique = true;
            for (String key : server.listClients.keySet() ) {
                if (username.equals(key)) {
                    writer.println(ColorANSI.MAGENTA + "Sorry, this username is already in use. " +
                            "Please try again o.o" + ColorANSI.RESET );
                    username = reader.readLine();
                    isUnique = false;
                    break;
                }
            }
            if(isUnique) uniqueUsername = true;
        }
        server.listClients.put(username, this);

        server.broadcast(ColorANSI.RESET +
                        ColorANSI.GREEN +
                        "joined" +
                        ColorANSI.RESET,
                this);
    }
}
