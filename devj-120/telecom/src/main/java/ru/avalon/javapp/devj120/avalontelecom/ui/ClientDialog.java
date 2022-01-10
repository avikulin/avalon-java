package ru.avalon.javapp.devj120.avalontelecom.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.zip.DataFormatException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;
import ru.avalon.javapp.devj120.avalontelecom.enums.OperationType;
import ru.avalon.javapp.devj120.avalontelecom.interfaces.ClientTypeDependable;
import ru.avalon.javapp.devj120.avalontelecom.models.ClientInfo;
import ru.avalon.javapp.devj120.avalontelecom.ui.components.JFormField;
import ru.avalon.javapp.devj120.avalontelecom.ui.components.JPhoneNumberField;

import static ru.avalon.javapp.devj120.avalontelecom.constants.Constants.DATE_STR_FORMAT;
import static ru.avalon.javapp.devj120.avalontelecom.constants.Constants.MIN_YEAR_ALLOWED;

/**
 * Dialog window for client attributes entering/editing.
 */
public class ClientDialog extends JDialog {
	private final JComboBox<ClientType> cbClientType;
	private JPanel dataFieldsPane;
	private final JPhoneNumberField phoneNumberField;
	private final JTextField clientNameField;
	private final JTextField clientAddrField;
	private final JFormattedTextField birthDateField;
	private final JTextField managerNameField;
	private final JTextField contactPersonNameField;
	private final JTextField registrationDateField;
	
	/**
	 * Set to {@code true}, when a user closes the dialog with "OK" button. 
	 * This field value is returned by {@link #showModal} method.
	 */
	private boolean okPressed;

	private OperationType operationType;

