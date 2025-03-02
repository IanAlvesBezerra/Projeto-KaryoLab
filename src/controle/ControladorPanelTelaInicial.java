package controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import visual.Frame;
import visual.PanelCariotipagem;
import visual.PanelTelaInicial;

public class ControladorPanelTelaInicial implements ActionListener{
	
	Frame frame;
	PanelTelaInicial panelTelaInicial;
	PanelCariotipagem panelCariotipagem;
	
	public ControladorPanelTelaInicial(Frame frame, PanelTelaInicial panelTelaInicial) {
		this.frame = frame;
		this.panelTelaInicial = panelTelaInicial;
		addEventos();
	}
	
	public void addEventos() {
		panelTelaInicial.getButtonNovaCariotipagem().addActionListener(this);
		panelTelaInicial.getButtonCarregarCariotipagem().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelTelaInicial.getButtonNovaCariotipagem()) {
			panelCariotipagem = new PanelCariotipagem();
			new ControladorPanelCariotipagem(frame, panelCariotipagem, false);
			frame.setContentPane(panelCariotipagem);
		}
		else if(e.getSource() == panelTelaInicial.getButtonCarregarCariotipagem()) {
			panelCariotipagem = new PanelCariotipagem();
			new ControladorPanelCariotipagem(frame, panelCariotipagem, true);
			frame.setContentPane(panelCariotipagem);
		}
		frame.revalidate();
		frame.repaint();
	}
}
