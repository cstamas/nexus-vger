package org.sonatype.nexus.vui.app;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Component;

interface FeatureList
    extends Component
{
    /**
     * Shows the given Features
     * 
     * @param c Container with Features to show.
     */
    public void setFeatureContainer( HierarchicalContainer c );
}
