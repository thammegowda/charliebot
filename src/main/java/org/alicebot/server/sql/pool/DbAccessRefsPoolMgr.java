// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.sql.pool;


// Referenced classes of package org.alicebot.server.sql.pool:
//            ObjectPool, DbAccess

public class DbAccessRefsPoolMgr extends ObjectPool {

    protected String driver;
    protected String url;
    protected String user;
    protected String password;

    public DbAccessRefsPoolMgr(String s, String s1, String s2, String s3) {
        driver = s;
        url = s1;
        user = s2;
        password = s3;
    }

    protected Object create() {
        return new DbAccess(driver, url, user, password);
    }

    public void populate(int i) {
        for (int j = i; --j >= 0; )
            super.checkIn(create());

    }

    public void returnDbaRef(DbAccess dbaccess) {
        super.checkIn(dbaccess);
    }

    public DbAccess takeDbaRef() {
        return (DbAccess) super.checkOut();
    }

    protected boolean validate(Object obj) {
        return true;
    }

    protected void expire(Object obj) {
    }
}
