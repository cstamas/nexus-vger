package org.sonatype.nexus.vui.core.configuration;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.ConfigureFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = ConfigureFeature.class, hint = ScheduledTasksFeature.HINT )
public class ScheduledTasksFeature
    extends AbstractFeature
{
    public static final String HINT = "tasks";

    @Override
    public String getDescription()
    {
        return "Schedule tasks on regular basis to keep your Nexus healthy and up-to-date.";
    }

    @Override
    public String getName()
    {
        return "Scheduled Tasks";
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
