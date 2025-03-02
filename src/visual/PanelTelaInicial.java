package visual;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.ImageIcon;

public class PanelTelaInicial extends JPanel {
	private JButton buttonCarregarCariotipagem;
	private JButton buttonNovaCariotipagem;
	public PanelTelaInicial() {
		setSize(1280, 720);
		setLayout(null);
		add(getButtonCarregarCariotipagem());
		add(getButtonNovaCariotipagem());
	}
	public JButton getButtonCarregarCariotipagem() {
		if (buttonCarregarCariotipagem == null) {
			buttonCarregarCariotipagem = new JButton("<html>Carregar<br>Cariotipagem</html>");
			buttonCarregarCariotipagem.setIcon(new ImageIcon(PanelTelaInicial.class.getResource("/images/icons/carregar.png")));
			buttonCarregarCariotipagem.setFont(new Font("Montserrat", Font.PLAIN, 25));
			buttonCarregarCariotipagem.setBounds(32, 238, 330, 180);
		}
		return buttonCarregarCariotipagem;
	}
	public JButton getButtonNovaCariotipagem() {
		if (buttonNovaCariotipagem == null) {
			buttonNovaCariotipagem = new JButton("<html>Nova<br>Cariotipagem</html>");
			buttonNovaCariotipagem.setIcon(new ImageIcon(PanelTelaInicial.class.getResource("/images/icons/novo.png")));
			buttonNovaCariotipagem.setFont(new Font("Montserrat", Font.PLAIN, 25));
			buttonNovaCariotipagem.setBounds(32, 47, 330, 180);
		}
		return buttonNovaCariotipagem;
	}
}
