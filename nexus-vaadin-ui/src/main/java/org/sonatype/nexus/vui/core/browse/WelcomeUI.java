package org.sonatype.nexus.vui.core.browse;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.nexus.vui.UIComponent;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@Component( role = UIComponent.class, hint = WelcomeFeature.HINT )
public class WelcomeUI
    implements UIComponent
{
    public com.vaadin.ui.Component getUI()
    {
        VerticalLayout vl = new VerticalLayout();

        vl.setSpacing( true );

        vl.addComponent( new Label( "<h3>Nexus | the repository manager</h3>", Label.CONTENT_XHTML ) );
        vl
            .addComponent( new Label(
                "Nexus manages software \"artifacts\" required for development, deployment, and provisioning. If you develop software, Nexus can help you share those artifacts with other developers and end-users.",
                Label.CONTENT_XHTML ) );
        vl
            .addComponent( new Label(
                "Maven’s central repository has always served as a great convenience for users of Maven, but it has always been recommended to maintain your own repositories to ensure stability within your organization. Nexus greatly simplifies the maintenance of your own internal repositories and access to external repositories. With Nexus you can completely control access to, and deployment of, every artifact in your organization from a single location. Now you can setup a Maven Repository in just minutes with Nexus.",
                Label.CONTENT_XHTML ) );

        return vl;
    }
}
