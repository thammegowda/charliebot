// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting.gui;

import org.alicebot.server.core.targeting.Target;
import org.alicebot.server.core.targeting.TargetingTool;
import org.alicebot.server.core.util.InputNormalizer;
import org.alicebot.server.core.util.Toolkit;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.targeting.gui:
//            TargetingGUI

public class TargetPanel extends JPanel {
    public static Random RNG = new Random();
    JLabel activationsField;
    JLabel countField;
    TargetingGUI guiparent;
    InputBar inputBar;
    JScrollBar inputScroller;
    MatchedBar matchedBar;
    TargetBar targetBar;
    AIMLTextPane templatePane;
    AIMLTextPane replyPane;
    ActionButtonsBar actionButtonsBar;
    private Target selectedTarget;
    private boolean hasTarget;

    public TargetPanel(TargetingGUI targetinggui) {
        selectedTarget = null;
        guiparent = targetinggui;
        setLayout(new BoxLayout(this, 1));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        PatternsPanel patternspanel = new PatternsPanel();
        patternspanel.setAlignmentX(0.0F);
        TemplateAndReplyPanel templateandreplypanel = new TemplateAndReplyPanel();
        templateandreplypanel.setAlignmentX(0.0F);
        actionButtonsBar = new ActionButtonsBar();
        actionButtonsBar.setAlignmentX(0.0F);
        add(patternspanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(templateandreplypanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(actionButtonsBar);
    }

    public void setTarget(Target target) {
        selectedTarget = target;
        int i = target.getActivations();
        activationsField.setText(i + " activations");
        inputBar.setFields(target.getFirstInputText(), target.getFirstInputThat(), target.getFirstInputTopic());
        inputScroller.setMinimum(1);
        inputScroller.setMaximum(i);
        inputScroller.setValue(1);
        showInput(1);
        matchedBar.setFields(target.getMatchPattern(), target.getMatchThat(), target.getMatchTopic());
        targetBar.setFields(target.getFirstExtensionPattern(), target.getFirstExtensionThat(), target.getFirstExtensionTopic());
        replyPane.setText(target.getFirstReply());
        templatePane.setText(target.getMatchTemplate());
    }

    public void nextTarget() {
        Target target = TargetingTool.nextTarget();
        if (target != null) {
            setTarget(target);
            hasTarget = true;
        } else {
            inputBar.setFields("", "", "");
            inputScroller.setMinimum(0);
            inputScroller.setMaximum(0);
            inputScroller.setValue(0);
            matchedBar.setFields("", "", "");
            targetBar.setFields("", "", "");
            templatePane.setText("");
            guiparent.setStatus("No more targets meet your selection criteria.");
            hasTarget = false;
        }
        updateCountDisplay();
    }

    private void showInput(int i) {
        if (i > 0 && selectedTarget != null) {
            inputBar.setFields(selectedTarget.getNthInputText(i - 1), selectedTarget.getNthInputThat(i - 1), selectedTarget.getNthInputTopic(i - 1));
            targetBar.setFields(selectedTarget.getNthExtensionPattern(i - 1), selectedTarget.getNthExtensionThat(i - 1), selectedTarget.getNthExtensionTopic(i - 1));
            replyPane.setText(selectedTarget.getNthReply(i - 1));
        }
    }

    public void scrollToInput(int i) {
        inputScroller.setValue(i);
        showInput(i);
    }

    public boolean hasTarget() {
        return hasTarget;
    }

    public void updateCountDisplay() {
        countField.setText(TargetingTool.countLive() + " live, " + TargetingTool.countSaved() + " saved, " + TargetingTool.countDiscarded() + " discarded");
    }

    public void saveTarget() {
        String s = templatePane.getText().trim();
        if (s.length() == 0) {
            templatePane.setText("Template is empty!");
            return;
        }
        if (selectedTarget != null) {
            selectedTarget.setNewPattern(InputNormalizer.patternFit(targetBar.patternField.getText()));
            selectedTarget.setNewThat(InputNormalizer.patternFit(targetBar.thatField.getText()));
            selectedTarget.setNewTopic(InputNormalizer.patternFit(targetBar.topicField.getText()));
            selectedTarget.setNewTemplate(s);
            TargetingTool.saveCategory(selectedTarget);
            nextTarget();
        }
    }

    class TemplateButtons extends JPanel {

        private JButton random;
        private JButton sr;
        private JButton srai;
        private JButton think;
        private JButton reduce;
        private JButton clear;


        public TemplateButtons() {
            random = new JButton("<random>");
            sr = new JButton("<sr/>");
            srai = new JButton("<srai>");
            think = new JButton("<think>");
            reduce = new JButton("Reduce");
            clear = new JButton("Clear");
            setLayout(new BoxLayout(this, 1));
            random.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    templatePane.setText("<random><li>" + templatePane.getText() + "</li><li></random>");
                }

            });
            think.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    templatePane.setText("<think>" + templatePane.getText() + "</think>");
                }

            });
            sr.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    templatePane.setText(templatePane.getText() + "<sr/>");
                }

            });
            reduce.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    String s = targetBar.patternField.getText();
                    StringTokenizer stringtokenizer = new StringTokenizer(s);
                    int i = stringtokenizer.countTokens();
                    String s1 = "";
                    if (i > 2) {
                        for (int j = 0; j < i - 2; j++)
                            s1 = s1 + stringtokenizer.nextToken() + " ";

                        s1 = s1 + "<star/>";
                        s1 = "<srai>" + s1 + "</srai>";
                    } else {
                        s1 = "<sr/>";
                    }
                    templatePane.setText(templatePane.getText() + s1);
                }

            });
            srai.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    templatePane.setText("<srai>" + templatePane.getText() + "</srai>");
                }

            });
            clear.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionevent) {
                    templatePane.setText("");
                }

            });
            think.setBackground(Color.orange);
            think.setFont(new Font("Fixedsys", 0, 12));
            think.setMinimumSize(new Dimension(120, 30));
            think.setPreferredSize(new Dimension(120, 30));
            think.setMaximumSize(new Dimension(120, 30));
            think.setAlignmentY(0.5F);
            think.setToolTipText("Encloses the current template contents in a <think> tag.");
            random.setBackground(Color.orange);
            random.setFont(new Font("Fixedsys", 0, 12));
            random.setMinimumSize(new Dimension(120, 30));
            random.setPreferredSize(new Dimension(120, 30));
            random.setMaximumSize(new Dimension(120, 30));
            random.setAlignmentY(0.5F);
            random.setToolTipText("Encloses the current template contents in a <random> tag.");
            sr.setBackground(Color.orange);
            sr.setFont(new Font("Fixedsys", 0, 12));
            sr.setMinimumSize(new Dimension(120, 30));
            sr.setPreferredSize(new Dimension(120, 30));
            sr.setMaximumSize(new Dimension(120, 30));
            sr.setAlignmentY(0.5F);
            srai.setBackground(Color.orange);
            srai.setFont(new Font("Fixedsys", 0, 12));
            srai.setMinimumSize(new Dimension(120, 30));
            srai.setPreferredSize(new Dimension(120, 30));
            srai.setMaximumSize(new Dimension(120, 30));
            srai.setAlignmentY(0.5F);
            srai.setToolTipText("Encloses the current template contents in a <srai> tag.");
            reduce.setBackground(Color.orange);
            reduce.setFont(new Font("Fixedsys", 0, 12));
            reduce.setMinimumSize(new Dimension(120, 30));
            reduce.setPreferredSize(new Dimension(120, 30));
            reduce.setMaximumSize(new Dimension(120, 30));
            reduce.setAlignmentY(0.5F);
            clear.setBackground(Color.white);
            clear.setFont(new Font("Fixedsys", 0, 12));
            clear.setMinimumSize(new Dimension(120, 30));
            clear.setPreferredSize(new Dimension(120, 30));
            clear.setMaximumSize(new Dimension(120, 30));
            clear.setAlignmentY(0.5F);
            clear.setToolTipText("Clears the current template contents.");
            add(think);
            add(random);
            add(sr);
            add(srai);
            add(reduce);
            add(clear);
        }
    }

    class SaveTarget
            implements ActionListener {

        SaveTarget() {
        }

        public void actionPerformed(ActionEvent actionevent) {
            saveTarget();
        }
    }

    class DiscardAllTargets
            implements ActionListener {

        DiscardAllTargets() {
        }

        public void actionPerformed(ActionEvent actionevent) {
            TargetingTool.discardAll();
            nextTarget();
        }
    }

    class DiscardTarget
            implements ActionListener {

        DiscardTarget() {
        }

        public void actionPerformed(ActionEvent actionevent) {
            TargetingTool.discard(selectedTarget);
            nextTarget();
        }
    }

    class NextInput
            implements AdjustmentListener {

        NextInput() {
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
            showInput(inputScroller.getValue());
        }
    }

    class NextTarget
            implements ActionListener {

        NextTarget() {
        }

        public void actionPerformed(ActionEvent actionevent) {
            nextTarget();
        }
    }

    class TargetBar extends CategoryBar {

        public TargetBar() {
            super("New category:", "<pattern>", "<that>", "<topic>", 56);
            patternField.setToolTipText("Suggestion for a new <pattern>");
            thatField.setToolTipText("Suggestion for a new <that>");
            topicField.setToolTipText("Suggestion for a new <topic>");
            setEditable(true);
        }
    }

    class MatchedBar extends CategoryBar {

        public MatchedBar() {
            super("Matched:", "<pattern>", "<that>", "<topic>", 56);
            patternField.setToolTipText("The <pattern> that was matched");
            thatField.setToolTipText("The <that> that was matched");
            topicField.setToolTipText("The <topic> that was matched");
            setEditable(false);
        }
    }

    class InputBar extends CategoryBar {

        public InputBar() {
            super("Input:", "text", "<that>", "<topic>", 56);
            patternField.setToolTipText("What the user said");
            thatField.setToolTipText("What the bot had said previously");
            topicField.setToolTipText("The value of <topic> at the time");
            setEditable(false);
        }
    }

    class CategoryBar extends JPanel {
        JTextArea patternField;
        JTextArea thatField;
        JTextArea topicField;

        public CategoryBar(String s, String s1, String s2, String s3, int i) {
            setLayout(new BoxLayout(this, 0));
            JLabel jlabel = new JLabel(s);
            jlabel.setMinimumSize(new Dimension(80, i));
            jlabel.setPreferredSize(new Dimension(80, i));
            jlabel.setMaximumSize(new Dimension(80, i));
            jlabel.setHorizontalAlignment(2);
            jlabel.setFont(new Font("Fixedsys", 0, 12));
            jlabel.setForeground(Color.black);
            jlabel.setAlignmentY(0.5F);
            patternField = new JTextArea();
            patternField.setLineWrap(true);
            patternField.setWrapStyleWord(true);
            patternField.setFont(new Font("Courier New", 0, 12));
            patternField.addKeyListener(new PatternFitter(patternField));
            JScrollPane jscrollpane = new JScrollPane(patternField);
            jscrollpane.setBorder(BorderFactory.createTitledBorder(s1));
            jscrollpane.setMinimumSize(new Dimension(200, i));
            jscrollpane.setPreferredSize(new Dimension(200, i));
            jscrollpane.setMaximumSize(new Dimension(32767, i));
            jscrollpane.setAlignmentY(0.5F);
            thatField = new JTextArea();
            thatField.setFont(new Font("Courier New", 0, 12));
            thatField.setLineWrap(true);
            thatField.setWrapStyleWord(true);
            thatField.addKeyListener(new PatternFitter(thatField));
            JScrollPane jscrollpane1 = new JScrollPane(thatField);
            jscrollpane1.setMinimumSize(new Dimension(150, i));
            jscrollpane1.setPreferredSize(new Dimension(150, i));
            jscrollpane1.setMaximumSize(new Dimension(32767, i));
            jscrollpane1.setBorder(BorderFactory.createTitledBorder(s2));
            jscrollpane1.setAlignmentY(0.5F);
            topicField = new JTextArea();
            topicField.setFont(new Font("Courier New", 0, 12));
            topicField.setLineWrap(true);
            topicField.setWrapStyleWord(true);
            topicField.addKeyListener(new PatternFitter(topicField));
            JScrollPane jscrollpane2 = new JScrollPane(topicField);
            jscrollpane2.setMinimumSize(new Dimension(150, i));
            jscrollpane2.setPreferredSize(new Dimension(150, i));
            jscrollpane2.setMaximumSize(new Dimension(32767, i));
            jscrollpane2.setBorder(BorderFactory.createTitledBorder(s3));
            jscrollpane2.setAlignmentY(0.5F);
            add(jlabel);
            add(jscrollpane);
            add(jscrollpane1);
            add(jscrollpane2);
        }

        public void setEditable(boolean flag) {
            patternField.setEditable(flag);
            thatField.setEditable(flag);
            topicField.setEditable(flag);
        }

        public void setFields(String s, String s1, String s2) {
            patternField.setText(s);
            patternField.setCaretPosition(0);
            thatField.setText(s1);
            thatField.setCaretPosition(0);
            topicField.setText(s2);
            topicField.setCaretPosition(0);
        }

        public class PatternFitter
                implements KeyListener {

            private JTextComponent field;

            public PatternFitter(JTextComponent jtextcomponent) {
                field = jtextcomponent;
            }

            public void keyTyped(KeyEvent keyevent) {
                char c = keyevent.getKeyChar();
                if (c == '*' || c == '_') {
                    int i = field.getCaretPosition();
                    if (i > 0) {
                        char c1 = field.getText().charAt(i - 1);
                        if (c1 != ' ')
                            field.setText(field.getText() + ' ');
                    }
                    return;
                }
                if (!Character.isLetterOrDigit(c) && c != ' ')
                    keyevent.consume();
                if (!Character.isUpperCase(c))
                    keyevent.setKeyChar(Character.toUpperCase(c));
                int j = field.getCaretPosition();
                if (j > 0) {
                    char c2 = field.getText().charAt(j - 1);
                    if (c2 == '*' || c2 == '_')
                        field.setText(field.getText() + ' ');
                }
            }

            public void keyPressed(KeyEvent keyevent) {
            }

            public void keyReleased(KeyEvent keyevent) {
            }
        }
    }

    class ActionButtonsBar extends JPanel {

        public ActionButtonsBar() {
            setLayout(new BoxLayout(this, 0));
            countField = new JLabel();
            countField.setFont(new Font("Fixedsys", 0, 12));
            countField.setForeground(Color.black);
            countField.setHorizontalAlignment(2);
            countField.setAlignmentX(1.0F);
            JButton jbutton = new JButton("Discard Target");
            jbutton.setFont(new Font("Fixedsys", 0, 12));
            jbutton.setBackground(Color.red);
            jbutton.setForeground(Color.white);
            jbutton.addActionListener(new DiscardTarget());
            jbutton.setAlignmentX(1.0F);
            jbutton.setToolTipText("Discards the current target.");
            JButton jbutton1 = new JButton("Discard All Targets");
            jbutton1.setFont(new Font("Fixedsys", 1, 12));
            jbutton1.setBackground(Color.red);
            jbutton1.setForeground(Color.white);
            jbutton1.addActionListener(new DiscardAllTargets());
            jbutton1.setAlignmentX(1.0F);
            jbutton1.setToolTipText("Discards all targets.");
            JButton jbutton2 = new JButton("Save Category");
            jbutton2.setFont(new Font("Fixedsys", 0, 12));
            jbutton2.setBackground(Color.green);
            jbutton2.setForeground(Color.black);
            jbutton2.addActionListener(new SaveTarget());
            jbutton2.setAlignmentX(1.0F);
            jbutton2.setToolTipText("Saves a new category using the information you have entered.");
            JButton jbutton3 = new JButton("Next Target");
            jbutton3.setFont(new Font("Fixedsys", 0, 12));
            jbutton3.setBackground(Color.yellow);
            jbutton3.setForeground(Color.black);
            jbutton3.addActionListener(new NextTarget());
            jbutton3.setAlignmentX(1.0F);
            jbutton2.setToolTipText("Gets the next live target.");
            Dimension dimension = new Dimension(50, 30);
            Dimension dimension1 = new Dimension(50, 30);
            Dimension dimension2 = new Dimension(32767, 30);
            add(countField);
            add(new Box.Filler(dimension, dimension1, dimension2));
            add(jbutton);
            add(jbutton1);
            add(jbutton2);
            add(jbutton3);
        }
    }

    class AIMLTextPane extends JPanel {

        JTextArea textArea;

        public AIMLTextPane(String s) {
            setLayout(new BoxLayout(this, 0));
            textArea = new JTextArea();
            textArea.setFont(new Font("Courier New", 0, 12));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setTabSize(4);
            JScrollPane jscrollpane = new JScrollPane(textArea);
            jscrollpane.setAlignmentY(0.5F);
            jscrollpane.setBorder(BorderFactory.createTitledBorder(s));
            add(jscrollpane);
        }

        public String getText() {
            return textArea.getText();
        }

        public void setText(String s) {
            textArea.setText(Toolkit.formatAIML(s));
            textArea.setCaretPosition(0);
        }
    }

    public class TemplateAndReplyPanel extends JPanel {

        public TemplateAndReplyPanel() {
            setLayout(new BoxLayout(this, 0));
            setPreferredSize(new Dimension(300, 300));
            TemplateButtons templatebuttons = new TemplateButtons();
            templatebuttons.setAlignmentY(0.0F);
            templatePane = new AIMLTextPane("<template>");
            replyPane = new AIMLTextPane("reply");
            JTabbedPane jtabbedpane = new JTabbedPane();
            jtabbedpane.setAlignmentY(0.0F);
            jtabbedpane.setFont(new Font("Fixedsys", 0, 12));
            jtabbedpane.setTabPlacement(3);
            jtabbedpane.add("reply", replyPane);
            jtabbedpane.add("template", templatePane);
            add(templatebuttons);
            add(Box.createRigidArea(new Dimension(10, 0)));
            add(jtabbedpane);
        }
    }

    public class PatternsPanel extends JPanel {

        public PatternsPanel() {
            setLayout(new BoxLayout(this, 1));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            activationsField = new JLabel();
            activationsField.setFont(new Font("Fixedsys", 0, 12));
            activationsField.setForeground(Color.black);
            activationsField.setHorizontalAlignment(2);
            activationsField.setAlignmentX(0.0F);
            activationsField.setMinimumSize(new Dimension(200, 20));
            activationsField.setPreferredSize(new Dimension(200, 20));
            activationsField.setMaximumSize(new Dimension(200, 20));
            inputScroller = new JScrollBar(0, 0, 0, 0, 0);
            inputScroller.setAlignmentX(0.0F);
            inputScroller.setBackground(Color.gray);
            inputScroller.addAdjustmentListener(new NextInput());
            inputBar = new InputBar();
            inputBar.setAlignmentX(0.0F);
            matchedBar = new MatchedBar();
            matchedBar.setAlignmentX(0.0F);
            targetBar = new TargetBar();
            targetBar.setAlignmentX(0.0F);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(activationsField);
            add(inputScroller);
            add(inputBar);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(matchedBar);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(targetBar);
        }
    }


}
