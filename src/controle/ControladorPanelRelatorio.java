package controle;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import modelo.Chromosome;
import visual.PanelRelatorio;

public class ControladorPanelRelatorio implements ActionListener {

	PanelRelatorio panelRelatorio;
	
	public ControladorPanelRelatorio(PanelRelatorio panelRelatorio) {
		this.panelRelatorio = panelRelatorio;
		addEventos();
	}
	
	private void addEventos() {
		panelRelatorio.getButtonExportarPDF().addActionListener(this);
		
		panelRelatorio.getListOpcoesDeVisualizacao().addListSelectionListener(e -> {
	        if (!e.getValueIsAdjusting()) { // Evita chamadas duplas
	        	if(panelRelatorio.getListOpcoesDeVisualizacao().getSelectedIndex() == 0) {
	        		panelRelatorio.setViewingInSets(true);
	        	} else {
	        		panelRelatorio.setViewingInSets(false);
	        	}
	        	panelRelatorio.repaint();
	        }
	    });
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == panelRelatorio.getButtonExportarPDF()) {
			exportPDF();
		}
	}
	
	private void exportPDF() {
	    panelRelatorio.getButtonExportarPDF().setVisible(false);
	    panelRelatorio.getListOpcoesDeVisualizacao().setVisible(false);
	    try {
	        double scaleFactor = 4.0;

	        int width = (int) (panelRelatorio.getWidth() * scaleFactor);
	        int height = (int) (panelRelatorio.getHeight() * scaleFactor);

	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = bi.createGraphics();

	        // Ativar antialiasing para melhorar a qualidade
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	        // Aplicar escala para manter proporções ao aumentar a resolução
	        g2d.scale(scaleFactor, scaleFactor);
	        panelRelatorio.paint(g2d);
	        g2d.dispose(); 

	        File tempFile = File.createTempFile("relatorio", ".png", new File("."));
	        ImageIO.write(bi, "png", tempFile);
	        Desktop.getDesktop().print(tempFile);

	        // **Agendamento de exclusão para o final do programa**
	        tempFile.deleteOnExit();

	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Erro ao exportar ou imprimir: " + e.getMessage());
	        e.printStackTrace(); 
	    }
	}
}
