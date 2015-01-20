// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.targeting.Target;
import org.alicebot.server.core.targeting.TargetingTool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            Tabulator, TargetingGUI

public class InputPanel extends Tabulator {

    public InputPanel(TargetingGUI targetinggui) {
        super(new String[]{
                "<input>", "<pattern>", "<that>", "<topic>"
        });
        guiparent = targetinggui;
    }

    public void updateFromTargets() {
        List list = TargetingTool.getSortedTargets();
        Iterator iterator = list.iterator();
        ArrayList arraylist = new ArrayList();
        while (iterator.hasNext()) {
            Target target = (Target) iterator.next();
            String s = target.getMatchPattern();
            String s1 = target.getMatchThat();
            String s2 = target.getMatchTopic();
            ListIterator listiterator = target.getInputTexts().listIterator();
            ArrayList arraylist1 = new ArrayList();
            while (listiterator.hasNext()) {
                String s3 = (String) listiterator.next();
                if (!arraylist1.contains(s3)) {
                    arraylist.add(((Object) (new Object[]{
                            s3, s, s1, s2, target, new Integer(listiterator.previousIndex() + 1)
                    })));
                    arraylist1.add(s3);
                }
            }
        }
        Object aobj[][] = new Object[0][];
        aobj = (Object[][]) arraylist.toArray(((Object[]) (aobj)));
        if (aobj.length > 0)
            reloadData(aobj);
    }
}
