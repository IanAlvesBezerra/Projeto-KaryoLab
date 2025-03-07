package visual;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import modelo.Chromosome;

import javax.swing.JButton;
import java.awt.Font;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

public class PanelCariotipagem extends JPanel {
	private JButton buttonUploadFotograma;
	private JButton buttonMarcar;
	private JButton buttonDiminuir;
	private JButton buttonExpandir;
	private JButton buttonGerarRelatorio;
	private PanelViewFotograma panelViewFotograma;
	private JScrollPane panelViewCromossomos;
	private JButton buttonCorrigir;
	private JList listChromosomes;
	private JButton buttonExcluirSelecionado;
	private JToolBar toolBar;
	private JButton buttonNovaCariotipagem;
	private JButton buttonSalvar;
	private JButton buttonCarregar;
	private JButton buttonAlternarNatureza;
	private JButton buttonZoom;
	private JButton buttonMarcarBanda;
	
	
	public PanelCariotipagem() {
		setSize(1280, 720);
		setLayout(null);
		add(getButtonUploadFotograma());
		add(getButtonMarcar());
		add(getButtonDiminuir());
		add(getButtonExpandir());
		add(getButtonGerarRelatorio());
		add(getPanelViewFotograma());
		add(getPanelViewCromossomos());
		add(getButtonCorrigir());
		add(getButtonExcluirSelecionado());
		add(getToolBar());
		add(getButtonAlternarNatureza());
		add(getButtonZoom());
		add(getButtonMarcarBanda());
	}
	
	public JButton getButtonUploadFotograma() {
		if (buttonUploadFotograma == null) {
			buttonUploadFotograma = new JButton("Upload Fotograma\r\n");
			buttonUploadFotograma.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/upload.png")));
			buttonUploadFotograma.setFont(new Font("Montserrat", Font.PLAIN, 24));
			buttonUploadFotograma.setBounds(32, 32, 315, 70);
		}
		return buttonUploadFotograma;
	}
	public JButton getButtonMarcar() {
		if (buttonMarcar == null) {
			buttonMarcar = new JButton("Marcar Centrômero");
			buttonMarcar.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/marcar.png")));
			buttonMarcar.setFont(new Font("Montserrat", Font.PLAIN, 24));
			buttonMarcar.setBounds(32, 112, 315, 70);
		}
		return buttonMarcar;
	}
	public JButton getButtonCorrigir() {
		if (buttonCorrigir == null) {
			buttonCorrigir = new JButton("Corrigir");
			buttonCorrigir.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/corrigir.png")));
			buttonCorrigir.setFont(new Font("Montserrat", Font.PLAIN, 24));
			buttonCorrigir.setBounds(32, 192, 315, 70);
		}
		return buttonCorrigir;
	}
	public JButton getButtonDiminuir() {
		if (buttonDiminuir == null) {
			buttonDiminuir = new JButton("Diminuir");
			buttonDiminuir.setToolTipText("Diminui o tamanho dos nós que representam as marcações");
			buttonDiminuir.setFont(new Font("Montserrat", Font.PLAIN, 20));
			buttonDiminuir.setBounds(32, 629, 140, 40);
		}
		return buttonDiminuir;
	}
	public JButton getButtonExpandir() {
		if (buttonExpandir == null) {
			buttonExpandir = new JButton("Expandir");
			buttonExpandir.setToolTipText("Aumenta o tamanho dos nós que representam as marcações");
			buttonExpandir.setFont(new Font("Montserrat", Font.PLAIN, 20));
			buttonExpandir.setBounds(207, 629, 140, 40);
		}
		return buttonExpandir;
	}
	public JButton getButtonGerarRelatorio() {
		if (buttonGerarRelatorio == null) {
			buttonGerarRelatorio = new JButton("Gerar Relatório");
			buttonGerarRelatorio.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/relatorio.png")));
//			buttonGerarRelatorio.setToolTipText("Função em desenvolvimento");
			buttonGerarRelatorio.setFont(new Font("Montserrat", Font.PLAIN, 18));
			buttonGerarRelatorio.setBounds(1045, 618, 204, 50);
		}
		return buttonGerarRelatorio;
	}
	public PanelViewFotograma getPanelViewFotograma() {
	    if (panelViewFotograma == null) {
	        panelViewFotograma = new PanelViewFotograma();
	        panelViewFotograma.setBackground(new Color(128, 128, 128));
	        panelViewFotograma.setBounds(367, 32, 636, 636);
	        panelViewFotograma.setLayout(null);
	    }
	    return panelViewFotograma;
	}
	public JScrollPane getPanelViewCromossomos() {
	    if (panelViewCromossomos == null) {
	        panelViewCromossomos = new JScrollPane();
	        panelViewCromossomos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        panelViewCromossomos.setBackground(Color.GRAY);
	        panelViewCromossomos.setBounds(1023, 32, 226, 503);
	        
	        panelViewCromossomos.setViewportView(getListChromosomes());
	    }
	    return panelViewCromossomos;
	}

