package visual;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class PanelInfo extends JPanel {
	private JTextField textFieldNomeDaEspecie;
	private JLabel labelNomeDaEspecie;
	private JLabel labelPloidia;
	private JComboBox comboBox;
	private JButton buttonEnviar;
	
	public PanelInfo() {
		setSize(400, 400);
		setLayout(null);
		add(getLabelNomeDaEspecie());
		add(getTextFieldNomeDaEspecie());
		add(getLabelPloidia());
		add(getComboBox());
		add(getButtonEnviar());
	}
	public JTextField getTextFieldNomeDaEspecie() {
		if (textFieldNomeDaEspecie == null) {
			textFieldNomeDaEspecie = new JTextField();
			textFieldNomeDaEspecie.setBounds(30, 79, 325, 34);
			textFieldNomeDaEspecie.setColumns(10);
		}
		return textFieldNomeDaEspecie;
	}
	public JLabel getLabelNomeDaEspecie() {
		if (labelNomeDaEspecie == null) {
			labelNomeDaEspecie = new JLabel("Digite o nome da espécie");
			labelNomeDaEspecie.setFont(new Font("Montserrat", Font.PLAIN, 16));
			labelNomeDaEspecie.setBounds(30, 48, 205, 20);
		}
		return labelNomeDaEspecie;
	}
	public JLabel getLabelPloidia() {
		if (labelPloidia == null) {
			labelPloidia = new JLabel("Selecione a ploidia da espécie");
			labelPloidia.setFont(new Font("Montserrat", Font.PLAIN, 16));
			labelPloidia.setBounds(30, 137, 238, 20);
		}
		return labelPloidia;
	}
	public JComboBox getComboBox() {
		if (comboBox == null) {
			String[] options = {"Haplóide", "Diplóide"};
			comboBox = new JComboBox(options);
			comboBox.setFont(new Font("Montserrat", Font.PLAIN, 15));
			comboBox.setBounds(30, 168, 238, 34);
		}
		return comboBox;
	}
	public JButton getButtonEnviar() {
		if (buttonEnviar == null) {
			buttonEnviar = new JButton("Enviar");
			buttonEnviar.setFont(new Font("Montserrat", Font.PLAIN, 20));
			buttonEnviar.setBounds(30, 235, 325, 42);
		}
		return buttonEnviar;
	}
}
