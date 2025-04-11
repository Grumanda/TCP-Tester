package de.gozilalp.socket;

import de.gozilalp.configSetup.ConfigData;
import de.gozilalp.socket.gui.AbstractMessageTab;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SocketServerHandler {

    private static SocketServerHandler instance;
    private  ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private ScheduledExecutorService scheduler;
    private static Logger logger = Logger.getLogger(SocketServerHandler.class.getName());
    private static AbstractMessageTab messageTab;

    private SocketServerHandler() {
        try {
            int port = Integer.parseInt(ConfigData.PORT.getValue());
            serverSocket = new ServerSocket(port);
            logger.info("Startet Server on port " + port);

            new Thread(this::waitForClient).start();
        } catch (IOException e) {
            logger.severe("Could not start server on port");
            throw new RuntimeException(e);
        }
    }

    public static SocketServerHandler getInstance(AbstractMessageTab tab) {
        if (instance == null) {
            instance = new SocketServerHandler();
        }
        messageTab = tab;
        return instance;
    }

    private void waitForClient() {
        try {
            clientSocket = serverSocket.accept();
            messageTab.getMessageArea().append("[INFO] Client{IP: " + clientSocket.getLocalAddress()
                    + "} connected to server\n");
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            listenForMessages();
        } catch (IOException e) {
            // do nothing
        }
    }

    private void listenForMessages() {
        new Thread(() -> {
           try {
               String message;
               while ((message = input.readLine()) != null) {
                   String timestamp = LocalDateTime.now().format(
                           DateTimeFormatter.ofPattern("HH:mm:ss"));
                   String fullMessage = "[" + timestamp + "] {RECEIVED}: "
                           + message;
                   logger.finest(fullMessage);
                   messageTab.getMessageArea().append(fullMessage + "\n");
                   messageTab.scrollDown();
               }
           } catch (IOException e) {
               logger.info("Client closed the connection to the server");
               messageTab.getMessageArea().append("[INFO] Client closed connection\n");
               messageTab.scrollDown();
           }
        }).start();
    }

    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
            String timestamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm:ss"));
            String fullMessage = "[" + timestamp + "] {SENT}: " + message;
            messageTab.getMessageArea().append(fullMessage + "\n");
            messageTab.scrollDown();
        }
    }

    public void startAutoMessage(String message, int intervalSeconds) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> sendMessage(message),
                0, intervalSeconds, TimeUnit.SECONDS);
        logger.info("Started auto sending with message '" + message +
                "' every " + intervalSeconds + " seconds");
    }

    public void stopAutoMessage() {
        if (scheduler != null) {
            scheduler.shutdown();
            logger.info("Stopped auto sending");
        }
    }

    public static void stopInstance() {
        try {
            if (instance != null) {
                if (instance.scheduler != null) {
                    instance.scheduler.shutdown();
                }
                if (instance.clientSocket != null) {
                    instance.clientSocket.close();
                }
                if (instance.serverSocket != null) {
                    instance.serverSocket.close();
                }
                instance = null;
                logger.info("Server has been stopped");
            }
        } catch (IOException e) {
            logger.severe("Could not stop server? " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
