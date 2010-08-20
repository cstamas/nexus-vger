package org.sonatype.nexus.vui.app;

import java.net.URL;

import org.sonatype.nexus.vui.Feature;

import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.URIHandler;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings( "serial" )
public class SourceWindow
    extends Window
{
    private final NexusVuiApplication nexusVuiApplication;

    public SourceWindow( NexusVuiApplication nva )
    {
        this.nexusVuiApplication = nva;

        addURIHandler( new URIHandler()
        {
            public DownloadStream handleURI( URL context, String relativeUri )
            {
                Feature f = nexusVuiApplication.getFeatureByPath( relativeUri );
                if ( f != null )
                {
                    addComponent( new Label( f.getExampleSource() ) );
                }
                else
                {
                    addComponent( new Label( "Sorry, no source found for " + relativeUri ) );
                }
                return null;
            }

        } );

        addListener( new CloseListener()
        {
            public void windowClose( CloseEvent e )
            {
                nexusVuiApplication.removeWindow( SourceWindow.this );
            }
        } );
    }
}
