// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.targeting;

import org.alicebot.server.core.util.StringTriple;

public class Category {

    private static final String EMPTY_STRING = "";
    private StringTriple address;
    private String template;

    public Category() {
        address = new StringTriple("", "", "");
    }

    public Category(String s, String s1, String s2, String s3) {
        address = new StringTriple(s, s1, s2);
        template = s3;
    }

    public String getPattern() {
        return address.getFirst();
    }

    public void setPattern(String s) {
        address.setFirst(s);
    }

    public String getThat() {
        return address.getSecond();
    }

    public void setThat(String s) {
        address.setSecond(s);
    }

    public String getTopic() {
        return address.getThird();
    }

    public void setTopic(String s) {
        address.setThird(s);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String s) {
        template = s;
    }
}
