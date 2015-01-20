// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.UserError;
import org.alicebot.server.sql.pool.DbAccess;
import org.alicebot.server.sql.pool.DbAccessRefsPoolMgr;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

// Referenced classes of package org.alicebot.server.core:
//            Multiplexor, NoSuchPredicateException, Globals

public class DBMultiplexor extends Multiplexor {

    private static final String ENC_UTF8 = "UTF-8";
    private static DbAccessRefsPoolMgr dbManager;
    private static HashMap userCacheForBots = new HashMap();

    public DBMultiplexor() {
    }

    public void initialize() {
        super.initialize();
        Log.devinfo("Opening database pool.", new String[]{
                Log.DATABASE, Log.STARTUP
        });
        dbManager = new DbAccessRefsPoolMgr(Globals.getProperty("programd.database.driver", ""), Globals.getProperty("programd.database.url", ""), Globals.getProperty("programd.database.user", ""), Globals.getProperty("programd.database.password", ""));
        Log.devinfo("Populating database pool.", new String[]{
                Log.DATABASE, Log.STARTUP
        });
        dbManager.populate(Integer.parseInt(Globals.getProperty("programd.database.connections", "")));
    }

    public void savePredicate(String s, String s1, String s2, String s3) {
        String s4;
        try {
//            s4 = URLEncoder.encode(s1.trim(), "UTF-8");
            s4 = URLEncoder.encode(s1.trim());
        }
//        catch(UnsupportedEncodingException unsupportedencodingexception)
        catch (Exception unsupportedencodingexception) {
            throw new DeveloperError("This platform does not support UTF-8!");
        }
        DbAccess dbaccess = null;
        try {
            dbaccess = dbManager.takeDbaRef();
        } catch (Exception exception) {
            throw new UserError("Could not get database reference when setting predicate name \"" + s + "\" to value \"" + s1 + "\" for \"" + s2 + "\" as known to \"" + s3 + "\".", exception);
        }
        try {
            ResultSet resultset = dbaccess.executeQuery("select value from predicates where botid = '" + s3 + "' and userid = '" + s2 + "' and name = '" + s + "'");
            int i;
            for (i = 0; resultset.next(); i++) ;
            if (i > 0)
                dbaccess.executeUpdate("update predicates set value = '" + s4 + "' where botid = '" + s3 + "' and userid= '" + s2 + "' and name = '" + s + "'");
            else
                dbaccess.executeUpdate("insert into predicates (userid, botid, name, value) values ('" + s2 + "', '" + s3 + "' , '" + s + "','" + s4 + "')");
            resultset.close();
            dbManager.returnDbaRef(dbaccess);
        } catch (SQLException sqlexception) {
            Log.userinfo("Database error: " + sqlexception, new String[]{
                    Log.DATABASE, Log.ERROR
            });
        }
    }

    public String loadPredicate(String s, String s1, String s2)
            throws NoSuchPredicateException {
        String s3 = null;
        DbAccess dbaccess = null;
        try {
            dbaccess = dbManager.takeDbaRef();
        } catch (Exception exception) {
            throw new UserError("Could not get database reference when getting value for predicate name \"" + s + "\" for \"" + s1 + "\" as known to \"" + s2 + "\".", exception);
        }
        try {
            ResultSet resultset = dbaccess.executeQuery("select value from predicates where botid = '" + s2 + "' and userid = '" + s1 + "' and name = '" + s + "'");
            int i = 0;
            while (resultset.next()) {
                i++;
                s3 = resultset.getString("value");
            }
            resultset.close();
            dbManager.returnDbaRef(dbaccess);
        } catch (SQLException sqlexception) {
            Log.log("Database error: " + sqlexception, Log.ERROR);
            throw new NoSuchPredicateException(s);
        }
        if (s3 == null)
            throw new NoSuchPredicateException(s);
        try {
//            return URLDecoder.decode(s3, "UTF-8");
            return URLDecoder.decode(s3);
        }
//        catch(UnsupportedEncodingException unsupportedencodingexception)
        catch (Exception unsupportedencodingexception) {
            throw new DeveloperError("This platform does not support UTF-8!");
        }
    }

