package org.sonatype.nexus.vui.app;

import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.vui.AbstractFeatureSet;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.TopLevelFeature;

@Component( role = RootFeatureSet.class )
public class RootFeatureSet
    extends AbstractFeatureSet
{
    public static final String NAME = "root";

    @Requirement( role = TopLevelFeature.class )
    private Map<String, Feature> features;

    public RootFeatureSet()
    {
        super( RootFeatureSet.NAME, "Root", "Root of Nexus Features (not shown!)" );
    }

    @Override
    protected Map<String, Feature> getFeatureMap()
    {
        return features;
    }
}
