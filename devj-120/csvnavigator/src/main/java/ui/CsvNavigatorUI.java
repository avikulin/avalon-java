package ui;

import contracts.CachedDataSource;
import contracts.DiagnosticLogger;
import contracts.SourceRepo;
import contracts.Tokenizer;
import dal.CsvDataSource;
import dal.CsvFileRepo;
import enums.DlgResult;
import model.CsvTableModel;
import ui.components.JCsvFileChooser;
import utils.SequenceTokenizer;
import utils.Tracer;
import utils.UiFabric;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static constants.Constants.*;

public class CsvNavigatorUI extends JFrame {
    private JTable dataGrid;
    private final CsvTableModel csvModel;
    private JLabel fileName;
    private final Tokenizer tokenizer;
    private final CachedDataSource dataSource;
    private int maxColumnsToFitInWindow;
    private final DiagnosticLogger logger;

    public CsvNavigatorUI() {
        super("CSV-files navigator");
        setBounds(300, 200, 600, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        SourceRepo repo = new CsvFileRepo();
        this.tokenizer = new SequenceTokenizer(SEPARATOR_COMMA_SYMBOL);
        this.dataSource = new CsvDataSource(repo, tokenizer);
        this.dataSource.setHeaderRowPresent(DEFAULT_HAS_HEADER_ROW);
        this.maxColumnsToFitInWindow = DEFAULT_MAX_COLUMNS_TO_FIT;
        this.csvModel = new CsvTableModel(dataSource);
        this.logger = Tracer.get();

        initLayout();

        logger.logInfo(this.getClass(), "Instance has been created");
    }

    private void initLayout(){
        JMenuBar menuBar = new JMenuBar();
        JMenu operations = new JMenu("Operations");
        operations.setMnemonic('O');
        menuBar.add(operations);

        UiFabric.addMenuItemTo(operations, "Open", 'O',
                KeyStroke.getKeyStroke('O', InputEvent.ALT_DOWN_MASK),
                e -> {
                    String currentDir = System.getProperty("user.dir");
                    JCsvFileChooser fileChooser = new JCsvFileChooser(new File(currentDir));

                    int ret = fileChooser.showDialog(this, "Open CSV-file");
                    if (ret == JFileChooser.APPROVE_OPTION){
                        char delimiter = fileChooser.getDelimiter();
                        boolean hasTitles = fileChooser.hasTitleRow();
                        String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                        logger.logInfo(
                                this.getClass(),
                                String.format(
                                        "Opening file to load data: %s (delimiter = '%c', title row present = %b)",
                                        fileName,
                                        delimiter,
                                        hasTitles)
                        );
                        try {
                            openFile(fileChooser.getSelectedFile(), delimiter, hasTitles);
                        }catch (Exception ex){
                            String msg = "Unexpected error during the reading of the CSV-file: \n"
                                    .concat(fileName);

                            Tracer.get().logError(this.getClass(), ex, msg);
                            JOptionPane.showMessageDialog(
                                    this,
                                    msg,
                                    "Read CSV-file error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );

        UiFabric.addMenuItemTo(operations, "Close", 'C',
                KeyStroke.getKeyStroke('C', InputEvent.ALT_DOWN_MASK),
                e -> resetDataGrid()
        );

        UiFabric.addMenuItemTo(operations, "Exit", 'E',
                KeyStroke.getKeyStroke('E', InputEvent.ALT_DOWN_MASK),
                e -> {
                        int res = JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure to exit?",
                                "Exit confirmation",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (res == JOptionPane.YES_OPTION) {
                            logger.logInfo(this.getClass(), "Closing the program");
                            this.setVisible(false);
                            this.dispose();
                        }
                }
        );

        JMenu settings = new JMenu("Settings");
        settings.setMnemonic('S');
        UiFabric.addMenuItemTo(settings, "Change settings", 'S',
                KeyStroke.getKeyStroke('S', InputEvent.ALT_DOWN_MASK),
                e -> {
                    CsvOptionsDialog dialog = new CsvOptionsDialog(
                            this,
                            "Manage settings",
                            tokenizer.getCurrentDelimiter(),
                            dataSource.isHeaderRowPresent(),
                            this.maxColumnsToFitInWindow,
                            dataSource.getColumnNamesTemplate()
                            );
                    DlgResult res = dialog.showModal();
                    if (res == DlgResult.APPROVED){
                        logger.logInfo(this.getClass(), "New settings received: ".concat(dialog.toString()));
                        char delimiter = dialog.getDelimiter();
                        boolean hasTitles = dialog.hasTitleRow();
                        int maxColumnsToFit = dialog.getMaxColumnsToFit();
                        String columnNameTemplate = dialog.getColumnNameTemplate();
                        tokenizer.setDelimiterSymbol(delimiter);
                        dataSource.setHeaderRowPresent(hasTitles);
                        dataSource.setColumnNameTemplate(columnNameTemplate);
                        this.maxColumnsToFitInWindow = maxColumnsToFit;

                        try {
                            dataSource.reloadDataFromFile();
                            csvModel.invalidateCache();
                            int autoFitValue = (dataSource.getNumberOfColumns() > maxColumnsToFit) ?
                                                JTable.AUTO_RESIZE_OFF : JTable.AUTO_RESIZE_ALL_COLUMNS;
                            logger.logInfo(
                                    this.getClass(),
                                    "Grid auto-fit enabled: "
                                            .concat(
                                                    Integer.toString(autoFitValue)
                                            )
                            );
                            dataGrid.setAutoResizeMode(autoFitValue);
                            dataGrid.repaint();
                        } catch (Exception ex) {
                            String msg = "Unexpected error during the applying settings: \n"
                                    .concat("\n")
                                    .concat(ex.getMessage());

                            Tracer.get().logError(this.getClass(), ex, msg);
                            JOptionPane.showMessageDialog(
                                    this,
                                    msg,
                                    "Apply new settings error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }

                });

        UiFabric.addMenuItemTo(settings, "View logs", 'L',
                KeyStroke.getKeyStroke('L', InputEvent.ALT_DOWN_MASK),
                e -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm-ss.SSS");

                    String fileName = String.format("Log %s.txt", LocalDateTime.now().format(formatter));
                    try {
                        logger.saveToFile(fileName);
                        java.awt.Desktop.getDesktop().edit(new File(fileName));
                    } catch (IllegalArgumentException | IOException ioException) {
                        String msg = "Save log data error: \n"
                                .concat("\n")
                                .concat(ioException.getMessage());

                        Tracer.get().logError(this.getClass(), ioException, msg);
                        JOptionPane.showMessageDialog(
                                this,
                                msg,
                                "Save data error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        ioException.printStackTrace();
                    }
                }
        );
        menuBar.add(settings);

        JMenu about = new JMenu("Help");
        about.setMnemonic('H');
        UiFabric.addMenuItemTo(about, "About", 'A',
                KeyStroke.getKeyStroke('A', InputEvent.ALT_DOWN_MASK),
                e -> {
                    JOptionPane.showMessageDialog(
                            this,
                            "CSV-file navigator.\n" +
                            "Version: 0.0.1-alpha.\n" +
                            "© 2022 Avalon Software Inc.",
                            "About the program",
                            JOptionPane.INFORMATION_MESSAGE);

                });

        menuBar.add(about);

        setJMenuBar(menuBar);

        dataGrid = new JTable();
        dataGrid.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        dataGrid.setModel(this.csvModel);
        JTableHeader dataHeaders = dataGrid.getTableHeader();

        JScrollPane gridScrollPane = new JScrollPane(dataGrid);
        gridScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(dataHeaders, BorderLayout.NORTH);
        tablePanel.add(gridScrollPane, BorderLayout.CENTER);

        fileName = new JLabel(MSG_NO_DATA_LOADED);
        add(tablePanel, BorderLayout.CENTER);
        fileName.setFont(this.getContentPane().getFont().deriveFont(Font.ITALIC, 18f));
        fileName.setHorizontalAlignment(SwingConstants.CENTER);
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        fileName.setPreferredSize(new Dimension(this.getWidth(), 48));
        add(fileName, BorderLayout.NORTH);
    }

    private void resetDataGrid() {
        logger.logInfo(this.getClass(), "Resetting data-grid");
        dataSource.reset();
        csvModel.invalidateCache();
        dataGrid.repaint();
        this.fileName.setText(MSG_NO_DATA_LOADED);

    }

    public void openFile(File file, char delimiter, boolean hasTiles){
        if (file == null){
            throw new NullPointerException("File reference must be not null");
        }

        tokenizer.setDelimiterSymbol(delimiter);
        dataSource.setHeaderRowPresent(hasTiles);

        try {
            dataSource.readDataFromFile(file);
            dataGrid.setAutoResizeMode(
                    (dataSource.getNumberOfColumns() > this.maxColumnsToFitInWindow) ? JTable.AUTO_RESIZE_OFF
                            : JTable.AUTO_RESIZE_ALL_COLUMNS
            );

            csvModel.invalidateCache();
            dataGrid.repaint();
            fileName.setText(String.format("Currently opened file: %s", dataSource.getTableName()));

            logger.logInfo(this.getClass(), "File has been successfully processed");
        } catch (Exception e) {
            String msg = "Unexpected error during the data loading from file: \n"
                    .concat(file.getAbsolutePath())
                    .concat("\n\n")
                    .concat(e.getMessage());

            logger.logError(this.getClass(), e, msg);
            JOptionPane.showMessageDialog(this, msg, "Data load error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
