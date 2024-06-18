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
        clientRegistration();
        parseClientMessage();
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(ColorANSI.MAGENTA + "Client disconnected" + ColorANSI.RESET);
    }

    private void parseClientMessage(){
        String message;
        try {
            while((message = reader.readLine())!= null){
                if (message.equals("/exit")) {
                    server.disconnect(ColorANSI.GREEN +
                            "[SERVER]: " +
                            username +
                            " left a chat" +
                            ColorANSI.RESET, this);
                    break;
                }else if(message.equals("/online")){
                    for(String username :server.clients.keySet())
                        writer.println("\t" + ColorANSI.BLUE + username + ColorANSI.RESET);
                }else if(message.startsWith("/pm ")){
                    String[] splitMessage = message.split(" ",3);
                    server.privateMessage(splitMessage[2],this,server.clients.get(splitMessage[1]));
                } else if(message.startsWith("/command")) {
                    StringBuffer text = new StringBuffer();
                    text.append("\t• \"/exit\" - to exit the server;\n")
                            .append("\t• \"/pm username_receiver message\" - to send a private message;\n")
                            .append("\t• \"/online\" - to show a list of all chat members\n")
                            .append("\t• \"/command\" - to show all commands");
                    writer.println(ColorANSI.BLUE + text + ColorANSI.RESET);
                } else server.broadcast(message,this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void error(){
        writer.println(ColorANSI.MAGENTA +
                "This user is not connected -_-" +
                ColorANSI.RESET);
    }

    public void sendMessage(String message, ServerHandler sender){
        writer.println(ColorANSI.BLUE +
                sender.username + ": " +
                ColorANSI.RESET +
                message);
    }

    public void sendServerMessage(String message){
        writer.println(message);
    }

    public void sendPrivateMessage(String message, ServerHandler sender){
        writer.println(ColorANSI.BLUE +
                sender.username +
                " (PM): " +
                ColorANSI.RESET +
                message);
    }

    private void clientRegistration()  {
        writer.println();
        writer.println(ColorANSI.BLUE + "Welcome to the server ^_^" );
        getUsername();
        StringBuffer text = new StringBuffer();
        text.append(ColorANSI.BLUE + "Thanks ^-^\n")
                .append("Now you can chat with other users ^.^\n")
                .append("Additional commands:\n")
                .append("\t• \"/exit\" - to exit the server;\n")
                .append("\t• \"/pm username_receiver message\" - to send a private message;\n")
                .append("\t• \"/online\" - to show a list of all chat members\n")
                .append("\t• \"/command\" - to show all commands\n")
                .append("Let's go!\n"+ ColorANSI.RESET);
        writer.println(text);
    }

    private void getUsername() {
        writer.println("Write your username so that other users know that you have connected to the server o_o"
                + ColorANSI.RESET);

        try {
            username = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        validateUniqueUsername();
        server.clients.put(username, this);
        server.broadcast(ColorANSI.GREEN +
                        "[SERVER]: " +
                        username +
                        " joined" +
                        ColorANSI.RESET,
                this);
    }

    private void validateUniqueUsername(){
        boolean uniqueUsername = false;
        while(!uniqueUsername) {
            boolean isUnique = true;
            for (String key : server.clients.keySet() ) {
                if (username.equals(key)) {
                    try {
                        writer.println(ColorANSI.MAGENTA + "Sorry, this username is already in use. " +
                                "Please try again o.o" + ColorANSI.RESET );
                        username = reader.readLine();
                        isUnique = false;
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if(isUnique) uniqueUsername = true;
        }
    }
}
