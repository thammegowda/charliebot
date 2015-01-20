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
    - complete javadoc
    - made all imports explicit
    - inlined method calls to avoid temporary variable
*/

/*
    Further optimizations {4.1.3 [01] - November 2001, Noel Bush)
    - changed to extend (not implement) AIMLProcessor (latter is now an abstract class)
      (includes necessary public field "label")
*/

/*
    More fixes (4.1.3 [02] - November 2001, Noel Bush)
    - added match trace indicator
    - changed to use getInternalResponse() method of ActiveMultiplexor
*/

package org.alicebot.server.core.processor;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.Multiplexor;
import org.alicebot.server.core.parser.TemplateParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Trace;


/**
 * Implements the &lt;srai/&gt; element.
 *
 * @author Jon Baer
 * @author Thomas Ringate, Pedro Colla
 * @version 4.1.3
 */
public class SRAIProcessor extends AIMLProcessor {
    public static final String label = "srai";


    /**
     * Processes a &lt;srai/&gt; element.
     * <p/>
     * First, all elements contained within a given &lt;srai/&gt; are evaluated,
     * and the result is recursively fed as input to the pattern matching
     * process. The result of such evaluation (which itself might be recursive)
     * is returned as the result.
     *
     * @see org.alicebot.server.core.processor.AIMLProcessor#process
     */
    public String process(int level, XMLNode tag, TemplateParser parser) throws AIMLProcessorException {
        String result = new String();

        if (tag.XMLType == XMLNode.TAG) {
            // Check for infinite loops.
            if (tag.XMLChild.size() == 1) {
                XMLNode sraiChild = (XMLNode) tag.XMLChild.get(0);

                if (sraiChild.XMLType == XMLNode.DATA) {
                    String sraiContent = sraiChild.XMLData;
                    //adjamir
                    /*Iterator inputsIterator = parser.getInputs().iterator();

                    while (inputsIterator.hasNext())
                    {
                        String input = (String)inputsIterator.next();

                        if (sraiContent.equalsIgnoreCase(input))
                        {
                            if (!sraiContent.equalsIgnoreCase(Globals.getInfiniteLoopInput()))
                            {
                                sraiChild.XMLData = Globals.getInfiniteLoopInput();
                                Log.userinfo("Infinite loop detected; substituting \"" + Globals.getInfiniteLoopInput() + "\".", Log.RUNTIME);
                            }
                            else
                            {
                                Log.userinfo("Unrecoverable infinite loop.", Log.RUNTIME);
                                return EMPTY_STRING;
                            }
                        }
                    }
                    */
                    parser.addInput(sraiContent);
                }
            }
            if (Globals.showMatchTrace()) {
                Trace.userinfo("Symbolic Reduction:");
            }
            parser.push();
            result = Multiplexor.getInternalResponse(parser.evaluate(level++, tag.XMLChild),
                    parser.getUserID(), parser.getBotID(),
                    parser);
            parser.pop();
            return result;
        } else {
            throw new AIMLProcessorException("<srai></srai> must have content!");
        }
    }
}
