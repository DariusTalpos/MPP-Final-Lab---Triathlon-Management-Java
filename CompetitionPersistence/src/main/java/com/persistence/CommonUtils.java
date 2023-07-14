package com.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonUtils {
    private Properties databaseProperties;
    private static final Logger logger = LogManager.getLogger();

    public CommonUtils(Properties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    private Connection instance = null;

    private Connection getNewConnection(){

        logger.traceEntry();
        String url = databaseProperties.getProperty("competition.jdbc.url");
        String username = databaseProperties.getProperty("competition.jdbc.username");
        String password = databaseProperties.getProperty("competition.jdbc.password");
        logger.info("trying to connect to database: {}", url);
        logger.info("username: {}", username);
        logger.info("password: {}", password);
        Connection connection;
        try
        {
            connection = DriverManager.getConnection(url,username,password);
            return connection;
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error getting connection "+e);
            return null;
        }
    }

    public Connection getConnection()
    {
        logger.traceEntry();
        try
        {
            if(instance==null || instance.isClosed())
                instance=getNewConnection();
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
