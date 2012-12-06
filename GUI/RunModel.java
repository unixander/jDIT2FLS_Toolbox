import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


@SuppressWarnings("serial")
public class RunModel extends JDialog {

	private JPanel contentPane;
	private JTextField textField;
	private DITSystem system;
	private JTextField[] textFields;
	private int  winH=148, quantity=0;
	private JLabel[] labels;
	/**
	 * Create the frame.
	 */
	public RunModel(DITSystem s) {
		setResizable(false);
		this.system=s;
		setTitle("Result");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblResult = new JLabel("Result");
		lblResult.setBounds(292, 82, 46, 14);
		contentPane.add(lblResult);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(348, 79, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.setText("");
		
		JButton btnNewButton = new JButton("Calculate");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Double[] IN=new Double[quantity];
				int i=0;
				for(JTextField field: textFields){
					try{
						IN[i] = new Double(field.getText().toString());
						if(IN[i]>system.LIvars.get(i).max||IN[i]<system.LIvars.get(i).min){
							JOptionPane.showMessageDialog(null, "Parameter is out of range in variable: "+system.LIvars.get(i).name,"Error",JOptionPane.ERROR_MESSAGE);
							return;
						};
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Invalid input. Check input parameters.","Error",JOptionPane.ERROR_MESSAGE);
						IN=null;
						return;
					}
					i++;
				}
				Double result=system.Calculate(IN);
				if(result!=null){
					textField.setText(result.toString());
				} else {
					JOptionPane.showMessageDialog(null, "Something went wrong. Please, check all parameters","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(292, 11, 142, 46);
		contentPane.add(btnNewButton);
		
		quantity=system.LIvars.size();
		int x=10,y=11,
			lwidth=70, lheight=14 ,dy=20,fwidth=153, fheight=20,
			currentWidth=y+2*dy;
		labels=new JLabel[quantity];
		textFields=new JTextField[quantity];
		for(int i=0;i<quantity;i++){
			labels[i]=new JLabel(system.LIvars.get(i).name);
			labels[i].setBounds(x,y,lwidth,lheight);
			textFields[i]=new JTextField();
			textFields[i].setToolTipText("Enter IN parameter for "+system.LIvars.get(i).name+".\n Parameter must be in range ["+system.LIvars.get(i).min+" ; "+system.LIvars.get(i).max+"]");
			textFields[i].setBounds(x+lwidth,y,fwidth,fheight);
			textFields[i].setColumns(10);
			y+=dy;
			currentWidth+=dy;
		}
		if(currentWidth-dy>winH) winH=currentWidth;
		int i=0;
		
		for(JLabel lbl: labels){
			contentPane.add(lbl);
			contentPane.add(textFields[i]);
			i++;
		}
		
		setBounds(100, 100, 478, winH);

	}
}
