
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import javax.swing.JTextField;


public class MainWindow implements JUWindow {

	private JFrame frmDitflsGui;
	private JList<String> ovList,ivList;
	private JEditorPane nameSystem,editName, editRange, editDiscret;
	private DefaultListModel<String> ivModel,ovModel;
	private MainWindow instance;
	private DITSystem system;
	private int currentIndex, currentType;
	private JScrollPane ivScroll, ovScroll;
	private Object response;
	private JPanel panel_2;
	private JTextField textModelPath;
	private JTextField textLIpath;
	private JTextField textLOpath;
	private JTextField textRulesPath;
	private JButton btnAddOV;
	private String currentPath;
    private JFileChooser fc=new JFileChooser();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainWindow window = new MainWindow();
					window.frmDitflsGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		instance=this;
	}

	private void initializeData(){
		
		if(system==null) return;
		//system.convertDatatoInner(-1);
		if(system.name!=null)  nameSystem.setText(system.name);
		if(system.LIvars!=null){
			ivModel.removeAllElements();
			for(DITVariable var: system.LIvars){
				ivModel.addElement(var.name);
			}
		}
		if(system.LOvars!=null){
			ovModel.removeAllElements();
			for(DITVariable var: system.LOvars){
				ovModel.addElement(var.name);
			}
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		File directory = new File (".");
		currentPath=directory.getAbsolutePath();
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setCurrentDirectory(new File(currentPath));
		fc.setMultiSelectionEnabled(false);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new XMLFilter());
		
		this.currentIndex=-1; this.currentType=-1;
		frmDitflsGui = new JFrame();
		frmDitflsGui.setForeground(new Color(204, 204, 204));
		frmDitflsGui.setResizable(false);
		frmDitflsGui.setTitle("jDIT2FLS Toolbox");
		frmDitflsGui.setBounds(100, 100, 797, 493);
		frmDitflsGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		system=new DITSystem();
		
		JMenuBar menuBar = new JMenuBar();
		frmDitflsGui.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadInputVariable = new JMenuItem("Load input variables");
		mntmLoadInputVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int res=fc.showDialog(null, "Open");
				if(res==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					updateCurrentPath(file);
					system.loadLI(file.getAbsolutePath());
				}
				initializeData();
			}
		});
		mnFile.add(mntmLoadInputVariable);
		
		JMenuItem mntmLoadOutputVariables = new JMenuItem("Load output variables");
		mntmLoadOutputVariables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						int res=fc.showDialog(frmDitflsGui, "Open");
						if(res==JFileChooser.APPROVE_OPTION){
							File file = fc.getSelectedFile();
							updateCurrentPath(file);
							system.loadLO(file.getAbsolutePath());
						}
						initializeData();
					}
				});
		mnFile.add(mntmLoadOutputVariables);
		
		JMenuItem mntmLoadRulesDb = new JMenuItem("Load rules db");
		mntmLoadRulesDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res=fc.showDialog(frmDitflsGui, "Open");
				if(res==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					updateCurrentPath(file);
					system.loadRules(file.getAbsolutePath());
				}
				initializeData();
			}
		});
		mnFile.add(mntmLoadRulesDb);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmSaveInputVariables = new JMenuItem("Save input variables");
		mntmSaveInputVariables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system.LIvars.size()==0){
					JOptionPane.showMessageDialog(frmDitflsGui, "There are no input variables to save.","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int res=fc.showDialog(frmDitflsGui, "Save");
				if(res==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					updateCurrentPath(file);
					system.saveLI(file.getAbsolutePath());
				}
			}
		});
		mnFile.add(mntmSaveInputVariables);
		
		JMenuItem mntmSaveOutputVariables = new JMenuItem("Save output variables");
		mntmSaveOutputVariables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system.LOvars.size()==0){
					JOptionPane.showMessageDialog(frmDitflsGui, "There are no output variables to save.","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int res=fc.showDialog(frmDitflsGui, "Save");
				if(res==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					updateCurrentPath(file);
					system.saveLO(file.getAbsolutePath());
				}
			}
		});
		mnFile.add(mntmSaveOutputVariables);
		
		JMenuItem mntmSaveRulesDb = new JMenuItem("Save rules db");
		mntmSaveRulesDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system.rule.rules.length==0){
					JOptionPane.showMessageDialog(frmDitflsGui, "There is no rules base to save.","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int res=fc.showDialog(frmDitflsGui, "Save");
				if(res==JFileChooser.APPROVE_OPTION){
					File file = fc.getSelectedFile();
					updateCurrentPath(file);
					system.saveRules(file.getAbsolutePath());
				}
			}
		});
		mnFile.add(mntmSaveRulesDb);
		
		JSeparator separator_2 = new JSeparator();
		mnFile.add(separator_2);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ivModel.removeAllElements();
				ovModel.removeAllElements();
				system=new DITSystem();
				system.modelPath=textModelPath.getText();
				system.LIpath=textLIpath.getText();
				system.LOpath=textLOpath.getText();
				system.Rulepath=textRulesPath.getText();
			}
		});
		mnFile.add(mntmClose);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(frmDitflsGui,"Are you sure you want to close program?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION){
					exit(frmDitflsGui);
				};
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmEditRulesDb = new JMenuItem("Edit rules db");
		mntmEditRulesDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final CreateRulesDB r=new CreateRulesDB(instance,system.rule,system.LIvars,system.LOvars);
				r.setVisible(true);
				r.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent arg0) {
						system.rule=(DITRule)response;
					}
				});
			}
		});
		mnEdit.add(mntmEditRulesDb);
		
		JMenu mnModel = new JMenu("Model");
		menuBar.add(mnModel);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mntmRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system.LIvars!=null||system.LOvars!=null||system.rule!=null)
					if(system.LIvars.size()<=0||system.LOvars.size()<=0||system.rule.rules.length<=0){
						JOptionPane.showMessageDialog(frmDitflsGui, "You must load or enter all required variables and rules","Warning",JOptionPane.WARNING_MESSAGE);
					} else {
						RunModel run=new RunModel(system);
						run.setVisible(true);
					}
			}
		});
		mnModel.add(mntmRun);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About about=new About();
				about.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);
		frmDitflsGui.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Input|Output variables", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 0, 771, 221);
		frmDitflsGui.getContentPane().add(panel);
		panel.setLayout(null);
		
		ovModel= new DefaultListModel<String>();
		ovList = new JList<String>(ovModel);
		ovList.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mouseClicked(MouseEvent evt) {
						if(ovModel.isEmpty()) return;
						if(evt.getClickCount()==2&&evt.getButton()==MouseEvent.BUTTON1){
							DITVariable variable=system.LOvars.get(ovList.getSelectedIndex());
							final EditVariable editVar=new EditVariable(instance,variable,1);
							editVar.setVisible(true);
							editVar.addWindowListener(new WindowAdapter() {
		
								@Override
								public void windowClosing(WindowEvent arg0) {
									int index=ovList.getSelectedIndex();
									system.LOvars.remove(index);
									system.LOvars.add(index,(DITVariable)response);
									
								}
							});
						} else if(evt.getButton()==MouseEvent.BUTTON1){
							int index=ovList.getSelectedIndex();
							editName.setText(system.LOvars.get(index).name);
							editRange.setText(system.LOvars.get(index).range);
							editDiscret.setText(system.LOvars.get(index).discret);
						}
						currentIndex=ovList.getSelectedIndex();
						currentType=1;
						if(currentIndex>=0) setPanelEnable(panel_2, true);
					}
		
				});
		ovScroll=new JScrollPane(ovList);
		ovScroll.setBounds(400, 46, 274, 164);
		panel.add(ovScroll);
		
		JLabel lblNewLabel = new JLabel("Input Variables");
		lblNewLabel.setBounds(10, 21, 123, 14);
		panel.add(lblNewLabel);
		
		ivModel= new DefaultListModel<String>();
		ivList = new JList<String>(ivModel);
		ivList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ivList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(ivList.getSelectedIndex()==-1){
					editName.setText("");
					editRange.setText("");
					editDiscret.setText("");
					return;
				}
				if(ivModel.isEmpty()) return;
				if(evt.getClickCount()==2&&evt.getButton()==MouseEvent.BUTTON1){
					DITVariable variable=system.LIvars.get(ivList.getSelectedIndex());
					final EditVariable editVar=new EditVariable(instance,variable,0);
					editVar.setVisible(true);
					editVar.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosing(WindowEvent arg0) {
							int index=ivList.getSelectedIndex();
							system.LIvars.remove(index);
							system.LIvars.add(index,(DITVariable)response);
						}
					});
				} else if(evt.getButton()==MouseEvent.BUTTON1){
					int index=ivList.getSelectedIndex();
					editName.setText(system.LIvars.get(index).name);
					editRange.setText(system.LIvars.get(index).range);
					editDiscret.setText(system.LIvars.get(index).discret);
				}
				currentIndex=ivList.getSelectedIndex();
				currentType=0;
				if (currentIndex>=0)setPanelEnable(panel_2, true);
			}

		});
		ivScroll=new JScrollPane(ivList);
		ivScroll.setBounds(10, 46, 293, 164);
		panel.add(ivScroll);
		
		JLabel lblOutputVariables = new JLabel("Output Variables");
		lblOutputVariables.setBounds(398, 21, 123, 14);
		panel.add(lblOutputVariables);
		
		JButton btnAddIV = new JButton("ADD");
		btnAddIV.setToolTipText("Add input linguistic variable");
		btnAddIV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ivName = JOptionPane.showInputDialog(frmDitflsGui, "Enter name: ", "Input variable", 1);
				if(ivName==null) return;
				if(ivName.isEmpty()) {
					JOptionPane.showMessageDialog(frmDitflsGui, "You should enter name","Error",JOptionPane.ERROR_MESSAGE);
				} else if(!system.checkInputVariableName(ivName,-1)){
					JOptionPane.showMessageDialog(frmDitflsGui, "Variable with the same name already exists","Error",JOptionPane.ERROR_MESSAGE);
				} else {
					ivModel.addElement(ivName);
					system.addNewVar(ivName,0);
				}
			}
		});
		btnAddIV.setBounds(313, 46, 77, 77);
		panel.add(btnAddIV);
		
		JButton btnDeleteIV = new JButton("DEL");
		btnDeleteIV.setToolTipText("Delete input linguistic variable");
		btnDeleteIV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=ivList.getSelectedIndex();
				if(index!=-1){
					ivModel.remove(index);
					system.LIvars.remove(index);
				}
				if(ivList.getSelectedIndex()<0) setPanelEnable(panel_2, false);
			}
		});
		btnDeleteIV.setBounds(313, 133, 77, 77);
		panel.add(btnDeleteIV);
		
		btnAddOV = new JButton("ADD");
		btnAddOV.setToolTipText("Add output linguistic variable");
		btnAddOV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ovModel.size()==1){
					JOptionPane.showMessageDialog(frmDitflsGui, "Current version of DIT2FLS library allows only one output linguistic variable","Warning",JOptionPane.WARNING_MESSAGE);
					return;
				}
				String ovName = JOptionPane.showInputDialog(frmDitflsGui, "Enter name: ", "Output variable", 1);
				if(ovName==null) return;
				if(ovName.isEmpty()) {
					JOptionPane.showMessageDialog(frmDitflsGui, "You should enter name","Error",JOptionPane.ERROR_MESSAGE);
				} else if(!system.checkOutputVariableName(ovName,-1)){
					JOptionPane.showMessageDialog(frmDitflsGui, "Variable with the same name already exists","Error",JOptionPane.ERROR_MESSAGE);
				} else {
					ovModel.addElement(ovName);
					system.addNewVar(ovName,1);
				}
			}
		});
		btnAddOV.setBounds(684, 46, 77, 77);
		panel.add(btnAddOV);
		
		JButton btnDeleteOV = new JButton("DEL");
		btnDeleteOV.setToolTipText("Delete output linguistic variable");
		btnDeleteOV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=ovList.getSelectedIndex();
				if(index!=-1){
					ovModel.remove(index);
					system.LOvars.remove(index);
					btnAddOV.setEnabled(true);
				}
				if(ovList.getSelectedIndex()<0) setPanelEnable(panel_2, false);
			}
		});
		btnDeleteOV.setBounds(684, 133, 77, 77);
		panel.add(btnDeleteOV);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "System", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 223, 388, 210);
		frmDitflsGui.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 30, 46, 14);
		panel_1.add(lblName);
		
		nameSystem = new JEditorPane();
		nameSystem.setToolTipText("System name");
		nameSystem.setBounds(122, 24, 208, 20);
		panel_1.add(nameSystem);
		
		JLabel lblModelPath = new JLabel("Model path:");
		lblModelPath.setBounds(10, 58, 102, 14);
		panel_1.add(lblModelPath);
		
		textModelPath = new JTextField();
		textModelPath.setToolTipText("DIT2FLS model folder");
		textModelPath.setBounds(122, 55, 208, 20);
		panel_1.add(textModelPath);
		textModelPath.setColumns(10);
		
		JButton btnModel = new JButton("...");
		btnModel.setToolTipText("DIT2FLS model folder");
		btnModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JFileChooser dir=new JFileChooser();
				 dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				 dir.setMultiSelectionEnabled(false);
				 dir.setCurrentDirectory(new File(currentPath));
				 int res=dir.showDialog(frmDitflsGui, "Open");
					if(res==JFileChooser.APPROVE_OPTION){
						File file = dir.getSelectedFile();
						system.modelPath=file.getAbsolutePath();
						textModelPath.setText(system.modelPath);
						system.LIpath=system.modelPath+"\\LI.xml";
						textLIpath.setText(system.LIpath);
						system.LOpath=system.modelPath+"\\LO.xml";
						textLOpath.setText(system.LOpath);
						system.Rulepath=system.modelPath+"\\Rules.xml";
						textRulesPath.setText(system.Rulepath);
						
					}
			}
		});
		btnModel.setBounds(340, 54, 38, 23);
		panel_1.add(btnModel);
		
		textLIpath = new JTextField();
		textLIpath.setToolTipText("Path to file with LI variables");
		textLIpath.setColumns(10);
		textLIpath.setBounds(122, 84, 208, 20);
		panel_1.add(textLIpath);
		
		JLabel lblInputVarsFile = new JLabel("Input vars file:");
		lblInputVarsFile.setBounds(10, 87, 102, 14);
		panel_1.add(lblInputVarsFile);
		
		JButton btnLI = new JButton("...");
		btnLI.setToolTipText("Path to file with LI variables");
		btnLI.setBounds(340, 83, 38, 23);
		btnLI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(!system.modelPath.isEmpty()) fc.setCurrentDirectory(new File(system.modelPath));
				 int res=fc.showDialog(frmDitflsGui, "Open");
					if(res==JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
						updateCurrentPath(file);
						system.LIpath=file.getAbsolutePath();
						textLIpath.setText(system.LIpath);
					}
			}
		});
		panel_1.add(btnLI);
		
		textLOpath = new JTextField();
		textLOpath.setToolTipText("Path to file with LO variables");
		textLOpath.setColumns(10);
		textLOpath.setBounds(122, 116, 208, 20);
		panel_1.add(textLOpath);
		
		JLabel lblOutputVarsFile = new JLabel("Output vars file");
		lblOutputVarsFile.setBounds(10, 119, 102, 14);
		panel_1.add(lblOutputVarsFile);
		
		JButton btnLO = new JButton("...");
		btnLO.setToolTipText("Path to file with LO variables");
		btnLO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(!system.modelPath.isEmpty()) fc.setCurrentDirectory(new File(system.modelPath));
				 int res=fc.showDialog(frmDitflsGui, "Open");
					if(res==JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
						updateCurrentPath(file);
						system.LOpath=file.getAbsolutePath();
						textLOpath.setText(system.LOpath);
					}
			}
		});
		btnLO.setBounds(340, 115, 38, 23);
		panel_1.add(btnLO);
		
		textRulesPath = new JTextField();
		textRulesPath.setToolTipText("Path to file with rules base");
		textRulesPath.setColumns(10);
		textRulesPath.setBounds(122, 148, 208, 20);
		panel_1.add(textRulesPath);
		
		JLabel lblRulesFile = new JLabel("Rules file:");
		lblRulesFile.setBounds(10, 151, 102, 14);
		panel_1.add(lblRulesFile);
		
		JButton btnRules = new JButton("...");
		btnRules.setToolTipText("Path to file with rules base");
		btnRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if(!system.modelPath.isEmpty()) fc.setCurrentDirectory(new File(system.modelPath));
				 int res=fc.showDialog(frmDitflsGui, "Open");
					if(res==JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
						updateCurrentPath(file);
						system.Rulepath=file.getAbsolutePath();
						textRulesPath.setText(system.Rulepath);
					}
			}
		});
		btnRules.setBounds(340, 147, 38, 23);
		panel_1.add(btnRules);
		
		JButton btnSaveSystem = new JButton("Save system");
		btnSaveSystem.setToolTipText("Save system to files");
		btnSaveSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system==null) return;
				system.name=nameSystem.getText();
				if(system.LIpath.isEmpty()||system.LOpath.isEmpty()
						||system.Rulepath.isEmpty()||(system.modelPath.isEmpty()&&!system.name.isEmpty())){
					JOptionPane.showMessageDialog(frmDitflsGui, "You should fill in all fields in System panel","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(system.LIvars.size()==0||system.LOvars.size()==0||system.rule.rules.length==0){
					JOptionPane.showMessageDialog(frmDitflsGui, "Input, output variables and rules base must be filled in to save","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				initPathes();
				if(!system.modelPath.isEmpty())system.checkTargetDirectory(system.modelPath);
				if(!system.name.isEmpty()) system.saveSystemConfiguration(system.modelPath);
				system.saveLI(system.LIpath);
				system.saveLO(system.LOpath);
				system.saveRules(system.Rulepath);
			}
		});
		btnSaveSystem.setBounds(10, 176, 174, 23);
		panel_1.add(btnSaveSystem);
		
		JButton btnLoadSystem = new JButton("Load system");
		btnLoadSystem.setToolTipText("Load system from selected files");
		btnLoadSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system==null) return;
				initPathes();
				if(system.LIpath.isEmpty()||system.LOpath.isEmpty()
						||system.Rulepath.isEmpty()||(system.modelPath.isEmpty()&&!system.name.isEmpty())){
					JOptionPane.showMessageDialog(frmDitflsGui, "You should fill in all the fields in System panel","Error",JOptionPane.ERROR_MESSAGE);
					return;
				}
				system.loadLI(system.LIpath);
				system.loadLO(system.LOpath);
				system.loadRules(system.Rulepath);
				system.loadSystemConfiguration(system.modelPath);
				initializeData();
			}
		});
		btnLoadSystem.setBounds(194, 176, 184, 23);
		panel_1.add(btnLoadSystem);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Selected Variable", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(408, 223, 373, 210);
		frmDitflsGui.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblName_1 = new JLabel("Name");
		lblName_1.setBounds(10, 27, 46, 14);
		panel_2.add(lblName_1);
		
		JLabel lblRange = new JLabel("Range");
		lblRange.setBounds(10, 58, 46, 14);
		panel_2.add(lblRange);
		
		JLabel lblDiscretization = new JLabel("Discretization");
		lblDiscretization.setBounds(10, 89, 80, 14);
		panel_2.add(lblDiscretization);
		
		editName = new JEditorPane();
		editName.setToolTipText("Name of the selected variable");
		editName.setBounds(90, 21, 106, 20);
		panel_2.add(editName);
		
	    editRange = new JEditorPane();
	    editRange.setToolTipText("Range of the selected variable");
		editRange.setBounds(90, 52, 106, 20);
		panel_2.add(editRange);
		
		editDiscret = new JEditorPane();
		editDiscret.setToolTipText("Discretization of the selected variable");
		editDiscret.setBounds(90, 83, 106, 20);
		panel_2.add(editDiscret);
		
		JButton btnSaveVar = new JButton("SAVE");
		btnSaveVar.setToolTipText("Save name, range and discretization of the selected variable");
		btnSaveVar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String 	vname=editName.getText(), 
						vrange=editRange.getText(), 
						vdiscret=editDiscret.getText();
				if(vname.isEmpty()||vrange.isEmpty()||vdiscret.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You must enter all parameters");
				}
				int index=currentIndex, type=currentType;
				system.editVar(vname, vrange, vdiscret, type, index);
				switch(type){
					case 0: 
							if(!system.checkInputVariableName(vname,currentIndex)){
								JOptionPane.showMessageDialog(frmDitflsGui, "Variable with the same name already exists");
							} else {
								ivModel.remove(index);
								ivModel.add(index, vname);
							}
							break;
					case 1: 
							if(!system.checkOutputVariableName(vname,currentIndex)){
								JOptionPane.showMessageDialog(frmDitflsGui, "Variable with the same name already exists");
							} else {
								ovModel.remove(index);
								ovModel.add(index, vname);
							}
							break;
					}
			}
		});
		btnSaveVar.setBounds(249, 21, 89, 89);
		panel_2.add(btnSaveVar);
		setPanelEnable(panel_2, false);
		frmDitflsGui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int confirmed = JOptionPane.showConfirmDialog(frmDitflsGui,"Are you sure you want to close program?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION){
					exit(frmDitflsGui);
				};
				
			}
		});
	};
	
	static void exit(JFrame frame){
		frame.dispose();
		System.exit(0);
	}
	/**
	 * Enables|Disables panel with all its elements
	 * @param panel - panel to enable
	 * @param flag - true - enable, false - disable
	 */
	private void setPanelEnable(JPanel panel, boolean flag){
		Component[] comp=panel.getComponents();
		panel.setEnabled(flag);
		for(Component c: comp){
			c.setEnabled(flag);
		}
	}
	private void initPathes(){
		String name=nameSystem.getText(),
				model=textModelPath.getText(),
				li=textLIpath.getText(),
				lo=textLOpath.getText(),
				r=textRulesPath.getText();
		if(!name.isEmpty()&&!name.equals(system.name)) system.name=name;
		if(!model.isEmpty()&&!model.equals(system.modelPath)) system.modelPath=model;
		if(!li.isEmpty()&&!li.equals(system.LIpath)) system.LIpath=li;
		if(!lo.isEmpty()&&!lo.equals(system.LOpath)) system.LOpath=lo;
		if(!r.isEmpty()&&!r.equals(system.Rulepath)) system.Rulepath=r;
	}
	
	private void updateCurrentPath(File file){
		String path=file.getAbsolutePath();
		path=path.substring(0,path.lastIndexOf("\\")+1);
		if(!path.equals(currentPath)) {
			currentPath=path;
			fc.setCurrentDirectory(new File(currentPath));
		}
	}
	@Override
	public void setResultFromChild(Object obj) {
		response=obj;
	}

	public class XMLFilter extends FileFilter {
		 
	    public boolean accept(File f) {
	        if (f.isDirectory()) {
	            return true;
	        }
	       
	        String extension = this.getExtension(f);
	        if (extension != null) {
	            if (extension.equals("xml")) 
	                    return true;
	            } else {
	                return false;
	            }
	    	return false;
		}

		@Override
		public String getDescription() {
			return "XML files";
		}
		
		public String getExtension(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');
	        
	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
		}
	}
}


