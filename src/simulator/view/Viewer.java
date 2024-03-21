package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	// ...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private boolean _showVectors;
	private static final String _HELP_MESSAGE_1 = "h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit  ";
	private static final String _HELP_MESSAGE_2 = "Scaling ratio: ";
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		
		// TODO add border with title
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
		BorderFactory.createLineBorder(Color.black, 2), "Viewer", TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(20, 400));
		setMinimumSize(new Dimension(20, 400));
		
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		_showVectors = true;
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case '-':
						_scale = _scale * 1.1;
						repaint();
						break;
					case '+':
						_scale = Math.max(1000.0, _scale / 1.1);
						repaint();
						break;
					case '=':
						autoScale();
						repaint();
						break;
					case 'h':
						_showHelp = !_showHelp;
						repaint();
						break;
					case 'v':
						_showVectors = !_showVectors;
						repaint();
						break;
					default:
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
			}
	
			@Override
			public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
			}
			
		});
		
		addMouseListener(new MouseListener() {
			// ...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// use 'gr' to draw not 'g' --- it gives nicer results
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
	
		// TODO draw a cross at center
		gr.setColor(Color.RED);
		gr.drawLine(_centerX - 4 , _centerY, _centerX + 4, _centerY);
		gr.drawLine(_centerX, _centerY + 4, _centerX, _centerY - 4);
	
		// TODO draw bodies (with vectors if _showVectors is true)
	
	 
		for(Body i : _bodies) {
			
			gr.setColor(Color.BLUE);
			 
			int x_body = _centerX + (int)(i.getPosition().getX()/_scale); 
			int y_body = _centerY - (int)(i.getPosition().getY()/_scale); 
			
			gr.fillOval(x_body-5, y_body-5, 10, 10); //coge la esquina, (x_body, y_body) es el centro
		
			int tw = gr.getFontMetrics().stringWidth(i.getId());  //dibujar nombre
			gr.drawString(i.getId() , x_body - (tw / 2), y_body - 10);
			
			
			if(_showVectors) {
				Vector2D v = i.getVelocity().direction(); 
				int x2 = (int) (v.getX() * 20 + x_body); 
				int y2 = (int) (y_body - v.getY() * 20);
				
				drawLineWithArrow(gr, x_body, y_body, x2 , y2, 5, 5, Color.GREEN, Color.GREEN); 
				
				Vector2D f = i.getForce().direction(); 
				int x_f = (int) (f.getX() * 20 + x_body); 
				int y_f = (int) (y_body - f.getY() * 20);
					
				drawLineWithArrow(gr, x_body, y_body, x_f , y_f, 5, 5, Color.RED, Color.RED); 
			}
		
	
		}
	
		// TODO draw help if _showHelp is true
		
		if(_showHelp) {
			gr.setColor(Color.RED);
			gr.drawString(_HELP_MESSAGE_1, 10, 25);
			gr.drawString(_HELP_MESSAGE_2 + Double.toString(_scale), 10, 40);
		}
		
	}
	// other private/protected methods
	// ...
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}
		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(Graphics g, int x1, int y1, int x2, int y2, int w, int h, 
			Color lineColor, Color arrowColor) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;
		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;
		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;
		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}
	// SimulatorObserver methods
	// ...
	
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_bodies = bodies; 
		autoScale();
		repaint(); 
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_bodies = bodies; 
		autoScale();
		repaint(); 
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies = bodies; 
		autoScale();
		repaint(); 
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies = bodies; 
		repaint(); 	
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}
	
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		
	}
}
