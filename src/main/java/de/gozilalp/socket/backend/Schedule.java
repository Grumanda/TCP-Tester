package de.gozilalp.socket.backend;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class defines a schedule for the {@link SocketServerHandler}.
 * They are used to define telegrams with an interval in order to automatically send telegrams.
 *
 * @author grumanda
 */
public class Schedule {

    private final ScheduledExecutorService SCHEDULAR;
    private final String NAME;
    private final String PAYLOAD;
    private final int INTERVAL;

    public Schedule(String name, String payload, int interval) {
        this.NAME = name;
        this.PAYLOAD = payload;
        this.INTERVAL = interval;
        SCHEDULAR = Executors.newSingleThreadScheduledExecutor();
    }

    public String getNAME() {
        return NAME;
    }

    public String getPAYLOAD() {
        return PAYLOAD;
    }

    public int getINTERVAL() {
        return INTERVAL;
    }

    public ScheduledExecutorService getSCHEDULAR() {
        return SCHEDULAR;
    }
}