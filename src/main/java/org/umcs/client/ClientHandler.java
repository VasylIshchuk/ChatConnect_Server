package org.umcs.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        String message;
        try {
            while((message = reader.readLine())!= null) System.out.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message){
        writer.println(message);
    }
}
