// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core.parser;

import org.alicebot.server.core.util.Toolkit;

// Referenced classes of package org.alicebot.server.core.parser:
//            UnknownDeprecatedAIMLException, XMLNode, TemplateParser

public class DeprecatedAIMLParser {

    public static final String QUOTE_MARK = "\"";
    public static final String ALICE = "alice";
    public static final String NAME = "name";
    public static final String JUSTBEFORETHAT = "justbeforethat";
    public static final String JUSTTHAT = "justthat";
    public static final String BEFORETHAT = "beforethat";
    public static final String GET_OLD = "get_";
    public static final String GET_IP = "get_ip";
    public static final String GETNAME = "getname";
    public static final String GETSIZE = "getsize";
    public static final String GETTOPIC = "gettopic";
    public static final String GETVERSION = "getversion";
    public static final String LOAD = "load";
    public static final String SETTOPIC = "settopic";
    public static final String SETNAME = "setname";
    public static final String SET_OLD = "set_";
    public static final String BIRTHDAY = "birthday";
    public static final String BIRTHPLACE = "birthplace";
    public static final String BOYFRIEND = "boyfriend";
    public static final String FAVORITEBAND = "favoriteband";
    public static final String FAVORITEBOOK = "favoritebook";
    public static final String FAVORITECOLOR = "favoritecolor";
    public static final String FAVORITEFOOD = "favoritefood";
    public static final String FAVORITEMOVIE = "favoritemovie";
    public static final String FAVORITESONG = "favoritesong";
    public static final String FOR_FUN = "for_fun";
    public static final String FRIENDS = "friends";
    public static final String GENDER = "gender";
    public static final String GIRLFRIEND = "girlfriend";
    public static final String KIND_MUSIC = "kind_music";
    public static final String LOCATION = "location";
    public static final String LOOK_LIKE = "look_like";
    public static final String BOTMASTER = "botmaster";
    public static final String QUESTION = "question";
    public static final String SIGN = "sign";
    public static final String TALK_ABOUT = "talk_about";
    public static final String WEAR = "wear";
    private static final String EMPTY_STRING = "";
    private static final String FILENAME = "filename";
    private static final String ATTR_NAME_NAME = "name=\"name\"";
    private static final String ATTR_INDEX_2_1 = "index=\"2,1\"";
    private static final String ATTR_INDEX_2 = "index=\"2\"";
    private static final String ATTR_INDEX_3 = "index=\"3\"";
    private static final String ATTR_NAME_TOPIC = "name=\"topic\"";
    private static final String NAME_EQUALS_QUOTE = "name=\"";
    private static final String UNDERSCORE = "_";
    public DeprecatedAIMLParser() {
    }

    public static String processTag(int i, String s, XMLNode xmlnode, TemplateParser templateparser)
            throws UnknownDeprecatedAIMLException {
        if (xmlnode.XMLData.equals("load") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "learn", 0, "", Toolkit.getAttributeValue("filename", xmlnode.XMLAttr), 2);
        if (xmlnode.XMLData.equals("name") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"name\"", "", 1);
        if (xmlnode.XMLData.equals("justbeforethat") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "that", 1, "index=\"2,1\"", "", 1);
        if (xmlnode.XMLData.equals("justthat") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "input", 1, "index=\"2\"", "", 1);
        if (xmlnode.XMLData.equals("beforethat") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "input", 1, "index=\"3\"", "", 1);
        if (xmlnode.XMLData.equals("getname") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "get", 1, "name=\"name\"", "", 1);
        if (xmlnode.XMLData.equals("getsize") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "size", 1, "", "", 1);
        if (xmlnode.XMLData.equals("gettopic") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "get", 1, "name=\"topic\"", "", 1);
        if (xmlnode.XMLData.equals("getversion") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "version", 1, "", "", 1);
        if (xmlnode.XMLData.equals("get_ip") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "id", 1, "", "", 1);
        if (xmlnode.XMLData.equals("settopic") && xmlnode.XMLType == 0)
            return templateparser.shortcutTag(i, "set", 0, "name=\"topic\"", templateparser.evaluate(i, xmlnode.XMLChild), 2);
        if (xmlnode.XMLData.equals("setname") && xmlnode.XMLType == 0)
            return templateparser.shortcutTag(i, "set", 0, "name=\"name\"", templateparser.evaluate(i, xmlnode.XMLChild), 2);
        if (xmlnode.XMLData.indexOf("set_", 0) >= 0 && xmlnode.XMLType == 0)
            return templateparser.shortcutTag(i, "set", 0, "name=\"" + xmlnode.XMLData.substring(xmlnode.XMLData.indexOf("_", 0) + 1, xmlnode.XMLData.length()) + "\"", templateparser.evaluate(i, xmlnode.XMLChild), 2);
        if (xmlnode.XMLData.indexOf("get_", 0) >= 0 && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "get", 1, "name=\"" + xmlnode.XMLData.substring(xmlnode.XMLData.indexOf("_", 0) + 1, xmlnode.XMLData.length()) + "\"", "", 2);
        if (xmlnode.XMLData.equals("birthday") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"birthday\"", "", 1);
        if (xmlnode.XMLData.equals("birthplace") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"birthplace\"", "", 1);
        if (xmlnode.XMLData.equals("boyfriend") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"boyfriend\"", "", 1);
        if (xmlnode.XMLData.equals("favoriteband") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoriteband\"", "", 1);
        if (xmlnode.XMLData.equals("favoritebook") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoritebook\"", "", 1);
        if (xmlnode.XMLData.equals("favoritecolor") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoritecolor\"", "", 1);
        if (xmlnode.XMLData.equals("favoritefood") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoritefood\"", "", 1);
        if (xmlnode.XMLData.equals("favoritemovie") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoritemovie\"", "", 1);
        if (xmlnode.XMLData.equals("favoritesong") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"favoritesong\"", "", 1);
        if (xmlnode.XMLData.equals("for_fun") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"forfun\"", "", 1);
        if (xmlnode.XMLData.equals("friends") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"friends\"", "", 1);
        if (xmlnode.XMLData.equals("gender") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"gender\"", "", 1);
        if (xmlnode.XMLData.equals("girlfriend") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"girlfriend\"", "", 1);
        if (xmlnode.XMLData.equals("kind_music") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"kindmusic\"", "", 1);
        if (xmlnode.XMLData.equals("location") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"location\"", "", 1);
        if (xmlnode.XMLData.equals("look_like") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"looklike\"", "", 1);
        if (xmlnode.XMLData.equals("botmaster") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"master\"", "", 1);
        if (xmlnode.XMLData.equals("question") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"question\"", "", 1);
        if (xmlnode.XMLData.equals("sign") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"sign\"", "", 1);
        if (xmlnode.XMLData.equals("talk_about") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"talkabout\"", "", 1);
        if (xmlnode.XMLData.equals("wear") && xmlnode.XMLType == 1)
            return templateparser.shortcutTag(i, "bot", 1, "name=\"wear\"", "", 1);
        else
            throw new UnknownDeprecatedAIMLException();
    }
}
