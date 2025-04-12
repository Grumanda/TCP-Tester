package de.gozilalp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class is not part of the program.
 * This class provides a client socket in order to test the connection and messaging.
 *
 * @author grumanda
 */
public class ClientStart {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 9999;

        try {
            Socket socket = new Socket(serverAddress, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Get message
            new Thread(() -> {
                String message;
                try {
                    while ((message = reader.readLine()) != null) {
                        System.out.println("[Server]: " + message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            Thread.sleep(30000);
            // send constant message
            while (true) {
                String message = "Hello, I'm Client";
                writer.println(message);
                Thread.sleep(10000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}