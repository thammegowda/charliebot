// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.InputNormalizer;
import org.alicebot.server.core.util.StringTriple;
import org.alicebot.server.core.util.StringTripleMatrix;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

// Referenced classes of package org.alicebot.server.core.targeting:
//            TargetExtensions, Category, TargetInputs, CannotExtendException

public class Target {

    private Category match;
    private TargetInputs inputs;
    private TargetExtensions extensions;
    private LinkedList replies;
    private Category newCategory;
    private int activations;

    public Target(String s, String s1, String s2, String s3, String s4, String s5, String s6,
                  String s7) {
        extensions = new TargetExtensions();
        replies = new LinkedList();
        newCategory = new Category();
        activations = 1;
        match = new Category(s, s1, s2, s3);
        inputs = new TargetInputs(s4, s5, s6);
        replies.add(s7);
    }

    public static int generateHashCode(String s, String s1, String s2) {
        return (s + s1 + s2).hashCode();
    }

    private static String extend(String s, String s1)
            throws CannotExtendException {
        if (s.indexOf('*') == -1 && s.indexOf('_') == -1)
            throw new CannotExtendException();
        StringTokenizer stringtokenizer = new StringTokenizer(s);
        StringTokenizer stringtokenizer1 = new StringTokenizer(s1);
        int i = stringtokenizer.countTokens();
        int j = stringtokenizer1.countTokens();
        if (i > j)
            i = j;
        StringBuffer stringbuffer = new StringBuffer();
        boolean flag = false;
        for (int k = 0; k < i && !flag; k++) {
            String s2 = stringtokenizer.nextToken();
            if (s2.equals("*") || s2.equals("_"))
                flag = true;
            stringbuffer.append(stringtokenizer1.nextToken());
            stringbuffer.append(' ');
        }

        if (stringtokenizer1.hasMoreTokens())
            stringbuffer.append("*");
        return stringbuffer.toString();
    }

    public int hashCode() {
        return (match.getPattern() + match.getThat() + match.getTopic()).hashCode();
    }

    public void merge(Target target) {
        if (hashCode() != target.hashCode())
            throw new DeveloperError("Targets with non-matching <match> segments cannot be merged!");
        Iterator iterator = target.getInputs().iterator();
        Iterator iterator1 = target.getReplies().iterator();
        Iterator iterator2 = target.getExtensions().iterator();
        while (iterator.hasNext()) {
            StringTriple stringtriple = (StringTriple) iterator.next();
            StringTriple stringtriple1 = null;
            try {
                stringtriple1 = (StringTriple) iterator2.next();
            } catch (NoSuchElementException nosuchelementexception) {
            }
            String s = (String) iterator1.next();
            if (!inputs.contains(stringtriple)) {
                inputs.add(stringtriple);
                replies.add(s);
                if (stringtriple1 != null)
                    extensions.add(stringtriple1);
            }
        }
        if (inputs.size() != replies.size()) {
            throw new DeveloperError("Merge operation failed to maintain stable activation count.");
        } else {
            activations = inputs.size();
            return;
        }
    }

    public String getMatchPattern() {
        return match.getPattern();
    }

    public String getMatchThat() {
        return match.getThat();
    }

    public String getMatchTopic() {
        return match.getTopic();
    }

    public String getMatchTemplate() {
        return match.getTemplate();
    }

    public StringTripleMatrix getInputs() {
        return inputs;
    }

    public LinkedList getInputTexts() {
        return inputs.getTexts();
    }

    public LinkedList getInputThats() {
        return inputs.getThats();
    }

    public LinkedList getInputTopics() {
        return inputs.getTopics();
    }

    public String getFirstInputText() {
        return (String) inputs.getTexts().getFirst();
    }

    public String getFirstInputThat() {
        return (String) inputs.getThats().getFirst();
    }

    public String getFirstInputTopic() {
        return (String) inputs.getTopics().getFirst();
    }

    public String getLastInputText() {
        return (String) inputs.getTexts().getLast();
    }

