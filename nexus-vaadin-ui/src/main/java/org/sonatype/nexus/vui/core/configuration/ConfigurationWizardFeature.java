package org.sonatype.nexus.vui.core.configuration;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.ConfigureFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;
import org.sonatype.nexus.vui.core.browse.RepositoriesFeature;

@Component( role = ConfigureFeature.class, hint = RepositoriesFeature.HINT )
public class ConfigurationWizardFeature
    extends AbstractFeature
{
    public static final String HINT = "configure";

    @Override
    public String getDescription()
    {
        return "The configuration wizard helps you to quickly set up your instance of Nexus.";
    }

    public int getOrderRank()
    {
        return 5;
    }

    @Override
    public String getName()
    {
        return "Wizard";
    }

    @Override
    public APIResource[] getRelatedAPI()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<? extends Feature>[] getRelatedFeatures()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NamedExternalResource[] getRelatedResources()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public com.vaadin.ui.Component getUI()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
