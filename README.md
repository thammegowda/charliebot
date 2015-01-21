# charliebot
The ALICE/ALICEBOT/CHARLIE/CHARLIEBOT 

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
 
` 
