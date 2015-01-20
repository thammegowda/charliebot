// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.util.UserError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

// Referenced classes of package org.alicebot.server.core.parser:
//            GenericReaderListener

public abstract class GenericReader {
    protected static final String MARKER_START = "<";
    protected static final String MARKER_END = ">";
    protected static final String COMMENT_MARK = "!--";
    protected static final String EMPTY_STRING = "";
    protected static final String SLASH = "/";
    protected static final String QUOTE_MARK = "\"";
    protected static final String ASTERISK = "*";
    protected static final String COLON = ":";
    protected static final String SPACE = " ";
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");
    protected static int bufferStartCapacity = 100;
    protected GenericReader readerInstance;
    protected GenericReaderListener listener;
    protected BufferedReader buffReader;
    protected String fileName;
    protected boolean done;
    protected boolean searching;
    protected int state;
    protected int tagStart;
    protected int tagLength;
    protected int searchStart;
    protected int lineNumber;
    protected StringBuffer buffer;
    protected String bufferString;
    protected TransitionMade TRANSITION_MADE;
    protected long byteCount;
    protected String encoding;
    private boolean countBytes;
    public GenericReader(String s, BufferedReader bufferedreader, String s1, boolean flag, GenericReaderListener genericreaderlistener) {
        done = false;
        searching = false;
        tagStart = 0;
        tagLength = 0;
        searchStart = 0;
        lineNumber = 0;
        buffer = new StringBuffer(bufferStartCapacity);
        bufferString = null;
        fileName = s;
        buffReader = bufferedreader;
        encoding = s1;
        countBytes = flag;
        listener = genericreaderlistener;
        TRANSITION_MADE = new TransitionMade();
        initialize();
    }
    public GenericReader(String s, BufferedReader bufferedreader, GenericReaderListener genericreaderlistener) {
        done = false;
        searching = false;
        tagStart = 0;
        tagLength = 0;
        searchStart = 0;
        lineNumber = 0;
        buffer = new StringBuffer(bufferStartCapacity);
        bufferString = null;
        fileName = s;
        buffReader = bufferedreader;
        countBytes = false;
        listener = genericreaderlistener;
        TRANSITION_MADE = new TransitionMade();
        initialize();
    }

    protected abstract void initialize();

    public void read() {
        Object obj = null;
        while (!done) {
            searching = true;
            while (searching) {
                bufferString = buffer.toString();
                tagStart = bufferString.indexOf("<", searchStart);
                if (tagStart < 0)
                    try {
                        StringBuffer stringbuffer = new StringBuffer(buffReader.readLine());
                        if (countBytes)
                            try {
                                byteCount += stringbuffer.toString().getBytes(encoding).length;
                            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                                throw new UserError("Encoding \"" + encoding + "\" is not supported by your platform!");
                            }
                        lineNumber++;
                        buffer.append(stringbuffer.toString() + LINE_SEPARATOR);
                    } catch (IOException ioexception) {
                        Trace.userinfo("\"" + fileName + "\" could not be read.");
                        return;
                    } catch (NullPointerException nullpointerexception) {
                        searching = false;
                    }
                else
                    searching = false;
            }
            if (tagStart < 0) {
                done = true;
                continue;
            }
            try {
                tryStates();
            } catch (TransitionMade transitionmade) {
                continue;
            }
            searchStart = tagStart + 1;
        }
    }

    protected abstract void tryStates()
            throws TransitionMade;

    protected boolean succeed(String s, int i) {
        tagLength = s.length();
        if (bufferString.regionMatches(tagStart, s, 0, tagLength)) {
            state = i;
            searchStart = 0;
            buffer.delete(0, tagStart + tagLength);
            return true;
        } else {
            return false;
        }
    }

    protected void transition(String s, int i)
            throws TransitionMade {
        if (succeed(s, i))
            throw TRANSITION_MADE;
        else
            return;
    }

    protected void transition(String s, int i, Field field)
            throws TransitionMade {
        if (succeed(s, i)) {
            try {
                field.set(readerInstance, Toolkit.filterWhitespace(bufferString.substring(0, tagStart)));
            } catch (Exception exception) {
                throw new DeveloperError(exception);
            }
            throw TRANSITION_MADE;
        } else {
            return;
        }
    }

    protected void transition(String s, int i, Field field, String s1)
            throws TransitionMade {
        if (succeed(s, i)) {
            int j = bufferString.substring(tagStart).indexOf(">");
            if (j == -1) {
                Log.userinfo(s + " is missing closing \"" + ">" + "\" at " + lineNumber + " in \"" + fileName + "\".", Log.ERROR);
                Log.userinfo("Will not process this element.", Log.ERROR);
            } else {
                String s2 = Toolkit.getAttributeValue(s1, bufferString.substring(tagStart, tagStart + j));
                if (s2.length() > 0) {
                    try {
                        field.set(readerInstance, s2);
                    } catch (Exception exception) {
                        throw new DeveloperError(exception);
                    }
                    throw TRANSITION_MADE;
                }
            }
        }
    }

    public class TransitionMade extends Throwable {

        public TransitionMade() {
        }
    }

}
