package grapher.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;


public class Main extends JFrame {
    
    private FunctionList listeFonction;
    private JMenuBar menu;
    private JMenu expression;
    private JMenuItem add;
    private JMenuItem remove;
    
	Main(String title, String[] expressions) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Grapher grapher = new Grapher();		
		for(String expression : expressions) {
			grapher.add(expression);
		}
		listeFonction = new FunctionList(grapher);
		add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listeFonction,grapher));
                
                menu =  new JMenuBar();
                expression = new JMenu("Expression");
                add = new JMenuItem("Add...");
                remove = new JMenuItem("Remove");
                add.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        listeFonction.addExpression();
                    }
                });
                remove.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        listeFonction.removeExpression();
                    }
                });
                
                expression.add(add);
                expression.add(remove);
                menu.add(expression);
                this.setJMenuBar(menu);
		pack();
	}

	public static void main(String[] argv) {
		final String[] expressions = argv;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				new Main("grapher", expressions).setVisible(true); 
			}
		});
	}
}
