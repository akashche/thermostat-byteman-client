package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.UiSwingException;
import org.jboss.byteman.charts.ui.swing.pages.ContentPage;
import org.jboss.byteman.charts.ui.swing.pages.PageManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
class TreePageManager implements PageManager {

    private final Map<String, ContentPage> pageMap = new ConcurrentHashMap<String, ContentPage>();
    private final JTree tree;
    private final CardLayout deck;
    private final Container cardbox;

    TreePageManager(java.util.List<ContentPage> pages, JTree tree, CardLayout deck, Container cardbox) {
        for (ContentPage cp : pages) {
            pageMap.put(cp.getName(), cp);
        }
        this.tree = tree;
        this.deck = deck;
        this.cardbox = cardbox;
    }

    @Override
    public void switchPage(String pageName) {
        ContentPage cp = pageMap.get(pageName);
        if (null == cp) return;
        DefaultMutableTreeNode node = findNode(pageName);
        if (null == node) return;
        TreePath path = new TreePath(node.getPath());
        tree.expandPath(path);
        tree.setSelectionPath(path);
        cp.onShow();
        deck.show(cardbox, pageName);
    }

    @Override
    public void addPage(ContentPage page, String parentName) {
        if (null == page) throw new UiSwingException("Specified page is null");
        if (null == parentName) throw new UiSwingException("Specified parentName is null");
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
        // add cached
        pageMap.put(page.getName(), page);
    }

    @Override
    public void removePage(String pageName) {
        if (null == pageName) throw new UiSwingException("Specified pageName is null");
        // remove cached
        pageMap.remove(pageName);
        // remove node
        removeNode(pageName);
        // remove card
        Component[] components = cardbox.getComponents();
        for (Component co : components) {
            if (pageName.equals(co.getName())) {
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
