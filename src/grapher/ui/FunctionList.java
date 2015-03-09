/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import grapher.fc.Function;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;

public class FunctionList extends JList<Function> implements MouseListener{
    protected Grapher graphe;
    public FunctionList(Grapher graphe) {
        super(graphe.functions);
        this.graphe = graphe;
        addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        graphe.setBoldFunction(this.getSelectedIndex());
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
