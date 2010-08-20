package org.sonatype.nexus.vui.core.browse;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.BrowseFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.UIComponent;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = BrowseFeature.class, hint = RepositoriesFeature.HINT )
public class RepositoriesFeature
    extends AbstractFeature
{
    public static final String HINT = "repositories";

    @Requirement( role = UIComponent.class, hint = HINT )
    private RepositoriesUI repositoriesUI;

    @Override
    public String getDescription()
    {
        return "List the repositories managed by Nexus.";
    }

    public int getOrderRank()
    {
        return 20;
    }

    @Override
    public String getName()
    {
        return "Repositories";
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
        return repositoriesUI.getUI();
    }
}