	public JList<Chromosome> getListChromosomes() {
	    if (listChromosomes == null) {
	        listChromosomes = new JList<Chromosome>();
	        listChromosomes.setCellRenderer(new ChromosomeRenderer());
	        
	    }
	    return listChromosomes;
	}

	public JButton getButtonExcluirSelecionado() {
		if (buttonExcluirSelecionado == null) {
			buttonExcluirSelecionado = new JButton("Excluir Selecionado");
			buttonExcluirSelecionado.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/excluir.png")));
			buttonExcluirSelecionado.setEnabled(false);
			buttonExcluirSelecionado.setVisible(false);
			buttonExcluirSelecionado.setFont(new Font("Montserrat", Font.PLAIN, 24));
			buttonExcluirSelecionado.setBounds(32, 352, 315, 70);
		}
		return buttonExcluirSelecionado;
	}
	public JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setBounds(0, 0, 1280, 25);
			toolBar.add(getButtonNovaCariotipagem());
			toolBar.add(getButtonSalvar());
			toolBar.add(getButtonCarregar());
		}
		return toolBar;
	}
	public JButton getButtonNovaCariotipagem() {
		if (buttonNovaCariotipagem == null) {
			buttonNovaCariotipagem = new JButton("Nova Cariotipagem");
		}
		return buttonNovaCariotipagem;
	}
	public JButton getButtonSalvar() {
		if (buttonSalvar == null) {
			buttonSalvar = new JButton("Salvar");
		}
		return buttonSalvar;
	}
	public JButton getButtonCarregar() {
		if (buttonCarregar == null) {
			buttonCarregar = new JButton("Carregar");
		}
		return buttonCarregar;
	}
	public JButton getButtonAlternarNatureza() {
		if (buttonAlternarNatureza == null) {
			buttonAlternarNatureza = new JButton("<html>Definir<br>comoSexual</html>");
			buttonAlternarNatureza.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/alternar.png")));
			buttonAlternarNatureza.setEnabled(false);
			buttonAlternarNatureza.setVisible(false);
			buttonAlternarNatureza.setFont(new Font("Montserrat", Font.PLAIN, 24));
			buttonAlternarNatureza.setBounds(32, 272, 315, 70);
		}
		return buttonAlternarNatureza;
	}
	public JButton getButtonZoom() {
		if (buttonZoom == null) {
			buttonZoom = new JButton("");
			buttonZoom.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/zoom.png")));
			buttonZoom.setFont(new Font("Montserrat", Font.PLAIN, 7));
			buttonZoom.setBounds(307, 568, 40, 40);
		}
		return buttonZoom;
	}
	public JButton getButtonMarcarBanda() {
		if (buttonMarcarBanda == null) {
			buttonMarcarBanda = new JButton("Marcar Banda");
			buttonMarcarBanda.setIcon(new ImageIcon(PanelCariotipagem.class.getResource("/images/icons/bandas.png")));
			buttonMarcarBanda.setEnabled(false);
			buttonMarcarBanda.setFont(new Font("Montserrat", Font.PLAIN, 20));
			buttonMarcarBanda.setBounds(1023, 546, 226, 50);
		}
		return buttonMarcarBanda;
	}
}
