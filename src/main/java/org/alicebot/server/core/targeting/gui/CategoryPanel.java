// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.targeting.Target;
import org.alicebot.server.core.targeting.TargetingTool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            Tabulator, TargetingGUI

public class CategoryPanel extends Tabulator {

    public CategoryPanel(TargetingGUI targetinggui) {
        super(new String[]{
                "activations", "<pattern>", "<that>", "<topic>"
        });
        guiparent = targetinggui;
    }

    public void updateFromTargets() {
        List list = TargetingTool.getSortedTargets();
        Iterator iterator = list.iterator();
        ArrayList arraylist = new ArrayList();
        Target target;
        for (; iterator.hasNext(); arraylist.add(((Object) (new Object[]{
                new Integer(target.getActivations()), target.getMatchPattern(), target.getMatchThat(), target.getMatchTopic(), target, new Integer(1)
        }))))
            target = (Target) iterator.next();

        Object aobj[][] = new Object[0][];
        aobj = (Object[][]) arraylist.toArray(((Object[]) (aobj)));
        if (aobj.length > 0)
            reloadData(aobj);
    }
}
