// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

// Referenced classes of package org.alicebot.server.core.util:
//            DeveloperError, StringTriple, Trace

public class StringTripleMatrix {

    private LinkedList vertical[] = {
            new LinkedList(), new LinkedList(), new LinkedList()
    };
    private LinkedList horizontal;

    public StringTripleMatrix() {
        horizontal = new LinkedList();
    }

    public LinkedList getAll() {
        return horizontal;
    }

    public Iterator iterator() {
        return horizontal.iterator();
    }

    public ListIterator listIterator() {
        return horizontal.listIterator();
    }

    public boolean contains(StringTriple stringtriple) {
        return horizontal.contains(stringtriple);
    }

    public LinkedList getFirsts() {
        return vertical[0];
    }

    public LinkedList getSeconds() {
        return vertical[1];
    }

    public LinkedList getThirds() {
        return vertical[2];
    }

    public void add(StringTriple stringtriple) {
        horizontal.add(stringtriple);
        vertical[0].add(stringtriple.getFirst());
        vertical[1].add(stringtriple.getSecond());
        vertical[2].add(stringtriple.getThird());
    }

    public void addAll(StringTripleMatrix stringtriplematrix) {
        horizontal.addAll(stringtriplematrix.getAll());
        vertical[0].addAll(stringtriplematrix.getFirsts());
        vertical[1].addAll(stringtriplematrix.getSeconds());
        vertical[2].addAll(stringtriplematrix.getThirds());
    }

    public int size() {
        if (vertical[0].size() != vertical[1].size() || vertical[1].size() != vertical[2].size() || vertical[2].size() != horizontal.size()) {
            Trace.devinfo("vertical[0].size(): " + vertical[0].size());
            Trace.devinfo("vertical[1].size(): " + vertical[1].size());
            Trace.devinfo("vertical[2].size(): " + vertical[2].size());
            Trace.devinfo("horizontal.size(): " + horizontal.size());
            throw new DeveloperError("Triple matrix integrity violated!");
        } else {
            return horizontal.size();
        }
    }

    public void ensureSize(int i) {
        if (size() >= i)
            return;
        for (int j = 3; --j >= 0; ) {
            Vector vector = new Vector(vertical[j]);
            vector.setSize(i);
            vertical[j] = new LinkedList(vector);
        }

        Vector vector1 = new Vector(horizontal);
        vector1.setSize(i);
        horizontal = new LinkedList(vector1);
    }
}
