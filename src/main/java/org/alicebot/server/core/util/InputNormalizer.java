// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

// Referenced classes of package org.alicebot.server.core.util:
//            Toolkit

public class InputNormalizer {

    private static final String EMPTY_STRING = "";

    public InputNormalizer() {
    }

    public static ArrayList sentenceSplit(ArrayList arraylist, String s) {
        ArrayList arraylist1 = new ArrayList();
        int i = s.length();
        if (i == 0) {
            arraylist1.add("");
            return arraylist1;
        }
        ArrayList arraylist2 = new ArrayList();
        for (Iterator iterator = arraylist.iterator(); iterator.hasNext(); ) {
            String s1 = (String) iterator.next();
            for (int j = s.indexOf(s1); j != -1; j = s.indexOf(s1, j + 1))
                arraylist2.add(new Integer(j));

        }

        if (arraylist2.size() == 0) {
            arraylist1.add(s);
            return arraylist1;
        }
        Collections.sort(arraylist2);
        ListIterator listiterator = arraylist2.listIterator();
        int l;
        for (int k = ((Integer) listiterator.next()).intValue(); listiterator.hasNext(); k = l) {
            l = ((Integer) listiterator.next()).intValue();
            if (l == k + 1) {
                listiterator.previous();
                listiterator.previous();
                listiterator.remove();
            }
        }

        listiterator = arraylist2.listIterator();
        int i1 = 0;
        int j1 = i - 1;
        while (listiterator.hasNext()) {
            int k1 = ((Integer) listiterator.next()).intValue();
            arraylist1.add(s.substring(i1, k1 + 1).trim());
            i1 = k1 + 1;
        }
        if (i1 < i - 1)
            arraylist1.add(s.substring(i1).trim());
        return arraylist1;
    }

    public static String patternFit(String s) {
        s = Toolkit.removeMarkup(s);
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        StringBuffer stringbuffer = new StringBuffer(s.length());
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next())
            if (!Character.isLetterOrDigit(c) && c != '*' && c != '_')
                stringbuffer.append(' ');
            else
                stringbuffer.append(Character.toUpperCase(c));

        return Toolkit.filterWhitespace(stringbuffer.toString());
    }

    public static String patternFitIgnoreCase(String s) {
        s = Toolkit.removeMarkup(s);
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        StringBuffer stringbuffer = new StringBuffer(s.length());
        for (char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next())
            if (!Character.isLetterOrDigit(c))
                stringbuffer.append(' ');
            else
                stringbuffer.append(c);

        return Toolkit.filterWhitespace(stringbuffer.toString());
    }
}
