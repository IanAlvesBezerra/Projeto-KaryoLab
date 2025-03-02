package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import visual.Dialog;
import visual.PanelInfo;

public class ControladorPanelInfo implements ActionListener{

	PanelInfo panelInfo;
	ControladorPanelCariotipagem controladorPanelCariotipagem;
	Dialog dialog;
	
	public ControladorPanelInfo(PanelInfo panelInfo, ControladorPanelCariotipagem controladorPanelCariotipagem, Dialog dialog) {
		this.panelInfo = panelInfo;
		this.controladorPanelCariotipagem = controladorPanelCariotipagem;
		this.dialog = dialog;
		addEventos();
	}
	
	private void addEventos() {
		panelInfo.getButtonEnviar().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelInfo.getButtonEnviar()) {
			getInfo();
		}
	}
	
	private void getInfo() {
		String speciesName = panelInfo.getTextFieldNomeDaEspecie().getText();
		
		int ploidy;
		if(panelInfo.getComboBox().getSelectedIndex() == 0) {
			ploidy = 1;
		} else {
			ploidy = 2;
		}
		
		if(speciesName == null || speciesName.isBlank()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome da espécie!", "Erro", JOptionPane.WARNING_MESSAGE);
		} else {
			controladorPanelCariotipagem.setPloidy(ploidy);
			controladorPanelCariotipagem.setSpeciesName(speciesName);
			dialog.dispose();
		}
	}
}
