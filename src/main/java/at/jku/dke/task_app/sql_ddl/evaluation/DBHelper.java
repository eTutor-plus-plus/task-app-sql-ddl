package at.jku.dke.task_app.sql_ddl.evaluation;


import at.jku.dke.task_app.sql_ddl.config.DbConnectionParameters;
import ch.qos.logback.classic.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Db helper.
 */
// Class to handle the database connections for several users
public class DBHelper {
    //region Fields
    private static String driverClassName;
    private static String connUrl;
    private static String connPwd;
    private static String connUser;
    private static long maxLifetime;
    private static int maxPoolSize;


    private static Logger logger = null;
    private static Connection conn = null;
    private static Connection connWithSchema = null;
    private static HikariConfig config = null;
    private static HikariDataSource dataSource;
    private static Map<String, HikariDataSource> userDatasources;
    //endregion

    static {

        // Initialize logger
        logger = (Logger) LoggerFactory.getLogger(DBHelper.class);
        // Initialize map
        userDatasources = new HashMap<>();

    }

    /**
     * Function to initialize the connection with the application properties
     *
     * @param properties Specifies the application properties
     */
    public static void init(DbConnectionParameters properties) {
        // Initialize connections constants
        driverClassName = properties.driverClassName();
        connUrl = properties.url();
        connUser = properties.admin().username();
        connPwd = properties.admin().password();
        maxLifetime = properties.maxLifetime();
        maxPoolSize = properties.maxPoolSize();

        // Initialize Hikari connection
        config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(connUrl);
        config.setUsername(connUser);
        config.setPassword(connPwd);
        config.setMaxLifetime(maxLifetime);
        config.setMaximumPoolSize(maxPoolSize);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("socketTimeout", "30");
        dataSource = new HikariDataSource(config);
    }

    /**
     * Function to get the system connection
     *
     * @return Returns the Connection object for the system connection
     * @throws SQLException if an error occurs while opening the connection
     */
    public static synchronized Connection getSystemConnection() throws SQLException {
        // Check if the connection is already up
        if (conn != null && !conn.isClosed())
            return conn;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException ex) {
            logger.error("Error when opening module connection.", ex);
        }
        return null;
    }

    /**
     * Function to get the system connection
     *
     * @param schema The schema.
     * @return Returns the Connection object for the system connection
     * @throws SQLException if an error occurs while opening the connection
     */
    public static synchronized Connection getSystemConnectionWithSchema(String schema) throws SQLException {
        // Check if the connection is already up
        if (connWithSchema != null && !connWithSchema.isClosed())
            return connWithSchema;

        try {
            connWithSchema = dataSource.getConnection();
            connWithSchema.setAutoCommit(false);
            connWithSchema.setSchema(schema);
            return connWithSchema;
        } catch (SQLException ex) {
            logger.error("Error when opening module connection.", ex);
        }
        return null;
    }

    /**
     * Function to close the system connection
     */
    public static synchronized void closeSystemConnection() {
        if (conn != null) {
            try {
                conn.close();
                dataSource.close();
            } catch (SQLException ex) {
                logger.error("Error when closing module connection.", ex);
            }
        }
    }

    /**
     * Function to close the system connection
     */
    public static synchronized void closeSystemConnectionWithSchema() {
        if (connWithSchema != null) {
            try {
                connWithSchema.close();
            } catch (SQLException ex) {
                logger.error("Error when closing module connection.", ex);
            }
        }
    }

    /**
     * Function to get the connection for a specified user
     *
     * @param user   Specifies the username
     * @param pwd    Specifies the password
     * @param schema The schema.
     * @return Returns the established connection
     */
    public static Connection getUserConnection(String user, String pwd, String schema) {
        if (user == null || pwd == null)
            return null;

        try {
            // Initialize config element with the user and pwd
            HikariConfig userConfig = new HikariConfig();
            userConfig.setDriverClassName(driverClassName);
            userConfig.setJdbcUrl(connUrl);
            userConfig.setUsername(user);
            userConfig.setPassword(pwd);
            userConfig.setSchema(schema);
            userConfig.setMaxLifetime(maxLifetime);
            userConfig.setMaximumPoolSize(maxPoolSize);
            userConfig.setAutoCommit(false);
            userConfig.setConnectionTestQuery("SELECT 1");
            userConfig.addDataSourceProperty("testOnBorrow", "true");
            userConfig.addDataSourceProperty("cachePrepStmts", "true");
            userConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            userConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            userConfig.addDataSourceProperty("socketTimeout", "30");

            // Get connection
            HikariDataSource userDatasource = new HikariDataSource(userConfig);
            userDatasources.put(user, userDatasource);

            return userDatasource.getConnection();
        } catch (SQLException ex) {
            logger.error("Error when creating user connection.", ex);
        }

        return null;
    }

    /**
     * Function to reset the schema for a specified connection and close it
     *
     * @param userConn   Specifies the connection
     * @param user       The user.
     * @param schemaName The schema name.
     */
    public static void resetUserConnection(Connection userConn, String user, String schemaName) {
        try {
            if (userConn == null || userConn.isClosed())
                return;

            //drop all tables from schema
            Connection conn = getSystemConnection();
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '" + schemaName + "'");
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                stmt2.executeUpdate("DROP TABLE IF EXISTS  " + schemaName + "." + tableName + " CASCADE");
            }
            conn.commit();
            stmt.close();
            stmt2.close();


//            //drop schema with admin connection
//            Connection adminConn = getSystemConnection();
//            Statement stmt = adminConn.createStatement();
//            stmt.executeUpdate("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE");
//            adminConn.commit();
//            //restore schema AUTHORIZATION USER
//            stmt.execute("CREATE SCHEMA IF NOT EXISTS AUTHORIZATION " + user);
//            adminConn.commit();
//            //close admin connection
//            stmt.close();




            // Reset database schema
            //userConn.rollback();
            userConn.close();

            // Close datasource
            if (userDatasources.get(user) != null) {
                userDatasources.get(user).close();
            }
        } catch (SQLException ex) {
            logger.error("Error while resetting user connection.", ex);
        }
    }

    /**
     * Clear exercise schema tables.
     *
     * @param schemaName The schema name.
     */
    public static void clearExerciseSchemaTables(String schemaName){

        try {
            Connection conn = getSystemConnection();
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '" + schemaName + "'");
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                stmt2.executeUpdate("TRUNCATE TABLE " + schemaName + "." + tableName + " CASCADE");
            }
            conn.commit();
            stmt.close();
            stmt2.close();
        } catch (SQLException ex) {
            logger.error("Error while clearing exercise schema tables.", ex);
        }

    }

    /**
     * Gets the logger.
     *
     * @return The logger.
     */
    public static Logger getLogger() {
        return logger;
    }
}
