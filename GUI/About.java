import javax.swing.JFrame;


import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class About extends JFrame{
	private JTextField txtJditflsunixanderorgua;
	private JTextField txtDitflscom;
	public About() {
		setResizable(false);
		setTitle("jDIT2FLS-GUI About");
		setBounds(200, 200, 402, 354);
		getContentPane().setLayout(null);
		
		JLabel lblVersion = new JLabel("Version:");
		lblVersion.setBounds(123, 47, 66, 14);
		getContentPane().add(lblVersion);
		
		JLabel label = new JLabel("1.0.0.6");
		label.setBounds(199, 47, 46, 14);
		getContentPane().add(label);
		
		JLabel lblToolboxForDiscrete = new JLabel("Toolbox for Discrete Interval Type 2 Fuzzy Logic Systems");
		lblToolboxForDiscrete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblToolboxForDiscrete.setBounds(10, 11, 414, 25);
		getContentPane().add(lblToolboxForDiscrete);
		
		JLabel lblCognitiveuncertaintyResearchGroup = new JLabel("Cognitive&Uncertainty Research Group");
		lblCognitiveuncertaintyResearchGroup.setBounds(72, 74, 282, 14);
		getContentPane().add(lblCognitiveuncertaintyResearchGroup);
		
		JLabel lblDitflsWebsite = new JLabel("DIT2FLS Website:");
		lblDitflsWebsite.setBounds(105, 289, 93, 14);
		getContentPane().add(lblDitflsWebsite);
		
		txtDitflscom = new JTextField();
		txtDitflscom.setEditable(false);
		txtDitflscom.setText("dit2fls.com");
		txtDitflscom.setBounds(208, 286, 86, 20);
		getContentPane().add(txtDitflscom);
		txtDitflscom.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Leadership:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 99, 376, 85);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblPetrenkoTetyana = new JLabel("Petrenko Tetyana");
		lblPetrenkoTetyana.setBounds(10, 27, 127, 14);
		panel.add(lblPetrenkoTetyana);
		
		JLabel lblNewLabel_1 = new JLabel("Tymchuk Oleg");
		lblNewLabel_1.setBounds(10, 52, 127, 14);
		panel.add(lblNewLabel_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Engineering:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 190, 376, 85);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblYershovAlexander = new JLabel("Yershov Alexander");
		lblYershovAlexander.setBounds(10, 24, 147, 14);
		panel_1.add(lblYershovAlexander);
		
		JLabel lblEmail = new JLabel("e-mail:");
		lblEmail.setBounds(10, 51, 46, 14);
		panel_1.add(lblEmail);
		
		txtJditflsunixanderorgua = new JTextField();
		txtJditflsunixanderorgua.setBounds(66, 49, 159, 20);
		panel_1.add(txtJditflsunixanderorgua);
		txtJditflsunixanderorgua.setEditable(false);
		txtJditflsunixanderorgua.setText("jdit2fls@unixander.org.ua");
		txtJditflsunixanderorgua.setColumns(10);
		
	}
}
