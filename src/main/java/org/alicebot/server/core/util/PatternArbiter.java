// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.util:
//            NotAnAIMLPatternException

public class PatternArbiter {

    private static final char ASTERISK = 42;
    private static final char UNDERSCORE = 95;
    private static final char SPACE = 32;
    private static final char TAG_START = 60;
    private static final char QUOTE_MARK = 34;
    private static final String BOT_NAME_EQUALS = "<bot name=\"";
    private static final String ATOMIC_ELEMENT_END = "\"/>";
    private static final int UNKNOWN = 1;
    private static final int IS_WILDCARD = 2;
    private static final int IS_LETTERDIGIT = 4;
    private static final int IS_SPACE = 8;
    private static final int IS_WHITESPACE = 16;
    private static final int IS_NON_LETTERDIGIT = 32;
    private static final int NOT_PAST_END = 0;
    private static final int AT_END = 1;
    private static final int PAST_END = 2;
    private static final int CONTINUE_MATCHING = 1;
    private static final int STOP_MATCHING = 2;
    private static final int MATCH_FAILURE = 4;
    private static final int ADVANCE_LITERAL = 8;
    private static final int ADVANCE_PATTERN = 16;
    public PatternArbiter() {
    }

    public static boolean matches(String s, String s1, boolean flag)
            throws NotAnAIMLPatternException {
        checkAIMLPattern(s1, flag);
        StringCharacterIterator stringcharacteriterator;
        StringCharacterIterator stringcharacteriterator1;
        if (flag) {
            stringcharacteriterator = new StringCharacterIterator(s1.toUpperCase().trim());
            stringcharacteriterator1 = new StringCharacterIterator(s.toUpperCase().trim());
        } else {
            stringcharacteriterator = new StringCharacterIterator(s1.trim());
            stringcharacteriterator1 = new StringCharacterIterator(s.trim());
        }
        char c = stringcharacteriterator.first();
        char c1 = stringcharacteriterator1.first();
        int i = 1;
        int j = 1;
        int k = 0;
        int l = 0;
        int i1;
        for (i1 = 1; (i1 & 2) != 2; ) {
            if ((i1 & 1) == 1) {
                if ((i1 & 0x10) == 16)
                    c = stringcharacteriterator.next();
                if ((i1 & 8) == 8)
                    c1 = stringcharacteriterator1.next();
                i1 = 1;
            }
            if ((i1 & 1) == 1) {
                if (!Character.isLetterOrDigit(c1)) {
                    j = 32;
                    if (Character.isWhitespace(c1)) {
                        j |= 0x10;
                        if (c1 == ' ')
                            j |= 8;
                        else
                            i1 = 6;
                    }
                } else {
                    j = 4;
                }
                if (!Character.isLetterOrDigit(c)) {
                    i = 32;
                    if (Character.isWhitespace(c)) {
                        i |= 0x10;
                        if (c == ' ')
                            i |= 8;
                        else
                            i1 = 6;
                    }
                    if (c == '*' || c == '_')
                        i |= 2;
                } else {
                    i = 4;
                }
            }
            if ((i1 & 1) == 1) {
                if (c == '\uFFFF') {
                    k = 2;
                    i1 |= 2;
                } else if (stringcharacteriterator.getEndIndex() == stringcharacteriterator.getIndex() + 1)
                    k = 1;
                if (c1 == '\uFFFF') {
                    l = 2;
                    i1 |= 2;
                } else if (stringcharacteriterator1.getEndIndex() == stringcharacteriterator1.getIndex() + 1)
                    l = 1;
            }
            if ((i1 & 1) == 1)
                if (i == 4) {
                    if (j == 4) {
                        if (c == c1)
                            i1 = i1 | 8 | 0x10;
                        else
                            i1 = 6;
                    } else {
                        i1 = 6;
                    }
                    if (k == 1)
                        i1 |= 2;
                } else if ((i & 2) == 2) {
                    if (j == 4)
                        i1 |= 8;
                    else if ((j & 0x20) == 32)
                        if (k == 1) {
                            i1 |= 8;
                        } else {
                            char c2 = stringcharacteriterator.next();
                            if (c2 != ' ')
                                i1 = 6;
                            else
                                i1 = i1 | 0x10 | 8;
                        }
                } else if ((i & 8) == 8)
                    if ((j & 0x20) == 32)
                        i1 = i1 | 0x10 | 8;
                    else
                        i1 = 6;
        }

        if ((i1 & 4) == 4)
            return false;
        if (l == 1 || l == 2)
            return k == 1 || k == 2;
        else
            return false;
    }

