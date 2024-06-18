package org.umcs.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect(1960);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
