package controle;

import visual.Frame;
import visual.PanelTelaInicial;

public class ControladorFrame {
	
	Frame frame;
	PanelTelaInicial panelTelaInicial;
	
	public ControladorFrame() {
		frame = new Frame();
		panelTelaInicial = new PanelTelaInicial();
		new ControladorPanelTelaInicial(frame, panelTelaInicial);
		frame.setContentPane(panelTelaInicial);
		frame.revalidate();
	    frame.repaint();
	}
	
	public static void main(String[] args) {
		new ControladorFrame();
	}
}
