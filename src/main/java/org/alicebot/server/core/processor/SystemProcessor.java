// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

import java.io.*;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.processor:
//            AIMLProcessor, AIMLProcessorException

public class SystemProcessor extends AIMLProcessor {

    public static final String label = "system";
    private static final String arrayFormOSnames[] = {
            "mac os x", "linux", "solaris", "sunos", "mpe", "hp-ux", "pa_risc", "aix", "freebsd", "irix",
            "unix"
    };
    private static boolean useArrayExecForm;
    static {
        String s = System.getProperty("os.name").toLowerCase();
        for (int i = arrayFormOSnames.length; --i >= 0; )
            if (s.indexOf(arrayFormOSnames[i]) != -1)
                useArrayExecForm = true;

    }
    public SystemProcessor() {
    }

    public String process(int i, XMLNode xmlnode, TemplateParser templateparser)
            throws AIMLProcessorException {
        if (!Globals.osAccessAllowed()) {
            Log.userinfo("Use of <system> prohibited!", Log.SYSTEM);
            return "";
        }
        String s = Globals.getSystemDirectory();
        String s1 = Globals.getSystemPrefix();
        if (xmlnode.XMLType == 0) {
            String s2 = templateparser.evaluate(i++, xmlnode.XMLChild);
            if (s1 != null)
                s2 = s1 + s2;
            String s3 = "";
            s2 = s2.trim();
            Log.log("<system> call:", Log.SYSTEM);
            Log.log(s2, Log.SYSTEM);
            try {
                File file = null;
                if (s != null) {
                    Log.log("Executing <system> call in \"" + s + "\"", Log.SYSTEM);
                    file = new File(s);
                    if (!file.isDirectory()) {
                        Log.userinfo("programd.interpreter.system.directory (\"" + s + "\") does not exist or is not a directory.", Log.SYSTEM);
                        return "";
                    }
                } else {
                    Log.userinfo("No programd.interpreter.system.directory defined!", Log.SYSTEM);
                    return "";
                }
                Process process1;
                if (useArrayExecForm)
                    process1 = Runtime.getRuntime().exec((String[]) Toolkit.wordSplit(s2).toArray(new String[0]), null, file);
                else
                    process1 = Runtime.getRuntime().exec(s2, null, file);
                if (process1 == null) {
                    Log.userinfo("Could not get separate process for <system> command.", Log.SYSTEM);
                    return "";
                }
                try {
                    process1.waitFor();
                } catch (InterruptedException interruptedexception) {
                    Log.userinfo("System process interruped; could not complete.", Log.SYSTEM);
                    return "";
                }
                InputStream inputstream = process1.getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
                String s4;
                while ((s4 = bufferedreader.readLine()) != null)
                    s3 = s3 + s4 + "\n";
                Log.log("output:", Log.SYSTEM);
                Log.log(s3, Log.SYSTEM);
                s2 = s3;
                inputstream.close();
                Log.userinfo("System process exit value: " + process1.exitValue(), Log.SYSTEM);
            } catch (IOException ioexception) {
                Log.userinfo("Cannot execute <system> command.  Response logged.", Log.SYSTEM);
                for (StringTokenizer stringtokenizer = new StringTokenizer(ioexception.getMessage(), System.getProperty("line.separator")); stringtokenizer.hasMoreTokens(); Log.log(stringtokenizer.nextToken(), Log.SYSTEM))
                    ;
            }
            return s2.trim();
        } else {
            throw new AIMLProcessorException("<system></system> must have content!");
        }
    }
}
