package grapher.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import javax.swing.JPanel;

import java.awt.Point;

import java.util.Vector;

import static java.lang.Math.*;

import grapher.fc.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;


public class Grapher extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
	static final int MARGIN = 40;
	static final int STEP = 5;
	
	static final BasicStroke dash = new BasicStroke(1, BasicStroke.CAP_ROUND,
	                                                   BasicStroke.JOIN_ROUND,
	                                                   1.f,
	                                                   new float[] { 4.f, 4.f },
	                                                   0.f);
	                                                   
	protected int W = 400;
	protected int H = 300;
	
        public enum STATES {
            IDLE,
            PRESS_OR_DRAG_RIGHT,
            PRESS_OR_DRAG_LEFT,
            DRAG_RIGHT,
            DRAG_LEFT
        }
        private STATES state;
        private Rectangle selectionRectangle;
        protected int posX, posY;
	protected double xmin, xmax;
	protected double ymin, ymax;

	protected Vector<Function> functions;
        protected ArrayList<Function> bold_function = new ArrayList<Function>();
	protected ColorTableModel model;
        
	public Grapher() {
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;
		
                this.addMouseListener(this);
                this.addMouseMotionListener(this);
                this.addMouseWheelListener(this);
		functions = new Vector<Function>();
                bold_function = new ArrayList<Function>();
                this.state = STATES.IDLE;
	}
	
	public Function add(String expression) {
            Function f = FunctionFactory.createFunction(expression);
            add(f);
            return f;
	}
	
	public void add(Function function) {
            functions.add(function);
            repaint();
	}
        
        public void removeFuncs(int[] indices) {
            int offset = 0;
            for(Integer i : indices) {
                this.removeFuncs(i - offset);
                offset++;
            }
        }
        
        public void removeFuncs(int i) {
            this.functions.remove(i);
            repaint();
        }
		
        @Override
	public Dimension getPreferredSize() { return new Dimension(W, H); }
	
        @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		W = getWidth();
		H = getHeight();

		Graphics2D g2 = (Graphics2D)g;

		// background
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, W, H);
		
		g2.setColor(Color.BLACK);

		// box
		g2.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) { 
			return; 
		}
		
		g2.drawRect(0, 0, W, H);
		
		g2.drawString("x", W, H+10);
		g2.drawString("y", -10, 0);
		
	
		// plot
		g2.clipRect(0, 0, W, H);
		g2.translate(-MARGIN, -MARGIN);

		// x values
		final int N = W/STEP + 1;
		final double dx = dx(STEP);
		double xs[] = new double[N];
		int    Xs[] = new int[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
		}
		
		for(Function f : functions) {
			// y values
                        g2.setColor(model.getFunctionColor(f));
			int Ys[] = new int[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.y(xs[i]));
			}
			if(bold_function.contains(f)) {
                            g2.setStroke(new BasicStroke(3));
                            g2.drawPolyline(Xs, Ys, N);
                            g2.setStroke(new BasicStroke(1));
                        }else {
                            g2.drawPolyline(Xs, Ys, N);
                            
                        }
                        
                        g2.setColor(Color.BLACK);
		}

		g2.setClip(null);

		// axes
		drawXTick(g2, 0);
		drawYTick(g2, 0);
		
		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		g2.setStroke(dash);
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(g2, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(g2, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(g2, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(g2, y); }
                
                if (selectionRectangle != null) {
                    g2.setColor(Color.BLACK);
                    g2.draw(selectionRectangle);
                }
	}
	
	protected double dx(int dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(int dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(int X) { return xmin+dx(X-MARGIN); }
	protected double y(int Y) { return ymin+dy((Y-MARGIN)-H); }
	
	protected int X(double x) { 
		int Xs = (int)round((x-xmin)/(xmax-xmin)*W);
		return Xs + MARGIN; 
	}
	protected int Y(double y) { 
		int Ys = (int)round((y-ymin)/(ymax-ymin)*H);
		return (H - Ys) + MARGIN;
	}
		
	protected void drawXTick(Graphics2D g2, double x) {
		if(x > xmin && x < xmax) {
			final int X0 = X(x);
			g2.drawLine(X0, MARGIN, X0, H+MARGIN);
			g2.drawString((new Double(x)).toString(), X0, H+MARGIN+15);
		}
	}
	
	protected void drawYTick(Graphics2D g2, double y) {
		if(y > ymin && y < ymax) {
			final int Y0 = Y(y);
			g2.drawLine(0+MARGIN, Y0, W+MARGIN, Y0);
			g2.drawString((new Double(y)).toString(), 5, Y0);
		}
	}
	
	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; } 
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}
	

	protected void translate(int dX, int dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		repaint();	
	}
	
	protected void zoom(Point center, int dz) {
		double x = x(center.x);
		double y = y(center.y);
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		repaint();	
	}
	
	protected void zoom(Point p0, Point p1) {
		double x0 = x(p0.x);
		double y0 = y(p0.y);
		double x1 = x(p1.x);
		double y1 = y(p1.y);
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		repaint();	
	}
        
    public void setBoldFunction(int[] index) {
        bold_function.clear();
        for(Integer i : index) {
            bold_function.add(functions.get(i));
        }
        repaint();
    }
    
    public void mousePressed(MouseEvent e) {
       switch(this.state) {
           case IDLE : this.posX = e.getX();
                       this.posY = e.getY();
                       if(e.getButton() == 1){
                           this.state = STATES.PRESS_OR_DRAG_LEFT;
                       }else if(e.getButton() == 3){
                           this.state = STATES.PRESS_OR_DRAG_RIGHT;
                       }
                       break;
           default : break;

       }
    }

    public void mouseDragged(MouseEvent e) {
        switch(this.state) {
            case PRESS_OR_DRAG_LEFT : this.state = STATES.DRAG_LEFT;
                                        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));                
            case DRAG_LEFT :   int decalX = e.getX() - posX;
                                int decalY = e.getY() - posY;this.translate(decalX, decalY);
                                posX = e.getX();
                                posY = e.getY();
                                break;
                
            case PRESS_OR_DRAG_RIGHT : this.state = STATES.DRAG_RIGHT;
            case DRAG_RIGHT :  Point dragPoint = e.getPoint();
                                int x = Math.min(this.posX, dragPoint.x);
                                int y = Math.min(this.posY, dragPoint.y);
                                int width = Math.max(this.posX - dragPoint.x, dragPoint.x - this.posX);
                                int height = Math.max(this.posY - dragPoint.y, dragPoint.y - this.posY);
                                selectionRectangle = new Rectangle(x, y, width, height);
                                repaint();
                                break;
            default: break;
        }
    }

    public void mouseReleased(MouseEvent e) {
        switch (this.state) {
            case PRESS_OR_DRAG_LEFT : this.zoom(e.getPoint(), 5);
                                        this.state = STATES.IDLE;
                                        break;
            case PRESS_OR_DRAG_RIGHT : this.zoom(e.getPoint(), -5);
                                        this.state = STATES.IDLE;
                                        break;
                
            case DRAG_LEFT :   this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                this.state = STATES.IDLE;
                                break;
            
            case DRAG_RIGHT :  this.zoom(this.selectionRectangle.getLocation(), 
                                        new Point(this.selectionRectangle.x + this.selectionRectangle.width,
                                        this.selectionRectangle.y + this.selectionRectangle.height));
                                this.selectionRectangle = null;
                                this.repaint();
                                this.state = STATES.IDLE;
                                break;
            default: break;
        }        
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
        switch(this.state) {
            case IDLE : int rotation = e.getWheelRotation();
                        this.zoom(e.getPoint(), -rotation*2);
                        break;
            default : break;
        }  
    }
    
    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
