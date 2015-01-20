// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.lang.reflect.Field;
import java.util.Hashtable;

// Referenced classes of package org.alicebot.server.core.util:
//            UserError, DeveloperError

public abstract class ClassRegistry extends Hashtable {

    private static final String LABEL = "label";
    protected static String version;
    protected static String classesToRegister[];
    protected static String baseClassName;
    public ClassRegistry(String s, String as[], String s1) {
        super(as.length);
        version = s;
        classesToRegister = as;
        baseClassName = s1;
        Class class1 = null;
        try {
            class1 = Class.forName(s1);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new UserError("Could not find base class \"" + s1 + "\"!", classnotfoundexception);
        }
        for (int i = as.length; --i >= 0; ) {
            Class class2;
            try {
                class2 = Class.forName(as[i]);
            } catch (ClassNotFoundException classnotfoundexception1) {
                throw new UserError("\"" + as[i] + "\" is missing from your classpath.  Cannot initialize registry.", classnotfoundexception1);
            }
            if (!class1.isAssignableFrom(class2))
                throw new DeveloperError("Developer has incorrectly specified \"" + as[i] + "\" as a registrable class.");
            Field field = null;
            if (class2 != null)
                try {
                    field = class2.getDeclaredField("label");
                } catch (NoSuchFieldException nosuchfieldexception) {
                    throw new DeveloperError("Unlikely error: \"" + as[i] + "\" is missing label field!");
                }
            else
                throw new DeveloperError("Failed to get processor \"" + as[i] + "\"");
            String s2 = null;
            try {
                s2 = (String) field.get(null);
            } catch (IllegalAccessException illegalaccessexception) {
                throw new DeveloperError("Label field for \"" + as[i] + "\" is not accessible!");
            }
            if (s2 != null)
                put(s2, class2);
            else
                throw new DeveloperError("Tried to register class with null label!");
        }

    }
}
