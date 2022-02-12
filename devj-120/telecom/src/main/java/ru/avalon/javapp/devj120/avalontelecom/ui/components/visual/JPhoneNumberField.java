package ru.avalon.javapp.devj120.avalontelecom.ui.components.visual;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.ClientTypeDependable;

import javax.swing.*;
import java.awt.*;

public class JPhoneNumberField extends JPanel implements ClientTypeDependable {
    private final JTextField areaCode;
    private final JTextField phoneNumber;
    private int maskClientType;

    public JPhoneNumberField(ClientType... types) {
        super(new FlowLayout(FlowLayout.LEFT));
        areaCode = new JTextField(4);
        phoneNumber = new JTextField(8);

        JLabel lbl = new JLabel("Phone number (");
        lbl.setLabelFor(areaCode);
        super.add(lbl);
        super.add(areaCode);
        lbl = new JLabel(")");
        lbl.setLabelFor(phoneNumber);
        super.add(lbl);
        super.add(phoneNumber);

        this.maskClientType = 0b0;
        for (ClientType t: types){
            this.maskClientType = this.maskClientType | t.getMask();
        }
    }

    public String getAreaCode(){
        return areaCode.getText();
    }

    public void setAreaCode(String value){
        areaCode.setText(value);
    }

    public String getPhoneNumber(){
        return phoneNumber.getText();
    }

    public void setPhoneNumber(String value){
        phoneNumber.setText(value);
    }

    public void setEditable(boolean enabled){
        this.areaCode.setEditable(enabled);
        this.phoneNumber.setEditable(enabled);
    }

    @Override
    public void setAvailable(boolean available) {
        this.setVisible(available);
    }

    @Override
    public boolean isSuitableForType(ClientType type) {
        return (this.maskClientType & type.getMask()) == type.getMask();
    }
}
