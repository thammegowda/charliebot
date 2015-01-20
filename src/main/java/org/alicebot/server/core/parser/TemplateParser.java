/*
    Alicebot Program D
    Copyright (C) 1995-2001, A.L.I.C.E. AI Foundation
    
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.
    
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, 
    USA.
*/

/*
    Code cleanup (4.1.3 [00] - October 2001, Noel Bush)
    - formatting cleanup
    - general grammar fixes
    - complete javadoc
    - made all imports explicit
    - replaced String literals with constants (except for 0.9 bot predicates)
    - removed unused depth, length and interpreter fields and deprecated constructors using them
    - removed extraneous "extends Object" :-)
    - removed use of IdProcessor and DateProcessor (although they might return later)
    - renamed variables with more descriptive names
    - removed unused global fields (globals, classifier, graphmaster, bot)
    - changed tests like if(someVariable == true) to if(someVariable)
    - removed non-AIML1.0.1 support for <get></get> (only atomic form is supported)
    - inserted commented-out support for AIML 1.0.1 <gender/>
    - changed name of virtualtag() to shortcutTag()
    - changed formattag() to formatTag()
    - inlined some unnecessary variables that were created only for debugging
    - changed to use of ActiveMultiplexor
    - added getValid2dIndex method
    - added getValid1dIndex method
    - changed method names "countnode" to "nodeCount", "getnode" to "getNode"
    - made INPUT_STAR(S), THAT_STAR(S) and TOPIC_STAR(S) private and created get methods
*/

/*
    Further optimization (4.1.3 [01] - November 2001, Noel Bush
    - introduced pluggable processor usage
    - removed constructor that took Interpreter argument
    - moved processListItem to ConditionProcessor
    - moved processing of deprecated tags to DeprecatedAIMLParser and made optional
      (via setting in Globals)
    - changed to extend GenericParser and moved the following methods there:
      - getArg (renamed to getAttributeValue)
      - formatTag
      - nodeCount
      - getNode
      - processTag
      - processResponse
    - entirely removed constructor that takes depth as a parameter
*/

package org.alicebot.server.core.parser;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.logging.Log;
import org.alicebot.server.core.processor.AIMLProcessorException;
import org.alicebot.server.core.processor.AIMLProcessorRegistry;
import org.alicebot.server.core.processor.ProcessorException;

import java.util.ArrayList;
import java.util.Stack;


/**
 * <code>TemplateParser</code> is still a primitive class, implementing not a
 * &quot;real&quot; XML parser, but just enough (hopefully) to get the job
 * done.
 */
public class TemplateParser extends GenericParser {
    /**
     * The stack of inputStars Vector that will allow correct use of <srai>
     */
    private Stack stackStar = new Stack();

    /**
     * The values captured from the input by wildcards in the
     * <code>pattern</code>.
     */
    private ArrayList inputStars = new ArrayList();

    /**
     * The values captured from the input path by wildcards in the
     * <code>that</code>.
     */
    private ArrayList thatStars = new ArrayList();

    /**
     * The values captured from the input path by wildcards in the
     * <code>topic</code>.
     */
    private ArrayList topicStars = new ArrayList();

    /**
     * The input that matched the <code>pattern</code> associated with this
     * template (helps to avoid endless loops).
     */
    private ArrayList inputs = new ArrayList();

    /**
     * The userid for which this parser is used.
     */
    private String userid;

    /**
     * The botid on whose behalf this parser is working.
     */
    private String botid;


    /**
     * Initializes an <code>TemplateParser</code>. The <code>input</code> is a
     * required parameter!
     *
     * @param input the input that matched the <code>pattern</code> associated
     *              with this template (helps to avoid endless loops)
     *
     * @throws TemplateParserException if the <code>input</code> is null
     */
    public TemplateParser(String input, String userid, String botid) throws TemplateParserException {
        if (input == null) {
            throw new TemplateParserException("No input supplied for TemplateParser!");
        }
        this.inputs.add(input);
        this.userid = userid;
        this.botid = botid;
        super.processorRegistry = AIMLProcessorRegistry.getSelf();
    }


