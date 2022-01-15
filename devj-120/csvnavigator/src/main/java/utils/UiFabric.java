package utils;

import javax.swing.*;
import java.awt.event.ActionListener;

public class UiFabric {
    public static void addMenuItemTo(JMenu parent, String text, char mnemonic,
                                     KeyStroke accelerator, ActionListener al) {
        JMenuItem mi = new JMenuItem(text, mnemonic);
        mi.setAccelerator(accelerator);
        mi.addActionListener(al);
        parent.add(mi);
    }
}
