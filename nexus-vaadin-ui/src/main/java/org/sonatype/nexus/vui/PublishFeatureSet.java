package org.sonatype.nexus.vui;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component( role = TopLevelFeature.class, hint = PublishFeatureSet.NAME )
public class PublishFeatureSet
    extends AbstractFeatureSet
{
    public static final String NAME = "publish";
    
    public static final int ORDER_RANK = 75;

    @Requirement( role = PublishFeature.class )
    private Map<String, Feature> features;

    public PublishFeatureSet()
    {
        super( PublishFeatureSet.NAME, "Publish", "Upload or deploy artifacts to your Nexus." );
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
