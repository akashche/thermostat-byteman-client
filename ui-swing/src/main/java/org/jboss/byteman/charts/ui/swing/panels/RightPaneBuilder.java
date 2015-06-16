package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class RightPaneBuilder {

    public Result build(List<ContentPage> pages) {
        CardLayout deck = new CardLayout();
        JPanel jp = new JPanel(deck);
        jp.setBorder(createMatteBorder(0, 1, 0, 0, jp.getBackground().darker()));
        for (ContentPage pa : pages) {
            jp.add(pa.createPane(), pa.getName());
        }
        return new Result(jp, deck);
    }

    static class Result {
        private final JPanel cardbox;
        private final CardLayout deck;

        Result(JPanel cardbox, CardLayout deck) {
            this.cardbox = cardbox;
            this.deck = deck;
        }

        public JPanel getCardbox() {
            return cardbox;
        }

        public CardLayout getDeck() {
            return deck;
        }
    }
}
