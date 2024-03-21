package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.border.Border;

import extra.jtable.EventEx;
import extra.jtable.EventsTableModel;
import simulator.control.Controller;

public class MainWindow extends JFrame {

	private Controller _ctrl;
	private JPanel _mainPanel;
	private ControlPanel _controlPanel;
	private BodiesTable _bodiesTable;
	private Viewer _viewer;
	private StatusBar _statusBar;
	
	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		
		_mainPanel = new JPanel(new BorderLayout());
		setContentPane(_mainPanel);
		
		_controlPanel = new ControlPanel(_ctrl);
		_mainPanel.add(_controlPanel, BorderLayout.PAGE_START);

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
		
		
		_bodiesTable = new BodiesTable(_ctrl);
		centralPanel.add(_bodiesTable);
		
		_viewer = new Viewer(_ctrl);
		
		centralPanel.add(_viewer);
		
		_mainPanel.add(centralPanel);
		
		
		_statusBar = new StatusBar(_ctrl);
		_mainPanel.add(_statusBar, BorderLayout.PAGE_END);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
		// other private/protected methods
		
	
	private void setPreferredSize() {
		
	}
}

	


