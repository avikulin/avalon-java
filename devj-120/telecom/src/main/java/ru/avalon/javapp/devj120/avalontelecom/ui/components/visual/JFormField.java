package ru.avalon.javapp.devj120.avalontelecom.ui.components.visual;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.Bindable;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.ClientTypeDependable;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.Validatable;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class JFormField<T> extends JPanel implements ClientTypeDependable, Validatable, Bindable<T> {
    private int maskClientType;
    private final JTextField component;
    private final Validator validator;
    private boolean isAvailable;

    public JFormField(String title, JTextField component, Validator validator, ClientType... types) {
        super(new FlowLayout(FlowLayout.LEFT));
        if (title == null){
            throw new IllegalArgumentException("Title string reference param must be not-null");
        }

        if (component == null){
            throw new IllegalArgumentException("Component reference param must be not-null");
        }

        if (validator == null){
            throw new IllegalArgumentException("Validator reference param must be not-null");
        }

        if (types == null){
            throw new IllegalArgumentException("Type reference params are mandatory");
        }

        this.component = component;
        this.validator = validator;

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

    @Override
    public void bind(Consumer<T> func) {

    }

    @Override
    public boolean validateValue() {
        return false;
    }
}
