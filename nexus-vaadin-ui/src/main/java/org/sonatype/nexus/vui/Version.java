package org.sonatype.nexus.vui;

import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

public enum Version
{
    OLD( 0 ), BUILD( Integer.parseInt( AbstractApplicationServlet.VERSION_MAJOR + ""
        + AbstractApplicationServlet.VERSION_MINOR ) ), V1420( 1420 );

    private final int version;

    Version( int version )
    {
        this.version = version;
    }

    /**
     * Checks whether this version is newer or as new as the build that it is included in. You can use Version.BUILD if
     * you wish for a Feature to always appear as new.
     * 
     * @return
     */
    public boolean isNew()
    {
        return BUILD.version <= version;
    }
}
