# CharlieBot
CharlieBot is an artificial intelligence program written in the Java language. It is based on work from the ALICE, ANNA v7.0, and Program D v4.1.5 projects.
The source has been retrieved from http://sourceforge.net/projects/charliebot/ under GPLv2 licence

#### This project has same code as its origin, except these :
   + upgraded to latest version of tools such as jetty, loggers
   + Mavenized the build : try `mvn package` or `mvn jetty:run`
   + removed System.exits's, using exceptions to figure out why it crashed
   + Clean up unnessary code
   + Along with embedded server, project has been restructured to make a java web app deployable in external container
   + Added support for JSON response
   + Modernised UI with bootstrap and jquery

#### Build and usage instructions 
 + `mvn clean compile` resolves dependencies and compiles the source code
 + `mvn jetty:run` launches jetty on port 8080
 + `mvn package` gives you `.war`
 + `mvn exec:java -Dexec.mainClass=org.alicebot.gui.SimpleConsole` gives you GUI with JPanels and Frames

NOTE:
 web interface has limited functionality.
 Use `mvn exec:java -Dexec.mainClass=org.alicebot.gui.SimpleConsole` to
 access full features.


## AIML
To learn more about AIML :
https://www.pandorabots.com/pandora/pics/wallaceaimltutorial.html


