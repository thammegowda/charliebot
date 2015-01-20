// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.targeting.Target;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            TableSorter, TargetingGUI, TargetPanel

public abstract class Tabulator extends JPanel {
    TargetingGUI guiparent;
    private JTable table;
    private int columnCount;
    private int visibleColumnCount;
    private TabulatorTableModel dataTableModel;
    private TableSorter sorterTableModel;
    public Tabulator(String as[]) {
        visibleColumnCount = as.length;
        dataTableModel = new TabulatorTableModel(as);
        sorterTableModel = new TableSorter(dataTableModel);
        table = new JTable(sorterTableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(0);
        table.addMouseListener(new TargetOpener());
        sorterTableModel.addMouseListenerToHeaderInTable(table);
        JScrollPane jscrollpane = new JScrollPane(table);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BoxLayout(this, 1));
        add(jscrollpane);
    }

    public void reloadData(Object aobj[][]) {
        dataTableModel.setData(aobj);
        columnCount = aobj[0].length;
        Object obj = null;
        Object obj1 = null;
        boolean flag = false;
        boolean flag1 = false;
        Object aobj1[] = dataTableModel.getLongestRow();
        if (aobj1 == null)
            return;
        for (int k = 0; k < visibleColumnCount; k++) {
            TableColumn tablecolumn = table.getColumnModel().getColumn(k);
            Component component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(null, tablecolumn.getHeaderValue(), false, false, 0, 0);
            int i = component.getPreferredSize().width;
            component = table.getDefaultRenderer(sorterTableModel.getColumnClass(k)).getTableCellRendererComponent(table, aobj1[k], false, false, 0, k);
            int j = component.getPreferredSize().width;
            tablecolumn.setPreferredWidth(Math.max(i, j));
        }

    }

    private class TargetOpener extends MouseAdapter {

        private TargetOpener() {
        }

        public void mouseClicked(MouseEvent mouseevent) {
            if (mouseevent.getClickCount() == 2) {
                Target target = (Target) sorterTableModel.getValueAt(table.rowAtPoint(mouseevent.getPoint()), columnCount - 2);
                int i = ((Integer) sorterTableModel.getValueAt(table.rowAtPoint(mouseevent.getPoint()), columnCount - 1)).intValue();
                guiparent.targetPanel.setTarget(target);
                guiparent.targetPanel.scrollToInput(i);
                guiparent.viewTargets();
            }
        }

    }

    private class TabulatorTableModel extends AbstractTableModel {

        private String columnNames[];
        private Object data[][];

        public TabulatorTableModel(String as[]) {
            columnNames = as;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public synchronized int getRowCount() {
            if (data == null)
                return 0;
            else
                return data.length;
        }

        public String getColumnName(int i) {
            return columnNames[i];
        }

        public Object getValueAt(int i, int j) {
            return data[i][j];
        }

        public Class getColumnClass(int i) {
            return getValueAt(0, i).getClass();
        }

        public synchronized void setData(Object aobj[][]) {
            data = aobj;
            fireTableDataChanged();
        }

        public Object[] getLongestRow() {
            if (data == null)
                return null;
            int i = 0;
            int j = 0;
            for (int k = 0; k < data.length; k++) {
                int l = 0;
                for (int i1 = 0; i1 < columnNames.length; i1++)
                    l += data[k][i1].toString().length();

                i = l <= i ? i : l;
                j = l <= i ? j : k;
            }

            return data[j];
        }
    }


}
