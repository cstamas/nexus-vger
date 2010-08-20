package org.sonatype.nexus.vui;

import org.sonatype.nexus.vui.app.APIResource;
import org.sonatype.nexus.vui.app.NamedExternalResource;
import org.sonatype.nexus.vui.app.NexusVuiApplication;

import com.vaadin.ui.Component;

public abstract class AbstractFeature
    implements Feature
{
    public static final Object PROPERTY_ICON = "Icon";

    public static final Object PROPERTY_NAME = "Name";

    public static final Object PROPERTY_DESCRIPTION = "Description";

    /**
     * Gets the name of this feature. Try not to exceed 25 characters too much.
     * 
     * @return
     */
    abstract public String getName();

    /**
     * Gets the description for this feature. Should describe what the example intends to showcase. May contain HTML.
     * 100 words should be enough, and about 7 rows...
     * 
     * @return the description
     */
    abstract public String getDescription();

    /**
     * Gets related resources, i.e links to resources related to the example.
     * <p>
     * Good candidates are resources used to make the example (CSS, images, custom layouts), documentation links
     * (reference manual), articles (e.g. pattern description, usability discussion).
     * </p>
     * <p>
     * May return null, if the example has no related resources.
     * </p>
     * <p>
     * The name of the NamedExternalResource will be shown in the UI. <br/>
     * Note that Javadoc should be referenced via {@link #getRelatedAPI()}.
     * </p>
     * 
     * @see #getThemeBase()
     * @return related external stuff
     */
    abstract public NamedExternalResource[] getRelatedResources();

    /**
     * Gets related API resources, i.e links to javadoc of used classes.
     * <p>
     * Good candidates are Vaadin classes being demoed in the example, or other classes playing an important role in the
     * example.
     * </p>
     * <p>
     * May return null, if the example uses no interesting classes.
     * <p>
     * 
     * @return
     */
    abstract public APIResource[] getRelatedAPI();

    /**
     * Gets related Features; the classes returned should extend Feature.
     * <p>
     * Good candidates are Features similar to this one, Features using the functionality demoed in this one, and
     * Features being used in this one.
     * </p>
     * <p>
     * May return null, if no other Features are related to this one.
     * <p>
     * 
     * @return
     */
    abstract public Class<? extends Feature>[] getRelatedFeatures();

    /**
     * Get the example instance. Override if instantiation needs parameters.
     * 
     * @return
     */
    public abstract Component getUI();

    /**
     * Gets the name of the icon for this feature, usually simpleName + extension.
     * 
     * @return
     */
    public String getIconName()
    {
        String iconName = getClass().getSimpleName() + ".gif";

        if ( getClass().getResourceAsStream( iconName ) != null )
        {
            return iconName;
        }
        else
        {
            return "/default-feature.gif";
        }
    }

    public String getExampleSource()
    {
        return null;
    }

    /**
     * Gets the name used when resolving the path for this feature. Usually no need to override.
     * 
     * @return
     */
    public String getPathName()
    {
        return getClass().getSimpleName();
    }

    public int getOrderRank()
    {
        return ORDER_RANK_UNDEFINED;
    }

    /**
     * Returns the Vaadin version number in which this feature was added to Sampler. Usually features should only be
     * added in major and minor version, not in maintenance versions. Uses int internally for easy comparison: version
     * 6.1.4 -> 61 (maintenance version number is ignored) Override in each feature. Returns Version.OLD otherwise.
     * 
     * @return Version Vaadin version when this feature was added to Sampler
     */
    public Version getSinceVersion()
    {
        return Version.OLD;
    }

    // ==

    //protected static final String getThemeBase()
    // {
    //    return NexusVuiApplication.getThemeBase();
    // }

    // ==

    @Override
    public String toString()
    {
        return getName();
    }

    @Override
    public boolean equals( Object obj )
    {
        // A feature is uniquely identified by its class name
        if ( obj == null )
        {
            return false;
        }
        return obj.getClass() == getClass();
    }

    @Override
    public int hashCode()
    {
        // A feature is uniquely identified by its class name
        return getClass().hashCode();
    }
}
