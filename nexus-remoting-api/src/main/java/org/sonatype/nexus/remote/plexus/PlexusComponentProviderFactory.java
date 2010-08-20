package org.sonatype.nexus.remote.plexus;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.core.spi.component.ioc.IoCManagedComponentProvider;

public class PlexusComponentProviderFactory
    implements IoCComponentProviderFactory
{
    private final ResourceConfig resourceConfig;

    private final PlexusContainer plexusContainer;

    private final HashMap<Class<?>, ComponentDescriptor<?>> componentsByImplementors;

    public PlexusComponentProviderFactory( ResourceConfig resourceConfig, ServletContext context )
    {
        this.resourceConfig = resourceConfig;

        this.plexusContainer = (PlexusContainer) context.getAttribute( PlexusConstants.PLEXUS_KEY );

        this.componentsByImplementors = new HashMap<Class<?>, ComponentDescriptor<?>>();

        registerComponents();
    }

    public ResourceConfig getResourceConfig()
    {
        return resourceConfig;
    }

    public PlexusContainer getPlexusContainer()
    {
        return plexusContainer;
    }

    private void registerComponents()
    {
        // TODO: a dirty solution! What should happen here, is actually to _enumerate_ all components (how?)
        // and as ResourceConfig is the class in question a provider or resource.
        List<ComponentDescriptor<?>> resources =
            getPlexusContainer().getComponentDescriptorList( PlexusResource.class.getName() );

        for ( ComponentDescriptor<?> cd : resources )
        {
            if ( ResourceConfig.isProviderClass( cd.getImplementationClass() )
                || ResourceConfig.isRootResourceClass( cd.getImplementationClass() ) )
            {
                getResourceConfig().getClasses().add( cd.getImplementationClass() );
                
                // and put it into map for later
                componentsByImplementors.put( cd.getImplementationClass(), cd );
            }
        }
    }

    public IoCComponentProvider getComponentProvider( Class<?> c )
    {
        return getComponentProvider( null, c );
    }

    public IoCComponentProvider getComponentProvider( ComponentContext cc, Class<?> c )
    {
        ComponentDescriptor<?> component = componentsByImplementors.get( c );

        if ( component == null )
        {
            return null;
        }

        return new PlexusComponentProvider( getPlexusContainer(), component );
    }

    public static class PlexusComponentProvider
        implements IoCManagedComponentProvider
    {
        private final PlexusContainer plexusContainer;

        private final ComponentDescriptor<?> component;

        public PlexusComponentProvider( PlexusContainer plexusContainer, ComponentDescriptor<?> component )
        {
            this.plexusContainer = plexusContainer;

            this.component = component;
        }

        public ComponentScope getScope()
        {
            if ( "per-lookup".equals( component.getInstantiationStrategy() ) )
            {
                return ComponentScope.PerRequest;
            }
            else
            {
                return ComponentScope.Singleton;
            }
        }

        public Object getInstance()
        {
            try
            {
                return plexusContainer.lookup( component.getRole(), component.getRoleHint() );
            }
            catch ( ComponentLookupException e )
            {
                throw new IllegalStateException( "Cannot get the component " + component.toString(), e );
            }
        }

        public Object getInjectableInstance( Object o )
        {
            return o;
        }
    }

}
