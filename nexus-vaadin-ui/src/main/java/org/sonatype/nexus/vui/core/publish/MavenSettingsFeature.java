package org.sonatype.nexus.vui.core.publish;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.PublishFeature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = PublishFeature.class, hint = MavenSettingsFeature.HINT )
public class MavenSettingsFeature
    extends AbstractFeature
{
    public static final String HINT = "mavenSettings";

    @Override
    public String getDescription()
    {
        return "Deploy artifacts using Maven to Nexus, share and reuse them in whole your team.";
    }

    @Override
    public String getName()
    {
        return "Maven Deployment Settings";
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
