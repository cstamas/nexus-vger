package org.sonatype.nexus.vui;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Component;

public abstract class AbstractFeatureSet
    extends AbstractFeature
    implements FeatureSet
{
    private final String pathname;

    private final String name;

    private final String desc;

    private final String icon = "folder.gif";

    private HierarchicalContainer container = null;

    private final boolean containerRecursive = false;

    public AbstractFeatureSet( String pathname, String name )
    {
        this( pathname, name, "" );
    }

    public AbstractFeatureSet( String pathname, String name, String desc )
    {
        this.pathname = pathname;
        this.name = name;
        this.desc = desc;
    }

    protected abstract Map<String, Feature> getFeatureMap();

    public TreeSet<Feature> getFeatures()
    {
        // todo, make order changeable (abc, orderRank, etc);
        TreeSet<Feature> result = new TreeSet<Feature>( new FeatureComparator() );

        result.addAll( getFeatureMap().values() );

        return result;
    }

    public Feature getFeatureByPath( String path )
    {
        LinkedList<String> parts = new LinkedList<String>();
        Collections.addAll( parts, path.split( "/" ) );
        FeatureSet f = this;
        while ( f != null )
        {
            Collection<Feature> features = f.getFeatures();
            f = null; // break while if no new found
            String part = parts.remove( 0 );
            for ( Feature feature : features )
            {
                if ( feature.getPathName().equalsIgnoreCase( part ) )
                {
                    if ( parts.isEmpty() )
                    {
                        return feature;
                    }
                    else if ( feature instanceof FeatureSet )
                    {
                        f = (FeatureSet) feature;
                        break;
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public HierarchicalContainer getContainer( boolean recurse )
    {
        if ( container == null || containerRecursive != recurse )
        {
            container = new HierarchicalContainer();
            container.addContainerProperty( Feature.PROPERTY_NAME, String.class, "" );
            container.addContainerProperty( Feature.PROPERTY_DESCRIPTION, String.class, "" );
            // fill
            addFeatures( this, container, recurse );
        }
        return container;
    }

    private void addFeatures( FeatureSet f, HierarchicalContainer c, boolean recurse )
    {
        Collection<Feature> features = f.getFeatures();
        for ( Feature feature : features )
        {
            Item item = c.addItem( feature );
            Property property = item.getItemProperty( Feature.PROPERTY_NAME );
            property.setValue( feature.getName() );
            property = item.getItemProperty( Feature.PROPERTY_DESCRIPTION );
            property.setValue( feature.getDescription() );
            if ( recurse )
            {
                c.setParent( feature, f );
                if ( feature instanceof FeatureSet )
                {
                    addFeatures( (FeatureSet) feature, c, recurse );
                }
            }
            if ( !( feature instanceof FeatureSet ) )
            {
                c.setChildrenAllowed( feature, false );
            }
        }
    }

    @Override
    public String getDescription()
    {
        return desc;
    }

    @Override
    public String getPathName()
    {
        return pathname;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getIconName()
    {
        return icon;
    }

    @Override
    public APIResource[] getRelatedAPI()
    {
        return null;
    }

    @Override
    public Component getUI()
    {
        return null;
    }

    @Override
    public Class<? extends Feature>[] getRelatedFeatures()
    {
        return null;
    }

    @Override
    public NamedExternalResource[] getRelatedResources()
    {
        return null;
    }
}
