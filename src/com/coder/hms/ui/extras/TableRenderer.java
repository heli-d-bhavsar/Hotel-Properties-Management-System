package com.coder.hms.ui.extras;

import javax.swing.*;
import java.awt.*;

public interface TableRenderer {
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                    int row, int column) ;
}
