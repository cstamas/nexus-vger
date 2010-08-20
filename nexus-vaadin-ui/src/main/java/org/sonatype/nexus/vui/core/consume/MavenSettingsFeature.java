package org.sonatype.nexus.vui.core.consume;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.ConsumeFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = ConsumeFeature.class, hint = MavenSettingsFeature.HINT )
public class MavenSettingsFeature
    extends AbstractFeature
{
    public static final String HINT = "mavenSettings";

    @Override
    public String getDescription()
    {
        return "The settings to be used with Maven2/Maven3, and enjoy the benefits of local Nexus.";
    }

    @Override
    public String getName()
    {
        return "Maven Settings";
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
