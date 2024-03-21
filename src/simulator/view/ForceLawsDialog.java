package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONObject;

public class ForceLawsDialog extends JDialog {

	private int _status;
	private List<JSONObject> _forceLawsInfo;
	private String[] _descValues;
	private DefaultComboBoxModel<String> _comboModel;
	private JComboBox<String> _comboBox;
	private JTable _table;
	private int _selectedLawsIndex;
	private TableModel _tableModel;

	ForceLawsDialog() {
		// TODO Auto-generated constructor stub
	}

	ForceLawsDialog(Frame parent, List<JSONObject> forceLawsInfo) {
		super(parent, true);
		_forceLawsInfo = forceLawsInfo;
		_descValues = new String[_forceLawsInfo.size()];
		for (int i = 0; i < _forceLawsInfo.size(); ++i)
			_descValues[i] = forceLawsInfo.get(i).getString("desc");

		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Force Laws Selection");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // realmente no sé qué layout es
		
		setContentPane(mainPanel);

		JLabel helpMsg = new JLabel(
				"Select a force law and provide values for the parameters in the Value column (default values are used for parameters with no value).");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(helpMsg);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		_tableModel = new TableModel();
		_table = new JTable(_tableModel) {

			private static final long serialVersionUID = 1L;

			// we override prepareRenderer to resized rows to fit to content
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		
		_table.setPreferredSize(new Dimension (mainPanel.getWidth(), 300));
		
		JScrollPane tableScroll = new JScrollPane(_table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroll.setMinimumSize(new Dimension(mainPanel.getWidth(), 200));
		mainPanel.add(tableScroll);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		viewsPanel.add(new JLabel("Force Law: "));

		_comboModel = new DefaultComboBoxModel<>(_descValues);
		_comboBox = new JComboBox<>(_comboModel);
		_comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_selectedLawsIndex = _comboBox.getSelectedIndex();
				_tableModel.update();
			}
		});

		viewsPanel.add(_comboBox);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ForceLawsDialog.this.setVisible(false); // ya no se ve el cuadro de diálogo
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_comboModel.getSelectedItem() != null) {
					_status = 1;
					ForceLawsDialog.this.setVisible(false); // ya no es visible
				}
			}
		});
		buttonsPanel.add(okButton);

		// Las siguientes opciones hay que adaptarlas probando:
		setPreferredSize(new Dimension(900, 500));
		pack();
		setResizable(false);// no permite al usuario agrandar esta ventana
		setVisible(false);// en el constructor no se hace visible todavía
	}

	private class TableModel extends AbstractTableModel {

		private String[] _colNames = { "Key", "Value", "Description" };
		private String[][] _data;

		TableModel() {
			createTable();
		}

		public void createTable() {

			JSONObject force = _forceLawsInfo.get(_selectedLawsIndex);

			JSONObject data = force.getJSONObject("data");
			_data = new String[data.keySet().size()][_colNames.length];

			int i = 0;
			for (String key : data.keySet()) {
				_data[i][0] = key;
				_data[i][1] = "";
				_data[i][2] = data.getString(key);
				++i;
			}
		}

		@Override
		public int getColumnCount() {
			return _colNames.length;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return _colNames[columnIndex];
		}

		@Override
		public int getRowCount() {
			return _data.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return _data[rowIndex][columnIndex];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}

		@Override
		public void setValueAt(Object o, int rowIndex, int columnIndex) {
			_data[rowIndex][columnIndex] = o.toString();
		}

		public void update() {
			createTable();
			fireTableStructureChanged();
		}

		public String getData() {
			StringBuilder s = new StringBuilder();
			s.append('{');
			for (int i = 0; i < _data.length; i++) {
				if (!_data[i][1].isEmpty()) {

					s.append('"');
					s.append(_data[i][0]);
					s.append('"');
					s.append(':');
					s.append(_data[i][1]);
					s.append(',');
				}
			}
			// Quita la "," y pone la "}"
			if (s.length() > 1)
				s.deleteCharAt(s.length() - 1);
			s.append('}');

			return s.toString();
		}
	}

	public int open() {
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true); // aquí es donde la hacemos visible, aquí se para hasta que el usuario
							// interactúe
		return _status;
	}

	public String getString() {
		StringBuilder s = new StringBuilder();
		s.append("{\"type\":");
		String s1 = "" + _forceLawsInfo.get(_selectedLawsIndex).getString("type");
		s.append(s1);
		s.append(",");
		s.append("\"data\":");
		s.append(_tableModel.getData());
		s.append(",");
		s.append("\"desc\":");
		String s2 = "" + _forceLawsInfo.get(_selectedLawsIndex).getString("desc");
		s.append(s2);
		s.append("}");
		return s.toString();
	}

}
