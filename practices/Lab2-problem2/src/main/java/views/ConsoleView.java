package views;

import interfaces.Repository;
import interfaces.View;
import interfaces.base.FileSystemObject;
import interfaces.base.Viewable;

public class ConsoleView implements View {

    private static final int MIN_COLUMN_WIDTH = 10;
    private static final int MAX_TABLE_WIDTH = 110;

    private enum JustificationTypes {
        LEFT,
        RIGHT
    }

    private int fileNameColumnWidth;
    private int sizeInBytesColumnWidth;
    private int detailedInfoColumnWidth;

    private Repository<FileSystemObject> repository;
    private final StringBuilder contentHolder;
    String[] columnNames;

    public ConsoleView(int fileNameColumnWidth, int sizeInBytesColumnWidth, int detailedInfoColumnWidth)
            throws IllegalArgumentException {
        if (fileNameColumnWidth + sizeInBytesColumnWidth + detailedInfoColumnWidth > MAX_TABLE_WIDTH) {
            throw new IllegalArgumentException("Not enough width on the screen");
        }

        setFileNameColumnWidth(fileNameColumnWidth);
        setSizeInBytesColumnWidth(sizeInBytesColumnWidth);
        setDetailedInfoColumnWidth(detailedInfoColumnWidth);

        columnNames = new String[]{"Filename", "Size (Bytes)", "Details"};
        contentHolder = new StringBuilder();
    }

    private void setFileNameColumnWidth(int value) throws IllegalArgumentException {
        if (value < MIN_COLUMN_WIDTH) {
            throw new IllegalArgumentException("Filename column width must be positive value");
        }
        this.fileNameColumnWidth = value;
    }

    private void setSizeInBytesColumnWidth(int value) throws IllegalArgumentException {
        if (value < MIN_COLUMN_WIDTH) {
            throw new IllegalArgumentException("Size column width must be positive value");
        }
        this.sizeInBytesColumnWidth = value;
    }

    private void setDetailedInfoColumnWidth(int value) throws IllegalArgumentException {
        if (value < MIN_COLUMN_WIDTH) {
            throw new IllegalArgumentException("Details column width must be positive value");
        }
        this.detailedInfoColumnWidth = value;
    }

    private String[] columnFormatter(String value, int width) {
        int rows = value.length() / width + ((value.length() % width) > 0 ? 1 : 0);
        String[] res = new String[rows];
        int rowId = 0;
        int cuttingPos = 0;
        while (cuttingPos < value.length()) {
            res[rowId] = value.substring(cuttingPos, Math.min(cuttingPos + width, value.length()));
            cuttingPos += width;
            rowId++;
        }
        return res;
    }

    private void init() {
        contentHolder.setLength(0);
        constructHeaderRow();
        constructDataRow(columnNames[0], columnNames[1], columnNames[2]);
    }

    private String constructRepeatedString(int width, String template) {
        String res = "";
        while (width > 0) {
            res = res.concat(template);
            width--;
        }
        return res;
    }

    private String constructFixedString(String s, int width, JustificationTypes justification) {
        String justificationKey = (justification == JustificationTypes.LEFT) ? "-" : "";
        String template = "%".concat(justificationKey).concat(Integer.toString(width)).concat("s");
        return String.format(template, s);
    }

    private void constructHeaderRow() {
        contentHolder.append("┌");
        contentHolder.append(constructRepeatedString(fileNameColumnWidth, "─"));
        contentHolder.append("┬");
        contentHolder.append(constructRepeatedString(sizeInBytesColumnWidth, "─"));
        contentHolder.append("┬");
        contentHolder.append(constructRepeatedString(detailedInfoColumnWidth, "─"));
        contentHolder.append("┐");
    }

    private void constructSplittingRow() {
        contentHolder.append("├");
        contentHolder.append(constructRepeatedString(fileNameColumnWidth, "─"));
        contentHolder.append("┼");
        contentHolder.append(constructRepeatedString(sizeInBytesColumnWidth, "─"));
        contentHolder.append("┼");
        contentHolder.append(constructRepeatedString(detailedInfoColumnWidth, "─"));
        contentHolder.append("┤");
    }

    private void constructBottomRow() {
        contentHolder.append("└");
        contentHolder.append(constructRepeatedString(fileNameColumnWidth, "─"));
        contentHolder.append("┴");
        contentHolder.append(constructRepeatedString(sizeInBytesColumnWidth, "─"));
        contentHolder.append("┴");
        contentHolder.append(constructRepeatedString(detailedInfoColumnWidth, "─"));
        contentHolder.append("┘");
        contentHolder.append("\n");
    }

    private void constructDataRow(String fileName, String sizeInBytes, String detailedInfo) {
        String[] fileNameColumn = columnFormatter(fileName, fileNameColumnWidth);
        String[] sizeInBytesColumn = columnFormatter(sizeInBytes, sizeInBytesColumnWidth);
        String[] detailsColumn = columnFormatter(detailedInfo, detailedInfoColumnWidth);
        int numberOfRows = Math.max(Math.max(fileNameColumn.length, sizeInBytesColumn.length), detailsColumn.length);
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < numberOfRows; i++) {
            res.append("\n");
            res.append("│");
            if (i < fileNameColumn.length) {
                res.append(constructFixedString(fileNameColumn[i], fileNameColumnWidth, JustificationTypes.LEFT));
            } else {
                res.append(constructRepeatedString(fileNameColumnWidth, " "));
            }
            res.append("│");
            if (i < sizeInBytesColumn.length) {
                res.append(constructFixedString(sizeInBytesColumn[i], sizeInBytesColumnWidth, JustificationTypes.RIGHT));
            } else {
                res.append(constructRepeatedString(sizeInBytesColumnWidth, " "));
            }
            res.append("│");
            if (i < detailsColumn.length) {
                res.append(constructFixedString(detailsColumn[i], detailedInfoColumnWidth, JustificationTypes.LEFT));
            } else {
                res.append(constructRepeatedString(detailedInfoColumnWidth, " "));
            }
            res.append("│");
        }
        contentHolder.append(res.toString());
        contentHolder.append("\n");
    }

    @Override
    public void registerSource(Repository<FileSystemObject> repository) throws IllegalArgumentException {
        if (repository == null) {
            throw new IllegalArgumentException("Reference to repository must be set");
        }
        this.repository = repository;
    }

    @Override
    public void show() {
        init(); // сбрасываем предыдущее состояние модели

        // иллюстрация полиморфизма, за счет которого работает данная функциональность.
        for (FileSystemObject obj : repository) { //<-тут все поддерживаемые типы преобразуются в первый базовый тип
            //  "FileSystemObject", в котором определены методы получения значения
            //  полей "getFileName()" и "getSizeInBytes()".

            String details = ((Viewable) obj).getDetailedInfo(); //<--тут все поддерживаемые типы преобразуются во
            //   второй базовый тип "Viewable", в котором определен
            //   метод получения детального
            //   описания "getDetailedInfo()"
            constructSplittingRow();
            constructDataRow(obj.getFileName(), Integer.toString(obj.getSizeInBytes()), details);
        }
        constructBottomRow();
        System.out.println();
        System.out.println(contentHolder.toString());
        System.out.println();
    }

}
