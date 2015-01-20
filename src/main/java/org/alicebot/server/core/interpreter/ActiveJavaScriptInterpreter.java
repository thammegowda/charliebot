// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.interpreter;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Interpreter;
import org.alicebot.server.core.util.DeveloperError;

public abstract class ActiveJavaScriptInterpreter
        implements Interpreter {

    private static Interpreter interpreter;
    static {
        try {
            interpreter = (Interpreter) Class.forName(Globals.javaScriptInterpreter()).newInstance();
        } catch (Exception exception) {
            throw new DeveloperError(exception);
        }
    }

    private ActiveJavaScriptInterpreter() {
    }

    public static Interpreter getInstance() {
        return interpreter;
    }

    protected Object clone()
            throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
