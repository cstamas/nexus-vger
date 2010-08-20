package org.sonatype.nexus.vui.app;

import java.util.HashMap;
import java.util.Iterator;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class ModeSwitch extends CustomComponent {

    GridLayout layout = new GridLayout(3, 1);

    HashMap<Object, Button> idToButton = new HashMap<Object, Button>();
    Object mode = null;

    public ModeSwitch() {
        setSizeUndefined();
        layout.setSizeUndefined();
        setCompositionRoot(layout);
        setStyleName("ModeSwitch");
    }

    public Object getMode() {
        return mode;
    }

    public void setMode(Object mode) {
        if (idToButton.containsKey(mode)) {
            this.mode = mode;
            updateStyles();
            fireEvent(new ModeSwitchEvent());
        }
    }

    public void addListener(ModeSwitchListener listener) {
        super.addListener(listener);
    }

    public void addMode(Object id, String caption, String description,
            Resource icon) {
        if (idToButton.containsKey(id)) {
            removeMode(id);
        }
        Button b = new NativeButton();
        if (caption != null) {
            b.setCaption(caption);
        }
        if (description != null) {
            b.setDescription(description);
        }
        if (icon != null) {
            b.setIcon(icon);
        }
        b.setData(id);
        b.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                setMode(event.getButton().getData());
            }
        });
        idToButton.put(id, b);
        layout.addComponent(b);
        updateStyles();
    }

    public void removeMode(Object id) {
        Button b = idToButton.remove(id);
        layout.removeComponent(b);
        updateStyles();
    }

    private void updateStyles() {
        boolean first = true;
        for (Iterator<Component> it = layout.getComponentIterator(); it.hasNext();) {
            Component c = it.next();
            if (c instanceof Button) {
                Button b = (Button) c;
                String isOn = (b.getData() == mode ? "-on" : "");
                if (first) {
                    first = false;
                    b.setStyleName("first" + isOn);
                } else if (!it.hasNext()) {
                    b.setStyleName("last" + isOn);
                } else {
                    b.setStyleName("mid" + isOn);
                }
            }
        }
    }

    public interface ModeSwitchListener extends Listener {

    }

    public class ModeSwitchEvent extends Event {

        public ModeSwitchEvent() {
            super(ModeSwitch.this);
        }

        public Object getMode() {
            return ModeSwitch.this.getMode();
        }
    }

}
