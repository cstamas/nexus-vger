package org.sonatype.nexus.vui;

import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;

public interface FeatureSet
    extends Feature
{
    Collection<Feature> getFeatures();

    Feature getFeatureByPath( String path );

    HierarchicalContainer getContainer( boolean recurse );
}
