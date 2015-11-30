package com.redhat.thermostat.byteman.ui.swing.panels;

import com.redhat.thermostat.byteman.config.UiSwingException;
import com.redhat.thermostat.byteman.ui.swing.pages.ContentPage;
import com.redhat.thermostat.byteman.ui.swing.pages.ContentPagesRegister;
import com.redhat.thermostat.byteman.ui.swing.pages.PageManager;
import com.redhat.thermostat.byteman.ui.swing.util.SplashablePane;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.newSetFromMap;
import static javax.swing.BorderFactory.createMatteBorder;
import static javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION;

/**
 * User: alexkasko
 * Date: 6/10/15
 */
class TreePaneBuilder {

    public JScrollPane build(List<ContentPage> pages, Container cardbox, CardLayout deck,
                             ConcurrentHashMap<String, SplashablePane> cards) {
        JTree tree = new JTree();
        // todo: context access
        PageManager pm = new TreePageManager(ContentPagesRegister.APP_CONTEXT, pages, tree, deck, cardbox, cards);
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
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("", true);
        Set<String> poppedChildren = new HashSet<String>();
        for (ContentPage cp : pages) {
            if(poppedChildren.contains(cp.getName())) continue;
            popAddWithChildren(root, cp, pages, poppedChildren);
        }
        return root;
    }

    // note: linear search won't harm here
    private void popAddWithChildren(DefaultMutableTreeNode granny, ContentPage parent,
                                           List<ContentPage> pages, Set<String> poppedChildren) {
        poppedChildren.add(parent.getName());
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new PageNodeObject(parent), true);
        for (String name : parent.getChildren()) {
            for(ContentPage cp : pages) {
                if(poppedChildren.contains(cp.getName())) continue;
                if (name.equals(cp.getName())) {
                    popAddWithChildren(node, cp, pages, poppedChildren);
                }
            }
        }
        granny.add(node);
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

    static class PageCellRenderer extends DefaultTreeCellRenderer {
        // most probably will be used only from EDT, so concurrent just in case
        private final ConcurrentHashMap<String, ImageIcon> iconsCache = new ConcurrentHashMap<String, ImageIcon>();
        private final Set<String> loadingPages = newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        private JTree tree = null;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            if (null == this.tree) this.tree = tree;
            DefaultMutableTreeNode nd = (DefaultMutableTreeNode) value;
            if (nd.getUserObject() instanceof PageNodeObject) {
                PageNodeObject pn = (PageNodeObject) nd.getUserObject();
                setText(pn.getLabel());
                final ImageIcon icon;
                if (!loadingPages.contains(pn.getName())) {
                    icon = loadIcon(pn.getIcon());
                } else {
                    icon = loadIcon("loading_16.gif");
                    icon.setImageObserver(new NodeImageObserver(tree, nd));
                }
                setIcon(icon);
            }
            return this;
        }

        void addLoadingPage(String pageName) {
            loadingPages.add(pageName);
        }

        void removeLoadingPage(String pageName) {
            loadingPages.remove(pageName);
        }

        private ImageIcon loadIcon(String path) {
            ImageIcon cached = iconsCache.get(path);
            if (null != cached) {
                return cached;
            } else {
                URL url = ContentPage.class.getResource("icons/" + path);
                if (null == url) throw new UiSwingException("Icon not found: [" + path + "]");
                ImageIcon icon = new ImageIcon(url);
                iconsCache.put(path, icon);
                return icon;
            }
        }
    }

    static class NodeImageObserver implements ImageObserver {
        private final JTree tree;
        private TreePath path;

        NodeImageObserver(JTree tree, TreeNode node) {
            this.tree = tree;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            this.path = new TreePath(model.getPathToRoot(node));
        }

        @Override
        public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) {
            if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
                Rectangle rect = tree.getPathBounds(path);
                tree.repaint(rect);
            }
            return (flags & (ALLBITS | ABORT)) == 0;
        }
    }
}
