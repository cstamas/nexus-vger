package org.sonatype.nexus.vui.core.browse;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.BrowseFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.UIComponent;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = BrowseFeature.class, hint = WelcomeFeature.HINT )
public class WelcomeFeature
    extends AbstractFeature
{
    public static final String HINT = "welcome";

    @Requirement( role = UIComponent.class, hint = HINT )
    private WelcomeUI welcomeUI;

    @Override
    public String getDescription()
    {
        return "Welcome to the greatest of all the Maven Repository Managers! Seriously.";
    }

    public int getOrderRank()
    {
        return 5;
    }

    @Override
    public String getName()
    {
        return "Welcome";
    }

    @Override
    public APIResource[] getRelatedAPI()
    {
        return null;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public Class<? extends Feature>[] getRelatedFeatures()
    {
        return new Class[] { StatusFeature.class };
    }

    @Override
    public NamedExternalResource[] getRelatedResources()
    {
        return new NamedExternalResource[] {
            new NamedExternalResource( "Nexus OSS Site", "http://nexus.sonatype.org/" ),
            new NamedExternalResource( "Why do I need Nexus?", "http://nexus.sonatype.org/why-nexus.html" ),
            new NamedExternalResource( "Nexus Professional", "http://www.sonatype.com/products/nexus" ),
            new NamedExternalResource( "Sonatype", "http://www.sonatype.com/" ) };
    }

    @Override
    public com.vaadin.ui.Component getUI()
    {
        return welcomeUI.getUI();
    }

}
