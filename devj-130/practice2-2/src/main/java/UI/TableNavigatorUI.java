package UI;

import UI.CustonRenderers.DateTimeCellRenderer;
import dal.CachingDataSource;
import dal.DbContext;
import interfaces.dal.DbConnection;
import model.ArbitraryTableModel;
import model.TableListModel;
import utils.Tracer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.Constants.MSG_NODATACONTEXT;

public class TableNavigatorUI extends JFrame {
    private DbConnection connection;
    private ArbitraryTableModel selectedTableModel;
    private TableListModel tablesListModel;
    private JTextField fieldConnectionString;
    private JButton btnMakeConnection;
    private JTable dataTable;
    private JTableHeader dataHeaders;
    private JScrollPane gridScrollPane;
    private JComboBox<String> cbDbTables;
    private JTextArea txtStatusInfo;
    private JTabbedPane tabbedPane;
    private Pattern checkConnectionString;

    public TableNavigatorUI() {
        super("Навигатор по таблицам базы данных");
        setBounds(300, 200, 600, 400);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initLayout();
        initControls();
        checkConnectionString = Pattern.compile(
                "^jdbc:derby:[A-Za-z0-9,/:;=.()]+$",
                Pattern.CASE_INSENSITIVE
        );
    }

    private void initLayout() {
        JLabel lbConnectionString = new JLabel("Строка подключения:");
        fieldConnectionString = new JTextField();
        fieldConnectionString.setToolTipText("Формат: jdbc:derby://<адрес сервера>:<порт>/<название БД>");
        fieldConnectionString.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectToDb(fieldConnectionString.getText());
                }
            }
        });
        JLabel lbStatusInfo = new JLabel("Диагностическая информация:", JLabel.LEFT);

        txtStatusInfo = new JTextArea();
        txtStatusInfo.setLineWrap(true);
        JScrollPane diagInfoScrollPane = new JScrollPane(txtStatusInfo);
        diagInfoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        diagInfoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        btnMakeConnection = new JButton("Connect");
        btnMakeConnection.addActionListener(x -> connectToDb(fieldConnectionString.getText()));

        dataTable = new JTable();
        dataHeaders = dataTable.getTableHeader();
        gridScrollPane = new JScrollPane(dataTable);
        gridScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(dataHeaders, BorderLayout.NORTH);
        tablePanel.add(gridScrollPane, BorderLayout.CENTER);

        cbDbTables = new JComboBox<>();
        cbDbTables.addItem(MSG_NODATACONTEXT);
        JPanel panelSelectedTable = new JPanel(new BorderLayout());
        panelSelectedTable.add(new JLabel("Отображаемая таблица БД: "), BorderLayout.WEST);
        panelSelectedTable.add(cbDbTables, BorderLayout.CENTER);

        JPanel gridDataViewPanel = new JPanel(new BorderLayout());
        gridDataViewPanel.add(panelSelectedTable, BorderLayout.NORTH);
        gridDataViewPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel diagnosticInfo = new JPanel(new BorderLayout());
        diagnosticInfo.add(lbStatusInfo, BorderLayout.NORTH);
        diagnosticInfo.add(diagInfoScrollPane, BorderLayout.CENTER);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Данные", gridDataViewPanel);
        tabbedPane.addTab("Журнал", diagnosticInfo);
        tabbedPane.addChangeListener(x -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                txtStatusInfo.setText(Tracer.get().getLog());
                DefaultCaret caret = (DefaultCaret) txtStatusInfo.getCaret();
                caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            }
        });
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(groupLayout);

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                groupLayout.createSequentialGroup()
                                        .addComponent(lbConnectionString)
                                        .addComponent(fieldConnectionString)
                                        .addComponent(btnMakeConnection)
                        )
                        .addComponent(tabbedPane)
        );

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(
                                groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbConnectionString)
                                        .addComponent(fieldConnectionString)
                                        .addComponent(btnMakeConnection)
                        )
                        .addComponent(tabbedPane)
        );
        this.pack();
    }

    private void initControls() {
        fieldConnectionString.setText("jdbc:derby://localhost:1527/?");
        btnMakeConnection.setText("Соединиться");

        DefaultTableModel defaultTableModel = new DefaultTableModel();
        defaultTableModel.setColumnCount(0);
        dataTable.setModel(defaultTableModel);
        dataTable.setDefaultRenderer(Time.class, new DateTimeCellRenderer(true));
        dataTable.setDefaultRenderer(Date.class, new DateTimeCellRenderer(false));
        dataHeaders = dataTable.getTableHeader();

        gridScrollPane.getHorizontalScrollBar().setValue(0);
        gridScrollPane.getVerticalScrollBar().setValue(0);

        DefaultComboBoxModel<String> defaultListModel = new DefaultComboBoxModel<>();
        defaultListModel.addElement(MSG_NODATACONTEXT);
        cbDbTables.setModel(defaultListModel);
        cbDbTables.setEnabled(false);
        cbDbTables.addActionListener(x -> setSelectedTable((String) cbDbTables.getSelectedItem()));

        txtStatusInfo.setText("");
        tabbedPane.setEnabled(false);
        connection = DbContext.get();
    }

    private boolean checkConnectionString(String s) {
        Matcher matcher = checkConnectionString.matcher(s.trim());
        return matcher.matches();
    }

    private void connectToDb(String s) {
        if (connection.isReady()) {
            try {
                connection.disconnect();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ощибка разъединения с БД:\n".concat(e.getMessage()),
                        "Ошибка разъединения",
                        JOptionPane.ERROR_MESSAGE);
                Tracer.get().logError(this.getClass(), e, "Ошибка разъединения с БД");
                return;
            }

            initControls();
            return;
        }

        if (!checkConnectionString(s)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Неправильный формат строки подключения.\n" +
                            "Строка подключения должна соответствовать шаблону: \n " +
                            "jdbc:derby://<адрес сервера>:<порт>/<название БД> \n" +
                            "Пример: jdbc:derby://localhost.company.com:1527/db1;user=demo;password=demo;create=true",
                    "Ошибка подключения",
                    JOptionPane.ERROR_MESSAGE);
            Tracer.get().logError(
                    this.getClass(),
                    "Некорректный формат строки подключения: "
                            .concat(s));
            return;
        }

        try {
            connection.connectTo(s);
            tablesListModel = new TableListModel(connection);
            cbDbTables.setModel(tablesListModel);
        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ощибка подключения к БД:\n".concat(e.getMessage()),
                    "Ошибка подключения",
                    JOptionPane.ERROR_MESSAGE);
            Tracer.get().logError(this.getClass(), e, "Ошибка подключения к БД");
            return;
        }
        btnMakeConnection.setText("Отключиться");
        cbDbTables.setEnabled(true);
        cbDbTables.setPopupVisible(true);
        cbDbTables.requestFocus();
    }

    private void setSelectedTable(String tableName) {
        if (tableName != null) {
            try {
                connection.setCurrentTable(tableName);
                selectedTableModel = new ArbitraryTableModel(new CachingDataSource(connection));
                dataTable.setModel(selectedTableModel);
                gridScrollPane.getHorizontalScrollBar().setValue(0);
                gridScrollPane.getVerticalScrollBar().setValue(0);
                tabbedPane.setEnabled(true);
            } catch (IllegalArgumentException | IllegalStateException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ощибка БД:\n".concat(e.getMessage()),
                        "Ошибка выполнения операции",
                        JOptionPane.ERROR_MESSAGE);
                Tracer.get().logError(this.getClass(), e, "Ошибка подключения к БД");
                return;
            }
        }
    }
}