    /**
     * Processes the AIML within and including a given AIML element.
     *
     * @param level the current level in the XML trie
     * @param tag   the tag being evaluated
     *
     * @return the result of processing the tag
     *
     * @throws AIMLProcessorException if the AIML cannot be processed
     */
    public String processTag(int level, XMLNode tag) throws ProcessorException {
        try {
            return super.processTag(level, tag);
        }
        // A ProcessorException at this point can mean several things.
        catch (ProcessorException e0) {
            // It could be a deprecated tag.
            if (Globals.supportDeprecatedTags()) {
                try {
                    return DeprecatedAIMLParser.processTag(level, userid, tag, this);
                } catch (UnknownDeprecatedAIMLException e1) {
                    // For now, do nothing (drop down to next).
                }
            }
            // It could also be a non-AIML tag.
            if (Globals.nonAIMLRequireNamespaceQualification()) {
                // If namespace qualification is required, check for a colon.
                if (tag.XMLData.indexOf(COLON) == -1) {
                    throw new AIMLProcessorException("Unknown element \"" + tag.XMLData + "\"");
                }
            }
            // But if namespace qualification is not required, don't care.
            return formatTag(level, tag);
        } catch (StackOverflowError e) {
            Log.userinfo("Stack overflow error processing " + tag.XMLData + " tag.", Log.ERROR);
            return EMPTY_STRING;
        }
    }


    /**
     * Adds an input to the inputs list (for avoiding infinite loops).
     *
     * @param input the input to add
     */
    public void addInput(String input) {
        this.inputs.add(input);
    }


    /**
     * Returns the input that matched the <code>pattern</code> associated with
     * this template.
     *
     * @return the input that matched the <code>pattern</code> associated with
     * this template
     */
    public ArrayList getInputs() {
        return this.inputs;
    }


    /**
     * Returns the values captured from the input path by wildcards in the
     * <code>pattern</code>.
     *
     * @return the values captured from the input path by wildcards in the
     * <code>pattern</code>
     */
    public ArrayList getInputStars() {
        return this.inputStars;
    }

    /**
     * Sets the <code>inputStars</code> list.
     *
     * @param values captured from the input path by wildcards in the
     *               <code>pattern</code>
     */
    public void setInputStars(ArrayList stars) {
        this.inputStars = stars;
    }

    /**
     * Returns the the values captured from the input path by wildcards in the
     * <code>that</code>.
     *
     * @return the values captured from the input path by wildcards in the
     * <code>that</code>
     */
    public ArrayList getThatStars() {
        return this.thatStars;
    }

    /**
     * Sets the <code>thatStars</code> list.
     *
     * @param values captured from the input path by wildcards in the
     *               <code>that</code>
     */
    public void setThatStars(ArrayList stars) {
        this.thatStars = stars;
    }

    /**
     * Returns the values captured from the input path by wildcards in the
     * <code>topic name</code>.
     *
     * @return the values captured from the input path by wildcards in the
     * <code>topic name</code>
     */
    public ArrayList getTopicStars() {
        return this.topicStars;
    }

    /**
     * Sets the <code>topicStars</code> Vector.
     *
     * @param values captured from the input path by wildcards in the
     *               <code>topic name</code>
     */
    public void setTopicStars(ArrayList stars) {
        this.topicStars = stars;
    }

    /**
     * Push <code>inputStars</code> Vector
     */
    public void push() {
        this.stackStar.push(this.inputStars);
    }

    /**
     * Pop <code>inputStars</code> Vector
     */
    public void pop() {
        setInputStars((ArrayList) this.stackStar.pop());
    }

    /**
     * Returns the userid.
     *
     * @return the userid
     */
    public String getUserID() {
        return this.userid;
    }


    /**
     * Returns the botid.
     *
     * @return the botid
     */
    public String getBotID() {
        return this.botid;
    }
}
