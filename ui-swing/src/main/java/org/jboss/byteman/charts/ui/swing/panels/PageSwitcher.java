package org.jboss.byteman.charts.ui.swing.panels;

import java.awt.*;

/**
 * User: alexkasko
 * Date: 6/11/15
 */
class PageSwitcher {
    private final Container cardbox;
    private final CardLayout deck;

    PageSwitcher(Container cardbox, CardLayout deck) {
        this.cardbox = cardbox;
        this.deck = deck;
    }

    void showCard(String name) {
        deck.show(cardbox, name);
    }
}
