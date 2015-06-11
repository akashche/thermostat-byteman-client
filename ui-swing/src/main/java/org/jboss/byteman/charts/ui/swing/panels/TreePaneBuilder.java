package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;
import org.jboss.byteman.charts.ui.swing.pages.ContentPageNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class TreePaneBuilder {

    public JScrollPane build(List<ContentPage> pages, PageSwitcher switcher) {
        JTree tree = new JTree();
        tree.setModel(new DefaultTreeModel(createNodes(pages), false));
        tree.addTreeSelectionListener(new PageListener(switcher));
        tree.getSelectionModel().setSelectionMode(SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new PageCellRenderer());
        JScrollPane jp = new JScrollPane();
        jp.setViewportView(tree);
        jp.setBorder(createMatteBorder(0, 0, 0, 1, jp.getBackground().darker()));
        return jp;
    }

    // note: currently root is at 0 index and all other nodes are its children
    // linear search doesn't matter here
    private DefaultMutableTreeNode createNodes(List<ContentPage> pages) {
        if (null == pages || 0 == pages.size()) throw new IllegalArgumentException("Invalid empty pages list");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(pages.get(0).createNode(), true);
        for (int i = 1; i < pages.size(); i++) {
            ContentPageNode pn = pages.get(i).createNode();
            root.add(new DefaultMutableTreeNode(pn, true));
        }
        return root;
    }

    private static class PageListener implements TreeSelectionListener {
        private final PageSwitcher switcher;

        private PageListener(PageSwitcher switcher) {
            this.switcher = switcher;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            if (node.getUserObject() instanceof ContentPageNode) {
                ContentPageNode pn = (ContentPageNode) node.getUserObject();
                switcher.showCard(pn.getName());
            }
        }
    }

    private static class PageCellRenderer extends DefaultTreeCellRenderer {
        // most probably will be used only from EDT, so concurrent just in case
        ConcurrentHashMap<String, ImageIcon> iconsCache = new ConcurrentHashMap<String, ImageIcon>();

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode nd = (DefaultMutableTreeNode) value;
            if (nd.getUserObject() instanceof ContentPageNode) {
                ContentPageNode pn = (ContentPageNode) nd.getUserObject();
                setText(pn.getLabel());
                ImageIcon icon = loadIcon(pn.getIcon());
                setIcon(icon);
            }
            return this;
        }

        private ImageIcon loadIcon(String path) {
            ImageIcon cached = iconsCache.get(path);
            if (null != cached) {
                return cached;
            } else {
                URL url = ContentPageNode.class.getResource("icons/" + path);
                ImageIcon icon = new ImageIcon(url);
                iconsCache.put(path, icon);
                return icon;
            }
        }
    }
}
