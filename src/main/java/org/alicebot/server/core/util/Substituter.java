// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Substituter {

    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";

    public Substituter() {
    }

    public static String replace(String s, String s1, String s2) {
        StringBuffer stringbuffer = new StringBuffer(" " + s2 + " ");
        for (int i = stringbuffer.toString().indexOf(s); i != -1; i = stringbuffer.toString().indexOf(s))
            stringbuffer.replace(i, i + s.length(), s1);

        return stringbuffer.toString().trim();
    }

    public static String replaceIgnoreCase(String s, String s1, String s2) {
        StringBuffer stringbuffer = new StringBuffer(" " + s2 + " ");
        s = s.toUpperCase();
        for (int i = stringbuffer.toString().toUpperCase().indexOf(s); i != -1; i = stringbuffer.toString().toUpperCase().indexOf(s))
            stringbuffer.replace(i, i + s.length(), s1);

        return stringbuffer.toString().trim();
    }

    public static String applySubstitutions(HashMap hashmap, String s) {
        LinkedList linkedlist = new LinkedList();
        linkedlist.add(" " + s + " ");
        LinkedList linkedlist1 = new LinkedList();
        for (Iterator iterator = hashmap.keySet().iterator(); iterator.hasNext(); ) {
            String s1 = (String) iterator.next();
            for (ListIterator listiterator = linkedlist.listIterator(0); listiterator.hasNext(); ) {
                String s2 = (String) listiterator.next();
                int i = s2.toUpperCase().indexOf(s1.toUpperCase());
                if (i >= 0 && i < s2.length()) {
                    listiterator.set(s2.substring(0, i));
                    String s3 = (String) hashmap.get(s1);
                    linkedlist1.add(listiterator.nextIndex() - 1, s3);
                    if (i + s3.length() < s2.length())
                        listiterator.add(s2.substring(i + s1.length()));
                    else
                        listiterator.add("");
                }
            }

        }

        StringBuffer stringbuffer = new StringBuffer();
        ListIterator listiterator1 = linkedlist.listIterator(0);
        ListIterator listiterator2 = linkedlist1.listIterator(0);
        while (listiterator1.hasNext()) {
            stringbuffer.append(listiterator1.next());
            if (listiterator2.hasNext())
                stringbuffer.append(listiterator2.next());
        }
        int j = stringbuffer.length();
        if (j >= 2) {
            int k = 0;
            if (stringbuffer.charAt(0) == ' ')
                k = 1;
            if (stringbuffer.charAt(j - 1) == ' ')
                j--;
            if (k == j)
                return "";
            else
                return stringbuffer.substring(k, j);
        } else {
            return stringbuffer.toString();
        }
    }
}
