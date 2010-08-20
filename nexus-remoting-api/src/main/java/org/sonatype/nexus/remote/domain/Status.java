package org.sonatype.nexus.remote.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.sonatype.nexus.OperationMode;
import org.sonatype.nexus.SystemState;

@XmlRootElement
public class Status
{
    private String appName;

    private String formattedAppName;

    private String version;

    private String apiVersion;

    private String editionLong;

    private String editionShort;

    private SystemState state;

    private OperationMode operationMode;

    private Date initializedAt;

    private Date startedAt;

    private Date lastConfigChange;

    private boolean firstStart;

    private boolean instanceUpgraded;

    private boolean configurationUpgraded;

    private String errorCauseMessage;

    public String getAppName()
    {
        return appName;
    }

    public void setAppName( String appName )
    {
        this.appName = appName;
    }

    public String getFormattedAppName()
    {
        return formattedAppName;
    }

    public void setFormattedAppName( String formattedAppName )
    {
        this.formattedAppName = formattedAppName;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion( String apiVersion )
    {
        this.apiVersion = apiVersion;
    }

    public String getEditionLong()
    {
        return editionLong;
    }

    public void setEditionLong( String editionLong )
    {
        this.editionLong = editionLong;
    }

    public String getEditionShort()
    {
        return editionShort;
    }

    public void setEditionShort( String editionShort )
    {
        this.editionShort = editionShort;
    }

    public SystemState getState()
    {
        return state;
    }

    public void setState( SystemState state )
    {
        this.state = state;
    }

    public OperationMode getOperationMode()
    {
        return operationMode;
    }

    public void setOperationMode( OperationMode operationMode )
    {
        this.operationMode = operationMode;
    }

    public Date getInitializedAt()
    {
        return initializedAt;
    }

    public void setInitializedAt( Date initializedAt )
    {
        this.initializedAt = initializedAt;
    }

    public Date getStartedAt()
    {
        return startedAt;
    }

    public void setStartedAt( Date startedAt )
    {
        this.startedAt = startedAt;
    }

    public Date getLastConfigChange()
    {
        return lastConfigChange;
    }

    public void setLastConfigChange( Date lastConfigChange )
    {
        this.lastConfigChange = lastConfigChange;
    }

    public boolean isFirstStart()
    {
        return firstStart;
    }

    public void setFirstStart( boolean firstStart )
    {
        this.firstStart = firstStart;
    }

    public boolean isInstanceUpgraded()
    {
        return instanceUpgraded;
    }

    public void setInstanceUpgraded( boolean instanceUpgraded )
    {
        this.instanceUpgraded = instanceUpgraded;
    }

    public boolean isConfigurationUpgraded()
    {
        return configurationUpgraded;
    }

    public void setConfigurationUpgraded( boolean configurationUpgraded )
    {
        this.configurationUpgraded = configurationUpgraded;
    }

    public String getErrorCauseMessage()
    {
        return errorCauseMessage;
    }

    public void setErrorCauseMessage( String errorCauseMessage )
    {
        this.errorCauseMessage = errorCauseMessage;
    }
}
