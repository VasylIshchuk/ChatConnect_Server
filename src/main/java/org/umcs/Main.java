package org.umcs;

import org.umcs.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start(1599);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
