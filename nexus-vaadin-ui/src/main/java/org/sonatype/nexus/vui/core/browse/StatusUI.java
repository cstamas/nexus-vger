package org.sonatype.nexus.vui.core.browse;

import java.text.DateFormat;
import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.Nexus;
import org.sonatype.nexus.configuration.application.NexusConfiguration;
import org.sonatype.nexus.proxy.registry.ContentClass;
import org.sonatype.nexus.proxy.registry.RepositoryRegistry;
import org.sonatype.nexus.proxy.registry.RepositoryTypeRegistry;
import org.sonatype.nexus.proxy.repository.ProxyRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.vui.UIComponent;

import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;

@Component( role = UIComponent.class, hint = StatusFeature.HINT )
public class StatusUI
    implements UIComponent
{
    private DateFormat df = DateFormat.getDateTimeInstance();

    @Requirement
    private Nexus nexus;

    @Requirement
    private RepositoryRegistry repositoryRegistry;

    @Requirement
    private RepositoryTypeRegistry repositoryTypeRegistry;

    @Requirement
    private NexusConfiguration nexusConfiguration;

    public String calculateRepositoryCounts()
    {
        List<Repository> repositories = repositoryRegistry.getRepositories();

        StringBuilder sb = new StringBuilder();

        int hosted = 0;

        int proxy = 0;

        for ( Repository repository : repositories )
        {
            if ( repository.getRepositoryKind().isFacetAvailable( ProxyRepository.class ) )
            {
                proxy++;
            }
            else
            {
                hosted++;
            }
        }

        sb.append( String.valueOf( repositories.size() ) ).append( " total (" ).append( String.valueOf( proxy ) )
            .append( " proxy + " ).append( String.valueOf( hosted ) ).append( " hosted)" );

        return sb.toString();
    }

    public com.vaadin.ui.Component getUI()
    {
        VerticalLayout vl = new VerticalLayout();

        vl.setSpacing( true );

        vl.addComponent( new Label( "<h3>Nexus state and version</h3>", Label.CONTENT_XHTML ) );
        vl.addComponent( new Label(
            "Nexus exposes it's <b>state</b> and <b>exact version and edition</b> over UI and also over REST API.",
            Label.CONTENT_XHTML ) );

        vl.addComponent( new Label( "<h2>Nexus</h2>", Label.CONTENT_XHTML ) );
        vl.addComponent( new Label( "You have " + nexus.getSystemStatus().getFormattedAppName() + ", version "
            + nexus.getSystemStatus().getVersion() + " (it operates in \""
            + nexus.getSystemStatus().getOperationMode().toString() + "\" mode).", Label.CONTENT_XHTML ) );

        // instance data
        VerticalLayout instanceData = new VerticalLayout();
        instanceData.setSpacing( true );

        instanceData.addComponent( new Label( "<h3>Instance data</h3>", Label.CONTENT_XHTML ) );
        instanceData.addComponent( new Label( "Up since: " + df.format( nexus.getSystemStatus().getStartedAt() ) ) );
        instanceData.addComponent( new Label( "Repositories defined: " + calculateRepositoryCounts() ) );
        instanceData.addComponent( new Label( "Disk usage on Nexus storage volume:" ) );
        instanceData.addComponent( new ProgressIndicator( (float) nexusConfiguration.getWorkingDirectory()
            .getFreeSpace()
            / (float) nexusConfiguration.getWorkingDirectory().getTotalSpace() ) );
        vl.addComponent( instanceData );

        // artifacts
        VerticalLayout artifactsData = new VerticalLayout();
        artifactsData.setSpacing( true );

        artifactsData.addComponent( new Label( "<h3>Artifacts data</h3>", Label.CONTENT_XHTML ) );

        artifactsData.addComponent( new Label( "Totals artifacts: " + "X artifacts (Y cached)" ) );

        StringBuilder sb = new StringBuilder();
        for ( ContentClass cc : repositoryTypeRegistry.getContentClasses().values() )
        {
            sb.append( cc.getId() ).append( ", " );
        }

        artifactsData.addComponent( new Label( "Supported repository layouts: " + sb.substring( 0, sb.length() - 2 ) ) );

        vl.addComponent( artifactsData );
        //

        return vl;
    }
}
