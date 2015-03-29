/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author anthony
 */
public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    
    private Color color;
    private JButton button;
    private JColorChooser colorChooser;
    private JDialog dialog;
    
    public ColorCellEditor() {
        super();
 
        button = new JButton();
        button.setActionCommand("change");
        button.addActionListener(this);
        //button.setBorderPainted(false);
        
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button, "Choisir une couleur", true, colorChooser, this, null);
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        color = (Color) value;
        return button;
    }

    public Object getCellEditorValue() {
        return color;
    }

    public void actionPerformed(ActionEvent e) {
        if ("change".equals(e.getActionCommand())) {
            button.setBackground(color);
            colorChooser.setColor(color);
            dialog.setVisible(true);
            // averti le JTable qu'on a terminé l'édition et qu'il faut afficher à nouveau le renderer
            fireEditingStopped();
        } else {
            color = colorChooser.getColor();
        }
    }
    
}
