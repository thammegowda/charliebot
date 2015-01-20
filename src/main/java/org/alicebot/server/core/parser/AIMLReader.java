// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.processor.ProcessorException;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.NotAnAIMLPatternException;
import org.alicebot.server.core.util.PatternArbiter;

import java.io.BufferedReader;
import java.lang.reflect.Field;

// Referenced classes of package org.alicebot.server.core.parser:
//            GenericReader, AIMLReaderListener, StartupFileParser

public class AIMLReader extends GenericReader {

    private static final String PROPERTY_OPEN = "<property";
    private static final String TEMPLATE_START = "<template>";
    private static final String TEMPLATE_END = "</template>";
    private static final String PATTERN_START = "<pattern>";
    private static final String PATTERN_END = "</pattern>";
    private static final String CATEGORY_START = "<category>";
    private static final String CATEGORY_END = "</category>";
    private static final String THAT_START = "<that>";
    private static final String THAT_END = "</that>";
    private static final String TOPIC_START = "<topic name=\"";
    private static final String TOPIC_END = "</topic>";
    private static final String AIML_START = "<aiml>";
    private static final String AIML_VERSION_START = "<aiml version=\"1.0\">";
    private static final String AIML_END = "</aiml>";
    private static final String STARTUP_START = "<programd-startup>";
    private static final String STARTUP_END = "</programd-startup>";
    private static final String LOCALHOST = "localhost";
    private static final String NAME = "name";
    private static final String PATTERN = "pattern";
    private static final String THAT = "that";
    private static final String TOPIC = "topic";
    private static final String TEMPLATE = "template";
    private static final String UNEXPECTED_OUTSIDE_TEMPLATE[] = {
            "</template>", "</pattern>", "<category>", "<template>", "<pattern>", "</that>", "<that>", "</topic>", "<topic name=\""
    };
    private static final String UNEXPECTED_GENERAL[] = {
            "</aiml>", "<aiml>", "<aiml version=\"1.0\">"
    };
    private static final String STARTUP_AND_ERROR[];
    private final int S_NONE = 1;
    private final int S_IN_AIML = 2;
    private final int S_IN_TOPIC = 3;
    private final int S_IN_CATEGORY = 4;
    private final int S_IN_PATTERN = 5;
    private final int S_OUT_PATTERN = 6;
    private final int S_IN_THAT = 7;
    private final int S_OUT_THAT = 8;
    private final int S_IN_TEMPLATE = 9;
    private final int S_OUT_TEMPLATE = 10;
    private final int S_OUT_CATEGORY = 11;
    private final int S_OUT_TOPIC = 12;
    private final int S_OUT_AIML = 13;
    private final int S_IN_STARTUP = 14;
    private final int S_OUT_STARTUP = 15;
    private final int DELIVER_CATEGORY = 0;
    private final int SET_DONE = 1;
    private final int ABORT = 2;
    private final int UNSET_TOPIC = 3;
    private final int PROCESS_STARTUP = 4;
    protected String pattern;
    protected Field patternField;
    protected String that;
    protected Field thatField;
    protected String topic;
    protected Field topicField;
    protected String template;
    protected Field templateField;
    static {
        STARTUP_AND_ERROR = (new String[]{
                Log.STARTUP, Log.ERROR
        });
    }
    private boolean warnNonAIML;
    private int categoryCount;
    public AIMLReader(String s, BufferedReader bufferedreader, AIMLReaderListener aimlreaderlistener, boolean flag) {
        super(s, bufferedreader, aimlreaderlistener);
        categoryCount = 0;
        pattern = "*";
        that = "*";
        topic = "*";
        template = "*";
        super.readerInstance = this;
        warnNonAIML = flag;
        state = 1;
    }

    protected void initialize() {
        try {
            patternField = getClass().getDeclaredField("pattern");
            thatField = getClass().getDeclaredField("that");
            topicField = getClass().getDeclaredField("topic");
            templateField = getClass().getDeclaredField("template");
        } catch (NoSuchFieldException nosuchfieldexception) {
            throw new DeveloperError("The developer has specified a field that does not exist in AIMLReader.");
        } catch (SecurityException securityexception) {
            throw new DeveloperError("Security manager prevents AIMLReader from functioning.");
        }
    }

