package by.epam.shop.service;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;


/**
 * This Connection Manager. It's responsible for connections to Database
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

public class ConnectionManager {

    final static Logger log = Logger.getLogger(ConnectionManager.class);


    private static ConnectionManager instance = null;

    /**
     * Connection pool
     */
    public static ArrayList<Connection> connection =
            new ArrayList<Connection>();

//    private static Queue<ConfigManager>

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
            Integer connectionCount = Integer.valueOf(
                    ConfigManager.getProperty(ConfigManager.DB_MAX_NUMBER_OF_CONNECTION)
            );
            for (int i = 0; i < connectionCount; i++) {
                connection.add(createConnection());
            }
        }
        return instance;
    }

//    public static ConnectionManager getInstance() {
//        if (instance == null) {
//            System.out.println("instance == null");
//            synchronized (ConnectionManager.class) {
//                if (instance == null) {
//                    System.out.println("Synchronized");
//                    instance = new ConnectionManager();
//                    Integer connectionCount = Integer.valueOf(
//                            ConfigManager.getProperty(ConfigManager.DB_MAX_NUMBER_OF_CONNECTION)
//                    );
//                    for (int i = 0; i < connectionCount; i++) {
//                        System.out.print("| " + (i+1) + " |");
//                        connection.add(createConnection());
//                    }
//                    System.out.println("");
//                }
//
//            }
//        }
//        return instance;
//    }

    /**
     * This method creates connection
     */
    private static Connection createConnection() {
        Connection connection = null;
        String user = ConfigManager.getProperty(ConfigManager.DB_USER);
        String pass = ConfigManager.getProperty(ConfigManager.DB_PASSWORD);
        String driverName = ConfigManager.getProperty(ConfigManager.DB_DRIVER);
        String dbURL = ConfigManager.getProperty(ConfigManager.DB_URL);
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", pass);
        properties.setProperty("useSSL", "false");
        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(dbURL, properties);
        } catch (SQLException e) {
            log.error("SQLException " + e);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException " + e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException " + e);
        } catch (InstantiationException e) {
            log.error("InstantiationException " + e);
        }
        return connection;
    }

    /**
     * This method takes connection from pool of the connection
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = connection.get(0);
            connection.remove(0);
        } catch (IndexOutOfBoundsException e) {
        }
        return con;
    }

    /**
     * This method returns connection in pool of the connection
     */
    public void releaseConnection(Connection con) {
        connection.add(con);
    }
}
