package org.sonatype.nexus.vui.core.browse;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.BrowseFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = BrowseFeature.class, hint = EventsFeature.HINT )
public class EventsFeature
    extends AbstractFeature
{
    public static final String HINT = "events";

    @Override
    public String getDescription()
    {
        return "Inspect system and user related events in your Nexus.";
    }

    @Override
    public String getName()
    {
        return "Events";
    }

    @Override
    public APIResource[] getRelatedAPI()
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

    @Override
    public com.vaadin.ui.Component getUI()
    {
        return null;
    }

}
