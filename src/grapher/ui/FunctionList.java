/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import grapher.fc.Function;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

public class FunctionList extends JPanel {

    protected Grapher graphe;
    protected JTable liste;
    protected ColorTableModel model;
    protected JToolBar toolbar = new JToolBar();

    public FunctionList(Grapher graphe) {
        super(new BorderLayout());

        liste = new JTable(new ColorTableModel());
        model = (ColorTableModel) liste.getModel();
        for (Function f : graphe.functions) {
            model.add(f, Color.BLACK);
        }
        liste.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        TableColumn colColor = liste.getColumnModel().getColumn(1);
        colColor.setCellRenderer(new ColorCellRenderer());
        colColor.setCellEditor(new ColorCellEditor());
        this.add(liste, BorderLayout.CENTER);

        this.graphe = graphe;
        graphe.model = this.model;

        this.addButton();
        this.add(toolbar, BorderLayout.PAGE_END);
    }

    private void addButton() {
        JButton btplus = new JButton("+");
        JButton btmoins = new JButton("-");
        btplus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpression();
            }
        });

        btmoins.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeExpression();
            }
        });

        toolbar.add(btplus);
        toolbar.add(btmoins);
    }

    protected void addExpression() {
        String res = JOptionPane.showInputDialog("Nouvelle expression");
        if (res != null && !res.equals("")) {
            Function f = graphe.add(res);
            model.add(f, Color.BLACK);
        }
        repaint();
    }

    protected void removeExpression() {
        graphe.removeFuncs(liste.getSelectedRows());
        model.remove(liste.getSelectedRows());
        repaint();
    }

    private class TableSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            graphe.setBoldFunction(liste.getSelectedRows());
        }
    }
}
