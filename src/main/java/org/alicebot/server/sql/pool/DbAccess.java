// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.sql.pool;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.UserError;

import java.sql.*;

public class DbAccess {

    protected Connection connection;
    protected Statement statement;
    protected String driver;
    protected String url;
    protected String user;
    protected String password;

    public DbAccess(String s, String s1, String s2, String s3) {
        driver = s;
        url = s1;
        user = s2;
        password = s3;
        connect();
    }

    public DbAccess(Connection connection1) {
        connection = connection1;
    }

    public void connect() {
        if (connection == null) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new UserError("Could not find your database driver.");
            }
            try {
                if (user == null || password == null)
                    connection = DriverManager.getConnection(url);
                else
                    connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException sqlexception) {
                throw new UserError("Could not connect to \"" + url + "\".  Please check that the parameters specified in your server properties file are correct.", sqlexception);
            }
            try {
                statement = connection.createStatement();
            } catch (SQLException sqlexception1) {
                throw new UserError("Could not create a SQL statement using your database.");
            }
        }
    }

    public ResultSet executeQuery(String s)
            throws SQLException {
        if (statement == null)
            throw new DeveloperError("Tried to execute query before creating Statement object!");
        try {
            return statement.executeQuery(s);
        } catch (SQLException sqlexception) {
            Log.userinfo("Problem executing a query on your database.  Check structure and availability.", new String[]{
                    Log.ERROR, Log.DATABASE
            });
            throw sqlexception;
        }
    }

    public int executeUpdate(String s) {
        if (statement == null)
            throw new DeveloperError("Tried to execute query before creating Statement object!");
        try {
            return statement.executeUpdate(s);
        } catch (SQLException sqlexception) {
            throw new UserError("Problem executing an update on your database.  Check structure and availability.", sqlexception);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection1) {
        connection = connection1;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String s) {
        driver = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        password = s;
    }

    public Statement getStatement() {
        return statement;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String s) {
        url = s;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String s) {
        user = s;
    }
}
