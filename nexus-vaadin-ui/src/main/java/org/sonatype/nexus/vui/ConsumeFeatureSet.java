package org.sonatype.nexus.vui;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component( role = TopLevelFeature.class, hint = ConsumeFeatureSet.NAME )
public class ConsumeFeatureSet
    extends AbstractFeatureSet
{
    public static final String NAME = "consume";

    public static final int ORDER_RANK = 50;

    @Requirement( role = ConsumeFeature.class )
    private Map<String, Feature> features;

    public ConsumeFeatureSet()
    {
        super( ConsumeFeatureSet.NAME, "Consume", "Consume artifacts stored in and proxied by your Nexus instance." );
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
