package org.jboss.byteman.charts.ui.swing.pages;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
public interface PageManager {

    void switchPage(String pageName);

    void addPage(ContentPage page, String parentName);

    void removePage(String pageName);
}
