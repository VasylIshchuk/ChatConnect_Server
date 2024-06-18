package org.umcs.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    public void connect(int port) throws IOException {
        Socket clientSocket = new Socket("localhost",port);
        while(true) {
          ClientHandler is responsible for connecting to the server and represents the client that receives the message
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            Thread thread = new Thread(clientHandler);
            thread.start();
           Represents the client that sends the message
            Scanner reader = new Scanner(System.in);
            String message;
            while((message = reader.nextLine())!= null){
                clientHandler.send(message);
                if (message.equals("/exit")) System.exit(0);
            }
        }
    }
    
}

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