    public String getLastInputThat() {
        return (String) inputs.getThats().getLast();
    }

    public String getLastInputTopic() {
        return (String) inputs.getTopics().getLast();
    }

    public String getNthInputText(int i) {
        return (String) inputs.getTexts().get(i);
    }

    public String getNthInputThat(int i) {
        return (String) inputs.getThats().get(i);
    }

    public String getNthInputTopic(int i) {
        return (String) inputs.getTopics().get(i);
    }

    public StringTripleMatrix getExtensions() {
        return extensions;
    }

    public LinkedList getExtensionPatterns() {
        return extensions.getPatterns();
    }

    public LinkedList getExtensionThats() {
        return extensions.getThats();
    }

    public LinkedList getExtensionTopics() {
        return extensions.getTopics();
    }

    public String getFirstExtensionPattern() {
        return (String) extensions.getPatterns().getFirst();
    }

    public String getFirstExtensionThat() {
        return (String) extensions.getThats().getFirst();
    }

    public String getFirstExtensionTopic() {
        return (String) extensions.getTopics().getFirst();
    }

    public String getLastExtensionPattern() {
        return (String) extensions.getPatterns().getLast();
    }

    public String getLastExtensionThat() {
        return (String) extensions.getThats().getLast();
    }

    public String getLastExtensionTopic() {
        return (String) extensions.getTopics().getLast();
    }

    public String getNthExtensionPattern(int i) {
        extend(i);
        return (String) extensions.getPatterns().get(i);
    }

    public String getNthExtensionThat(int i) {
        extend(i);
        return (String) extensions.getThats().get(i);
    }

    public String getNthExtensionTopic(int i) {
        extend(i);
        return (String) extensions.getTopics().get(i);
    }

    public LinkedList getReplies() {
        return replies;
    }

    public String getFirstReply() {
        return (String) replies.getFirst();
    }

    public String getLastReply() {
        return (String) replies.getLast();
    }

    public String getNthReply(int i) {
        return (String) replies.get(i);
    }

    public String getNewPattern() {
        return newCategory.getPattern();
    }

    public void setNewPattern(String s) {
        newCategory.setPattern(s);
    }

    public String getNewThat() {
        return newCategory.getThat();
    }

    public void setNewThat(String s) {
        newCategory.setThat(s);
    }

    public String getNewTopic() {
        return newCategory.getTopic();
    }

    public void setNewTopic(String s) {
        newCategory.setTopic(s);
    }

    public String getNewTemplate() {
        return newCategory.getTemplate();
    }

    public void setNewTemplate(String s) {
        newCategory.setTemplate(s);
    }

    public int getActivations() {
        return activations;
    }

    public void extend() {
        for (int i = activations; --i >= 0; )
            extend(i);

    }

    private void extend(int i) {
        extensions.ensureSize(i + 1);
        String s = getNthInputText(i);
        String s1 = getNthInputThat(i);
        String s2 = getNthInputTopic(i);
        String s3;
        String s4;
        String s5;
        try {
            s3 = InputNormalizer.patternFit(extend(match.getPattern(), s));
            s4 = InputNormalizer.patternFit(match.getThat());
            s5 = InputNormalizer.patternFit(match.getTopic());
        } catch (CannotExtendException cannotextendexception) {
            s3 = InputNormalizer.patternFit(match.getPattern());
            try {
                s4 = InputNormalizer.patternFit(extend(match.getThat(), s1));
                s5 = InputNormalizer.patternFit(match.getTopic());
            } catch (CannotExtendException cannotextendexception1) {
                s4 = InputNormalizer.patternFit(match.getThat());
                try {
                    s5 = InputNormalizer.patternFit(extend(match.getTopic(), s2));
                } catch (CannotExtendException cannotextendexception2) {
                    return;
                }
            }
        }
        extensions.getFirsts().set(i, s3);
        extensions.getSeconds().set(i, s4);
        extensions.getThirds().set(i, s5);
    }
}
