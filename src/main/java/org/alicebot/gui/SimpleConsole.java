// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.gui;

import org.alicebot.server.core.Bots;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Graphmaster;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.util.Shell;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.net.AliceServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

// Referenced classes of package org.alicebot.gui:
//            ListDialog

public class SimpleConsole extends JPanel {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    private static final Object HELP_MESSAGE[] = {
            "Simple Console for", "Charliebot version 4.1.8", "(c) A.L.I.C.E. AI Foundation (http://alicebot.org)"
    };
    private static final ImageIcon aliceLogo = new ImageIcon(SimpleConsole.class.getClassLoader().getResource("icons/aliceLogo.jpg"));
    private static final ImageIcon aliceIcon = new ImageIcon(SimpleConsole.class.getClassLoader().getResource("icons/aliceIcon.jpg"));
    private static JMenuBar menuBar;
    private AliceServer server;
    private Shell shell;
    private JTextArea display;
    private InputPanel inputPanel;
    private ConsoleDisplayStream consoleDisplay;
    private JFrame frame;
    private PrintStream displayStream;
    private PrintStream promptStream;
    private ConsoleInputStream inStream;

    public SimpleConsole() {
        consoleDisplay = new ConsoleDisplayStream();
        displayStream = new PrintStream(consoleDisplay);
        promptStream = new PrintStream(new ConsolePromptStream());
        inStream = new ConsoleInputStream();
        setLayout(new BoxLayout(this, 1));
        display = new JTextArea(40, 90);
        display.setFont(new Font("Courier New", 0, 12));
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setTabSize(4);
        display.setForeground(Color.black);
        display.setEditable(false);
        JScrollPane jscrollpane = new JScrollPane(display);
        jscrollpane.setAlignmentY(0.5F);
        inputPanel = new InputPanel();
        add(jscrollpane);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(inputPanel);
        menuBar = new JMenuBar();
        JMenu jmenu = new JMenu("File");
        jmenu.setFont(new Font("Fixedsys", 0, 12));
        jmenu.setMnemonic(70);
        JMenuItem jmenuitem = new JMenuItem("Load AIML from URL...");
        jmenuitem.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem.setMnemonic(85);
        jmenuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                loadAIMLURLBox();
            }

        });
        JMenuItem jmenuitem1 = new JMenuItem("Load AIML from file path...");
        jmenuitem1.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem1.setMnemonic(80);
        jmenuitem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                loadAIMLFilePathChooser();
            }

        });
        JMenuItem jmenuitem2 = new JMenuItem("Exit");
        jmenuitem2.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem2.setMnemonic(88);
        jmenuitem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shutdown();
            }

        });
        jmenu.add(jmenuitem);
        jmenu.add(jmenuitem1);
        jmenu.addSeparator();
        jmenu.add(jmenuitem2);
        JMenu jmenu1 = new JMenu("Actions");
        jmenu1.setFont(new Font("Fixedsys", 0, 12));
        jmenu1.setMnemonic(65);
        JCheckBoxMenuItem jcheckboxmenuitem = new JCheckBoxMenuItem("Pause Console");
        jcheckboxmenuitem.setFont(new Font("Fixedsys", 0, 12));
        jcheckboxmenuitem.setMnemonic(80);
        jcheckboxmenuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                consoleDisplay.togglePause();
            }

        });
        JMenuItem jmenuitem3 = new JMenuItem("Talk to bot...");
        jmenuitem3.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem3.setMnemonic(66);
        jmenuitem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                chooseBot();
            }

        });
        JMenuItem jmenuitem4 = new JMenuItem("List bot files");
        jmenuitem4.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem4.setMnemonic(70);
        jmenuitem4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shell.listBotFiles();
            }

        });
        JMenuItem jmenuitem5 = new JMenuItem("List bots");
        jmenuitem5.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem5.setMnemonic(76);
        jmenuitem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shell.showBotList();
            }

        });
        JMenuItem jmenuitem6 = new JMenuItem("Roll targets");
        jmenuitem6.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem6.setMnemonic(84);
        jmenuitem6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shell.rollTargets();
            }

        });
        jmenu1.add(jcheckboxmenuitem);
        jmenu1.addSeparator();
        jmenu1.add(jmenuitem3);
        jmenu1.add(jmenuitem5);
        jmenu1.add(jmenuitem4);
        jmenu1.addSeparator();
        jmenu1.add(jmenuitem6);
        JMenu jmenu2 = new JMenu("Help");
        jmenu2.setFont(new Font("Fixedsys", 0, 12));
        jmenu2.setMnemonic(72);
        JMenuItem jmenuitem7 = new JMenuItem("Shell Help...");
        jmenuitem7.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem7.setMnemonic(72);
        jmenuitem7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                shell.help();
            }

        });
        JMenuItem jmenuitem8 = new JMenuItem("About Simple Console...");
        jmenuitem8.setFont(new Font("Fixedsys", 0, 12));
        jmenuitem8.setMnemonic(65);
        jmenuitem8.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                showAboutBox();
            }

        });
        jmenu2.add(jmenuitem8);
        jmenu2.add(jmenuitem7);
        menuBar.add(jmenu);
        menuBar.add(jmenu1);
        menuBar.add(jmenu2);
        frame = new JFrame();
        frame.setTitle("Charliebot Simple Console");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(3);
        frame.pack();
        frame.setLocation(50, 50);
        frame.setVisible(true);
        System.out.println("Initialised the frame");
    }

    public static void main(String args[]) {
        String s;
        if (args.length > 0) {
            s = args[0];
        } else {
            s = Globals.DEFAULT_SETTINGS;
        }
        new SimpleConsole().start(s);
        System.out.println("Started");
    }

    public void start(String s) {
        Globals.load(s);
        shell = new Shell(inStream, displayStream, promptStream);
        server = new AliceServer(s, shell);
        Trace.setOut(displayStream);
        server.startup(true);
        shutdown();
    }

    private void shutdown() {
        if (server != null) {
            SimpleConsole _tmp = this;
            AliceServer.shutdown();
        }
    }

    private void loadAIMLURLBox() {
        Object obj = JOptionPane.showInputDialog(null, "Enter the URL from which to load.", "Load AIML from URL", -1, null, null, null);
        if (obj == null) {
            return;
        } else {
            int i = Graphmaster.getTotalCategories();
            Graphmaster.load((String) obj, shell.getCurrentBotID());
            Log.userinfo((Graphmaster.getTotalCategories() - i) + " categories loaded from \"" + (String) obj + "\".", Log.LEARN);
            return;
        }
    }

    private void loadAIMLFilePathChooser() {
        JFileChooser jfilechooser = new JFileChooser();
        jfilechooser.setDialogTitle("Choose AIML File");
        int i = jfilechooser.showDialog(this, "Choose");
        if (i == 0) {
            File file = jfilechooser.getSelectedFile();
            String s = null;
            try {
                s = file.getCanonicalPath();
            } catch (IOException ioexception) {
                Trace.userinfo("I/O error trying to access \"" + s + "\".");
                return;
            }
            int j = Graphmaster.getTotalCategories();
            Graphmaster.load(s, shell.getCurrentBotID());
            Log.userinfo((Graphmaster.getTotalCategories() - j) + " categories loaded from \"" + s + "\".", Log.LEARN);
        }
    }

    private void chooseBot() {
        String as[] = (String[]) Bots.getIDs().toArray(new String[0]);
        ListDialog.initialize(frame, as, "Choose a bot", "Choose the bot with whom you want to talk.");
        String s = ListDialog.showDialog(null, shell.getCurrentBotID());
        if (s != null)
            shell.switchToBot(s);
    }

    private void showAboutBox() {
        JOptionPane.showMessageDialog(null, ((Object) (HELP_MESSAGE)), "About", 1, aliceLogo);
    }

    public class ConsoleInputStream extends InputStream {

        byte content[];
        private int mark;

        public ConsoleInputStream() {
            content = new byte[0];
            mark = 0;
        }

        public void receive(String s) {
            content = (s + '\n').getBytes();
            mark = 0;
        }

        public int read(byte abyte0[], int i, int j)
                throws IOException {
            while (mark >= content.length)
                try {
                    Thread.sleep(0L);
                } catch (InterruptedException interruptedexception) {
                    return -1;
                }
            if (abyte0 == null)
                throw new NullPointerException();
            if (i < 0 || i > abyte0.length || j < 0 || i + j > abyte0.length || i + j < 0)
                throw new IndexOutOfBoundsException();
            if (j == 0)
                return 0;
            if (content.length == 0)
                return -1;
            int k = 1;
            abyte0[i] = content[mark++];
            for (; k < j && k < content.length; k++)
                abyte0[i + k] = content[mark++];

            return k;
        }

        public int available()
                throws IOException {
            return content.length - mark - 1;
        }

        public boolean markSupported() {
            return false;
        }

        public int read() {
            while (mark >= content.length)
                try {
                    Thread.sleep(0L);
                } catch (InterruptedException interruptedexception) {
                    return -1;
                }
            if (mark < content.length)
                return content[mark++];
            else
                return -1;
        }
    }

    public class ConsolePromptStream extends OutputStream {

        public ConsolePromptStream() {
        }

        public void write(byte abyte0[], int i, int j) {
            inputPanel.setPrompt(new String(abyte0, i, j));
        }

        public void write(int i) {
            inputPanel.setPrompt(String.valueOf((char) i));
        }
    }

    public class ConsoleDisplayStream extends OutputStream {

        private boolean paused;

        public ConsoleDisplayStream() {
            paused = false;
        }

        public void write(byte abyte0[], int i, int j) {
            while (paused)
                try {
                    Thread.sleep(0L);
                } catch (InterruptedException interruptedexception) {
                }
            display.append(new String(abyte0, i, j));
            display.setCaretPosition(display.getText().length());
        }

        public void write(int i) {
            while (paused)
                try {
                    Thread.sleep(0L);
                } catch (InterruptedException interruptedexception) {
                }
            display.append(String.valueOf((char) i));
            display.setCaretPosition(display.getText().length());
        }

        private void togglePause() {
            paused = !paused;
        }
    }

    class InputPanel extends JPanel {
        private JLabel prompt;
        private JTextField input;

        public InputPanel() {
            setLayout(new BoxLayout(this, 0));
            prompt = new JLabel();
            prompt.setFont(new Font("Courier New", 0, 12));
            prompt.setForeground(Color.black);
            prompt.setBackground(Color.white);
            prompt.setHorizontalAlignment(2);
            prompt.setAlignmentY(0.5F);
            input = new JTextField();
            input.setFont(new Font("Courier New", 0, 12));
            input.setForeground(Color.black);
            input.setMinimumSize(new Dimension(50, 20));
            input.setPreferredSize(new Dimension(200, 20));
            input.setMaximumSize(new Dimension(32767, 20));
            input.setHorizontalAlignment(2);
            input.setAlignmentY(0.5F);
            input.addActionListener(new InputSender());
            JButton jbutton = new JButton("Enter");
            jbutton.setFont(new Font("Fixedsys", 0, 10));
            jbutton.setForeground(Color.black);
            jbutton.setMinimumSize(new Dimension(70, 20));
            jbutton.setPreferredSize(new Dimension(70, 20));
            jbutton.setMaximumSize(new Dimension(70, 20));
            jbutton.addActionListener(new InputSender());
            jbutton.setAlignmentY(0.5F);
            add(prompt);
            add(input);
            add(jbutton);
        }

        public void setPrompt(String s) {
            prompt.setText(s);
            prompt.revalidate();
            input.requestFocus();
        }

        private class InputSender
                implements ActionListener {

            private InputSender() {
            }

            public void actionPerformed(ActionEvent actionevent) {
                String s = actionevent.getActionCommand();
                display.append(prompt.getText() + s + SimpleConsole.LINE_SEPARATOR);
                inStream.receive(s);
                input.setText(null);
            }

        }
    }


}
