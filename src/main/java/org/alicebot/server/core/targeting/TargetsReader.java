// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.parser.GenericReader;
import org.alicebot.server.core.util.DeveloperError;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.lang.reflect.Field;

// Referenced classes of package org.alicebot.server.core.targeting:
//            TargetsReaderListener

public class TargetsReader extends GenericReader
        implements Runnable {

    private static final String INPUT_TEXT = "inputText";
    private static final String INPUT_THAT = "inputThat";
    private static final String INPUT_TOPIC = "inputTopic";
    private static final String MATCH_PATTERN = "matchPattern";
    private static final String MATCH_THAT = "matchThat";
    private static final String MATCH_TOPIC = "matchTopic";
    private static final String MATCH_TEMPLATE = "matchTemplate";
    private static final String REPLY = "reply";
    private final int S_NONE = 1;
    private final int S_IN_TARGETS = 2;
    private final int S_OUT_TARGETS = 3;
    private final int S_IN_TARGET = 4;
    private final int S_OUT_TARGET = 5;
    private final int S_IN_INPUT = 6;
    private final int S_OUT_INPUT = 7;
    private final int S_IN_TEXT = 8;
    private final int S_OUT_TEXT = 9;
    private final int S_IN_MATCH = 10;
    private final int S_OUT_MATCH = 11;
    private final int S_IN_PATTERN = 12;
    private final int S_OUT_PATTERN = 13;
    private final int S_IN_THAT = 14;
    private final int S_OUT_THAT = 15;
    private final int S_IN_TOPIC = 16;
    private final int S_OUT_TOPIC = 17;
    private final int S_IN_TEMPLATE = 18;
    private final int S_OUT_TEMPLATE = 19;
    private final int S_IN_REPLY = 20;
    private final int S_OUT_REPLY = 21;
    private final int SET_INPUT_CONTEXT = 0;
    private final int SET_MATCH_CONTEXT = 1;
    private final int SET_REPLY_CONTEXT = 2;
    private final int DELIVER_TARGET = 3;
    private final int SET_DONE = 4;
    private final int ABORT = 5;
    public String matchPattern;
    public String matchThat;
    public String matchTopic;
    public String matchTemplate;
    public String inputText;
    public String inputThat;
    public String inputTopic;
    public String reply;
    public Field patternField;
    public Field thatField;
    public Field topicField;
    public Field templateField;
    private ProgressMonitor monitor;
    private double progress;
    private double progressScaleFactor;
    public TargetsReader(String s, BufferedReader bufferedreader, TargetsReaderListener targetsreaderlistener, String s1, long l, Component component) {
        super(s, bufferedreader, s1, true, targetsreaderlistener);
        matchPattern = "*";
        matchThat = "*";
        matchTopic = "*";
        matchTemplate = "";
        inputText = "";
        inputThat = "";
        inputTopic = "";
        reply = "";
        progress = 0.0D;
        super.readerInstance = this;
        state = 1;
        monitor = new ProgressMonitor(component, "Reading targets from \"" + s + "\"", null, 0, 100);
        monitor.setProgress(0);
        monitor.setMillisToPopup(0);
        progressScaleFactor = 100D / (double) l;
    }

    public void run() {
        super.read();
    }

    protected void initialize() {
        try {
            patternField = getClass().getDeclaredField("inputText");
            thatField = getClass().getDeclaredField("inputThat");
            topicField = getClass().getDeclaredField("inputTopic");
            templateField = getClass().getDeclaredField("reply");
        } catch (NoSuchFieldException nosuchfieldexception) {
            throw new DeveloperError("The developer has specified a field that does not exist in TargetsReader.");
        } catch (SecurityException securityexception) {
            throw new DeveloperError("Security manager prevents TargetsReader from functioning.");
        }
    }

    protected void tryStates()
            throws TransitionMade {
        if (monitor != null) {
            if (monitor.isCanceled()) {
                monitor.close();
                done = true;
                return;
            }
            monitor.setProgress((int) ((double) byteCount * progressScaleFactor));
        }
        switch (state) {
            case 1: // '\001'
                transition("<targets>", 2);
                break;

            case 2: // '\002'
                transition("<target>", 4);
                break;

            case 4: // '\004'
                transition("<input>", 6, 0);
                transition("<match>", 10, 1);
                transition("<reply>", 20, 2);
                transition("</target>", 5, 3);
                break;

            case 6: // '\006'
                transition("<text>", 8);
                break;

            case 8: // '\b'
                transition("</text>", 9, patternField);
                break;

            case 10: // '\n'
                transition("<pattern>", 12);
                break;

            case 12: // '\f'
                transition("</pattern>", 13, patternField);
                break;

            case 9: // '\t'
            case 13: // '\r'
                transition("<that>", 14);
                break;

            case 14: // '\016'
                transition("</that>", 15, thatField);
                break;

            case 15: // '\017'
                transition("<topic>", 16);
                break;

            case 16: // '\020'
                transition("</topic>", 17, topicField);
                break;

            case 17: // '\021'
                transition("</input>", 4);
                transition("<template>", 18);
                break;

            case 18: // '\022'
                transition("</template>", 19, templateField);
                break;

            case 19: // '\023'
                transition("</match>", 4);
                break;

            case 20: // '\024'
                transition("</reply>", 4, templateField);
                break;

            case 5: // '\005'
                transition("<target>", 4);
                transition("</targets>", 4);
                break;
        }
    }

    private void transition(String s, int i, int j)
            throws TransitionMade {
        if (succeed(s, i)) {
            switch (j) {
                default:
                    break;

                case 3: // '\003'
                    ((TargetsReaderListener) super.listener).loadTarget(matchPattern, matchThat, matchTopic, matchTemplate, inputText, inputThat, inputTopic, reply);
                    matchPattern = matchThat = matchTopic = "*";
                    inputText = inputThat = inputTopic = matchTemplate = reply = "";
                    buffer = new StringBuffer(Math.max(GenericReader.bufferStartCapacity, buffer.length()));
                    buffer.append(bufferString);
                    searchStart = 0;
                    break;

                case 4: // '\004'
                    done = true;
                    break;

                case 0: // '\0'
                    try {
                        patternField = getClass().getDeclaredField("inputText");
                        thatField = getClass().getDeclaredField("inputThat");
                        topicField = getClass().getDeclaredField("inputTopic");
                        templateField = null;
                    } catch (NoSuchFieldException nosuchfieldexception) {
                        throw new DeveloperError("The developer has specified a field that does not exist in TargetsReader.");
                    } catch (SecurityException securityexception) {
                        throw new DeveloperError("Security manager prevents TargetsReader from functioning.");
                    }
                    break;

                case 1: // '\001'
                    try {
                        patternField = getClass().getDeclaredField("matchPattern");
                        thatField = getClass().getDeclaredField("matchThat");
                        topicField = getClass().getDeclaredField("matchTopic");
                        templateField = getClass().getDeclaredField("matchTemplate");
                    } catch (NoSuchFieldException nosuchfieldexception1) {
                        throw new DeveloperError("The developer has specified a field that does not exist in TargetsReader.");
                    } catch (SecurityException securityexception1) {
                        throw new DeveloperError("Security manager prevents TargetsReader from functioning.");
                    }
                    break;

                case 2: // '\002'
                    try {
                        patternField = null;
                        thatField = null;
                        topicField = null;
                        templateField = getClass().getDeclaredField("reply");
                    } catch (NoSuchFieldException nosuchfieldexception2) {
                        throw new DeveloperError("The developer has specified a field that does not exist in TargetsReader.");
                    } catch (SecurityException securityexception2) {
                        throw new DeveloperError("Security manager prevents TargetsReader from functioning.");
                    }
                    break;
            }
            throw super.TRANSITION_MADE;
        } else {
            return;
        }
    }

    public void closeMonitor() {
        if (monitor != null)
            monitor.close();
    }
}
