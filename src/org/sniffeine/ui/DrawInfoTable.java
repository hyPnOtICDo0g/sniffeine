package org.sniffeine.ui;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTable;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.table.AbstractTableModel;
import org.sniffeine.handler.PacketCaptureHandler;

class WindowCloseEventHandler extends WindowAdapter{
	@Override
	public void windowClosing(WindowEvent evt){
		PacketCaptureHandler.RunUntilClosed = false;
	}
}

public class DrawInfoTable extends JFrame{
	public TableModel model;
	private final String[] header = {
		"No.",
		"Timestamp",
		"Source IP",
		"Destination IP",
		"Source Port",
		"Destination Port",
		"Protocol",
		"Packet Length",
		"Hex Stream"
	};

	public DrawInfoTable(String[][] TableObj){
		// set window name
		super("Sniffeine");
		// create a panel to hold the table
		JPanel panel = new JPanel(new BorderLayout());
		// create a model
		model = new TableModel(TableObj, header);
		// create a table for the model
		JTable table = new JTable(model);
		// set colors and font
		table.setBackground(Color.decode("#b7cde4"));
		table.setForeground(Color.black);
		table.setFont(new Font("Verdana", Font.PLAIN, 12));
		// make the table scrollable
		panel.add(new JScrollPane(table));
		// add a panel to the frame
		add(panel);
		addWindowListener(new WindowCloseEventHandler());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// set window dimensions
		setPreferredSize(new Dimension(1300, 600));
		setVisible(true);
		pack();
	}

	public class TableModel extends AbstractTableModel{
		// use an ArrayList to store all records
		ArrayList<String[]> al;
		// table headers
		String[] header;

		TableModel(String[][] TableObj, String[] header){
			this.header = header;
			al = new ArrayList<String[]>();
			// copy rows into the ArrayList
			for(int i = 0; i < TableObj.length; ++i)
				al.add(TableObj[i]);
		}

		// return row count
		@Override
		public int getRowCount(){
			return al.size();
		}

		// return column count
		@Override
		public int getColumnCount(){
			return header.length;
		}

		// return a record in the arrayList
		@Override
		public String getValueAt(int rowIndex, int columnIndex){
			return al.get(rowIndex)[columnIndex];
		}

		// return header
		@Override
		public String getColumnName(int index){
			return header[index];
		}

		// add a record to the table
		public void add(String[] record){
			al.add(record);
			// update the GUI
			fireTableDataChanged();
		}
	}
}
