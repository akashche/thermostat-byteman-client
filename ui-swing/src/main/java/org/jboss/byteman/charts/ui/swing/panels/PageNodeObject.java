package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
class PageNodeObject {
    private final String name;
    private final String label;
    private final String icon;
    private final boolean userNode;

    PageNodeObject(ContentPage pa) {
        this(pa.getName(), pa.getLabel(), pa.getIcon(), pa.isUserPage());
    }

    PageNodeObject(String name, String label, String icon, boolean userNode) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.userNode = userNode;
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

    public boolean isUserNode() {
        return userNode;
    }
}
