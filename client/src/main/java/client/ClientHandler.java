package org.umcs.client;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements  Runnable{
    BufferedReader reader;
    PrintWriter writer;

    public ClientHandler(Socket clientSocket) throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()
                ));
        writer = new PrintWriter(clientSocket.getOutputStream(),true);
    }

    @Override
    public void run() {
        String rawMessage;
        try {
            while((rawMessage = reader.readLine())!= null) {
                System.out.println(rawMessage);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(String message){
        writer.println(message);
    }
}
