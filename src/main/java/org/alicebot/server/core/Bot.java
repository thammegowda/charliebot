// Decompiled by Jad v1.5.8c. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package org.alicebot.server.core;

import org.alicebot.server.core.logging.XMLLog;
import org.alicebot.server.core.node.Nodemapper;
import org.alicebot.server.core.util.DeveloperError;
import org.alicebot.server.core.util.InputNormalizer;
import org.alicebot.server.core.util.Substituter;
import org.alicebot.server.core.util.XMLResourceSpec;

import java.io.File;
import java.util.*;

// Referenced classes of package org.alicebot.server.core:
//            PredicateInfo, Globals

public class Bot {

    private static final String EMPTY_STRING = "";
    protected String PREDICATE_EMPTY_DEFAULT;
    protected XMLResourceSpec chatlogSpec;
    private String id;
    private HashMap loadedFiles;
    private HashMap properties;
    private HashMap predicatesInfo;
    private HashMap inputSubstitutions;
    private HashMap personSubstitutions;
    private HashMap person2Substitutions;
    private HashMap genderSubstitutions;
    private ArrayList sentenceSplitters;
    private Map predicateCache;

    public Bot(String s) {
        loadedFiles = new HashMap();
        properties = new HashMap();
        predicatesInfo = new HashMap();
        inputSubstitutions = new HashMap();
        personSubstitutions = new HashMap();
        person2Substitutions = new HashMap();
        genderSubstitutions = new HashMap();
        sentenceSplitters = new ArrayList();
        predicateCache = Collections.synchronizedMap(new HashMap());
        PREDICATE_EMPTY_DEFAULT = Globals.getPredicateEmptyDefault();
        id = s;
        chatlogSpec = XMLLog.getChatlogSpecClone();
        chatlogSpec.path = Globals.getProperty("programd.logging.xml.chat.log-directory", "./logs") + File.separator + s + File.separator + "chat.xml";
    }

    public String getID() {
        return id;
    }

    public HashMap getLoadedFilesMap() {
        return loadedFiles;
    }

    public void addToFilenameMap(String s, Nodemapper nodemapper) {
        HashSet hashset = (HashSet) loadedFiles.get(s);
        if (hashset != null)
            hashset.add(nodemapper);
    }

    public String getPropertyValue(String s) {
        if (s.equals(""))
            return PREDICATE_EMPTY_DEFAULT;
        String s1 = (String) properties.get(s);
        if (s1 != null)
            return s1;
        else
            return PREDICATE_EMPTY_DEFAULT;
    }

    public void setPropertyValue(String s, String s1) {
        if (s.equals("")) {
            return;
        } else {
            properties.put(s, new String(s1));
            return;
        }
    }

    public void addPredicateInfo(String s, String s1, boolean flag) {
        PredicateInfo predicateinfo = new PredicateInfo();
        predicateinfo.name = s;
        predicateinfo.defaultValue = s1;
        predicateinfo.returnNameWhenSet = flag;
        predicatesInfo.put(s, predicateinfo);
    }

    public HashMap getPredicatesInfo() {
        return predicatesInfo;
    }

    public Map getPredicateCache() {
        return predicateCache;
    }

    public Map predicatesFor(String s) {
        Map map;
        if (!predicateCache.containsKey(s)) {
            map = Collections.synchronizedMap(new HashMap());
            predicateCache.put(s, map);
        } else {
            map = (Map) predicateCache.get(s);
            if (map == null)
                throw new DeveloperError("userPredicates is null.");
        }
        return map;
    }

    public void addInputSubstitution(String s, String s1) {
        addSubstitution(inputSubstitutions, s, s1);
    }

    public void addGenderSubstitution(String s, String s1) {
        addSubstitution(genderSubstitutions, s, s1);
    }

    public void addPersonSubstitution(String s, String s1) {
        addSubstitution(personSubstitutions, s, s1);
    }

    public void addPerson2Substitution(String s, String s1) {
        addSubstitution(person2Substitutions, s, s1);
    }

    private void addSubstitution(HashMap hashmap, String s, String s1) {
        if (s != null && s1 != null)
            hashmap.put(s.toUpperCase(), s1);
    }

    public void addSentenceSplitter(String s) {
        if (s != null)
            sentenceSplitters.add(s);
    }

    public HashMap getInputSubstitutionsMap() {
        return inputSubstitutions;
    }

    public HashMap getGenderSubstitutionsMap() {
        return genderSubstitutions;
    }

    public HashMap getPersonSubstitutionsMap() {
        return personSubstitutions;
    }

    public HashMap getPerson2SubstitutionsMap() {
        return person2Substitutions;
    }

    public ArrayList getSentenceSplitters() {
        return sentenceSplitters;
    }

    public ArrayList sentenceSplit(String s) {
        return InputNormalizer.sentenceSplit(sentenceSplitters, s);
    }

    public String applyInputSubstitutions(String s) {
        return Substituter.applySubstitutions(inputSubstitutions, s);
    }

    public XMLResourceSpec getChatlogSpec() {
        return chatlogSpec;
    }
}