	/**
	 * Constructs the dialog.
	 * 
	 * @param owner dialog window parent container
	 */
	private ClientDialog(JFrame owner, OperationType operationType){
		super(owner, true);
		this.operationType = operationType;
		cbClientType = new JComboBox<>(
				new ClientType[] {
						ClientType.PERSON,
						ClientType.COMPANY
				}
		);
		if (operationType == OperationType.CREATE) {
			cbClientType.setSelectedIndex(0);
		}

		cbClientType.addActionListener(x->{
			ClientType type = (ClientType) cbClientType.getSelectedItem();
			setDialogMode(type);
		});

		phoneNumberField = new JPhoneNumberField(ClientType.PERSON, ClientType.COMPANY);
		clientNameField = new JTextField(30);
		clientAddrField = new JTextField(30);

		MaskFormatter mask = null;
		try {
			mask = new MaskFormatter("##.##.####");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mask.setPlaceholderCharacter('_');
		birthDateField = new JFormattedTextField(mask);


		birthDateField.setColumns(10);

		managerNameField = new JTextField(30);
		contactPersonNameField = new JTextField(30);

		registrationDateField = new JTextField(10);
		registrationDateField.setEditable(false);

		initLayout();
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				ClientDialog dlg = (ClientDialog) e.getWindow();
				if (dlg.getDialogOperation() == OperationType.CREATE){
					dlg.cbClientType.setPopupVisible(true);
				}
			}
		});
	}

	private void setDialogMode(ClientType type){
		cbClientType.setSelectedItem(type);
		for (Component c: dataFieldsPane.getComponents()){
			ClientTypeDependable ctd = (ClientTypeDependable)c;
			ctd.setAvailable(ctd.isSuitableForType(type));
		}
		this.pack();
	}

	/**
	 * Prepares dialog for entering of data about new client.
	 * Clears all dialog controls; enables phone number fields.
	 */
	public ClientDialog(JFrame owner) {
		this(owner, OperationType.CREATE);

		setTitle("New client registration");

		phoneNumberField.setAreaCode("");
		phoneNumberField.setPhoneNumber("");
		clientNameField.setText("");
		clientAddrField.setText("");

		birthDateField.setText("");
		managerNameField.setText("");
		contactPersonNameField.setText("");
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		registrationDateField.setText(LocalDate.now().format(format));

		phoneNumberField.setEditable(true);
	}

	/**
	 * Prepares dialog for editing of information about client.
	 * Fills dialog controls with data from provided client information object;
	 * disables phone number fields.
	 *
	 * @param clientInfo client information used to fill dialog controls
	 */
	public ClientDialog(JFrame owner, ClientInfo clientInfo) {
		this(owner, OperationType.UPDATE);

		setTitle("Client properties change");

		phoneNumberField.setAreaCode(clientInfo.getPhoneNumber().getAreaCode());
		phoneNumberField.setPhoneNumber(clientInfo.getPhoneNumber().getLocalNum());
		phoneNumberField.setEditable(false);

		clientNameField.setText(clientInfo.getName());
		clientAddrField.setText(clientInfo.getAddress());

		DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_STR_FORMAT);

		if (clientInfo.getType()==ClientType.PERSON) {
			birthDateField.setText(clientInfo.getBirthDate().format(format));
			setDialogMode(ClientType.PERSON);
		}

		if (clientInfo.getType()==ClientType.COMPANY) {
			managerNameField.setText(clientInfo.getManagerName());
			contactPersonNameField.setText(clientInfo.getContactPersonName());
			setDialogMode(ClientType.COMPANY);
		}

		registrationDateField.setText(clientInfo.getRegDate().format(format));
	}

	/**
	 * Returns dialog operation type: create new record / update existing record
	 * @return
	 */
	public OperationType getDialogOperation(){
		return this.operationType;
	}

	/**
	 * Initializes dialog layout and puts required controls onto the dialog.
	 */
	private void initLayout() {
		initControls();
		initOkCancelButtons();
	}

	/**
	 * Creates layout for client information input fields and adds the fields
	 * to the dialog controls hierarchy.
	 */
	private void initControls() {
		JPanel controlsPane = new JPanel(null);
		controlsPane.setLayout(new BoxLayout(controlsPane, BoxLayout.Y_AXIS));

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lbl = new JLabel("Customer type");
		lbl.setLabelFor(cbClientType);
		p.add(lbl);
		p.add(cbClientType);
		controlsPane.add(p);

		if (operationType == OperationType.CREATE){
			//cbClientType.setPopupVisible(true);
			cbClientType.requestFocus();
		} else {
			clientNameField.requestFocusInWindow();
		}

		dataFieldsPane = new JPanel(null);
		dataFieldsPane.setLayout(new BoxLayout(dataFieldsPane, BoxLayout.Y_AXIS));

		dataFieldsPane.add(phoneNumberField);
		dataFieldsPane.add(new JFormField(clientNameField,"Customer name",ClientType.PERSON, ClientType.COMPANY));
		dataFieldsPane.add(new JFormField(clientAddrField,"Customer address",ClientType.PERSON, ClientType.COMPANY));
		dataFieldsPane.add(new JFormField(birthDateField,"Customer's date of birth",ClientType.PERSON));
		dataFieldsPane.add(new JFormField(managerNameField,"Manager's name",ClientType.COMPANY));
		dataFieldsPane.add(new JFormField(contactPersonNameField,"Contact person's name",ClientType.COMPANY));
		dataFieldsPane.add(new JFormField(registrationDateField,"Registration date",ClientType.PERSON, ClientType.COMPANY));

		controlsPane.add(dataFieldsPane);
		add(controlsPane, BorderLayout.CENTER);

		this.pack();
	}

	/**
	 * Creates bottom panel with "OK" and "Cancel" buttons.
	 */
	private void initOkCancelButtons() {
		JPanel btnsPane = new JPanel();
	
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(e -> {
			okPressed = true;
			setVisible(false);
		});
		okBtn.setDefaultCapable(true);
		btnsPane.add(okBtn);
		
		Action cancelDialogAction = new AbstractAction("Cancel") {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		}; 
		
		JButton cancelBtn = new JButton(cancelDialogAction);
		btnsPane.add(cancelBtn);
		
		add(btnsPane, BorderLayout.SOUTH);
		
		final String esc = "escape";
		getRootPane()
			.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
			.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), esc);
		getRootPane()
			.getActionMap()
			.put(esc, cancelDialogAction);
	}
	
	/**
	 * Shows dialog, and returns, when the user finishes his work with the dialog.
	 * Returns {@code true}, if the user closed the dialog with "OK" button, 
	 * {@code false} otherwise. 
	 * 
	 * @return {@code true}, if a user closed the dialog with "OK" button,
	 * 		{@code false} otherwise (with "Cancel" button, or with system close options).
	 */
	public boolean showModal() {
		pack();
		setLocationRelativeTo(getOwner());

		okPressed = false;
		setVisible(true);
		return okPressed;
	}

	/**
	 * Returns value of area code entered by a user.
	 */
	public String getAreaCode() {
		return phoneNumberField.getAreaCode();
	}
	
	/**
	 * Returns value of phone local number entered by a user.
	 */
	public String getPhoneNum() {
		return phoneNumberField.getPhoneNumber();
	}
	
	/**
	 * Returns value of client name entered by a user.
	 */
	public String getClientName() {
		return clientNameField.getText();
	}
	
	/**
	 * Returns value of client address entered by a user.
	 */
	public String getClientAddr() {
		return clientAddrField.getText();
	}

	public ClientType getClientType() {
		return (ClientType) cbClientType.getSelectedItem();
	}

	public LocalDate getBirthDate() {
		try {
			String strDate = birthDateField.getText();
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_STR_FORMAT);
			LocalDate date = LocalDate.parse(strDate, dateFormat);
			return date;
		}catch (DateTimeParseException exception){
			throw new IllegalStateException("Incorrect date of birth value");
		}
	}

	public String getManagerName() {
		return managerNameField.getText();
	}

	public String getContactPersonName() {
		return contactPersonNameField.getText();
	}
}
