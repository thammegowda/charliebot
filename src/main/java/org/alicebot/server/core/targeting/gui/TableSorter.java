// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.util.Trace;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            TableMap

public class TableSorter extends TableMap {

    int indexes[];
    Vector sortingColumns;
    boolean ascending;
    int compares;

    public TableSorter() {
        sortingColumns = new Vector();
        ascending = true;
        indexes = new int[0];
    }

    public TableSorter(TableModel tablemodel) {
        sortingColumns = new Vector();
        ascending = true;
        setModel(tablemodel);
    }

    public void setModel(TableModel tablemodel) {
        super.setModel(tablemodel);
        reallocateIndexes();
    }

    public int compareRowsByColumn(int i, int j, int k) {
        Class class1 = model.getColumnClass(k);
        TableModel tablemodel = model;
        Object obj = tablemodel.getValueAt(i, k);
        Object obj1 = tablemodel.getValueAt(j, k);
        if (obj == null && obj1 == null)
            return 0;
        if (obj == null)
            return -1;
        if (obj1 == null)
            return 1;
        if (class1.getSuperclass() == (Number.class)) {
            Number number = (Number) tablemodel.getValueAt(i, k);
            double d = number.doubleValue();
            Number number1 = (Number) tablemodel.getValueAt(j, k);
            double d1 = number1.doubleValue();
            if (d < d1)
                return -1;
            return d <= d1 ? 0 : 1;
        }
        if (class1 == (Date.class)) {
            Date date = (Date) tablemodel.getValueAt(i, k);
            long l = date.getTime();
            Date date1 = (Date) tablemodel.getValueAt(j, k);
            long l1 = date1.getTime();
            if (l < l1)
                return -1;
            return l <= l1 ? 0 : 1;
        }
        if (class1 == (String.class)) {
            String s = (String) tablemodel.getValueAt(i, k);
            String s1 = (String) tablemodel.getValueAt(j, k);
            int i1 = s.compareTo(s1);
            if (i1 < 0)
                return -1;
            return i1 <= 0 ? 0 : 1;
        }
        if (class1 == (Boolean.class)) {
            Boolean boolean1 = (Boolean) tablemodel.getValueAt(i, k);
            boolean flag = boolean1.booleanValue();
            Boolean boolean2 = (Boolean) tablemodel.getValueAt(j, k);
            boolean flag1 = boolean2.booleanValue();
            if (flag == flag1)
                return 0;
            return !flag ? -1 : 1;
        }
        Object obj2 = tablemodel.getValueAt(i, k);
        String s2 = obj2.toString();
        Object obj3 = tablemodel.getValueAt(j, k);
        String s3 = obj3.toString();
        int j1 = s2.compareTo(s3);
        if (j1 < 0)
            return -1;
        return j1 <= 0 ? 0 : 1;
    }

    public int compare(int i, int j) {
        compares++;
        for (int k = 0; k < sortingColumns.size(); k++) {
            Integer integer = (Integer) sortingColumns.elementAt(k);
            int l = compareRowsByColumn(i, j, integer.intValue());
            if (l != 0)
                return ascending ? l : -l;
        }

        return 0;
    }

    public void reallocateIndexes() {
        int i = model.getRowCount();
        indexes = new int[i];
        for (int j = 0; j < i; j++)
            indexes[j] = j;

    }

    public void tableChanged(TableModelEvent tablemodelevent) {
        reallocateIndexes();
        super.tableChanged(tablemodelevent);
    }

    public void checkModel() {
        if (indexes.length != model.getRowCount())
            Trace.devinfo("Sorter not informed of a change in model.");
    }

    public void sort(Object obj) {
        checkModel();
        compares = 0;
        shuttlesort((int[]) indexes.clone(), indexes, 0, indexes.length);
    }

    public void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i + 1; j < getRowCount(); j++)
                if (compare(indexes[i], indexes[j]) == -1)
                    swap(i, j);

        }

    }

    public void shuttlesort(int ai[], int ai1[], int i, int j) {
        if (j - i < 2)
            return;
        int k = (i + j) / 2;
        shuttlesort(ai1, ai, i, k);
        shuttlesort(ai1, ai, k, j);
        int l = i;
        int i1 = k;
        if (j - i >= 4 && compare(ai[k - 1], ai[k]) <= 0) {
            for (int j1 = i; j1 < j; j1++)
                ai1[j1] = ai[j1];

            return;
        }
        for (int k1 = i; k1 < j; k1++)
            if (i1 >= j || l < k && compare(ai[l], ai[i1]) <= 0)
                ai1[k1] = ai[l++];
            else
                ai1[k1] = ai[i1++];

    }

    public void swap(int i, int j) {
        int k = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = k;
    }

    public Object getValueAt(int i, int j) {
        checkModel();
        return model.getValueAt(indexes[i], j);
    }

    public void setValueAt(Object obj, int i, int j) {
        checkModel();
        model.setValueAt(obj, indexes[i], j);
    }

    public void sortByColumn(int i) {
        sortByColumn(i, true);
    }

    public void sortByColumn(int i, boolean flag) {
        ascending = flag;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(i));
        sort(this);
        super.tableChanged(new TableModelEvent(this));
    }

    public void addMouseListenerToHeaderInTable(JTable jtable) {
        final TableSorter sorter = this;
        final JTable tableView = jtable;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter mouseadapter = new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseevent) {
                TableColumnModel tablecolumnmodel = tableView.getColumnModel();
                int i = tablecolumnmodel.getColumnIndexAtX(mouseevent.getX());
                int j = tableView.convertColumnIndexToModel(i);
                if (mouseevent.getClickCount() == 1 && j != -1) {
                    int k = mouseevent.getModifiers() & 1;
                    boolean flag = k == 0;
                    sorter.sortByColumn(j, flag);
                }
            }

        };
        JTableHeader jtableheader = tableView.getTableHeader();
        jtableheader.addMouseListener(mouseadapter);
    }
}