    protected void tryStates()
            throws GenericReader.TransitionMade {
        switch (state) {
            case 1: // '\001'
                transition("<aiml version=\"1.0\">", 2);
                transition("<aiml>", 2);
                transition("<programd-startup>", 14);
                break;

            case 2: // '\002'
                transition("<category>", 4);
                transition("<topic name=\"", 3, topicField, "name");
                break;

            case 3: // '\003'
                transition("<category>", 4);
                break;

            case 4: // '\004'
                transition("<pattern>", 5);
                // fall through

            case 5: // '\005'
                transition("</pattern>", 6, patternField);
                break;

            case 6: // '\006'
                transition("<template>", 9);
                transition("<that>", 7);
                break;

            case 9: // '\t'
                transition("</template>", 10, templateField);
                break;

            case 7: // '\007'
                transition("</that>", 8, thatField);
                break;

            case 8: // '\b'
                transition("<template>", 9);
                break;

            case 10: // '\n'
                transition("</category>", 11, 0);
                break;

            case 11: // '\013'
                transition("<category>", 4);
                transition("</topic>", 12, 3);
                transition("<topic name=\"", 3, topicField, "name");
                transition("</aiml>", 1, 1);
                break;

            case 12: // '\f'
                transition("<category>", 4);
                transition("<topic name=\"", 3, topicField, "name");
                transition("</aiml>", 13, 1);
                break;

            case 14: // '\016'
                transition("</programd-startup>", 15, 4);
                break;
        }
        alertUnexpected();
    }

    private void transition(String s, int i, int j)
            throws GenericReader.TransitionMade {
        if (succeed(s, i)) {
            switch (j) {
                case 2: // '\002'
                default:
                    break;

                case 0: // '\0'
                    if (pattern.length() == 0) {
                        abortCategory("Pattern missing from category.");
                        break;
                    }
                    if (template.length() == 0) {
                        abortCategory("Template missing from category.");
                        break;
                    }
                    try {
                        PatternArbiter.checkAIMLPattern(pattern, false);
                        PatternArbiter.checkAIMLPattern(that, false);
                        PatternArbiter.checkAIMLPattern(topic, false);
                        ((AIMLReaderListener) super.listener).newCategory(pattern, that, topic, template);
                    } catch (NotAnAIMLPatternException notanaimlpatternexception) {
                        abortCategory(notanaimlpatternexception.getMessage());
                    }
                    pattern = template = "";
                    that = "*";
                    searchStart = 0;
                    categoryCount++;
                    buffer = new StringBuffer(Math.max(GenericReader.bufferStartCapacity, buffer.length()));
                    buffer.append(bufferString);
                    break;

                case 1: // '\001'
                    done = true;
                    break;

                case 3: // '\003'
                    topic = "*";
                    break;

                case 4: // '\004'
                    try {
                        (new StartupFileParser()).processResponse(bufferString.substring(0, tagStart));
                    } catch (ProcessorException processorexception) {
                    }
                    break;
            }
            throw super.TRANSITION_MADE;
        } else {
            return;
        }
    }

    private void alertUnexpected() {
        if (state != 9) {
            for (int i = UNEXPECTED_OUTSIDE_TEMPLATE.length; --i >= 0; ) {
                String s = UNEXPECTED_OUTSIDE_TEMPLATE[i];
                int k = s.length();
                if (bufferString.regionMatches(tagStart, s, 0, k)) {
                    Log.userinfo(new String[]{
                            "Unexpected " + s + "; aborting category.", "  (Line " + lineNumber + ", \"" + fileName + "\")"
                    }, Log.ERROR);
                    return;
                }
            }

            for (int j = UNEXPECTED_GENERAL.length; --j >= 0; ) {
                String s1 = UNEXPECTED_GENERAL[j];
                int i1 = s1.length();
                if (bufferString.regionMatches(tagStart, s1, 0, i1)) {
                    if (s1 == "</aiml>" && categoryCount == 0)
                        Log.userinfo("aiml element does not contain any AIML content in \"" + fileName + "\".", Log.ERROR);
                    else
                        Log.userinfo(new String[]{
                                "Unexpected " + s1 + "; rest of file ignored.", "  (Line " + lineNumber + ", \"" + fileName + "\")"
                        }, Log.ERROR);
                    done = true;
                    return;
                }
            }

            if (warnNonAIML && state == 2) {
                int l = bufferString.indexOf(" ", tagStart);
                if (l > -1) {
                    String s2 = bufferString.substring(tagStart + 1, l);
                    if (s2.indexOf(":") == -1 && !s2.equals("!--"))
                        Log.userinfo(new String[]{
                                "There is no \"" + s2 + "\" element in AIML.", "  (Line " + lineNumber + ", \"" + fileName + "\")"
                        }, Log.ERROR);
                }
            }
        }
    }

    private void abortCategory(String s) {
        Log.userinfo(new String[]{
                "Aborting category:", s, "  (Category ends line " + lineNumber + ", \"" + fileName + "\")."
        }, STARTUP_AND_ERROR);
    }
}
