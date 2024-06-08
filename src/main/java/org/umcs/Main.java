package org.umcs;

import org.umcs.server.Server;


public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start(1599);
    }
}
