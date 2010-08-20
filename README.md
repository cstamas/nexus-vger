Nexus VGER
==========

Here, I use a simple trick to "sneak in" Vaadin based UI for Nexus: create the UI in nexus-vaadin-ui JAR module, and then "stick" (overlay) the two (vaadin ui and the "real" NexusWebapp WAR) and replace web.xml with a modified one. That's it!


Have fun,  
~t~