    public static void checkAIMLPattern(String s, boolean flag)
            throws NotAnAIMLPatternException {
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        boolean flag1 = true;
        int j = 1;
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next()) {
            int i;
            if (!Character.isLetterOrDigit(c)) {
                i = 32;
                if (Character.isWhitespace(c)) {
                    i |= 0x10;
                    if (c == ' ')
                        i |= 8;
                    else
                        throw new NotAnAIMLPatternException("The only allowed whitespace is a space ( ).", s);
                }
                if (c == '*' || c == '_') {
                    i |= 2;
                    if (j != 1 && (j == 4 || (j & 2) == 2))
                        throw new NotAnAIMLPatternException("A wildcard cannot be preceded by a wildcard, a letter or a digit.", s);
                }
                if (c == '<') {
                    int k = stringcharacteriterator.getIndex();
                    if (s.regionMatches(false, k, "<bot name=\"", 0, 11)) {
                        stringcharacteriterator.setIndex(k + 11);
                        for (c = stringcharacteriterator.next(); c != '\uFFFF' && c != '"' && (Character.isLetterOrDigit(c) || c == ' ' || c == '_'); c = stringcharacteriterator.next())
                            ;
                        k = stringcharacteriterator.getIndex();
                        if (!s.regionMatches(false, k, "\"/>", 0, 3))
                            throw new NotAnAIMLPatternException("Invalid or malformed <bot/> element.", s);
                        stringcharacteriterator.setIndex(k + 3);
                    } else {
                        throw new NotAnAIMLPatternException("Invalid or malformed inner element.", s);
                    }
                }
            } else {
                i = 4;
                if (!flag && Character.toUpperCase(c) != c)
                    throw new NotAnAIMLPatternException("Characters with case mappings must be uppercase.", s);
                if (j != 1 && (j & 2) == 2)
                    throw new NotAnAIMLPatternException("A letter or digit may not be preceded by a wildcard.", s);
            }
            j = i;
        }

    }

    public static void main(String args[]) {
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
        Object obj = null;
        Object obj1 = null;
        boolean flag = false;
        boolean flag2 = false;
        int i = 0;
        int j = 0;
        do {
            String s2 = null;
            try {
                s2 = bufferedreader.readLine();
            } catch (IOException ioexception) {
                System.out.println("Cannot read from console!");
                return;
            }
            if (s2 == null)
                break;
            if (s2.toLowerCase().equals("exit")) {
                System.out.println("Exiting.");
                System.exit(0);
            }
            if (!s2.startsWith(";") && s2.trim().length() > 0) {
                StringTokenizer stringtokenizer = new StringTokenizer(s2, "|");
                String s;
                String s1;
                boolean flag1;
                boolean flag3;
                try {
                    s = stringtokenizer.nextToken();
                    s1 = stringtokenizer.nextToken();
                    flag1 = stringtokenizer.nextToken().equals("y");
                    flag3 = stringtokenizer.nextToken().equals("t");
                } catch (NoSuchElementException nosuchelementexception) {
                    System.out.println("Improperly formatted input. Use: literal|PATTERN|(y/n)|(t/f)");
                    continue;
                }
                long l = (new Date()).getTime();
                boolean flag4;
                try {
                    flag4 = matches(s, s1, flag1);
                } catch (NotAnAIMLPatternException notanaimlpatternexception) {
                    System.out.println("Exception: " + notanaimlpatternexception.getMessage());
                    flag4 = false;
                }
                l = (new Date()).getTime() - l;
                if (flag4 == flag3) {
                    j++;
                    System.out.print("TEST PASSED] ");
                } else {
                    i++;
                    System.out.print("TEST FAILED] ");
                }
                if (flag4)
                    System.out.print("match: " + s + " | " + s1 + (flag1 ? " (ignoreCase)" : ""));
                else
                    System.out.print("no match: " + s + " | " + s1 + (flag1 ? " (ignoreCase)" : ""));
                System.out.println(" (" + l + " ms)");
            } else {
                System.out.println(s2);
            }
        } while (true);
        System.out.println((j + i) + " tests: " + j + " successes, " + i + " failures");
    }
}
