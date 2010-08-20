package org.sonatype.nexus.vui.app;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.FeatureSet;

import com.vaadin.Application;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;

@SuppressWarnings( "serial" )
public class NexusVuiApplication
    extends Application
{
    private PlexusContainer plexusContainer;

    private FeatureSet rootFeatureSet;

    public PlexusContainer getPlexusContainer()
    {
        return plexusContainer;
    }

    @Override
    public void init()
    {
        ServletContext servletContext = ( (WebApplicationContext) getContext() ).getHttpSession().getServletContext();

        plexusContainer = (PlexusContainer) servletContext.getAttribute( PlexusConstants.PLEXUS_KEY );

        try
        {
            rootFeatureSet = plexusContainer.lookup( RootFeatureSet.class );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( "Cannot lookup root of features!", e );
        }
        
        setTheme( "runo" );
        
        setMainWindow( new NexusVuiWindow( this ) );
    }

    @Override
    public Window getWindow( String name )
    {
        Window w = super.getWindow( name );
        if ( w == null )
        {
            if ( name.startsWith( "src" ) )
            {
                w = new SourceWindow( this );
            }
            else
            {
                w = new NexusVuiWindow( this );
                w.setName( name );
            }

            addWindow( w );
        }
        return w;
    }

    @Override
    public void close()
    {
        removeWindow( getMainWindow() );

        super.close();
    }

    public Feature getFeatureByPath( String path )
    {
        return rootFeatureSet.getFeatureByPath( path );
    }

    public Collection<Feature> getFeatures()
    {
        return rootFeatureSet.getFeatures();
    }

    public HierarchicalContainer getAllFeatures()
    {
        return rootFeatureSet.getContainer( true );
    }

    /**
     * Gets absolute path for given Feature
     * 
     * @param f the Feature whose path to get, of null if not found
     * @return the path of the Feature
     */
    public String getPathFor( Feature f )
    {
        if ( f == null )
        {
            return "";
        }
        if ( getAllFeatures().containsId( f ) )
        {
            String path = f.getPathName();
            f = (Feature) getAllFeatures().getParent( f );
            while ( f != null )
            {
                path = f.getPathName() + "/" + path;
                f = (Feature) getAllFeatures().getParent( f );
            }
            return path;
        }
        return "";
    }

    /**
     * Gets the instance for the given Feature class, e.g DummyFeature.class.
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public Feature getFeatureFor( Class clazz )
    {
        for ( Iterator<Feature> it = getAllFeatures().getItemIds().iterator(); it.hasNext(); )
        {
            Feature f = it.next();
            if ( f.getClass() == clazz )
            {
                return f;
            }
        }
        return null;
    }

}
