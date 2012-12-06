
import gauss2mf.Gauss2MF;
import gaussmf.GaussMF;
import gbellmf.GbellMF;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import dsigmf.DsigMF;

import othermf.othermf;
import pimf.PiMF;
import psigmf.PsigMF;

import sigmf.SigMF;
import smf.SMF;
import trapmf.TrapMF;
import trimf.TriMF;
import zmf.ZMF;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class EditVariable extends JFrame {

	private JTextField textVarType;
	private JTextField textVarName;
	private JTextField textVarRands;
	private JTextField textVarDiscret;
	private JTextField textPropName;
	private JTextField textParamsLMF;
	private JTextField textParamsUMF;
	private JList<String> listTerms;
	private JTable tableCustomParams;
	private int type,signNumber=6;
	private JUWindow MainWnd;
	private DITVariable variable;
	private DefaultListModel<String> termsListModel;
	private int currentTermIndex, otherIndex=11; //otherIndex - index of "OtherMF" in types  
	private Double[][] data;
	private Object[] columnNames={"X","LMF","UMF"};
	private DefaultTableModel modelTable;
	private JScrollPane scrollPane,scrollpane;
	private JPanel panel_1,panel_2,panel_3,panel_4;
	private JComboBox<String> comboPropType,cmbxLMF,cmbxUMF;
	private ArrayList<String> functionTypes=new ArrayList<String>();
	private String[] types={"TrapMF","TriMF","GaussMF","Gauss2MF","DsigMF","GbellMF","PiMF","PsigMF","SigMF","SMF","ZMF","OtherMF"}; //Functions names
	private String[] defaultValues={"0;2;3;4;","0;0.5;1;","2;1;1;3;","1;5;","5;2;5;7;","2;4;6;","1;4;5;10;","2;3;-5;8;","2;4;","1;8;","3;7;",""};
	/**
	 * Create the application.
	 * @wbp.parser.constructor
	 */
	public EditVariable(JUWindow parent) {
		setTitle("Edit Variable");
		initialize();
		this.MainWnd=parent;
	}
	public EditVariable(JUWindow parent,DITVariable var,int type){
		this.variable=var;
		this.type=type;
		this.MainWnd=parent;
		setTitle("Edit Variable");
		initialize();
		updatePanelInfo();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		currentTermIndex=-1;
		for(String s: types){
			functionTypes.add(s);
		}
		
		setBounds(100, 100, 711, 493);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Current Linguistic Variable", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 28, 208, 127);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Type");
		lblNewLabel.setBounds(10, 24, 46, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(10, 49, 46, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Rands");
		lblNewLabel_2.setBounds(10, 74, 46, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblDescretization = new JLabel("Discretization");
		lblDescretization.setBounds(10, 99, 75, 14);
		panel.add(lblDescretization);
		
		textVarType = new JTextField();
		textVarType.setEditable(false);
		textVarType.setBounds(86, 21, 112, 20);
		panel.add(textVarType);
		textVarType.setColumns(10);
		
		textVarName = new JTextField();
		textVarName.setEditable(false);
		textVarName.setColumns(10);
		textVarName.setBounds(86, 46, 112, 20);
		panel.add(textVarName);
		
		textVarRands = new JTextField();
		textVarRands.setEditable(false);
		textVarRands.setColumns(10);
		textVarRands.setBounds(86, 71, 112, 20);
		panel.add(textVarRands);
		
		textVarDiscret = new JTextField();
		textVarDiscret.setEditable(false);
		textVarDiscret.setColumns(10);
		textVarDiscret.setBounds(86, 96, 112, 20);
		panel.add(textVarDiscret);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Terms", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(228, 28, 242, 127);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		listTerms = new JList<String>();
		listTerms.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getButton()!=MouseEvent.BUTTON1) return;
				if(listTerms.getSelectedIndex()!=-1) currentTermIndex=listTerms.getSelectedIndex();
				if(evt.getClickCount()==1){
					if(termsListModel.isEmpty()) return;
					textPropName.setText(termsListModel.elementAt(currentTermIndex));
					if(currentTermIndex==-1||variable.terms==null) return;
					data=variable.getTerm(currentTermIndex);
					int quantity=-1;
					if(variable.range.isEmpty())
					{
						JOptionPane.showMessageDialog(null, "Fill in Range and discretization");
						return;
					}
					comboPropType.setSelectedIndex(1);
					setPanelEnable(panel_3,false);
					quantity=(int)((variable.max-variable.min)/Double.parseDouble(variable.discret))+1;
					if(data==null)
					{
						data=new Double[quantity][3];
						variable.updateTerm(currentTermIndex,data);
						int j=0;
						for(Double i=variable.min;i<=variable.max;i+=Double.parseDouble(variable.discret)){
							data[j][0]=new Double(i);
							j++;
						}
					}
					setPanelEnable(panel_2, true);
					setPanelEnable(panel_4, true);
					reCreateTable(data);
				}
			}
		});
		listTerms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		termsListModel=new DefaultListModel<String>();
		listTerms.setModel(termsListModel);
		scrollpane=new JScrollPane(listTerms);
		scrollpane.setBounds(10, 24, 146, 92);
		panel_1.add(scrollpane);
		
		JButton btnAddTerm = new JButton("Add");
		btnAddTerm.setToolTipText("Add new term");
		btnAddTerm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String term = JOptionPane.showInputDialog(null, "Enter name: ", "New Term", 1);
				if(term==null) return;
				if(term.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You should enter name");
				} else if(!variable.checkTermName(term,-2)){
					JOptionPane.showMessageDialog(null, "Term with the same name already exists");
				} else {
					variable.terms.add(term);
					data=new Double[(int) ((variable.max-variable.min)/Double.parseDouble(variable.discret))][3];
					variable.addTerm(data);
					termsListModel.addElement(term);
					currentTermIndex=termsListModel.size()-1;
				}
			}
		});
		btnAddTerm.setBounds(166, 13, 66, 46);
		panel_1.add(btnAddTerm);
		
		JButton btnDelTerm = new JButton("Del");
		btnDelTerm.setToolTipText("Delete selected term");
		btnDelTerm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index=listTerms.getSelectedIndex();
				if(index!=-1){
					variable.terms.remove(index);
					variable.deleteTerm(index);
					termsListModel.remove(index);
					data=null;
					reCreateTable(data);
				}
				if(listTerms.getSelectedIndex()<0) {
					setPanelEnable(panel_2, false);
					setPanelEnable(panel_4, false);
				}
				
			}
		});
		btnDelTerm.setBounds(166, 70, 66, 46);
		panel_1.add(btnDelTerm);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(480, 28, 208, 127);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 27, 46, 14);
		panel_2.add(lblName);
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(10, 52, 46, 14);
		panel_2.add(lblType);
		
		textPropName = new JTextField();
		textPropName.setBounds(66, 24, 132, 20);
		panel_2.add(textPropName);
		textPropName.setColumns(10);
		
		JButton btnSaveProp = new JButton("Save");
		btnSaveProp.setToolTipText("Save term name");
		btnSaveProp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String name=textPropName.getText();
				if(!variable.checkTermName(name,currentTermIndex)){
					JOptionPane.showMessageDialog(null, "Term with the same name already exists");
					return;
				}
				if(name!=null){
					variable.terms.remove(currentTermIndex);
					variable.terms.add(currentTermIndex,name);
					termsListModel.remove(currentTermIndex);
					termsListModel.add(currentTermIndex,name);
				}
			}
		});
		btnSaveProp.setBounds(66, 93, 89, 23);
		panel_2.add(btnSaveProp);
		
		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Standart Param FS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(10, 161, 678, 80);
		getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Params LMF");
		lblNewLabel_4.setBounds(10, 23, 104, 14);
		panel_3.add(lblNewLabel_4);
		
		JLabel lblParamsUmf = new JLabel("Params UMF");
		lblParamsUmf.setBounds(10, 48, 104, 14);
		panel_3.add(lblParamsUmf);
		
		textParamsLMF = new JTextField();
		textParamsLMF.setToolTipText("Parameters for selected function");
		textParamsLMF.setBounds(280, 20, 213, 20);
		panel_3.add(textParamsLMF);
		textParamsLMF.setColumns(10);
		
		textParamsUMF = new JTextField();
		textParamsUMF.setToolTipText("Parameters for selected function");
		textParamsUMF.setColumns(10);
		textParamsUMF.setBounds(280, 45, 213, 20);
		panel_3.add(textParamsUMF);
		
		JButton btnSaveParam = new JButton("Convert to ParamFS");
		btnSaveParam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String LMF=textParamsLMF.getText(), UMF=textParamsUMF.getText();
				if(LMF.isEmpty()||UMF.isEmpty()) return;
				String[] lmf=LMF.split(";"),umf=UMF.split(";");
				String typeLMF=(String) cmbxLMF.getSelectedItem(),
					   typeUMF=(String) cmbxUMF.getSelectedItem();
				data=convertToParamsFS(functionTypes.indexOf(typeLMF),functionTypes.indexOf(typeUMF),
										lmf, umf, generateX(variable.min, variable.max, Double.parseDouble(variable.discret)));
				reCreateTable(data);
				setPanelEnable(panel_4,true);
			}
		});
		btnSaveParam.setBounds(503, 19, 165, 43);
		panel_3.add(btnSaveParam);
		
		cmbxLMF = new JComboBox<String>();
		cmbxLMF.setToolTipText("Select function for generating term values");
		cmbxLMF.setBounds(99, 20, 171, 20);
		panel_3.add(cmbxLMF);
		
		cmbxUMF = new JComboBox<String>();
		cmbxUMF.setToolTipText("Select function for generating term values");
		cmbxUMF.setBounds(99, 45, 171, 20);
		panel_3.add(cmbxUMF);
		
		for(String type:functionTypes){
			cmbxLMF.addItem(type);
			cmbxUMF.addItem(type);
		}
		cmbxLMF.setSelectedIndex(0);
		cmbxUMF.setSelectedIndex(0);
		textParamsLMF.setText(defaultValues[cmbxLMF.getSelectedIndex()]);
		textParamsUMF.setText(defaultValues[cmbxUMF.getSelectedIndex()]);
		
		cmbxLMF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textParamsLMF.setText(defaultValues[cmbxLMF.getSelectedIndex()]);
				if(cmbxLMF.getSelectedIndex()==otherIndex) {
					String message="OtherMF function is selected. Be careful its default implementaion is empty.\nYou can replace othermf.jar with your own implementation in app lib folder";
					JOptionPane.showMessageDialog(null, message, "Attention", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		cmbxUMF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textParamsUMF.setText(defaultValues[cmbxUMF.getSelectedIndex()]);
				if(cmbxUMF.getSelectedIndex()==otherIndex) {
					String message="OtherMF function is selected. Be careful its default implementaion is empty.\nYou can replace othermf.jar with your own implementation in app lib folder";
					JOptionPane.showMessageDialog(null, message, "Attention", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Custom Params FS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 242, 678, 202);
		getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		JButton btnSaveCustom = new JButton("Save");
		btnSaveCustom.setToolTipText("Save term values");
		btnSaveCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int columns=modelTable.getColumnCount(), rows=modelTable.getRowCount();
				if(tableCustomParams.isEditing()) tableCustomParams.getCellEditor().stopCellEditing();
				boolean correct=true;
				Double[][] newData=new Double[rows][columns];
				try{
					for(int i=0;i<columns;i++){
						for(int j=0;j<rows;j++){
							Double temp=new Double(modelTable.getValueAt(j,i).toString());
							newData[j][i]=temp;
						}
					}
				} catch (Exception e){
					JOptionPane.showMessageDialog(null, "Incorrect number format. Check table values.");
					correct=false;
				}
				if(correct) variable.updateTerm(currentTermIndex, newData);
				
			}
		});
		btnSaveCustom.setBounds(579, 18, 89, 23);
		panel_4.add(btnSaveCustom);

		modelTable = new DefaultTableModel(data,columnNames);
		tableCustomParams = new JTable(modelTable);
		
		scrollPane = new JScrollPane(tableCustomParams);
		scrollPane.setBounds(10, 18, 556, 173);
		panel_4.add(scrollPane);
		
		comboPropType = new JComboBox<String>();
		comboPropType.setToolTipText("Type of generating term values");
		comboPropType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(comboPropType.getSelectedIndex()){
				case 0:
					setPanelEnable(panel_3, true);
					setPanelEnable(panel_4, false);
					tableCustomParams.setEnabled(false);
					scrollPane.setEnabled(false);
					break;
				case 1:
					setPanelEnable(panel_3, false);
					setPanelEnable(panel_4, true);
					tableCustomParams.setEnabled(true);
					scrollPane.setEnabled(true);
					if(data==null){
						int quantity=(int)((variable.max-variable.min)/Double.parseDouble(variable.discret))+1;
						data=new Double[quantity][3];
						reCreateTable(data);
					}
					break;
				}
			}
		});
		comboPropType.addItem("Standart");
		comboPropType.addItem("User");
		if(variable.range!=null&&!variable.range.isEmpty())
		{
			comboPropType.setSelectedIndex(1);
			setPanelEnable(panel_3, true);
		}
		else 
		{
			comboPropType.setSelectedIndex(0);
			setPanelEnable(panel_3, true);
		}
		comboPropType.setBounds(66, 49, 132, 20);
		panel_2.add(comboPropType);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				MainWnd.setResultFromChild(variable);
			}
			
		});
		setPanelEnable(panel_2, false);
		setPanelEnable(panel_3, false);
		setPanelEnable(panel_4, false);
	}
	/**
	 * Recreate JTable with the modified model
	 */
	private void reCreateTable(Double[][] data){
		panel_4.remove(scrollPane);
		modelTable = new DefaultTableModel(data,columnNames);
		tableCustomParams = new JTable(modelTable);
		tableCustomParams.getTableHeader().setReorderingAllowed(false);
		tableCustomParams.getTableHeader().setResizingAllowed(false);
		scrollPane = new JScrollPane(tableCustomParams);
		scrollPane.setBounds(10, 18, 556, 173);
		panel_4.add(scrollPane);
	}
	/**
	 * Enable|Disable specified panel
	 * @param panel - panel to enable
	 * @param flag true - enable, false - disable
	 */
	private void setPanelEnable(JPanel panel, boolean flag){
		Component[] comp=panel.getComponents();
		panel.setEnabled(flag);
		for(Component c: comp){
			c.setEnabled(flag);
		}
	}
	/**
	 * Update info about variable
	 */
	private void updatePanelInfo(){
		String t=new String();
		switch(type){
		case 0: 
			t="Input variable";
			break;
		case 1:
			t="Output variable";
			break;
		}
		textVarType.setText(t);
		textVarName.setText(variable.name);
		textVarRands.setText(variable.range);
		textVarDiscret.setText(variable.discret);
		if(variable.terms==null) return;
		if(variable.terms.isEmpty()) return;
		for(String name: variable.terms){
			termsListModel.addElement(name);
		};
	}
	/**
	 * Converts input data to ParamsFS according to chosen function
	 * @param lmftype - type of function for lmf
	 * @param umftype - type of function for umf
	 * @param dlmf	- values of lmf for function
	 * @param dumf - values of umf for function
	 * @param x - array of x values
	 * @return generated table
	 */
	private Double[][] convertToParamsFS(int lmftype,int umftype, String[] dlmf, String[] dumf,Double[] x){
		Double[][] data=new Double[x.length][3];
		int i=0, type=0;
		Double[] lmf=new Double[x.length], umf=new Double[x.length],res=new Double[x.length];
		Double[] paramsMF=new Double[dlmf.length];
		
		for(int j=0;j<2;j++){
			switch(j){
				case 0:
					type=lmftype;
					i=0;
					for(String s: dlmf){
						paramsMF[i]=Double.parseDouble(s);
						if(paramsMF[i]>variable.max||paramsMF[i]<variable.min){
							JOptionPane.showMessageDialog(null, "LMF function parameters are out of range.");
							return null;
						}
						i++;
					}
					break;
				case 1: 
					type=umftype;
					i=0;
					for(String s: dumf){
						paramsMF[i]=Double.parseDouble(s);
						if(paramsMF[i]>variable.max||paramsMF[i]<variable.min){
							JOptionPane.showMessageDialog(null, "UMF function parameters are out of range.");
							return null;
						}
						i++;
					}
					break;
			}
			
			switch(type){
				case 0:
					TrapMF trapmf=new TrapMF();
					res=trapmf.GetDiscreteFunction(x, paramsMF);
					break;
				case 1:
					TriMF trimf=new TriMF();
					res=trimf.GetDiscreteFunction(x, paramsMF);
					break;
				case 2:
					GaussMF gauss=new GaussMF();
					res=gauss.GetDiscreteFunction(x, paramsMF);
					break;
				case 3:
					Gauss2MF gauss2=new Gauss2MF();
					res=gauss2.GetDiscreteFunction(x, paramsMF);
					break;
				case 4:
					DsigMF dsig=new DsigMF();
					res=dsig.GetDiscreteFunction(x, paramsMF);
					break;
				case 5:
					GbellMF gbell=new GbellMF();
					res=gbell.GetDiscreteFunction(x, paramsMF);
					break;
				case 6:
					PiMF pimf=new PiMF();
					res=pimf.GetDiscreteFunction(x, paramsMF);
					break;
				case 7:
					PsigMF psig=new PsigMF();
					res=psig.GetDiscreteFunction(x, paramsMF);
					break;
				case 8:
					SigMF sig=new SigMF();
					res=sig.GetDiscreteFunction(x, paramsMF);
					break;
				case 9:
					SMF smf=new SMF();
					res=smf.GetDiscreteFunction(x, paramsMF);
					break;
				case 10:
					ZMF zmf=new ZMF();
					res=zmf.GetDiscreteFunction(x, paramsMF);
					break;
				case 11:
					othermf other=new othermf();
					res=other.GetDiscreteFunction(x, paramsMF);
					break;
			}
			if (res==null) return data;
			switch(j){
				case 0:
					lmf=res.clone();
					break;
				case 1:
					umf=res.clone();
					break;
			}
		}
		i=0;
		for(Double d: x){
			data[i][0]=d;
			if(lmf[i]<umf[i]){
				data[i][1]=Round(lmf[i],this.signNumber);
				data[i][2]=Round(umf[i],this.signNumber);
			} else {
				data[i][1]=Round(umf[i],this.signNumber);
				data[i][2]=Round(lmf[i],this.signNumber);
			}
			i++;
		};
		return data;
	}
	/**
	 * Generate X array according to discretization and range
	 * @param min - lowest from range
	 * @param max - highest from range
	 * @param discr - discretization
	 * @return array of x
	 */
	private Double[] generateX(Double min, Double max, Double discr){
		Double[] x=new Double[(int) (Math.abs(max-min)/discr)+1];
		int i=0;
		while(min<=max){
			x[i]=Round(min,this.signNumber);
			min+=discr;
			i++;
		}
		x[i]=max;
		return x;
	}

	private static Double Round(Double valueToRound,int signNumber){
		double multiplicationFactor = Math.pow(10, signNumber);
        return Math.round(valueToRound * multiplicationFactor)/ multiplicationFactor; 
	}
}
