package aandrosov.cbs.database;

import java.io.IOException;

import java.util.NoSuchElementException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.Properties;

public class Database {

    private Database() {
    }

    public static Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();

        try (var in = Database.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new NullPointerException("You must create resource file db.properties");
            }

            properties.load(in);
        }

        String name = properties.getProperty("db.name", "").trim();
        String dbname = properties.getProperty("db.dbname", "").trim();
        String host = properties.getProperty("db.host", "").trim();
        String user = properties.getProperty("db.user", "").trim();
        String pass = properties.getProperty("db.pass", "").trim();

        if (name.isEmpty()) {
            throw new NoSuchElementException("Cannot find \"db.name\" property in \"db.properties\" file");
        }

        if (dbname.isEmpty()) {
            throw new NoSuchElementException("Cannot find \"db.dbname\" property in \"db.properties\" file");
        }

        if (host.isEmpty()) {
            throw new NoSuchElementException("Cannot find \"db.host\" property in \"db.properties\" file");
        }

        if (user.isEmpty()) {
            throw new NoSuchElementException("Cannot find \"db.user\" property in \"db.properties\" file");
        }

        if (pass.isEmpty()) {
            throw new NoSuchElementException("Cannot find \"db.pass\" property in \"db.properties\" file");
        }

        var url = String.format("jdbc:%s://%s/%s?user=%s&password=%s", name, host, dbname, user, pass);
        return DriverManager.getConnection(url);
    } 
}