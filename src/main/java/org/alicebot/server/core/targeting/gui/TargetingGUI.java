// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.targeting.TargetingTool;
import org.alicebot.server.core.util.Trace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            TargetPanel, InputPanel, CategoryPanel

public class TargetingGUI extends JPanel {

    static final int MIN_WIDTH = 600;
    static final int MIN_HEIGHT = 400;
    static final int PREF_WIDTH = 700;
    static final int PREF_HEIGHT = 500;
    static final Dimension minDimension = new Dimension(600, 400);
    static final Dimension prefDimension = new Dimension(700, 500);
    private static final Object HELP_MESSAGE[] = {
            "AIML Targeting Tool", "Charliebot version " + "4.1.8", "(c) A.L.I.C.E. AI Foundation (http://alicebot.org)"
    };
    private static final ImageIcon aliceLogo = new ImageIcon(ClassLoader.getSystemResource("org/alicebot/icons/aliceLogo.jpg"));
    private static final ImageIcon aliceIcon = new ImageIcon(ClassLoader.getSystemResource("org/alicebot/icons/aliceIcon.jpg"));
    private static TargetingTool targetingTool;
    private static JMenuBar menuBar;
    public TargetPanel targetPanel;
    public InputPanel inputPanel;
    public CategoryPanel categoryPanel;
    public JLabel statusBar;
    private JFrame frame;
    private JTabbedPane tabbedPane;
    public TargetingGUI(TargetingTool targetingtool) {
        targetingTool = targetingtool;
        targetPanel = new TargetPanel(this);
        targetPanel.setMinimumSize(minDimension);
        targetPanel.setPreferredSize(prefDimension);
        targetPanel.setAlignmentX(0.0F);
        inputPanel = new InputPanel(this);
        inputPanel.setMinimumSize(minDimension);
        inputPanel.setPreferredSize(prefDimension);
        inputPanel.setAlignmentX(0.0F);
        categoryPanel = new CategoryPanel(this);
        categoryPanel.setMinimumSize(minDimension);
        categoryPanel.setPreferredSize(prefDimension);
        categoryPanel.setAlignmentX(0.0F);
        statusBar = new JLabel();
        statusBar.setAlignmentX(1.0F);
        statusBar.setHorizontalAlignment(4);
        statusBar.setFont(new Font("Fixedsys", 0, 12));
        statusBar.setForeground(Color.black);
        statusBar.setMinimumSize(new Dimension(600, 14));
        statusBar.setPreferredSize(new Dimension(700, 14));
        statusBar.setMaximumSize(new Dimension(32767, 14));
        tabbedPane = new JTabbedPane();
        tabbedPane.setAlignmentX(1.0F);
        tabbedPane.setMinimumSize(new Dimension(600, 500));
        tabbedPane.setPreferredSize(new Dimension(700, 500));
        tabbedPane.setMaximumSize(new Dimension(32767, 32767));
        tabbedPane.setFont(new Font("Fixedsys", 0, 12));
        tabbedPane.setTabPlacement(3);
        tabbedPane.add("Targets", targetPanel);
        tabbedPane.add("Inputs", inputPanel);
        tabbedPane.add("Categories", categoryPanel);
        setLayout(new BoxLayout(this, 1));
        add(tabbedPane);
        add(statusBar);
        menuBar = new JMenuBar();
        JMenu jmenu = new JMenu("File");
        jmenu.setFont(new Font("Fixedsys", 0, 12));
        jmenu.setMnemonic(70);
        JMenuItem jmenuitem = new JMenuItem("Load targets data from URL...");
        jmenuitem.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem.setMnemonic(85);
        jmenuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                loadDataURLBox();
            }

        });
        JMenuItem jmenuitem1 = new JMenuItem("Load targets data from file path...");
        jmenuitem1.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem1.setMnemonic(80);
        jmenuitem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                loadDataFilePathChooser();
            }

        });
        JMenuItem jmenuitem2 = new JMenuItem("Reload target data");
        jmenuitem2.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem2.setMnemonic(82);
        jmenuitem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                reloadTargets();
            }

        });
        JMenuItem jmenuitem3 = new JMenuItem("Exit");
        jmenuitem3.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem3.setMnemonic(88);
        jmenuitem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shutdown();
            }

        });
        jmenu.add(jmenuitem);
        jmenu.add(jmenuitem1);
        jmenu.addSeparator();
        jmenu.add(jmenuitem2);
        jmenu.addSeparator();
        jmenu.add(jmenuitem3);
        JMenu jmenu1 = new JMenu("Edit");
        jmenu1.setFont(new Font("Fixedsys", 0, 12));
        jmenu1.setMnemonic(69);
        JMenu jmenu2 = new JMenu("View");
        jmenu2.setFont(new Font("Fixedsys", 0, 12));
        jmenu2.setMnemonic(86);
        JMenuItem jmenuitem4 = new JMenuItem("Targets");
        jmenuitem4.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem4.setMnemonic(84);
        jmenuitem4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                viewTargets();
            }

        });
        JMenuItem jmenuitem5 = new JMenuItem("Inputs");
        jmenuitem5.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem5.setMnemonic(73);
        jmenuitem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                viewInputs();
            }

        });
        JMenuItem jmenuitem6 = new JMenuItem("Inputs");
        jmenuitem6.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem6.setMnemonic(67);
        jmenuitem6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                viewCategories();
            }

        });
        jmenu2.add(jmenuitem4);
        jmenu2.add(jmenuitem5);
        jmenu2.add(jmenuitem6);
        JMenu jmenu3 = new JMenu("Options");
        jmenu3.setFont(new Font("Fixedsys", 0, 12));
        jmenu3.setMnemonic(79);
        JCheckBoxMenuItem jcheckboxmenuitem = new JCheckBoxMenuItem("Include incomplete <that>s", targetingtool.includeIncompleteThats());
        jcheckboxmenuitem.setFont(new Font("Fixedsys", 0, 12));
        jcheckboxmenuitem.setMnemonic(73);
        jcheckboxmenuitem.setAccelerator(KeyStroke.getKeyStroke(84, 2));
        jcheckboxmenuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                includeIncompleteThats(((JCheckBoxMenuItem) actionevent.getSource()).getState());
            }

        });
        JCheckBoxMenuItem jcheckboxmenuitem1 = new JCheckBoxMenuItem("Include incomplete <topic>s", targetingtool.includeIncompleteTopics());
        jcheckboxmenuitem1.setFont(new Font("Fixedsys", 0, 12));
        jcheckboxmenuitem1.setMnemonic(78);
        jcheckboxmenuitem1.setAccelerator(KeyStroke.getKeyStroke(80, 2));
        jcheckboxmenuitem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                includeIncompleteTopics(((JCheckBoxMenuItem) actionevent.getSource()).getState());
            }

        });
        JMenuItem jmenuitem7 = new JMenuItem("Change reload frequency...");
        jmenuitem7.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem7.setMnemonic(82);
        jmenuitem7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                showSetReloadFrequencyBox();
            }

        });
        jmenu3.add(jcheckboxmenuitem);
        jmenu3.add(jcheckboxmenuitem1);
        jmenu3.addSeparator();
        jmenu3.add(jmenuitem7);
        JMenu jmenu4 = new JMenu("Actions");
        jmenu4.setFont(new Font("Fixedsys", 0, 12));
        jmenu4.setMnemonic(65);
        JMenuItem jmenuitem8 = new JMenuItem("Discard target");
        jmenuitem8.setFont(new Font("Fixedsys", 0, 12));
