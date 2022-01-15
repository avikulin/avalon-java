package ui.components;

import javax.swing.*;
import java.awt.*;

public class JFormField extends JPanel {
    private final JComponent component;

    public JFormField(JComponent component, String title) {
        super(new FlowLayout(FlowLayout.LEFT));
        if (component == null || title == null || title.isEmpty()){
            throw new IllegalArgumentException("Component and title reference params are mandatory");
        }

        this.component = component;
        JLabel lbl = new JLabel(title);
        lbl.setLabelFor(component);
        super.add(lbl);
        super.add(component);
    }

    public Component getComponent(){
        return this.component;
    }
}
