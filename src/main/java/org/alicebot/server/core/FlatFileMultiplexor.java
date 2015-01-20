// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

// Referenced classes of package org.alicebot.server.core:
//            Multiplexor, NoSuchPredicateException

public class FlatFileMultiplexor extends Multiplexor {

    private static final String FFM_DIR_NAME = "ffm";
    private static final String PREDICATES_SUFFIX = ".predicates";
    private static final String FFM_FILE_LABEL = "FlatFileMultiplexor predicates file";
    private static Hashtable predicateSets;

    public FlatFileMultiplexor() {
    }

    private static Properties loadPredicates(String s, String s1) {
        Properties properties = new Properties();
        String s2 = "ffm" + File.separator + s1 + File.separator + s + ".predicates";
        Toolkit.checkOrCreate(s2, "FlatFileMultiplexor predicates file");
        File file = new File(s2);
        if (file.canRead())
            try {
                properties.load(new FileInputStream(file));
            } catch (IOException ioexception) {
                throw new DeveloperError("Error trying to load predicates.", ioexception);
            }
        return properties;
    }

    private static void savePredicates(Properties properties, String s, String s1) {
        String s2 = "ffm" + File.separator + s1 + File.separator + s + ".predicates";
        Toolkit.checkOrCreate(s2, "FlatFileMultiplexor predicates file");
        try {
            properties.store(new FileOutputStream(s2), null);
        } catch (IOException ioexception) {
            System.err.println(System.getProperty("user.dir"));
            System.err.println(ioexception.getMessage());
            throw new DeveloperError("Error trying to save predicates.", ioexception);
        }
    }

    public boolean checkUser(String s, String s1, String s2, String s3) {
        return true;
    }

    public boolean createUser(String s, String s1, String s2, String s3) {
        return true;
    }

    public boolean changePassword(String s, String s1, String s2, String s3) {
        return true;
    }

    public void savePredicate(String s, String s1, String s2, String s3) {
        if (predicateSets == null)
            predicateSets = new Hashtable();
        Properties properties = loadPredicates(s2, s3);
        properties.setProperty(s, s1);
        savePredicates(properties, s2, s3);
    }

    public String loadPredicate(String s, String s1, String s2)
            throws NoSuchPredicateException {
        if (predicateSets == null)
            predicateSets = new Hashtable();
        Properties properties = loadPredicates(s1, s2);
        String s3 = properties.getProperty(s);
        if (s3 == null)
            throw new NoSuchPredicateException(s);
        else
            return s3;
    }

    public int useridCount(String s) {
        return 0;
    }
}
