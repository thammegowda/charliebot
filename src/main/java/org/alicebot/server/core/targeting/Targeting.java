// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;


public abstract class Targeting {

    public static final String TARGETS = "targets";
    public static final String TARGETS_START = "<targets>";
    public static final String TARGETS_END = "</targets>";
    public static final String TARGET_START = "<target>";
    public static final String TARGET_END = "</target>";
    public static final String INPUT_START = "<input>";
    public static final String INPUT_END = "</input>";
    public static final String TEXT_START = "<text>";
    public static final String TEXT_END = "</text>";
    public static final String MATCH_START = "<match>";
    public static final String MATCH_END = "</match>";
    public static final String PATTERN_START = "<pattern>";
    public static final String PATTERN_END = "</pattern>";
    public static final String THAT_START = "<that>";
    public static final String THAT_END = "</that>";
    public static final String TOPIC_START = "<topic>";
    public static final String TOPIC_END = "</topic>";
    public static final String TOPIC_NAME_BEGIN = "<topic name=\"";
    public static final String TOPIC_NAME_END = "\">";
    public static final String CATEGORY_START = "<category>";
    public static final String CATEGORY_END = "</category>";
    public static final String TEMPLATE_START = "<template>";
    public static final String TEMPLATE_END = "</template>";
    public static final String REPLY_START = "<reply>";
    public static final String REPLY_END = "</reply>";
    public static final String INDENT = "    ";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    public static final String SPACE = " ";
    public static final String EMPTY_STRING = "";
    public Targeting() {
    }

}
