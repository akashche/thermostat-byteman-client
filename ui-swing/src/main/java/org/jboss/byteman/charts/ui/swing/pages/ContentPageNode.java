package org.jboss.byteman.charts.ui.swing.pages;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
public class ContentPageNode {

    private final String name;
    private final String label;
    private final String icon;

    public ContentPageNode(String name, String label, String icon) {
        this.name = name;
        this.label = label;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return label;
    }
}
