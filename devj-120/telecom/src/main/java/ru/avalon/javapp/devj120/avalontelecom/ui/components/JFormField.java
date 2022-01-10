package ru.avalon.javapp.devj120.avalontelecom.ui.components;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.ClientTypeDependable;

import javax.swing.*;
import java.awt.*;

public class JFormField extends JPanel implements ClientTypeDependable {
    private int maskClientType;
    private final JTextField component;

    public JFormField(JTextField component, String title, ClientType... types) {
        super(new FlowLayout(FlowLayout.LEFT));
        if (types == null || component == null){
            throw new IllegalArgumentException("Type and component reference params are mandatory");
        }
        this.component = component;
        JLabel lbl = new JLabel(title);
        lbl.setLabelFor(component);
        super.add(lbl);
        super.add(component);

        this.maskClientType = 0b0;
        for (ClientType t: types){
            this.maskClientType = this.maskClientType | t.getMask();
        }
    }

    @Override
    public void setAvailable(boolean available) {
        this.setVisible(available);
    }

    @Override
    public boolean isSuitableForType(ClientType type){
        return (this.maskClientType & type.getMask()) == type.getMask();
    }
}
