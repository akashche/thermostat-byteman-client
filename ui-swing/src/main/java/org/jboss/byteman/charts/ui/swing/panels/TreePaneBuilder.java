package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;
import org.jboss.byteman.charts.ui.swing.pages.PageManager;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class TreePaneBuilder {

    public JScrollPane build(List<ContentPage> pages, Container cardbox, CardLayout deck) {
        JTree tree = new JTree();
        PageManager pm = new TreePageManager(tree, deck, cardbox);
        // ctx init shortcut, it smells here
        if (pages.size() > 0) pages.get(0).getAppContext().init(pm);
        tree.setModel(new DefaultTreeModel(createNodes(pages), false));
        tree.addTreeSelectionListener(new PageListener(pm));
        tree.getSelectionModel().setSelectionMode(SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new PageCellRenderer());
        tree.setRootVisible(false);
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        JScrollPane jp = new JScrollPane();
        jp.setViewportView(tree);
        jp.setBorder(createMatteBorder(0, 0, 0, 1, jp.getBackground().darker()));
        return jp;
    }

    private DefaultMutableTreeNode createNodes(List<ContentPage> pages) {
        if (null == pages || 0 == pages.size()) throw new IllegalArgumentException("Invalid empty pages list");
        List<ContentPage> pool = new LinkedList<ContentPage>(pages);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("", true);
        Set<String> poppedChildren = new HashSet<String>();
        for (ContentPage cp : pages) {
            if(poppedChildren.contains(cp.getName())) continue;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new PageNodeObject(cp), true);
            for (String ch : cp.getChildren()) {
                ContentPage child = popAddChild(node, pool, ch);
                poppedChildren.add(child.getName());
            }
            root.add(node);
        }
        return root;
    }

    // note: linear search won't harm here
    private ContentPage popAddChild(DefaultMutableTreeNode parent, List<ContentPage> pages, String name) {
        for(ContentPage cp : pages) {
            if (name.equals(cp.getName())) {
                parent.add(new DefaultMutableTreeNode(new PageNodeObject(cp), true));
                return cp;
            }
        }
        return null;
    }

    private static class PageListener implements TreeSelectionListener {
        private final PageManager pm;

        private PageListener(PageManager pm) {
            this.pm = pm;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            if (node.getUserObject() instanceof PageNodeObject) {
                PageNodeObject pn = (PageNodeObject) node.getUserObject();
                pm.switchPage(pn.getName());
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
            if (nd.getUserObject() instanceof PageNodeObject) {
                PageNodeObject pn = (PageNodeObject) nd.getUserObject();
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
                URL url = ContentPage.class.getResource("icons/" + path);
                ImageIcon icon = new ImageIcon(url);
                iconsCache.put(path, icon);
                return icon;
            }
        }
    }
}
