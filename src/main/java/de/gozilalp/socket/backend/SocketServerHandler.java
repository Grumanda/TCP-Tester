package de.gozilalp.socket.backend;

import de.gozilalp.configSetup.ConfigData;
import de.gozilalp.socket.gui.tabs.AbstractMessageTab;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class hosts the socket server. It is the core of this program.
 *
 * @author grumanda
 */
public class SocketServerHandler {

    private static SocketServerHandler instance;
    private final ServerSocket SERVER_SOCKET;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private List<Schedule> scheduleList = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(SocketServerHandler.class.getName());
    private static AbstractMessageTab messageTab;

    private SocketServerHandler() {
        try {
            // Open the socket server
            int port = Integer.parseInt(ConfigData.PORT.getValue());
            SERVER_SOCKET = new ServerSocket(port);
            LOGGER.info("Startet Server on port " + port);

            new Thread(this::waitForClient).start();
        } catch (IOException e) {
            LOGGER.severe("Could not start server on port");
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

    /**
     * This method accepts the client, creates the inbound and outbound
     * and starts the listening for inbound messages.
     */
    private void waitForClient() {
        try {
            clientSocket = SERVER_SOCKET.accept();
            messageTab.getMessageArea().append("[INFO] Client{IP: " + clientSocket.getLocalAddress()
                    + "} connected to server\n");
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            listenForMessages();
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * This method creates a thread where inbound messages are processed and
     * added to the gui.
     */
    private void listenForMessages() {
        new Thread(() -> {
           try {
               String message;
               while ((message = input.readLine()) != null) {
                   String timestamp = LocalDateTime.now().format(
                           DateTimeFormatter.ofPattern("HH:mm:ss"));
                   String fullMessage = "[" + timestamp + "] {RECEIVED}: "
                           + message;
                   LOGGER.finest(fullMessage);
                   messageTab.getMessageArea().append(fullMessage + "\n");
                   messageTab.scrollDown();
               }
           } catch (IOException e) {
               LOGGER.info("Client closed the connection to the server");
               messageTab.getMessageArea().append("[INFO] Client closed connection\n");
               messageTab.scrollDown();
           }
        }).start();
    }

    /**
     * This method sends a message to the client.
     *
     * @param message Message to send to the client
     */
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

    /**
     * This method creates a new {@link Schedule} and starts auto messaging.
     *
     * @param name Name of the schedule
     * @param message Message which should be sent
     * @param intervalSeconds Interval in which the message will be sent
     */
    public void startAutoMessage(String name, String message, int intervalSeconds) {
        Schedule schedule = new Schedule(name, message, intervalSeconds);
        schedule.getSCHEDULAR().scheduleAtFixedRate(() -> sendMessage(message),
                0, intervalSeconds, TimeUnit.SECONDS);
        LOGGER.info("Started auto sending with message '" + message +
                "' every " + intervalSeconds + " seconds");
        scheduleList.add(schedule);
    }

    /**
     * Stops the sending of a schedule.
     *
     * @param scheduleName Name of the schedule
     */
    public void stopAutoMessage(String scheduleName) {
        for (Schedule schedule : scheduleList) {
            if (scheduleName.equals(schedule.getNAME()) && schedule.getSCHEDULAR() != null) {
                schedule.getSCHEDULAR().shutdown();
                LOGGER.info("Stopped auto sending for schedule '" + scheduleName + "'");
            }
        }
    }

    /**
     * This method stops all schedules.
     */
    public void stopAllAutoMessages() {
        for (Schedule schedule : scheduleList) {
            if (schedule.getSCHEDULAR() != null) {
                schedule.getSCHEDULAR().shutdown();
            }
        }
    }

    /**
     * This method resets the schedules and the instance.
     */
    public static void stopInstance() {
        try {
            if (instance != null) {
                for (Schedule schedule : instance.scheduleList) {
                    if (schedule.getSCHEDULAR() != null) {
                        schedule.getSCHEDULAR().shutdown();
                    }
                }
                instance.scheduleList = new ArrayList<>();
                if (instance.clientSocket != null) {
                    instance.clientSocket.close();
                }
                if (instance.SERVER_SOCKET != null) {
                    instance.SERVER_SOCKET.close();
                }
                instance = null;
                LOGGER.info("Server has been stopped");
            }
        } catch (IOException e) {
            LOGGER.severe("Could not stop server? " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}