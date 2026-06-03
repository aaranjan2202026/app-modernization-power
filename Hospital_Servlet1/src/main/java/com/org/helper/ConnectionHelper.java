package com.org.helper;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Component;

@Component
@DependsOnDatabaseInitialization
public class ConnectionHelper {

    private static DataSource staticDataSource;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void setStaticDataSource(DataSource dataSource) {
        ConnectionHelper.staticDataSource = dataSource;
    }

    /**
     * Get database connection using Spring Boot's auto-configured DataSource
     * This method uses configuration from application.properties
     */
    public Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            System.out.println("Connected to SQL Server via Spring DataSource successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    /**
     * Static method for backward compatibility with existing DAO classes
     * Uses Spring's auto-configured DataSource from application.properties
     */
    public static Connection getConObj() {
        try {
            if (staticDataSource != null) {
                Connection connection = staticDataSource.getConnection();
                System.out.println("Connected to SQL Server via Spring DataSource (static method) successfully!");
                return connection;
            } else {
                throw new IllegalStateException("DataSource not initialized. Make sure Spring context is loaded.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get database connection", e);
        }
    }
}