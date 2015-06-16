package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.swing.pages.ContentPage;
import org.jboss.byteman.charts.ui.swing.pages.PageManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
class TreePageManager implements PageManager {

    private final JTree tree;
    private final CardLayout deck;
    private final Container cardbox;

    TreePageManager(JTree tree, CardLayout deck, Container cardbox) {
        this.tree = tree;
        this.deck = deck;
        this.cardbox = cardbox;
    }

    @Override
    public void switchPage(String pageName) {
        deck.show(cardbox, pageName);
    }

    @Override
    public void addPage(ContentPage page, String parentName) {
        // add card
        cardbox.add(page.getName(), page.createPane());
        // add node
        DefaultMutableTreeNode parent = findNode(parentName);
        if (null == parent) return;
        DefaultMutableTreeNode existed = findNode(page.getName());
        if (null != existed) return;
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new PageNodeObject(page));
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.insertNodeInto(node, parent, parent.getChildCount());
        TreePath path = new TreePath(node.getPath());
        tree.expandPath(path);
        tree.setSelectionPath(path);
    }

    @Override
    public void removePage(String pageName) {
        // remove node
        removeNode(pageName);
        // remove card
        Component[] components = cardbox.getComponents();
        for (Component co : components) {
            if (co.getName().equals(pageName)) {
                deck.removeLayoutComponent(co);
                break;
            }
        }
    }

    private void removeNode(String name) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> enumer = root.depthFirstEnumeration();
        while (enumer.hasMoreElements()) {
            DefaultMutableTreeNode node = enumer.nextElement();
            if (node.getUserObject() instanceof PageNodeObject) {
                PageNodeObject pno = (PageNodeObject) node.getUserObject();
                if (name.equals(pno.getName()) && pno.isUserNode()) {
                    model.removeNodeFromParent(node);
                    break;
                }
            }
        }
    }

    private DefaultMutableTreeNode findNode(String name) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        @SuppressWarnings("unchecked")
        Enumeration<DefaultMutableTreeNode> enumer = root.depthFirstEnumeration();
        while (enumer.hasMoreElements()) {
            DefaultMutableTreeNode node = enumer.nextElement();
            if (node.getUserObject() instanceof PageNodeObject) {
                PageNodeObject pno = (PageNodeObject) node.getUserObject();
                if (name.equals(pno.getName())) return node;
            }
        }
        return null;
    }

}
