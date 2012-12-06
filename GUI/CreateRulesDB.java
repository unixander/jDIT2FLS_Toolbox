
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


@SuppressWarnings("serial")
public class CreateRulesDB extends JFrame{

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTable table; 		//for GUI
	private JFrame instance; 
	private JUWindow MainWnd; 	//Parent window
	private DITRule rule;		//Rules database
	public List<DITVariable> LIvars, LOvars; //variables
	private String[] columnNames;	//column names for table header
	private int columnNumber;		//number of columns
	private DefaultTableModel model;//for GUI
	private List<String[]> terms;	//temporary variable for combobox fillin
	
	/**
	 * Create window
	 * @param wnd - parent window, inherited from JUWindow interface. 
	 * @param r - instance of the DITRule, which contains rules database
	 * @param LIvars - list of linguistic input variables
	 * @param LOvars - list of linguistic output variables
	 */
	public CreateRulesDB(JUWindow wnd,DITRule r,List<DITVariable> LIvars,List<DITVariable> LOvars) {
		setResizable(false);
		this.instance=this;
		this.MainWnd=wnd;
		this.rule=r;
		this.LIvars=LIvars;
		this.LOvars=LOvars;
		initTable();
		initialize();
		if(rule!=null)
			if(rule.rules!=null)
				if(rule.rules.length!=0){
					fillinTable();
				}
	}
	/**
	 * Make JTable with the header
	 */
	private void initTable(){
		columnNumber=LIvars.size()+LOvars.size()+1;
		columnNames=new String[columnNumber];
		int i=0;
		for(DITVariable var:LIvars){
			columnNames[i]=var.name;
			i++;
		}
		for(DITVariable var:LOvars){
			columnNames[i]=var.name;
			i++;
		}
		columnNames[i]="Value";
	}
	
