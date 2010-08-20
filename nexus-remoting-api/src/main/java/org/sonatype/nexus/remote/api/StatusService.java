package org.sonatype.nexus.remote.api;

import javax.ws.rs.GET;

import org.sonatype.nexus.remote.domain.Status;
import org.sonatype.nexus.remote.plexus.PlexusResource;

public interface StatusService
    extends PlexusResource
{
    @GET
    Status getStatus();
}
