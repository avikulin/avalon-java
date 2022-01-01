package UI;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

import static constants.Constants.MSG_NO_DATACONTEXT;

public class MainWindow extends JFrame {
    private String connectionStr;
    private TableModel tableModel;
    public MainWindow(){
        super("Database table view");
        setBounds(300, 200, 600,400);
        setMinimumSize(new Dimension(600,400));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel lbConnectionString = new JLabel("Строка подключения:");
        JTextField fieldConnectionString = new JTextField();

        JLabel lbTableName = new JLabel("Название таблицы:");
        JTextField fieldTableName = new JTextField();

        JLabel lbStatusInfo = new JLabel("Диагностическая информация:", JLabel.LEFT);

        JTextArea txtStatusInfo = new JTextArea();
        txtStatusInfo.setLineWrap(true);
        JScrollPane diagInfoScrollPane = new JScrollPane(txtStatusInfo);
        diagInfoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        diagInfoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton btnTestConnection = new JButton("Connect");

        JTable dataTable = new JTable();
        JTableHeader dataHeaders = dataTable.getTableHeader();
        JScrollPane gridScrollPane = new JScrollPane(dataTable);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(dataHeaders, BorderLayout.NORTH);
        tablePanel.add(gridScrollPane, BorderLayout.CENTER);

        JComboBox<String> cbDbTables = new JComboBox<>();
        cbDbTables.addItem(MSG_NO_DATACONTEXT);

        JPanel gridDataViewPanel = new JPanel(new BorderLayout());
        gridDataViewPanel.add(cbDbTables, BorderLayout.NORTH);
        gridDataViewPanel.add(tablePanel, BorderLayout.CENTER);


        JPanel diagnosticInfo = new JPanel(new BorderLayout());
        diagnosticInfo.add(lbStatusInfo, BorderLayout.NORTH);
        diagnosticInfo.add(diagInfoScrollPane, BorderLayout.CENTER);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Данные", gridDataViewPanel);
        tabbedPane.addTab("Журнал", diagnosticInfo);

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
                                .addComponent(btnTestConnection)
                        )
                        .addComponent(tabbedPane)
        );

        groupLayout.setVerticalGroup(
                  groupLayout.createSequentialGroup()
                      .addGroup(
                          groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(lbConnectionString)
                            .addComponent(fieldConnectionString)
                            .addComponent(btnTestConnection)
                      )
                      .addComponent(tabbedPane)
        );
        this.pack();
    }
}
