package org.sonatype.nexus.vui.app;

import java.util.HashMap;

import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.FeatureSet;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings( "serial" )
public class FeatureView
    extends HorizontalLayout
{

    private static final String MSG_SHOW_SRC = "View Source";
    
    private final NexusVuiApplication nexusVuiApplication;

    private Panel right;

    private VerticalLayout left;

    private HorizontalLayout controls;

    private Label title = new Label();

    private HashMap<Feature, Component> exampleCache = new HashMap<Feature, Component>();

    private Feature currentFeature;

    private Window srcWindow;

    public FeatureView( NexusVuiApplication nva )
    {
        this.nexusVuiApplication = nva;

        setWidth( "100%" );
        setMargin( true );
        setSpacing( true );
        setStyleName( "sample-view" );

        left = new VerticalLayout();
        left.setWidth( "100%" );
        left.setSpacing( true );
        left.setMargin( false );
        addComponent( left );
        setExpandRatio( left, 1 );

        VerticalLayout rightLayout = new VerticalLayout();
        right = new Panel( rightLayout );
        rightLayout.setMargin( true, false, false, false );
        right.setStyleName( Panel.STYLE_LIGHT );
        right.addStyleName( "feature-info" );
        right.setWidth( "319px" );
        addComponent( right );

        controls = new HorizontalLayout();
        controls.setWidth( "100%" );
        controls.setStyleName( "feature-controls" );

        title.setStyleName( "title" );
        title.setSizeUndefined();
        controls.addComponent( title );
        controls.setExpandRatio( title, 1 );

        Button resetExample = new NativeButton( "Reset", new Button.ClickListener()
        {
            public void buttonClick( ClickEvent event )
            {
                resetExample();
            }
        } );
        resetExample.setStyleName( Button.STYLE_LINK );
        resetExample.addStyleName( "reset" );
        resetExample.setDescription( "Reset Sample" );
        controls.addComponent( resetExample );


    }

    public void showSource( String source )
    {
        if ( srcWindow == null )
        {
            srcWindow = new Window( "Java™ source" );
            srcWindow.getContent().setSizeUndefined();
            srcWindow.setWidth( "70%" );
            srcWindow.setHeight( "60%" );
            srcWindow.setPositionX( 100 );
            srcWindow.setPositionY( 100 );
        }
        srcWindow.removeAllComponents();
        srcWindow.addComponent( new Label( source ) );

        if ( srcWindow.getParent() == null )
        {
            getWindow().addWindow( srcWindow );
        }
    }

    private void resetExample()
    {
        if ( currentFeature != null )
        {
            Feature f = currentFeature;
            currentFeature = null;
            exampleCache.remove( f );
            setFeature( f );
        }
    }

    public void setFeature( Feature feature )
    {
        if ( feature != currentFeature )
        {
            currentFeature = feature;
            right.removeAllComponents();
            left.removeAllComponents();

            left.addComponent( controls );
            title.setValue( feature.getName() );
            if ( feature.getSinceVersion().isNew() )
            {
                title.addStyleName( "new" );
            }
            else
            {
                title.removeStyleName( "new" );
            }

            left.addComponent( getExampleFor( feature ) );

            right.setCaption( "Description and Resources" );

            // Do not show parent description if it's directly inside the root
            final HierarchicalContainer all = nexusVuiApplication.getAllFeatures();
            final FeatureSet parent = (FeatureSet) all.getParent( feature );
            boolean isRoot = all.getParent( parent ) == null;
            String desc = parent.getDescription();
            boolean hasParentDesc = false;

            if ( parent != null && !isRoot )
            {
                final Label parentLabel = new Label( parent.getDescription() );
                if ( desc != null && desc != "" )
                {
                    parentLabel.setContentMode( Label.CONTENT_XHTML );
                    right.addComponent( parentLabel );
                    hasParentDesc = true;
                }
            }

            desc = feature.getDescription();
            if ( desc != null && desc != "" )
            {
                // Sample description uses additional decorations if a parent
                // description is found
                final Label l =
                    new Label( "<div class=\"outer-deco\"><div class=\"deco\"><span class=\"deco\"></span>" + desc
                        + "</div></div>", Label.CONTENT_XHTML );
                right.addComponent( l );
                if ( hasParentDesc )
                {
                    l.setStyleName( "sample-description" );
                }
                else
                {
                    l.setStyleName( "description" );
                }
            }

            NamedExternalResource[] resources = feature.getRelatedResources();
            if ( resources != null )
            {
                VerticalLayout res = new VerticalLayout();
                Label caption = new Label( "<span>Additional Resources</span>", Label.CONTENT_XHTML );
                caption.setStyleName( "section" );
                caption.setWidth( "100%" );
                res.addComponent( caption );
                res.setMargin( false, false, true, false );
                for ( NamedExternalResource r : resources )
                {
                    final Link l = new Link( r.getName(), r );
                    l.setIcon( new ThemeResource( "../runo/icons/16/note.png" ) );
                    res.addComponent( l );
                }
                right.addComponent( res );
            }

            APIResource[] apis = feature.getRelatedAPI();
            if ( apis != null )
            {
                VerticalLayout api = new VerticalLayout();
                Label caption = new Label( "<span>API Documentation</span>", Label.CONTENT_XHTML );
                caption.setStyleName( "section" );
                caption.setWidth( "100%" );
                api.addComponent( caption );
                api.setMargin( false, false, true, false );
                for ( APIResource r : apis )
                {
                    final Link l = new Link( r.getName(), r );
                    l.setIcon( new ThemeResource( "../runo/icons/16/document-txt.png" ) );
                    api.addComponent( l );
                }
                right.addComponent( api );
            }

            Class<? extends Feature>[] features = feature.getRelatedFeatures();
            if ( features != null )
            {
                VerticalLayout rel = new VerticalLayout();
                Label caption = new Label( "<span>Related pages</span>", Label.CONTENT_XHTML );
                caption.setStyleName( "section" );
                caption.setWidth( "100%" );
                rel.addComponent( caption );
                rel.setMargin( false, false, true, false );
                for ( Class<? extends Feature> c : features )
                {
                    final Feature f = nexusVuiApplication.getFeatureFor( c );
                    if ( f != null )
                    {
                        String path = nexusVuiApplication.getPathFor( f );
                        Link al =
                            new Link( f.getName(), new ExternalResource( getApplication().getURL() + "#" + path ) );
                        al.setIcon( new ThemeResource( ( f instanceof FeatureSet ? "../sampler/icons/category.gif"
                                        : "../sampler/icons/sample.png" ) ) );
//                        al.addListener( new LinkActivatedListener()
//                        {
//                            public void linkActivated( LinkActivatedEvent event )
//                            {
//                                if ( event.isLinkOpened() )
//                                {
//                                    getWindow().showNotification( f.getName() + " opened if new window/tab" );
//                                }
//                                else
//                                {
//                                    NexusVuiWindow w = (NexusVuiWindow) getWindow();
//                                    w.setFeature( f );
//                                }
//                            }
//                        } );
                        rel.addComponent( al );
                    }
                }
                right.addComponent( rel );
            }
        }

    }

    private Component getExampleFor( Feature f )
    {
        Component ex = exampleCache.get( f );
        if ( ex == null )
        {
            ex = f.getUI();
            exampleCache.put( f, ex );
        }
        return ex;
    }

}
