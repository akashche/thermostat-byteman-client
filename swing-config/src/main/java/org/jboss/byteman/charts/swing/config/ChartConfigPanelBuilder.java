package org.jboss.byteman.charts.swing.config;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Collection;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class ChartConfigPanelBuilder {

    private String layoutGeneralOptions = "fillx";
    private String layoutColumnsOptions = "[right][left]";
    private String layoutRowsOptions = "[]";
    private String labelOptions = "width ::120lp";
    private String controlOptions = "pushx, growx, width :200lp:, wrap";

//    "text field"
//    "int field"
//    "float field"
//    "combo box"
//    "slider"
//    "date picker"
    public JPanel build(Collection<ChartConfigField> fields) {
        JPanel jp = new JPanel(new MigLayout(layoutGeneralOptions, layoutColumnsOptions, layoutRowsOptions);
        for(ChartConfigField cf : fields) {
            jp.add(new JLabel("<html>Hi</html>"), );
            jp.add(new JTextField(), "pushx, growx, width :200lp:, wrap");
        }
//        jp.add(new JLabel("<html>Hi</html>"), "width ::120lp");
//        jp.add(new JTextField(), "pushx, growx, width :200lp:, wrap");
//        jp.add(new JLabel("<html>Hidddddsdfdsfddddd ddddd ddddd 2</html>"), "width ::120lp");
//        jp.add(new JTextArea(), "pushx, growx, width :200lp:, wrap, height :40lp:");
        return jp;
    }

}
