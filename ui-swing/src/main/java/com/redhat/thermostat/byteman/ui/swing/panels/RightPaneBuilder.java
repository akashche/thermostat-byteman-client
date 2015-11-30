package com.redhat.thermostat.byteman.ui.swing.panels;

import com.redhat.thermostat.byteman.ui.swing.pages.ContentPage;
import com.redhat.thermostat.byteman.ui.swing.util.SplashablePane;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.swing.BorderFactory.createMatteBorder;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class RightPaneBuilder {

    public Result build(List<ContentPage> pages, ConcurrentHashMap<String, SplashablePane> cards) {
        CardLayout deck = new CardLayout();
        JPanel jp = new JPanel(deck);
        jp.setBorder(createMatteBorder(0, 1, 0, 0, jp.getBackground().darker()));
        for (ContentPage pa : pages) {
            SplashablePane pane = new SplashablePane(pa.createPane());
            cards.put(pa.getName(), pane);
            jp.add(pane, pa.getName());
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
