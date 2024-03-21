package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver {
	
	private double _time;
	private int _numBodies;
	private String _lawsDesc;
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		
		_currTime = new JLabel("Time: " + _time);
		add(_currTime);
		_currTime.setPreferredSize(new Dimension(120, 20));
		
		JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
		sep1.setPreferredSize(new Dimension(10, 20));
		add(sep1);
		
		_numOfBodies = new JLabel("Bodies: " + _numBodies);
		add(_numOfBodies);
		_numOfBodies.setPreferredSize(new Dimension(90, 20));
		
		JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
		sep2.setPreferredSize(new Dimension(10, 20));
		add(sep2);
		
		_currLaws = new JLabel("Laws: " + _lawsDesc);
		add(_currLaws);
		
	}
	// other private/protected methods
	// ...
	// SimulatorObserver methods
	// ...
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_time = time;
		_numBodies = bodies.size();
		_lawsDesc = fLawsDesc;
		_currTime.setText("Time: " + _time);
		_numOfBodies.setText("Bodies: " + _numBodies);
		_currLaws.setText("Laws: " + _lawsDesc);
	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_time = time;
		_numBodies = bodies.size();
		_lawsDesc = fLawsDesc;
		_currTime.setText("Time: " + _time);
		_numOfBodies.setText("Bodies: " + _numBodies);
		_currLaws.setText("Laws: " + _lawsDesc);
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_numBodies = bodies.size();
		_numOfBodies.setText("Bodies: " + _numBodies);
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_time = time;
		_numBodies = bodies.size();
		_currTime.setText("Time: " + _time);
		_numOfBodies.setText("Bodies: " + _numBodies);
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		_lawsDesc = fLawsDesc;
		_currLaws.setText("Laws: " + _lawsDesc);
	}
}


