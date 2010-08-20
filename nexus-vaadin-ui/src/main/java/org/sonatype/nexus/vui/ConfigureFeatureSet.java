package org.sonatype.nexus.vui;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

@Component( role = TopLevelFeature.class, hint = ConfigureFeatureSet.NAME )
public class ConfigureFeatureSet
    extends AbstractFeatureSet
{
    public static final String NAME = "configure";

    public static final int ORDER_RANK = 100;

    @Requirement( role = ConfigureFeature.class )
    private Map<String, Feature> features;

    public ConfigureFeatureSet()
    {
        super( ConfigureFeatureSet.NAME, "Configure", "Configure, customize and tune-up your Nexus instance to suit your needs." );
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