    public boolean createUser(String s, String s1, String s2, String s3) {
        if (!s2.equals(Multiplexor.SECRET_KEY)) {
            Log.userinfo("ACCESS VIOLATION: Tried to create a user with invalid secret key.", Log.ERROR);
            return false;
        }
        s = s.trim().toLowerCase();
        s1 = s1.trim().toLowerCase();
        DbAccess dbaccess = null;
        try {
            dbaccess = dbManager.takeDbaRef();
        } catch (Exception exception) {
            throw new UserError("Could not get database reference when creating user \"" + s + "\" with password \"" + s1 + "\" and secret key \"" + s2 + "\".", exception);
        }
        try {
            ResultSet resultset = dbaccess.executeQuery("select * from users where userid = '" + s + "' and botid = '" + s3 + "'");
            int i = 0;
            while (resultset.next())
                if (++i == 1) {
                    resultset.close();
                    dbManager.returnDbaRef(dbaccess);
                    return false;
                }
            dbaccess.executeUpdate("insert into users (userid, password, botid) values ('" + s + "' , '" + s1 + "' , '" + s3 + "')");
            resultset.close();
        } catch (SQLException sqlexception) {
            throw new UserError("Error working with database.", sqlexception);
        }
        dbManager.returnDbaRef(dbaccess);
        return true;
    }

    public boolean checkUser(String s, String s1, String s2, String s3) {
        if (!s2.equals(Multiplexor.SECRET_KEY)) {
            Log.userinfo("ACCESS VIOLATION: Tried to create a user with invalid secret key.", Log.ERROR);
            return false;
        }
        if (!userCacheForBots.containsKey(s3))
            userCacheForBots.put(s3, new HashMap());
        HashMap hashmap = (HashMap) userCacheForBots.get(s3);
        if (hashmap.containsKey(s))
            return ((String) hashmap.get(s)).equals(s1);
        if (checkUserInDB(s, s1, s3)) {
            hashmap.put(s, s1);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUserInDB(String s, String s1, String s2) {
        String s3 = null;
        s = s.trim().toLowerCase();
        s1 = s1.trim().toLowerCase();
        DbAccess dbaccess = null;
        try {
            dbaccess = dbManager.takeDbaRef();
        } catch (Exception exception) {
            throw new UserError("Could not get database reference when checking user \"" + s + "\" with password \"" + s1 + "\".", exception);
        }
        try {
            ResultSet resultset = dbaccess.executeQuery("select * from users where userid = '" + s + "' and botid = '" + s2 + "'");
            int i = 0;
            while (resultset.next()) {
                if (++i == 1)
                    s3 = resultset.getString("password");
                if (i == 0) {
                    resultset.close();
                    dbManager.returnDbaRef(dbaccess);
                    return false;
                }
                if (i > 1)
                    throw new UserError("Duplicate user name: \"" + s + "\"");
            }
            resultset.close();
            dbManager.returnDbaRef(dbaccess);
        } catch (SQLException sqlexception) {
            throw new UserError("Database error.", sqlexception);
        }
        if (s3 == null)
            return false;
        return s1.equals(s3);
    }

    public boolean changePassword(String s, String s1, String s2, String s3) {
        if (!s2.equals(Multiplexor.SECRET_KEY)) {
            Log.userinfo("ACCESS VIOLATION: Tried to create a user with invalid secret key.", Log.ERROR);
            return false;
        }
        s = s.trim().toLowerCase();
        s1 = s1.trim().toLowerCase();
        DbAccess dbaccess = null;
        try {
            dbaccess = dbManager.takeDbaRef();
        } catch (Exception exception) {
            throw new UserError("Could not get database reference when changing password to \"" + s1 + "\" for \"" + s + "\" as known to \"" + s3 + "\".", exception);
        }
        try {
            ResultSet resultset = dbaccess.executeQuery("select * from users where userid = '" + s + "' and botid = '" + s3 + "'");
            int i;
            for (i = 0; resultset.next(); i++) ;
            if (i == 0) {
                resultset.close();
                dbManager.returnDbaRef(dbaccess);
                return false;
            }
            dbaccess.executeUpdate("update users set password = '" + s1 + "' where userid = '" + s + "' and botid = '" + s3 + "'");
            resultset.close();
            dbManager.returnDbaRef(dbaccess);
        } catch (SQLException sqlexception) {
            throw new UserError("Database error.", sqlexception);
        }
        userCacheForBots.remove(s);
        userCacheForBots.put(s, s1);
        return true;
    }

    public int useridCount(String s) {
        return ((HashMap) userCacheForBots.get(s)).size();
    }

}
