package ru.avalon.javapp.devj120.avalontelecom.ui;

import ru.avalon.javapp.devj120.avalontelecom.dal.ClientListTableModel;
import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;
import ru.avalon.javapp.devj120.avalontelecom.lists.ClientList;
import ru.avalon.javapp.devj120.avalontelecom.models.ClientInfo;
import ru.avalon.javapp.devj120.avalontelecom.models.PhoneNumber;

import javax.management.OperationsException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

/**
 * Application main window.
 */
public class MainFrame extends JFrame {
    public static final int MOUSE_DBL_CLICK = 2;

    private final ClientListTableModel clientsTableModel = new ClientListTableModel();
    private final JTable clientsTable = new JTable();

    public MainFrame() {
        super("AvalonTelecom Ltd. clients list");

        initMenu();
        initLayout();

        setBounds(300, 200, 600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    ClientList.getInstance().saveToFile();
                } catch (OperationsException operationsException) {
                    JOptionPane.showMessageDialog(e.getComponent(), operationsException.getMessage(),
                            "Save data error", JOptionPane.ERROR_MESSAGE);
                    operationsException.printStackTrace();
                }
            }
        });
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu operations = new JMenu("Operations");
        operations.setMnemonic('O');
        menuBar.add(operations);

        addMenuItemTo(operations, "Add", 'A',
                KeyStroke.getKeyStroke('A', InputEvent.ALT_DOWN_MASK),
                e -> addClient());

        addMenuItemTo(operations, "Change", 'C',
                KeyStroke.getKeyStroke('C', InputEvent.ALT_DOWN_MASK),
                e -> changeClient());

        addMenuItemTo(operations, "Delete", 'D',
                KeyStroke.getKeyStroke('D', InputEvent.ALT_DOWN_MASK),
                e -> delClient());

        setJMenuBar(menuBar);
    }

    /**
     * Auxiliary method, which creates menu item with specifies text, mnemonic and accelerator,
     * installs specified action listener to the item, and adds the item to the specified menu.
     *
     * @param parent menu, which the created item is added to
     */
    private void addMenuItemTo(JMenu parent, String text, char mnemonic,
                               KeyStroke accelerator, ActionListener al) {
        JMenuItem mi = new JMenuItem(text, mnemonic);
        mi.setAccelerator(accelerator);
        mi.addActionListener(al);
        parent.add(mi);
    }

    private void initLayout() {
        clientsTable.setModel(clientsTableModel);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == MOUSE_DBL_CLICK) {
                    JTable table = (JTable) e.getSource();
                    int rowId = table.rowAtPoint(e.getPoint());
                    table.setRowSelectionInterval(rowId, rowId);
                    changeClient();
                }
            }
        });

        add(clientsTable.getTableHeader(), BorderLayout.NORTH);
        add(new JScrollPane(clientsTable), BorderLayout.CENTER);
    }

    private void addClient() {
        ClientDialog clientDialog = new ClientDialog(this);
        while (clientDialog.showModal()) {
            try {
                ClientType type = clientDialog.getClientType();
                PhoneNumber pn = new PhoneNumber(clientDialog.getAreaCode(), clientDialog.getPhoneNum());
                String name = clientDialog.getClientName();
                String address = clientDialog.getClientAddr();

                switch (type) {
                    case PERSON: {
                        LocalDate birthDate = clientDialog.getBirthDate();
                        clientsTableModel.addIndividualPersonAccount(pn, name, address, birthDate);
                        return;
                    }
                    case COMPANY: {
                        String managerName = clientDialog.getManagerName();
                        String contactPersonName = clientDialog.getContactPersonName();
                        clientsTableModel.addCompanyAccount(pn, name, address, managerName, contactPersonName);
                        return;
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Client registration error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changeClient() {
        int seldRow = clientsTable.getSelectedRow();
        if (seldRow == -1)
            return;

        ClientInfo ci = clientsTableModel.getClient(seldRow);
        ClientDialog clientDialog = new ClientDialog(this, ci);
        if (clientDialog.showModal()) {
            try {
                ClientType type = clientDialog.getClientType();
                ci.setType(type);
                ci.setName(clientDialog.getClientName());
                ci.setAddress(clientDialog.getClientAddr());
                switch (type) {
                    case PERSON: {
                        ci.setBirthDate(clientDialog.getBirthDate());
                        break;
                    }
                    case COMPANY: {
                        ci.setManagerName(clientDialog.getManagerName());
                        ci.setContactPersonName(clientDialog.getContactPersonName());
                        break;
                    }
                }
                clientsTableModel.clientChanged(seldRow);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Client update error",
                        JOptionPane.ERROR_MESSAGE
                );
                clientDialog.showModal();
            }
        }
    }

    private void delClient () {
        int seldRow = clientsTable.getSelectedRow();
        if (seldRow == -1)
            return;

        ClientInfo ci = clientsTableModel.getClient(seldRow);
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete client\n"
                        + "with phone number " + ci.getPhoneNumber() + "?",
                "Delete confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            clientsTableModel.dropClient(seldRow);
        }
    }
    }
