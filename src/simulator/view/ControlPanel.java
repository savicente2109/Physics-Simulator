package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.JSONObject;

import extra.dialog.ex1.Dish;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {

	private static final int DEFAULT_VALUE_SPINNER = 150;
	private static final int MIN_VALUE_SPINNER = 0;
	private static final int MAX_VALUE_SPINNER = 50000;
	private static final int CHANGE_VALUE_SPINNER = 100;
	
	private Controller _ctrl;
	private boolean _stopped;
	private JFileChooser fc;
	private JButton loadButton;
	private JButton forceButton;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;
	private JTextField text;
	private JSpinner spinner;
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
		fc = new JFileChooser();
	}
	
	private void initGUI() {
		
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setPreferredSize(new Dimension(700, 60));
		
		JToolBar bar = new JToolBar();
		add(bar);
		

		bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
		
		loadButton = new JButton();
		loadButton.setToolTipText("Load a file");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ret = fc.showOpenDialog(ControlPanel.this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					try {
						_ctrl.reset();
						_ctrl.loadBodies(new FileInputStream(fc.getSelectedFile()));
					}
					catch (Exception ex) {
						JOptionPane.showMessageDialog(ControlPanel.this, "An error occured. File could not be loaded. " + ex.getMessage(),
								"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		loadButton.setMinimumSize(new Dimension(40, 40));
		loadButton.setPreferredSize(new Dimension(40, 40));
		loadButton.setMaximumSize(new Dimension(40, 40));
		loadButton.setIcon(new ImageIcon("resources/icons/open.png"));
		bar.add(loadButton);
		bar.addSeparator();
		
		forceButton = new JButton();
		forceButton.setActionCommand("force");
		forceButton.setToolTipText("Select the force laws");
		forceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open_dialog(); 
			}
		});
		forceButton.setMinimumSize(new Dimension(40, 40));
		forceButton.setPreferredSize(new Dimension(40, 40));
		forceButton.setMaximumSize(new Dimension(40, 40));
		forceButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		bar.add(forceButton);
		bar.addSeparator();
		
		runButton = new JButton();
		runButton.setActionCommand("run");
		runButton.setToolTipText("Run simulation");
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					_ctrl.setDeltaTime(Double.parseDouble(text.getText()));
					int steps = Integer.parseInt(spinner.getValue().toString());
					setButtonsEnabled(false);
					_stopped = false;
					run_sim(steps);
				}
				catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(ControlPanel.this, "Invalid data. Delta-Time must be numeric. \n\nNumberFormatException: " + ex.getMessage() + ".",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(ControlPanel.this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		runButton.setMinimumSize(new Dimension(40, 40));
		runButton.setPreferredSize(new Dimension(40, 40));
		runButton.setMaximumSize(new Dimension(40, 40));
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		bar.add(runButton);
		
		stopButton = new JButton();
		stopButton.setActionCommand("stop");
		stopButton.setToolTipText("Stop the simulation");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
		});
		stopButton.setMinimumSize(new Dimension(40, 40));
		stopButton.setPreferredSize(new Dimension(40, 40));
		stopButton.setMaximumSize(new Dimension(40, 40));
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		bar.add(stopButton);
		
		bar.addSeparator();
		
		bar.add(new JLabel("Steps: "));
		
		spinner = new JSpinner(new SpinnerNumberModel(DEFAULT_VALUE_SPINNER, MIN_VALUE_SPINNER, MAX_VALUE_SPINNER, CHANGE_VALUE_SPINNER));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// Lo que hace al cambiarse
			}
		});
		spinner.setPreferredSize(new Dimension(70, 30));
		spinner.setMaximumSize(new Dimension(70, 30));
		bar.add(spinner);
		
		bar.addSeparator();
		
		bar.add(new JLabel("Delta-Time: "));
		
		text = new JTextField();
		text.setPreferredSize(new Dimension(100, 30));
		text.setMaximumSize(new Dimension(100, 30));
		bar.add(text);
		
		//Lo que ponga después de esta línea sale desplazado a la derecha
		bar.add(Box.createGlue());
		bar.addSeparator();
		
		//Aquí el botón de apagar:
		exitButton = new JButton();
		exitButton.setActionCommand("exit");
		exitButton.setToolTipText("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] options = { "Yes", "No" }; // Salía por defecto en español
				int n = JOptionPane.showOptionDialog(ControlPanel.this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0)
					System.exit(0);
			}
		});
		exitButton.setMinimumSize(new Dimension(40, 40));
		exitButton.setPreferredSize(new Dimension(40, 40));
		exitButton.setMaximumSize(new Dimension(40, 40));
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		bar.add(exitButton);
		bar.addSeparator();
	}
	// other private/protected methods
	// ...
	
	private void run_sim(int n) {
		if ( n>0 && !_stopped ) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(ControlPanel.this, "An error occured. " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				setButtonsEnabled(true);
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			});
		} else {
			_stopped = true;
			setButtonsEnabled(true);
		}
	}
	
	private void setButtonsEnabled(boolean state) {
		loadButton.setEnabled(state);
		forceButton.setEnabled(state);
		runButton.setEnabled(state);
		exitButton.setEnabled(state);
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		text.setText(Double.toString(dt));
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {
		// TODO Auto-generated method stub
		
	}
	
	public void open_dialog() {
		try {
			ForceLawsDialog myForceDialog = new ForceLawsDialog((Frame)SwingUtilities.getWindowAncestor(this), _ctrl.getForceLawsInfo()); 
			
			int status = myForceDialog.open();
			
			if(status==1) { 
				_ctrl.setForceLaws(new JSONObject(myForceDialog.getString()));
			}
		}
		catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}

}
