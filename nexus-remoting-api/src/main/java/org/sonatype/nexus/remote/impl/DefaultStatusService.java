package org.sonatype.nexus.remote.impl;

import javax.ws.rs.Path;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.nexus.Nexus;
import org.sonatype.nexus.SystemStatus;
import org.sonatype.nexus.remote.api.StatusService;
import org.sonatype.nexus.remote.domain.Status;
import org.sonatype.nexus.remote.plexus.PlexusResource;

@Path( "/status" )
@Component( role = PlexusResource.class, hint = "status" )
public class DefaultStatusService
    implements StatusService
{
    @Requirement
    private Nexus nexus;

    public Status getStatus()
    {
        Status result = new Status();

        SystemStatus systemStatus = nexus.getSystemStatus();

        result.setApiVersion( systemStatus.getApiVersion() );
        result.setAppName( systemStatus.getAppName() );
        result.setConfigurationUpgraded( systemStatus.isConfigurationUpgraded() );
        result.setEditionLong( systemStatus.getEditionLong() );
        result.setEditionShort( systemStatus.getEditionShort() );
        if ( systemStatus.getErrorCause() != null )
        {
            result.setErrorCauseMessage( systemStatus.getErrorCause().getLocalizedMessage() );
        }
        result.setFirstStart( systemStatus.isFirstStart() );
        result.setFormattedAppName( systemStatus.getFormattedAppName() );
        result.setInitializedAt( systemStatus.getInitializedAt() );
        result.setInstanceUpgraded( systemStatus.isInstanceUpgraded() );
        result.setLastConfigChange( systemStatus.getLastConfigChange() );
        result.setOperationMode( systemStatus.getOperationMode() );
        result.setStartedAt( systemStatus.getStartedAt() );
        result.setState( systemStatus.getState() );
        result.setVersion( systemStatus.getVersion() );

        return result;
    }
}
