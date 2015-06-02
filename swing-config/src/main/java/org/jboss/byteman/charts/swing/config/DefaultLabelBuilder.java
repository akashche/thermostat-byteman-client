package org.jboss.byteman.charts.swing.config;

import org.jboss.byteman.charts.utils.StringUtils;

import javax.swing.*;

import java.awt.*;

import static org.jboss.byteman.charts.utils.StringUtils.defaultString;

/**
 * User: alexkasko
 * Date: 6/2/15
 */
public class DefaultLabelBuilder implements ChartConfigLabelBuilder {

    private static final String WRAPABLE_TEXT_PREFIX = "<html>";
    private static final String WRAPABLE_TEXT_POSTFIX = "</html>";

    @Override
    public JLabel build(String text) {
        JLabel jl = new JLabel(WRAPABLE_TEXT_PREFIX + defaultString(text) + WRAPABLE_TEXT_POSTFIX);
        // make font bold
        Font font = jl.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        jl.setFont(boldFont);
        return jl;
    }


}
