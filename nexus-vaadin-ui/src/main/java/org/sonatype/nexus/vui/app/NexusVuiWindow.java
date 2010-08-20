package org.sonatype.nexus.vui.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.FeatureSet;
import org.sonatype.nexus.vui.app.ModeSwitch.ModeSwitchEvent;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings( "serial" )
public class NexusVuiWindow
    extends Window
{
    private final ObjectProperty currentFeature = new ObjectProperty( null, Feature.class );

    private final ModeSwitch mode;

    private final SplitPanel mainSplit;

    private final Tree navigationTree;

    private final FeatureView featureView;

    private FeatureList currentList;

    // "backbutton"
    private UriFragmentUtility uriFragmentUtility = new UriFragmentUtility();

    private Button previousSample;

    private Button nextSample;

    private ComboBox search;

    private ComboBox theme;

    private final NexusVuiApplication nexusVuiApplication;

    public NexusVuiWindow( NexusVuiApplication nvu )
    {
        this.nexusVuiApplication = nvu;
        this.featureView = new FeatureView( nexusVuiApplication );
        this.currentList = new FeatureGrid( nexusVuiApplication );
        // Main top/expanded-bottom layout
        VerticalLayout mainExpand = new VerticalLayout();
        setContent( mainExpand );
        setSizeFull();
        mainExpand.setSizeFull();
        setCaption( "Nexus VGER" );

        // topbar (navigation)
        HorizontalLayout nav = new HorizontalLayout();
        mainExpand.addComponent( nav );
        nav.setHeight( "44px" );
        nav.setWidth( "100%" );
        nav.setStyleName( "topbar" );
        nav.setSpacing( true );
        nav.setMargin( false, true, false, false );

        // Upper left logo
        Component logo = createLogo();
        nav.addComponent( logo );
        nav.setComponentAlignment( logo, Alignment.MIDDLE_LEFT );

        // invisible analytics -component
        // KUKU
        // nav.addComponent( webAnalytics );

        // "backbutton"
        nav.addComponent( uriFragmentUtility );
        uriFragmentUtility.addListener( new FragmentChangedListener()
        {
            public void fragmentChanged( FragmentChangedEvent source )
            {
                String frag = source.getUriFragmentUtility().getFragment();
                setFeature( frag );
            }
        } );

        // Main left/right split; hidden menu tree
        mainSplit = new SplitPanel( SplitPanel.ORIENTATION_HORIZONTAL );
        mainSplit.setSizeFull();
        mainSplit.setStyleName( "main-split" );
        mainExpand.addComponent( mainSplit );
        mainExpand.setExpandRatio( mainSplit, 1 );

        // List/grid/coverflow
        mode = createModeSwitch();
        mode.setMode( currentList );
        nav.addComponent( mode );
        nav.setComponentAlignment( mode, Alignment.MIDDLE_LEFT );

        // Layouts for top area buttons
        HorizontalLayout quicknav = new HorizontalLayout();
        HorizontalLayout arrows = new HorizontalLayout();
        nav.addComponent( quicknav );
        nav.addComponent( arrows );
        nav.setComponentAlignment( quicknav, Alignment.MIDDLE_LEFT );
        nav.setComponentAlignment( arrows, Alignment.MIDDLE_LEFT );
        quicknav.setStyleName( "segment" );
        arrows.setStyleName( "segment" );

        // Previous sample
        previousSample = createPrevButton();
        arrows.addComponent( previousSample );
        // Next sample
        nextSample = createNextButton();
        arrows.addComponent( nextSample );
        // "Search" combobox
        // TODO add input prompt
        Component searchComponent = createSearch();
        quicknav.addComponent( searchComponent );

        // Menu tree, initially shown
        CssLayout menuLayout = new CssLayout();
        navigationTree = createMenuTree();
        menuLayout.addComponent( navigationTree );
        mainSplit.setFirstComponent( menuLayout );

        // Show / hide tree
        Button treeSwitch = createTreeSwitch();
        quicknav.addComponent( treeSwitch );

        addListener( new CloseListener()
        {
            public void windowClose( CloseEvent e )
            {
                if ( nexusVuiApplication.getMainWindow() != NexusVuiWindow.this )
                {
                    nexusVuiApplication.removeWindow( NexusVuiWindow.this );
                }
            }
        } );

        // hide the tree
        hideTree();
    }

    @Override
    public void detach()
    {
        super.detach();

        search.setContainerDataSource( null );
        navigationTree.setContainerDataSource( null );
    }

    public void removeSubwindows()
    {
        Collection<Window> wins = getChildWindows();
        if ( null != wins )
        {
            for ( Window w : wins )
            {
                removeWindow( w );
            }
        }
    }

    public void showTree()
    {
        // show tree
        mainSplit.setSplitPosition( 250, SplitPanel.UNITS_PIXELS );
        mainSplit.setLocked( false );
        navigationTree.setVisible( true );
    }

    public void hideTree()
    {
        // hide the tree
        mainSplit.setSplitPosition( 0 );
        navigationTree.setVisible( false );
        mainSplit.setLocked( true );
    }

    /**
     * Displays a Feature(Set)
     * 
     * @param f the Feature(Set) to show
     */
    public void setFeature( Feature f )
    {
        removeSubwindows();
        currentFeature.setValue( f );
        String path = nexusVuiApplication.getPathFor( f );
        uriFragmentUtility.setFragment( path, false );

        previousSample.setEnabled( f != null );
        nextSample.setEnabled( !nexusVuiApplication.getAllFeatures().isLastId( f ) );

        updateFeatureList( currentList );
    }

    /**
     * Displays a Feature(Set) matching the given path, or the main view if no matching Feature(Set) is found.
     * 
     * @param path the path of the Feature(Set) to show
     */
    public void setFeature( String path )
    {
        Feature f = nexusVuiApplication.getFeatureByPath( path );
        setFeature( f );
    }

    /*
     * SamplerWindow helpers
     */

    private Component createSearch()
    {
        search = new ComboBox();
        search.setWidth( "160px" );
        search.setNewItemsAllowed( false );
        search.setFilteringMode( ComboBox.FILTERINGMODE_CONTAINS );
        search.setNullSelectionAllowed( true );
        search.setImmediate( true );
        search.setContainerDataSource( nexusVuiApplication.getAllFeatures() );
        for ( Iterator<?> it = nexusVuiApplication.getAllFeatures().getItemIds().iterator(); it.hasNext(); )
        {
            Object id = it.next();
            if ( id instanceof FeatureSet )
            {
                search.setItemIcon( id, new ClassResource( "folder.gif", nexusVuiApplication ) );
            }
        }
        search.addListener( new ComboBox.ValueChangeListener()
        {
            public void valueChange( ValueChangeEvent event )
            {
                Feature f = (Feature) event.getProperty().getValue();
                if ( f != null )
                {
                    NexusVuiWindow.this.setFeature( f );
                    event.getProperty().setValue( null );
                }

            }
        } );
        // TODO add icons for section/sample
        /*
         * PopupView pv = new PopupView("", search) { public void changeVariables(Object source, Map variables) {
         * super.changeVariables(source, variables); if (isPopupVisible()) { search.focus(); } } };
         */
        final PopupView pv = new PopupView( "<span></span>", search );
        pv.addListener( new PopupView.PopupVisibilityListener()
        {
            public void popupVisibilityChange( PopupVisibilityEvent event )
            {
                if ( event.isPopupVisible() )
                {
                    search.focus();
                }
            }
        } );
        pv.setStyleName( "quickjump" );
        pv.setDescription( "Quick jump" );

        return pv;
    }

    private Component createLogo()
    {
        Button logo = new NativeButton( "", new Button.ClickListener()
        {
            public void buttonClick( ClickEvent event )
            {
                setFeature( (Feature) null );
            }
        } );
        logo.setDescription( "↶ Home" );
        logo.setStyleName( Button.STYLE_LINK );
        logo.addStyleName( "logo" );
        return logo;
    }

    private Button createNextButton()
    {
        Button b = new NativeButton( "Next", new ClickListener()
        {
            public void buttonClick( ClickEvent event )
            {
                Object curr = currentFeature.getValue();
                Object next = nexusVuiApplication.getAllFeatures().nextItemId( curr );
                while ( next != null && next instanceof FeatureSet )
                {
                    next = nexusVuiApplication.getAllFeatures().nextItemId( next );
                }
                if ( next != null )
                {
                    currentFeature.setValue( next );
                }
                else
                {
                    // could potentially occur if there is an empty section
                    showNotification( "Last sample" );
                }
            }
        } );
        // b.setStyleName( "next" );
        b.setDescription( "Jump to the next sample" );
        return b;
    }

    private Button createPrevButton()
    {
        Button b = new NativeButton( "Prev", new ClickListener()
        {
            public void buttonClick( ClickEvent event )
            {
                Object curr = currentFeature.getValue();
                Object prev = nexusVuiApplication.getAllFeatures().prevItemId( curr );
                while ( prev != null && prev instanceof FeatureSet )
                {
                    prev = nexusVuiApplication.getAllFeatures().prevItemId( prev );
                }
                currentFeature.setValue( prev );
            }
        } );
        b.setEnabled( false );
        // b.setStyleName( "previous" );
        b.setDescription( "Jump to the previous sample" );
        return b;
    }

    private Button createTreeSwitch()
    {
        final Button b = new NativeButton( "Show tree" );
        b.setStyleName( "tree-switch" );
        // b.addStyleName( "down" );
        b.setDescription( "Toggle sample tree visibility" );
        b.addListener( new Button.ClickListener()
        {
            public void buttonClick( ClickEvent event )
            {
                if ( b.getStyleName().contains( "down" ) )
                {
                    b.removeStyleName( "down" );
                    hideTree();
                }
                else
                {
                    b.addStyleName( "down" );
                    showTree();
                }
            }
        } );
        mainSplit.setSplitPosition( 250, SplitPanel.UNITS_PIXELS );
        return b;
    }

    private ModeSwitch createModeSwitch()
    {
        ModeSwitch m = new ModeSwitch();
        m.addMode( currentList, "Icons", "View as Icons", new ThemeResource( "../sampler/sampler/grid.png" ) );
        /*- no CoverFlow yet
        m.addMode(coverFlow, "", "View as Icons", new ThemeResource(
                "../sampler/sampler/flow.gif"));
         */
        m.addMode( new FeatureTable( nexusVuiApplication ), "List", "View as List", new ThemeResource(
            "../sampler/sampler/list.png" ) );
        m.addListener( new ModeSwitch.ModeSwitchListener()
        {
            public void componentEvent( Event event )
            {
                if ( event instanceof ModeSwitchEvent )
                {
                    updateFeatureList( (FeatureList) ( (ModeSwitchEvent) event ).getMode() );
                }
            }
        } );
        return m;
    }

    private Tree createMenuTree()
    {
        final Tree tree = new Tree();
        tree.setImmediate( true );
        tree.setStyleName( "menu" );
        tree.setContainerDataSource( nexusVuiApplication.getAllFeatures() );
        currentFeature.addListener( new Property.ValueChangeListener()
        {
            public void valueChange( ValueChangeEvent event )
            {
                Feature f = (Feature) event.getProperty().getValue();
                Feature v = (Feature) tree.getValue();
                if ( ( f != null && !f.equals( v ) ) || ( f == null && v != null ) )
                {
                    tree.setValue( f );
                }
            }
        } );
        for ( Feature f : nexusVuiApplication.getFeatures() )
        {
            tree.expandItemsRecursively( f );
        }
        tree.expandItemsRecursively( nexusVuiApplication.getFeatures() );
        tree.addListener( new Tree.ValueChangeListener()
        {
            public void valueChange( ValueChangeEvent event )
            {
                Feature f = (Feature) event.getProperty().getValue();
                setFeature( f );
            }
        } );
        tree.setItemStyleGenerator( new Tree.ItemStyleGenerator()
        {
            public String getStyle( Object itemId )
            {
                Feature f = (Feature) itemId;
                if ( f.getSinceVersion().isNew() )
                {
                    return "new";
                }
                return null;
            }
        } );
        return tree;
    }

    private void updateFeatureList( FeatureList list )
    {
        currentList = list;
        Feature val = (Feature) currentFeature.getValue();
        if ( val == null )
        {
            currentList.setFeatureContainer( nexusVuiApplication.getAllFeatures() );
            mainSplit.setSecondComponent( currentList );
            mode.setVisible( true );
        }
        else if ( val instanceof FeatureSet )
        {
            currentList.setFeatureContainer( ( (FeatureSet) val ).getContainer( true ) );
            mainSplit.setSecondComponent( currentList );
            mode.setVisible( true );
        }
        else
        {
            mainSplit.setSecondComponent( featureView );
            featureView.setFeature( val );
            mode.setVisible( false );
        }

    }

    /**
     * Table -mode FeatureList. Displays the features in a Table.
     */
    private class FeatureTable
        extends Table
        implements FeatureList
    {
        private final HashMap<Object, Resource> iconCache = new HashMap<Object, Resource>();

        private final NexusVuiApplication nexusVuiApplication;

        FeatureTable( NexusVuiApplication nva )
        {
            this.nexusVuiApplication = nva;
            setStyleName( "featuretable" );
            alwaysRecalculateColumnWidths = true;
            setSelectable( false );
            setSizeFull();
            setColumnHeaderMode( Table.COLUMN_HEADER_MODE_HIDDEN );
            addGeneratedColumn( Feature.PROPERTY_ICON, new Table.ColumnGenerator()
            {
                public Component generateCell( Table source, Object itemId, Object columnId )
                {
                    Feature f = (Feature) itemId;
                    if ( f instanceof FeatureSet )
                    {
                        // no icon for sections
                        return null;
                    }
                    String resId =
                        f.getIconName().startsWith( "/" ) ? "/75-" + f.getIconName().substring( 1 ) : "75-"
                            + f.getIconName();
                    Resource res = iconCache.get( resId );
                    if ( res == null )
                    {
                        res = new ClassResource( f.getClass(), resId, nexusVuiApplication );
                        iconCache.put( resId, res );

                    }
                    Embedded emb = new Embedded( "", res );
                    emb.setWidth( "48px" );
                    emb.setHeight( "48px" );
                    emb.setType( Embedded.TYPE_IMAGE );
                    return emb;
                }

            } );
            addGeneratedColumn( "", new Table.ColumnGenerator()
            {
                public Component generateCell( Table source, Object itemId, Object columnId )
                {
                    final Feature feature = (Feature) itemId;
                    // if ( feature instanceof FeatureSet )
                    // {
                    return null;
                    // }
                    // else
                    // {
                    // ActiveLink b =
                    // new ActiveLink( "View sample ‣", new ExternalResource( "#"
                    // + nexusVuiApplication.getPathFor( feature ) ) );
                    // b.addListener( new ActiveLink.LinkActivatedListener()
                    // {
                    // public void linkActivated( LinkActivatedEvent event )
                    // {
                    // if ( !event.isLinkOpened() )
                    // {
                    // ( (NexusVuiWindow) getWindow() ).setFeature( feature );
                    // }
                    // }
                    // } );
                    //
                    // b.setStyleName( Button.STYLE_LINK );
                    // return b;
                    // }
                }

            } );

            addListener( new ItemClickListener()
            {
                public void itemClick( ItemClickEvent event )
                {
                    Feature f = (Feature) event.getItemId();
                    if ( event.getButton() == ItemClickEvent.BUTTON_MIDDLE || event.isCtrlKey() || event.isShiftKey() )
                    {
                        getWindow().open( new ExternalResource( getURL() + "#" + nexusVuiApplication.getPathFor( f ) ),
                            "_blank" );
                    }
                    else
                    {
                        ( (NexusVuiWindow) getWindow() ).setFeature( f );
                    }
                }
            } );

            setCellStyleGenerator( new CellStyleGenerator()
            {
                public String getStyle( Object itemId, Object propertyId )
                {
                    if ( propertyId == null && itemId instanceof FeatureSet )
                    {
                        if ( nexusVuiApplication.getAllFeatures().isRoot( itemId ) )
                        {
                            return "section";
                        }
                        else
                        {
                            return "subsection";
                        }

                    }
                    return null;
                }
            } );
        }

        public void setFeatureContainer( HierarchicalContainer c )
        {
            setContainerDataSource( c );
            setVisibleColumns( new Object[] { Feature.PROPERTY_ICON, Feature.PROPERTY_NAME, "" } );
            setColumnWidth( Feature.PROPERTY_ICON, 60 );

        }

    }

    private class FeatureGrid
        extends Panel
        implements FeatureList
    {

        private final CssLayout grid = new CssLayout();

        private final HashMap<Object, Resource> iconCache = new HashMap<Object, Resource>();

        private final NexusVuiApplication nexusVuiApplication;

        FeatureGrid( NexusVuiApplication nexusVuiApplication )
        {
            this.nexusVuiApplication = nexusVuiApplication;
            setSizeFull();
            setScrollable( true );
            setContent( grid );
            setStyleName( Panel.STYLE_LIGHT );
            grid.setStyleName( "grid" );
        }

        @SuppressWarnings( "unchecked" )
        public void setFeatureContainer( HierarchicalContainer c )
        {
            grid.removeAllComponents();
            Collection<Feature> features = c.getItemIds();

            CssLayout rootSet = new CssLayout();
            Label rootTitle = null;

            CssLayout highlightRow = new CssLayout();
            highlightRow.setStyleName( "highlight-row" );

            int sampleCount = 0;
            for ( Iterator<Feature> it = features.iterator(); it.hasNext(); )
            {
                final Feature f = it.next();
                if ( f instanceof FeatureSet )
                {
                    if ( c.isRoot( f ) )
                    {
                        if ( rootTitle != null )
                        {
                            rootTitle.setValue( "<em>" + sampleCount + " features</em>" + rootTitle.getValue() );
                            sampleCount = 0;
                        }
                        rootTitle =
                            new Label( "<h2>" + f.getName() + "</h2><span>"
                                + f.getDescription().substring( 0, f.getDescription().indexOf( "." ) + 1 ) + "</span>",
                                Label.CONTENT_XHTML );
                        rootTitle.setSizeUndefined();
                        if ( f.getRelatedFeatures() != null )
                        {
                            rootTitle.setValue( "<em>" + f.getRelatedFeatures().length + " features</em>"
                                + rootTitle.getValue() );
                        }
                        rootSet = new CssLayout();
                        rootSet.setStyleName( "root" );
                        rootTitle.setStyleName( "root-section" );
                        grid.addComponent( rootTitle );
                        grid.addComponent( rootSet );
                    }

                }
                else
                {
                    sampleCount++;
                    String resId =
                        f.getIconName().startsWith( "/" ) ? "/75-" + f.getIconName().substring( 1 ) : "75-"
                            + f.getIconName();
                    Resource res = iconCache.get( resId );
                    if ( res == null )
                    {
                        res = new ClassResource( f.getClass(), resId, nexusVuiApplication );
                        iconCache.put( resId, res );
                    }
                    if ( rootSet.getParent() == null )
                    {
                        // This sample is directly inside a non root feature
                        // set, we present these with higher priority

                        if ( rootTitle == null )
                        {
                            Feature parent = (Feature) nexusVuiApplication.getAllFeatures().getParent( f );
                            rootTitle = new Label( "<h2>" + parent.getName() + "</h2>", Label.CONTENT_XHTML );
                            rootTitle.setStyleName( "root-section highlights-title" );
                            rootTitle.setSizeUndefined();
                            grid.addComponent( rootTitle );

                            if ( parent.getDescription() != null )
                            {
                                Label desc = new Label( parent.getDescription(), Label.CONTENT_XHTML );
                                desc.setStyleName( "highlights-description" );
                                desc.setSizeUndefined();
                                grid.addComponent( desc );
                            }
                        }

                        // Two samples per row
                        if ( sampleCount % 2 == 1 )
                        {
                            highlightRow = new CssLayout();
                            highlightRow.setStyleName( "highlight-row" );
                            grid.addComponent( highlightRow );
                        }

                        CssLayout l = new CssLayout();
                        l.setStyleName( "highlight" );

                        Link sample =
                            new Link( f.getName(), new ExternalResource( "#" + nexusVuiApplication.getPathFor( f ) ) );
                        sample.setIcon( res );
                        if ( f.getSinceVersion().isNew() )
                        {
                            sample.addStyleName( "new" );
                        }
                        l.addComponent( sample );

                        if ( f.getDescription() != null && f.getDescription() != "" )
                        {
                            Label desc =
                                new Label( f.getDescription().substring( 0, f.getDescription().indexOf( "." ) + 1 ),
                                    Label.CONTENT_XHTML );
                            desc.setSizeUndefined();
                            l.addComponent( desc );
                        }

                        highlightRow.addComponent( l );

                    }
                    else
                    {
                        Link sample =
                            new Link( f.getName(), new ExternalResource( "#" + nexusVuiApplication.getPathFor( f ) ) );
                        sample.setStyleName( Button.STYLE_LINK );
                        sample.addStyleName( "screenshot" );
                        if ( f.getDescription() != null && f.getDescription() != "" )
                        {
                            sample.setDescription( f.getDescription().substring( 0,
                                f.getDescription().indexOf( "." ) + 1 ) );
                        }
                        if ( f.getSinceVersion().isNew() )
                        {
                            sample.addStyleName( "new" );
                        }
                        sample.setIcon( res );
                        rootSet.addComponent( sample );
                    }
                }
            }
            if ( rootTitle != null )
            {
                rootTitle.setValue( "<em>" + sampleCount + " features</em>" + rootTitle.getValue() );
            }
        }
    }

}
