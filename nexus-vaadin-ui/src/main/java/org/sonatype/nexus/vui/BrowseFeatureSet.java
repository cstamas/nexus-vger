package org.sonatype.nexus.vui;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component( role = TopLevelFeature.class, hint = BrowseFeatureSet.NAME )
public class BrowseFeatureSet
    extends AbstractFeatureSet
{
    public static final String NAME = "browse";
    
    public static final int ORDER_RANK = 25;

    @Requirement( role = BrowseFeature.class )
    private Map<String, Feature> features;

    public BrowseFeatureSet()
    {
        super( BrowseFeatureSet.NAME, "Explore", "Explore your Nexus instance and it's contents." );
    }

    @Override
    protected Map<String, Feature> getFeatureMap()
    {
        return features;
    }

    public int getOrderRank()
    {
        return ORDER_RANK;
    }
}
