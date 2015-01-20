// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TableMap extends AbstractTableModel
        implements TableModelListener {

    protected TableModel model;

    public TableMap() {
    }

    public TableModel getModel() {
        return model;
    }

    public synchronized void setModel(TableModel tablemodel) {
        model = tablemodel;
        tablemodel.addTableModelListener(this);
    }

    public Object getValueAt(int i, int j) {
        return model.getValueAt(i, j);
    }

    public void setValueAt(Object obj, int i, int j) {
        model.setValueAt(obj, i, j);
    }

    public synchronized int getRowCount() {
        return model != null ? model.getRowCount() : 0;
    }

    public int getColumnCount() {
        return model != null ? model.getColumnCount() : 0;
    }

    public String getColumnName(int i) {
        return model.getColumnName(i);
    }

    public Class getColumnClass(int i) {
        return model.getColumnClass(i);
    }

    public boolean isCellEditable(int i, int j) {
        return model.isCellEditable(i, j);
    }

    public void tableChanged(TableModelEvent tablemodelevent) {
        fireTableChanged(tablemodelevent);
    }
}
