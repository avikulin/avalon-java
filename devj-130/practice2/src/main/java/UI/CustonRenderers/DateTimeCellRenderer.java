package UI.CustonRenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeCellRenderer extends JLabel implements TableCellRenderer {
    private final boolean renderTimeOnly;
    private final String formatTemplate;

    public DateTimeCellRenderer(boolean renderTimeOnly) {
        super();
        this.renderTimeOnly = renderTimeOnly;
        this.formatTemplate = (renderTimeOnly) ? "hh:mm:ss" : "dd.MM.YYYY hh:mm:ss";
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        DateFormat formatter = new SimpleDateFormat(formatTemplate);
        Date dateTimeValue = (Date) value;
        String s = formatter.format(dateTimeValue);
        Component sourceComponent = renderer.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
        ((JLabel) sourceComponent).setHorizontalAlignment(SwingConstants.RIGHT);
        return sourceComponent;
    }
}