	/**
	 * Fill JTable with values from rule.rules variable
	 */
	private void fillinTable(){
		int rows=model.getRowCount();
		for(int j=rows-1;j>=0;j--){
			model.removeRow(j);
		}
		if(rule.rules.length==0||rule.rules[0].length!=columnNumber) rule.rules=new Double[0][0];
		for(int row=0;row<rule.rules.length;row++){
			AddNewRow();
			for(int column=0;column<columnNumber;column++){
				Double value=new Double(rule.rules[row][column]);
				String[] values = null;
				if(column!=columnNumber-1) 
					{
						values=terms.get(column);
						if(value>values.length) value=new Double(-1);
					}
				if(column!=columnNumber-1&&value!=-1){
					model.setValueAt(values[value.intValue()], row, column);
				} else {
					model.setValueAt(value, row, column);
					
				}
			}
		}
		table.invalidate();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Create Rules DB");
		setBounds(100, 100, 711, 493);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setToolTipText("Clear rules base");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.isEditing()) table.getCellEditor().stopCellEditing();
				model.setRowCount(0);
			}
		});
		btnClear.setBounds(493, 392, 89, 52);
		getContentPane().add(btnClear);
		
		JButton btnSave = new JButton("Save");
		btnSave.setToolTipText("Save rules base");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.isEditing()) table.getCellEditor().stopCellEditing();
				SaveResult();
			}
		});
		btnSave.setBounds(596, 392, 89, 52);
		getContentPane().add(btnSave);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Condition", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 397, 124, 47);
		getContentPane().add(panel);
		
		JRadioButton rdbtnOr = new JRadioButton("OR");
		buttonGroup.add(rdbtnOr);
		
		JRadioButton rdbtnAnd = new JRadioButton("AND");
		buttonGroup.add(rdbtnAnd);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnOr)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnAnd)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnOr)
						.addComponent(rdbtnAnd))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		// --- Remove this strings, when implemented
		panel.setEnabled(false); 
		rdbtnAnd.setEnabled(false);
		rdbtnOr.setEnabled(false);
		//End for removing 
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 675, 372);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		model = (DefaultTableModel)table.getModel();
		for(String name: columnNames){
			model.addColumn(name);
		}
		int currentColumn=0;
		terms=new ArrayList<String[]>();
		for(DITVariable var: LIvars){
			int i=0;
			String values[]=new String[var.getTermsQuantity()];
			for(String t:var.terms){
				values[i]=t;
				i++;
			}
			TableColumn col = table.getColumnModel().getColumn(currentColumn);
			col.setCellEditor(new DITComboBoxEditor(values));
			terms.add(values);
			currentColumn++;
		}
		for(DITVariable var: LOvars){
			int i=0;
			String values[]=new String[var.getTermsQuantity()];
			for(String t:var.terms){
				values[i]=t;
				i++;
			}
			TableColumn col = table.getColumnModel().getColumn(currentColumn);
			col.setCellEditor(new DITComboBoxEditor(values));
			terms.add(values);
			currentColumn++;
		}
		scrollPane.setViewportView(table);
		
		JButton btnAddRule = new JButton("Add rule");
		btnAddRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddNewRow();
			}
		});
		btnAddRule.setBounds(261, 421, 103, 23);
		getContentPane().add(btnAddRule);
		
		JButton btnDeleteRule = new JButton("Delete rule");
		btnDeleteRule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DelRow();
			}
		});
		btnDeleteRule.setBounds(380, 421, 103, 23);
		getContentPane().add(btnDeleteRule);
		
		JButton btnNewButton = new JButton("Rules auto generation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] columns=new int[LIvars.size()+LOvars.size()];
				int i=0;
				for(DITVariable var: LIvars){
					columns[i]=var.getTermsQuantity();
					i++;
				};
				for(DITVariable var: LOvars){
					columns[i]=var.getTermsQuantity();
					i++;
				};

				int linesCount=1;
				
				for (int j = 0; j < columns.length; j++) {
			        linesCount *= columns[j];
			    }
				
				int cur;
				Double[][] data=new Double[linesCount][columnNumber];
				
			    for(int j = 0; j < columnNumber-1; j++) {
			        cur = 0;
			        for(int k = 0; k < linesCount; k++) {
			            if (cur > columns[j]-1)
			                cur = 0;
			            data[k][j] =new Double(cur++);
			        }
			    }
			    for(int j=0;j<linesCount;j++){
			    	data[j][columnNumber-1]=new Double(1);
			    }
			    rule.rules=data.clone();
			    fillinTable();
			}
		});
		
		btnNewButton.setBounds(260, 392, 223, 23);
		getContentPane().add(btnNewButton);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int confirmed = JOptionPane.showConfirmDialog(null,"Save rules base before closing?", "User Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION);
				if (confirmed == JOptionPane.YES_OPTION){
					SaveResult();
					MainWnd.setResultFromChild(rule);
					exit(instance);
				} else if(confirmed==JOptionPane.NO_OPTION){
					MainWnd.setResultFromChild(rule);
					exit(instance);
				}
				
			}
			
		});

	}
	static void exit(JFrame frame) {
         frame.dispose();
	}
	/**
	 * Add new row to the JTable
	 */
	private void AddNewRow(){
		model.addRow(new Object[columnNumber]);
	}
	
	/**
	 * Delete selected row from JTable
	 */
	private void DelRow(){
		int select=table.getSelectedRow();
		if(select==-1) return;
		if(table.isEditing()) table.getCellEditor().stopCellEditing();
		model.removeRow(select);
	}
	
	/*
	 * Save result from JTable to variable
	 */
	private void SaveResult(){
		int columns=model.getColumnCount(), rows=model.getRowCount();
		Double[][] res=new Double[rows][columns];
		Double result=new Double(0);
		boolean flag=true;
		try{
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					Object obj=model.getValueAt(i, j);
					if(j!=columns-1){
						String temp=obj.toString();
						result=getPosition(terms.get(j), temp);
					} else result=new Double(obj.toString());
					res[i][j]=result;
				}
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Invalid content in cells. Check whether all cell are filled in with correct values","Error",JOptionPane.ERROR_MESSAGE);
			flag=false;
		} 
		if(flag) {
			rule.rules=res.clone();
		}
	}
	
	/**
	 * Get position of specified value in the array
	 * @param values - array of values
	 * @param value - element to search
	 * @return - position in the array
	 */
	private Double getPosition(String[] values, String value){
		Double result=new Double(-1);
		for(String s:values){
			result++;
			if(s.equals(value)) return result;
		}
		return result;
	}
	
	/**
	 * Cell editor with the combobox
	 * @author Unixander
	 */
	public class DITComboBoxEditor extends DefaultCellEditor {
	    public DITComboBoxEditor(String[] items) {
	        super(new JComboBox<String>(items));
	    } 
	}
}
