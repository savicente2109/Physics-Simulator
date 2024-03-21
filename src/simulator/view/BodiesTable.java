package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import simulator.control.Controller;

public class BodiesTable extends JPanel {
	
	private BodiesTableModel _model;
	private JTable _table;
	
	BodiesTable(Controller ctrl) {
		_model = new BodiesTableModel(ctrl);
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
		BorderFactory.createLineBorder(Color.black, 2), "Bodies", TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(20, 200));
		
		_table = new JTable(_model) {
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
		
		
		add(new JScrollPane(_table));
	}
}
