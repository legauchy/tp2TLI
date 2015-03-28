/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import grapher.fc.Function;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author anthony
 */
public class ColorTableModel extends AbstractTableModel{
    
    ArrayList<Function> rowFunc;
    ArrayList<Color> rowColor;
    
    public ColorTableModel() {
        super();
        rowFunc = new ArrayList<Function>();
        rowColor = new ArrayList<Color>();
    }
    
    public int getRowCount() {
        return rowFunc.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            if(columnIndex == 0) {
                return rowFunc.get(rowIndex);
            }else if(columnIndex == 1) {
                return rowColor.get(rowIndex);
            }
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } 
    }
    
    public Color getFunctionColor(Function f) {
        return this.rowColor.get(this.rowFunc.indexOf(f));
    }
    
    public void add(Function func, Color col) {
        this.rowFunc.add(func);
        this.rowColor.add(col);
    }
    
    public void remove(int[] toRemove) {
        int offset = 0;
        for(Integer i : toRemove) {
            this.rowColor.remove(i-offset);
            this.rowFunc.remove(i-offset);
            offset++;
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1;
    }

}