//        jmenuitem8.addActionListener(new TargetPanel.DiscardTarget(targetPanel));
        JMenuItem jmenuitem9 = new JMenuItem("Discard all targets");
        jmenuitem9.setFont(new Font("Fixedsys", 0, 12));
//        jmenuitem9.addActionListener(new TargetPanel.DiscardAllTargets(targetPanel));
        JMenuItem jmenuitem10 = new JMenuItem("Save new category from target");
        jmenuitem10.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem10.setMnemonic(83);
        jmenuitem10.setAccelerator(KeyStroke.getKeyStroke(83, 2));
//        jmenuitem10.addActionListener(new TargetPanel.SaveTarget(targetPanel));
        JMenuItem jmenuitem11 = new JMenuItem("Get next target");
        jmenuitem11.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem11.setMnemonic(78);
        jmenuitem11.setAccelerator(KeyStroke.getKeyStroke(78, 2));
//        jmenuitem11.addActionListener(new TargetPanel.NextTarget(targetPanel));
        jmenu4.add(jmenuitem10);
        jmenu4.add(jmenuitem11);
        jmenu4.add(jmenuitem8);
        jmenu4.add(jmenuitem9);
        JMenu jmenu5 = new JMenu("Help");
        jmenu5.setFont(new Font("Fixedsys", 0, 12));
        jmenu5.setMnemonic(72);
        JMenuItem jmenuitem12 = new JMenuItem("About...");
        jmenuitem12.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem12.setMnemonic(65);
        jmenuitem12.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                showAboutBox();
            }

        });
        jmenu5.add(jmenuitem12);
        menuBar.add(jmenu);
        menuBar.add(jmenu1);
        menuBar.add(jmenu2);
        menuBar.add(jmenu3);
        menuBar.add(jmenu4);
        menuBar.add(jmenu5);
    }

    public void start() {
        frame = new JFrame();
        updateTitle();
        frame.getContentPane().add(this);
        frame.setDefaultCloseOperation(3);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setLocation(50, 50);
        frame.setIconImage(aliceIcon.getImage());
        frame.setVisible(true);
        targetPanel.nextTarget();
    }

    public void shutdown() {
        TargetingTool.shutdown();
    }

    public void reloadTargets() {
        try {
            targetingTool.reload();
        } catch (Exception exception) {
            showError(exception.getMessage());
        }
    }

    private void includeIncompleteThats(boolean flag) {
        targetingTool.includeIncompleteThats(flag);
        if (!targetPanel.hasTarget())
            targetPanel.nextTarget();
    }

    private void includeIncompleteTopics(boolean flag) {
        targetingTool.includeIncompleteTopics(flag);
        if (!targetPanel.hasTarget())
            targetPanel.nextTarget();
    }

    private void showAboutBox() {
        JOptionPane.showMessageDialog(null, ((Object) (HELP_MESSAGE)), "About", 1, aliceLogo);
    }

    private void showSetReloadFrequencyBox() {
        int i = targetingTool.getReloadFrequency();
        Object obj = JOptionPane.showInputDialog(null, "Please input a value in seconds.", "Set Reload Frequency", -1, null, null, new Integer(i));
        if (obj == null)
            return;
        int j;
        try {
            j = Integer.parseInt((String) obj);
        } catch (NumberFormatException numberformatexception) {
            JOptionPane.showMessageDialog(null, "Invalid entry. Reload frequency unchanged from " + i + ".", "Invalid entry.", 2);
            return;
        }
        targetingTool.restartTimer(j);
        JOptionPane.showMessageDialog(null, "Reload frequency changed to " + j + ".", "Frequency changed.", -1);
    }

    private void loadDataURLBox() {
        String s = targetingTool.getTargetsDataPath();
        Object obj = JOptionPane.showInputDialog(null, "Enter the targets data URL from which to load.", "Change data URL", -1, null, null, s);
        if (obj == null) {
            return;
        } else {
            JOptionPane.showMessageDialog(null, "Targets data URL changed to " + (String) obj + ".", "Data path changed.", -1);
            setStatus("Loading targets data....");
            targetingTool.changeTargetsDataPath((String) obj);
            targetPanel.nextTarget();
            setStatus("");
            updateTitle();
            return;
        }
    }

    private void loadDataFilePathChooser() {
        String s = targetingTool.getTargetsDataPath();
        JFileChooser jfilechooser = new JFileChooser(s);
        jfilechooser.setDialogTitle("Choose Targets Data File");
        int i = jfilechooser.showDialog(this, "Choose");
        if (i == 0) {
            File file = jfilechooser.getSelectedFile();
            String s1 = null;
            try {
                s1 = file.getCanonicalPath();
            } catch (IOException ioexception) {
                showError("I/O error trying to access \"" + s1 + "\".");
                Trace.userinfo("I/O error trying to access \"" + s1 + "\".");
                return;
            }
            JOptionPane.showMessageDialog(null, "Targets data file path changed to " + s1 + ".", "Data path changed.", -1);
            setStatus("Loading targets data....");
            targetingTool.changeTargetsDataPath(s1);
            targetPanel.nextTarget();
            setStatus("");
            updateTitle();
        }
    }

    public void showError(String s) {
        JOptionPane.showMessageDialog(null, s, "Error", 0);
    }

    public void setStatus(String s) {
        statusBar.setText(s);
        Trace.devinfo(s);
    }

    private void updateTitle() {
        frame.setTitle("AIML Targeting Tool, Charliebot version " + "4.1.8" + " - " + targetingTool.getTargetsDataPath());
    }

    public void viewTargets() {
        tabbedPane.setSelectedIndex(0);
    }

    public void viewInputs() {
        tabbedPane.setSelectedIndex(1);
    }

    public void viewCategories() {
        tabbedPane.setSelectedIndex(2);
    }


}
