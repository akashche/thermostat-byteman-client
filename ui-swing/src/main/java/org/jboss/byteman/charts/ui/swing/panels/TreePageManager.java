package org.jboss.byteman.charts.ui.swing.panels;

import org.jboss.byteman.charts.ui.UiSwingException;
import org.jboss.byteman.charts.ui.swing.pages.ChartsAppContext;
import org.jboss.byteman.charts.ui.swing.pages.ContentPage;
import org.jboss.byteman.charts.ui.swing.pages.PageManager;
import org.jboss.byteman.charts.ui.swing.util.SplashablePane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.jboss.byteman.charts.utils.SwingUtils.sleepNonFlicker;

/**
 * User: alexkasko
 * Date: 6/16/15
 */
class TreePageManager implements PageManager {

    private final ChartsAppContext ctx;
    private final Map<String, ContentPage> pageMap = new ConcurrentHashMap<String, ContentPage>();
    private final JTree tree;
    private final CardLayout deck;
    private final Container cardbox;
    private final ConcurrentHashMap<String, SplashablePane> cards;

    TreePageManager(ChartsAppContext ctx, java.util.List<ContentPage> pages, JTree tree,
                    CardLayout deck, Container cardbox, ConcurrentHashMap<String, SplashablePane> cards) {
        this.ctx = ctx;
        for (ContentPage cp : pages) {
            pageMap.put(cp.getName(), cp);
        }
        this.tree = tree;
        this.deck = deck;
        this.cardbox = cardbox;
        this.cards = cards;
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
        ctx.setStatus(cp.getLabel());
    }

    @Override
    public void addPage(ContentPage page, String parentName) {
        if (null == page) throw new UiSwingException("Specified page is null");
        if (null == parentName) throw new UiSwingException("Specified parentName is null");
        // add card
        SplashablePane pane = new SplashablePane(page.createPane());
        cardbox.add(page.getName(), pane);
        cards.put(page.getName(), pane);
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
    public void addPageAsync(ContentPage page, String parentName) {
        if (null == page) throw new UiSwingException("Specified page is null");
        if (null == parentName) throw new UiSwingException("Specified parentName is null");
        // add empty page
        SplashablePane sp = new SplashablePane();
        cardbox.add(page.getName(), sp);
        cards.put(page.getName(), sp);
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
        // fire background page loading
        showPageSplash(page.getName());
        switchPage(page.getName());
        new PageAddWorker(page, sp).execute();
    }

    private class PageAddWorker extends SwingWorker<Void, Void> {
        final ContentPage page;
        final SplashablePane sp;
        Component comp;

        private PageAddWorker(ContentPage page, SplashablePane sp) {
            this.page = page;
            this.sp = sp;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                long start = System.currentTimeMillis();
                this.comp = page.createPane();
                sleepNonFlicker(start);
                return null;
            } catch (Exception e) {
                // todo: report properly
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void done() {
            sp.setContent(comp);
            page.onInit();
            hidePageSplash(page.getName());
        }
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
        cards.remove(pageName);
    }

    @Override
    public void showPageSplash(String pageName) {
        if (null == pageName) throw new UiSwingException("Specified pageName is null");
        // page
        cards.get(pageName).showSplash();
        // tree
        DefaultMutableTreeNode node = findNode(pageName);
        if (null == node) return;
        TreePaneBuilder.PageCellRenderer ren = (TreePaneBuilder.PageCellRenderer) tree.getCellRenderer();
        ren.addLoadingPage(pageName);
        repaintNode(node);
    }

    @Override
    public void hidePageSplash(String pageName) {
        if (null == pageName) throw new UiSwingException("Specified pageName is null");
        // page
        cards.get(pageName).hideSplash();
        // tree
        DefaultMutableTreeNode node = findNode(pageName);
        if (null == node) return;
        TreePaneBuilder.PageCellRenderer ren = (TreePaneBuilder.PageCellRenderer) tree.getCellRenderer();
        ren.removeLoadingPage(pageName);
        repaintNode(node);
    }

    private void repaintNode(DefaultMutableTreeNode node) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreePath path = new TreePath(model.getPathToRoot(node));
        Rectangle rect = tree.getPathBounds(path);
        if (null != rect) {
            tree.repaint(rect);
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
