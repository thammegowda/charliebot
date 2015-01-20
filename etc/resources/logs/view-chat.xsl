<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"
                indent="yes"
                omit-xml-declaration="no"
                cdata-section-elements="input"/>
    <!--This is the connect string that will be used to group 'sessions'.-->
    <xsl:variable name="connect-string">CONNECT</xsl:variable>
    <xsl:template match="/">
        <html>
            <head>
                <title>Chat Log</title>
                <style type="text/css">
                    *
                    {
                        font-family: Tahoma, Helvetica, sans-serif;
                        font-size: 12px;
                        font-weight: normal;
                        text-align: left;
                    }
                    h1
                    {
                        font-size: 24px;
                        font-weight: bold;
                        margin-top: 20px;
                        margin-bottom: 10px;
                    }
                    h2
                    {
                        font-size: 14px;
                        font-weight: bold;
                        margin-top: 3px;
                        margin-bottom: 3px;
                        margin-left: 3px;
                    }
                    ul
                    {
                        margin-top: 3px;
                        margin-bottom: 3px;
                    }
                    p.conversation-label
                    {
                        background: #dddddd;
                        cursor: move;
                        margin-bottom: 0px;
                    }
                    span.userid-value
                    {
                        color: #2222aa;
                    }
                    span.name-value
                    {
                        font-size: 13px;
                        font-weight: bold;
                        color: #dd5500;
                    }
                    p.connect-timestamp
                    {
                        font-family: "Courier New", Courier;
                        font-size: 13px;
                        font-weight: bold;
                        color: #222222;
                        cursor: move;
                        margin-top: 18px;
                    }
                    p.firstresponse
                    {
                        margin-top: 18px;
                        font-weight: bold;
                        color: #0000aa;
                    }
                    p.timestamp
                    {
                        font-family: "Courier New", Courier;
                        margin-top: 12px;
                        margin-left: 30px;
                        color: #222222;
                    }
                    p.input
                    {
                        margin-top: 12px;
                        font-weight: bold;
                        color: #aa0000;
                    }
                    span.clientname
                    {
                        margin-left: 0px;
                        margin-right: 0px;
                        font-weight: bold;
                        color: #dd5500;
                    }
                    p.response
                    {
                        margin-top: 3px;
                        font-weight: bold;
                        color: #0000aa;
                    }
                    span.botid
                    {
                        margin-left: 0px;
                        margin-right: 0px;
                        font-weight: bold;
                        color: #0055dd;
                    }
                    span.nodeMark
                    {
                        font-family: "Courier New", Courier;
                    }
                </style>
                <script language="JavaScript">
                    //<![CDATA[
                        function toggleConversationView(conversationid)
                        {
                            if(document.all["exchanges-" + conversationid].style.display == "none")
                            {
                                document.all["exchanges-" + conversationid].style.display = "block";
                                document.all["conversationNodeMark-" + conversationid].innerText = "-";
                            }
                            else
                            {
                                document.all["exchanges-" + conversationid].style.display = "none";
                                document.all["conversationNodeMark-" + conversationid].innerText = "+";
                            }
                        }
                        function toggleSession(sessionid)
                        {
                            if(document.all["session-" + sessionid].style.display == "none")
                            {
                                document.all["session-" + sessionid].style.display = "block";
                                document.all["firstresponse-" + sessionid].style.display = "block";
                                document.all["sessionNodeMark-" + sessionid].innerText = "-";
                            }
                            else
                            {
                                document.all["session-" + sessionid].style.display = "none";
                                document.all["firstresponse-" + sessionid].style.display = "none";
                                document.all["sessionNodeMark-" + sessionid].innerText = "+";
                            }
                        }
                    //]]>
                </script>
            </head>
            <body>
                <h1>
                    Conversation Logs
                </h1>
                <h2>
                    Statistics:
                </h2>
                <ul>
                    <li>
                        <xsl:value-of select="count(exchanges/exchange)"/> exchanges
                    </li>
                    <li>
                        <xsl:variable name="usercount" select="count(//userid[not(.=preceding::userid)])"/>
                        <xsl:value-of select="$usercount"/> user<xsl:if test="$usercount!=1">s</xsl:if>
                    </li>
                    <xsl:if test="exchanges/@starttime">
                        <li>
                            starts: <xsl:value-of select="exchanges/@starttime"/>
                        </li>
                    </xsl:if>
                    <xsl:if test="exchanges/@backlink">
                        <li>
                            previous:
                                <xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="exchanges/@backlink"/></xsl:attribute>
                                    <xsl:value-of select="exchanges/@backlink"/>
                                </xsl:element>
                        </li>
                    </xsl:if>
                </ul>
                <!--Build a conversation for each userid, sorting by reverse time order.-->
                <xsl:for-each select="//userid[not(.=preceding::userid)]">
                    <xsl:sort select="timestamp" order="descending"/>
                    <xsl:apply-templates select="."/>
                </xsl:for-each>
                <!--Print a link to previous files at the bottom as well.-->
                <xsl:if test="exchanges/@backlink">
                    <p>
                        previous:
                            <xsl:element name="a"><xsl:attribute name="href"><xsl:value-of select="exchanges/@backlink"/></xsl:attribute>
                                <xsl:value-of select="exchanges/@backlink"/>
                            </xsl:element>
                    </p>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
    <!--Creates a conversation block for a userid.-->
    <xsl:template match="userid">
        <!--Remember the userid.-->
        <xsl:variable name="userid" select="."/>
        <!--Get all exchanges for this userid.-->
        <xsl:variable name="user-exchanges" select="//exchange[userid=$userid]"/>
        <!--Generate a unique id for the conversation with this user.-->
        <xsl:variable name="conversation-id" select="generate-id($userid)"/>
        <!--Count the exchanges.-->
        <xsl:variable name="exchange-count" select="count($user-exchanges)"/>
        <!--Count the sessions.-->
        <xsl:variable name="session-count" select="count($user-exchanges[input=$connect-string])"/>
        <!--Only show exchanges if exchange-count is greater than session count.-->
        <xsl:if test="$exchange-count > $session-count">
            <!--Create the conversation label, marking it using the generated id.-->
            <p class="conversation-label">
                <xsl:attribute name="id">conversationlabel-<xsl:value-of select="$conversation-id"/></xsl:attribute>
                <!--Set the onClick event to toggle view of conversation.-->
                <xsl:attribute name="onClick">toggleConversationView('<xsl:value-of select="$conversation-id"/>');</xsl:attribute>
                <!--Create a node marker that can change between - and +.-->
                <span style="font-family: 'Courier New', Courier;"><xsl:attribute name="id">conversationNodeMark-<xsl:value-of select="$conversation-id"/></xsl:attribute>-</span>
                <!--Include the last known name of the client, the exchange and session counts, and the userid.-->
                <span class="name-value"><xsl:value-of select="//exchange[userid=$userid][position()=last()]/clientname"/></span>
                <!--A little grammar check (singular or plural).-->
                <xsl:choose>
                    <xsl:when test="$exchange-count=1">
                        [1 exchange
                    </xsl:when>
                    <xsl:otherwise>
                        [<xsl:value-of select="$exchange-count"/> exchanges
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$session-count=1">
                        over 1 session]
                    </xsl:when>
                    <xsl:otherwise>
                        over <xsl:value-of select="$session-count"/> sessions]
                    </xsl:otherwise>
                </xsl:choose>
                (userid: <span class="userid-value"><xsl:value-of select="$userid"/></span>)
            </p>
            <!--Create a table to hold the conversation exchanges with this userid,
                marking it using the generated id.-->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <xsl:attribute name="id">exchanges-<xsl:value-of select="$conversation-id"/></xsl:attribute>
                <tr>
                    <td>
                        <xsl:choose>
                            <!--Group sessions that have an identifiable connect string.-->
                            <xsl:when test="count($user-exchanges[input=$connect-string])>0">
                                <!--Get the set of nodes where the response is the connect-string.-->
                                <xsl:variable name="session-starts" select="$user-exchanges[input=$connect-string]"/>
                                <!--Show the most recent session (marked by the last connect-string node).-->
                                <xsl:for-each select="$session-starts[position()=last()]">
                                    <xsl:apply-templates select="." mode="one-bounded-session">
                                        <xsl:with-param name="user-exchanges" select="$user-exchanges"/>
                                    </xsl:apply-templates>
                                </xsl:for-each>
                                <!--Show sessions bounded by connect-string nodes (earlier-than-last sessions).-->
                                <xsl:for-each select="$session-starts[not(position()=last())]">
                                    <xsl:apply-templates select="." mode="two-bounded-session">
                                        <xsl:with-param name="userid" select="$userid"/>
                                    </xsl:apply-templates>
                                </xsl:for-each>
                            </xsl:when>
                            <!--Take care of exchanges that do not contain the connect-string.-->
                            <xsl:otherwise>
                                <xsl:call-template name="session-exchanges-row">
                                    <xsl:with-param name="exchanges" select="$user-exchanges"/>
                                    <xsl:with-param name="sessionid" select="$userid"/>
                                </xsl:call-template>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </table>
        </xsl:if>
    </xsl:template>
    <!--Creates session tables for sessions identifiable by two connect-string responses.-->
    <xsl:template match="exchange" mode="two-bounded-session">
        <xsl:param name="userid"/>
        <!--Generate a 'session id'.-->
        <xsl:variable name="sessionid" select="generate-id(.)"/>
        <!--Translate the timestamp of this node to a numeric value.-->
        <xsl:variable name="session-start-timestamp"
                      select="translate(timestamp,'-: ','')"/>
        <!--Find the timestamp of the end of the session
            (translated to a numeric value).-->
        <xsl:variable name="session-end-timestamp"
                      select="translate(following-sibling::exchange[userid=$userid][following-sibling::input/node()=$connect-string][position()=1]/timestamp,'-: ','')"/>
        <!--Don't create a session table if the session has only one exchange.-->
        <xsl:if test="$session-end-timestamp &gt; $session-end-timestamp">
            <!--Start the table, marking it using this id.-->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <!--Create a session start row.-->
                <xsl:apply-templates select="." mode="session-start-row">
                    <xsl:with-param name="sessionid" select="$sessionid"/>
                </xsl:apply-templates>
                <!--Collect subsequent exchanges.-->
                <xsl:variable name="subsequent-exchanges">
                    <xsl:for-each select="//exchange[userid=$userid]">
                        <xsl:variable name="this-timestamp" select="translate(./timestamp,'-: ','')"/>
                        <xsl:if test="$this-timestamp &gt; $session-start-timestamp and $this-timestamp &lt; $session-end-timestamp">
                            <xsl:copy-of select="."/>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:variable>
                <!--Write the subsequent exchanges in a session exchanges row.-->
                <xsl:call-template name="session-exchanges-row">
                    <xsl:with-param name="exchanges" select="$subsequent-exchanges"/>
                    <xsl:with-param name="sessionid" select="$sessionid"/>
                </xsl:call-template>
            </table>
        </xsl:if>
    </xsl:template>
    <!--Creates session tables for sessions identified only by one connect-string response.-->
    <xsl:template match="exchange" mode="one-bounded-session">
        <!--Generate a 'session id'.-->
        <xsl:variable name="sessionid" select="generate-id(.)"/>
        <!--Collect subsequent exchanges.-->
        <xsl:variable name="subsequent-exchanges"
                      select="following-sibling::exchange[userid=current()/userid][not(input/node()=$connect-string)]"/>
        <!--Don't create a session table if the session has only one exchange.-->
        <xsl:if test="count($subsequent-exchanges) &gt; 0">
            <!--Start the table, marking it using this id.-->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <!--Create a session start row.-->
                <xsl:apply-templates select="." mode="session-start-row">
                    <xsl:with-param name="sessionid" select="$sessionid"/>
                </xsl:apply-templates>
                <!--Write the subsequent exchanges in a session exchanges row.-->
                <xsl:call-template name="session-exchanges-row">
                    <xsl:with-param name="exchanges" select="$subsequent-exchanges"/>
                    <xsl:with-param name="sessionid" select="$sessionid"/>
                </xsl:call-template>
            </table>
        </xsl:if>
    </xsl:template>
    <!--Creates a session start row.-->
    <xsl:template match="exchange" mode="session-start-row">
        <xsl:param name="sessionid"/>
        <!--The row just shows the timestamp and the first response.-->
        <tr>
            <td width="25%" valign="top">
                <!--The timestamp can be clicked to collapse/expand the session.-->
                <p class="connect-timestamp">
                    <xsl:attribute name="onClick">toggleSession('<xsl:value-of select="$sessionid"/>');</xsl:attribute>
                    <span class="nodeMark"><xsl:attribute name="id">sessionNodeMark-<xsl:value-of select="$sessionid"/></xsl:attribute>-</span>
                    <xsl:copy-of select="timestamp"/>
                </p>
            </td>
            <td width="75%" valign="top">
                <p class="firstresponse">
                    <xsl:attribute name="id">firstresponse-<xsl:value-of select="$sessionid"/></xsl:attribute>
                    <span class="botid"><xsl:copy-of select="botid/node()"/>&gt;</span>&#x20;
                    <xsl:copy-of select="response/node()"/>
                </p>
            </td>
        </tr>
    </xsl:template>
    <!--Creates a session exchange rowset from a set of exchange nodes.-->
    <xsl:template name="session-exchanges-row">
        <xsl:param name="exchanges"/>
        <xsl:param name="sessionid"/>
        <tr>
            <td width="100%" valign="top" colspan="2">
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <xsl:attribute name="id">session-<xsl:value-of select="$sessionid"/></xsl:attribute>
                    <!--Find all exchanges whose position is between .-->
                    <xsl:for-each select="$exchanges">
                        <xsl:sort select="timestamp" order="ascending"/>
                        <tr>
                            <td width="25%" valign="top" rowspan="2">
                                <p class="timestamp">
                                    <xsl:copy-of select="timestamp"/>
                                </p>
                            </td>
                            <td width="75%" valign="top">
                                <p class="input">
                                    <span class="clientname"><xsl:copy-of select="clientname/node()"/>&gt;</span>&#x20;
                                    <xsl:copy-of select="input/node()"/>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td width="75%" valign="top">
                                <p class="response">
                                    <span class="botid"><xsl:copy-of select="botid/node()"/>&gt;</span>&#x20;
                                    <xsl:copy-of select="response/node()"/>
                                </p>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
