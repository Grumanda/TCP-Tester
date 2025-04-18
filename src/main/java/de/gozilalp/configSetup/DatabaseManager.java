package de.gozilalp.configSetup;

import de.gozilalp.socket.backend.Schedule;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages an embedded database connection where the config and
 * the schedule is stored.
 *
 * @author grumanda
 */
public class DatabaseManager {

    private static final String DB_NAME = "database.db";
    private static final String DB_FOLDER = System.getenv("LOCALAPPDATA") +
            File.separator + "Socket-Server";
    private static final String DB_PATH = Paths.get(DB_FOLDER, DB_NAME).toString();
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public DatabaseManager() {
        createDatabaseIfNeeded();
    }

    /**
     * This method can create the new database with all data if it does not exist.
     */
    private void createDatabaseIfNeeded() {
        File dir = new File(DB_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            if (connection != null) {
                createTables(connection);
                insertDefaultConfig(connection);
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates all tables for the database if not existing already.
     *
     * @param connection connection
     */
    private void createTables(Connection connection) {
        String createConfig = """
                CREATE TABLE IF NOT EXISTS Config (
                    ConfigName TEXT PRIMARY KEY,
                    Value TEXT NOT NULL
                );
                """;
        String createSchedule = """
                CREATE TABLE IF NOT EXISTS Schedule (
                    Name TEXT PRIMARY KEY,
                    Payload TEXT NOT NULL,
                    Interval INTEGER NOT NULL
                );
                """;

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(createConfig);
            stmt.execute(createSchedule);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertDefaultConfig(Connection connection) {
        insertConfigIfNotExists(connection, "LAF", "FLAT_MATERIAL_DESIGN_DARK_IJTHEME");
        insertConfigIfNotExists(connection, "PORT", "3004");
        insertConfigIfNotExists(connection, "TOUR", "0");
    }

    /**
     * If default values are not existing, it will create them.
     *
     * @param connection connection
     * @param name name of the config
     * @param value value for the config
     */
    private void insertConfigIfNotExists(Connection connection, String name, String value) {
        String sql = "INSERT OR IGNORE INTO Config (ConfigName, Value) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, value);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the value of the given config.
     *
     * @param configName name of the config
     * @return value of the config
     */
    public String getConfigValue(String configName) {
        String sql = "SELECT Value FROM Config WHERE ConfigName = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, configName);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                String value = result.getString("Value");
                connection.close();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Updates the value of the given config
     *
     * @param configName name of the config
     * @param value new value
     */
    public void setConfigValue(String configName, String value) {
        String sql = "UPDATE Config SET Value = ? WHERE ConfigName = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, value);
            stmt.setString(2, configName);
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list with all Schedules in database.
     *
     * @return list with {@link Schedule}
     */
    public List<Schedule> getAllSchedules() {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT * FROM Schedule";
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                list.add(new Schedule(
                        result.getString("Name"),
                        result.getString("Payload"),
                        result.getInt("Interval")
                ));
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void addSchedule(String name, String payload, int interval) {
        String sql = "INSERT INTO Schedule (Name, Payload, Interval) VALUES (?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, payload);
            stmt.setInt(3, interval);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteScheduleByName(String name) {
        String sql = "DELETE FROM Schedule WHERE Name = ?";
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLaf() {
        return getConfigValue("LAF");
    }

    public void setLaf(String value) {
        setConfigValue("LAF", value);
    }

    public String getPort() {
        return getConfigValue("PORT");
    }

    public void setPort(String value) {
        setConfigValue("PORT", value);
    }

    public String getTour() {
        return getConfigValue("TOUR");
    }

    public void setTour(String value) {
        setConfigValue("TOUR", value);
    }
}