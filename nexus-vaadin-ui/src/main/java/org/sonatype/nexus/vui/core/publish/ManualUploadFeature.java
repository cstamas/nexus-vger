package org.sonatype.nexus.vui.core.publish;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.AbstractFeature;
import org.sonatype.nexus.vui.Feature;
import org.sonatype.nexus.vui.PublishFeature;
import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;

@Component( role = PublishFeature.class, hint = ManualUploadFeature.HINT )
public class ManualUploadFeature
    extends AbstractFeature
{
    public static final String HINT = "upload";

    @Override
    public String getDescription()
    {
        return "Upload manually artifacts to Nexus and make them available to whole your team.";
    }

    @Override
    public String getName()
    {
        return "Manual upload";
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
