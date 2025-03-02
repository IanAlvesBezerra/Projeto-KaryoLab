package visual;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import modelo.Band;
import modelo.Chromosome;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;

public class PanelRelatorio extends JPanel {
	
	ArrayList<ArrayList<Chromosome>> homologousSet;
	double proprotion;
	
	public String speciesName;
	private JLabel labelEspecie;
	private JButton buttonExportarPDF;
	private JList<String> listOpcoesDeVisualizacao;
	private boolean isViewingInSets = true;
	
	public PanelRelatorio(ArrayList<ArrayList<Chromosome>> homologousSet, String speciesName) {
		setSize(874, 614);
		setLayout(null);
		this.homologousSet = homologousSet;
		this.proprotion = calculateHeightPorportion();
		this.speciesName = speciesName;
		add(getLabelEspecie());
		add(getButtonExportarPDF());
		add(getListOpcoesDeVisualizacao());
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int xPosition = 32;
		int yPosition = 100;
		
		for(ArrayList<Chromosome> set : homologousSet) {
			if(isViewingInSets) {
				for(Chromosome c : set) {
					g.setColor(Color.BLACK);
					int shorterHight = calculateArmHight(c.getShorterArmLength());
					int longerHight = calculateArmHight(c.getLongerArmLength());
					g.fillRect(xPosition, yPosition, 10,  shorterHight);
					g.fillOval(xPosition, (yPosition+shorterHight+1), 10, 10);
					g.fillRect(xPosition, (yPosition+12+shorterHight), 10,  longerHight);
					
					for (Band band : c.getBandsShorterArm()) {
					    g.setColor(band.getColor());

					    int bandY = (int) (yPosition + c.calculateDistanceToBand(band, 2));

					    g.fillRect(xPosition, bandY, 10, (int) band.calculateBendLength());
					}

					for (Band band : c.getBandsLongerArm()) {
					    g.setColor(band.getColor());

					    int bandY = (int) (yPosition + shorterHight + 12 + c.calculateDistanceToBand(band, 1));

					    g.fillRect(xPosition, bandY, 10, (int) band.calculateBendLength());
					}
		            
					xPosition += 15;
				}
			}
			//visualizar pela média dos conjuntos
			else {
				g.setColor(Color.BLACK);
				int averageShorterArm = 0;
				int averageLongerArm = 0;
				for(Chromosome c : set) {
					averageShorterArm += c.getShorterArmLength();
					averageLongerArm += c.getLongerArmLength();
				}
				averageShorterArm /= set.size();
				averageLongerArm /= set.size();
				
				g.fillRect(xPosition, yPosition, 10,  averageShorterArm);
				g.fillOval(xPosition, (yPosition+averageShorterArm+1), 10, 10);
				g.fillRect(xPosition, (yPosition+12+averageShorterArm), 10,  averageLongerArm);
				
				xPosition += 15;
			}
			xPosition += 15;
			// Se houverem muitos cromossomos, de forma que cheguem até o final da tela
			if(xPosition >= 827) { // Volta a posição x para o começo e desce a posição y
				xPosition = 32; 
				yPosition += 110;
			}
		}
		
	}
	
	private double calculateHeightPorportion() {
	    if (homologousSet.isEmpty() || homologousSet.getFirst().isEmpty()) {
	        return 1; // Valor padrão para evitar divisão por zero
	    }

	    Chromosome reference = homologousSet.getFirst().getFirst();
	    return 100 / reference.getTotalLength();
	}
	
	private int calculateArmHight(double armLength) {
		return (int)(armLength*proprotion);
	}
	public JLabel getLabelEspecie() {
		if (labelEspecie == null) {
			labelEspecie = new JLabel("New label");
			labelEspecie.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
			labelEspecie.setBounds(32, 32, 810, 58);
			labelEspecie.setText(speciesName);
		}
		return labelEspecie;
	}
	public JButton getButtonExportarPDF() {
		if (buttonExportarPDF == null) {
			buttonExportarPDF = new JButton("Exportar PDF");
			buttonExportarPDF.setIcon(new ImageIcon(PanelRelatorio.class.getResource("/images/icons/exportar.png")));
			buttonExportarPDF.setFont(new Font("Montserrat", Font.PLAIN, 15));
			buttonExportarPDF.setBounds(671, 520, 171, 43);
		}
		return buttonExportarPDF;
	}
	public JList<String> getListOpcoesDeVisualizacao() {
		if (listOpcoesDeVisualizacao == null) {
			String[] options = {"Conjuntos", "Média do Conjunto"};
			listOpcoesDeVisualizacao = new JList<String>(options);
			listOpcoesDeVisualizacao.setFont(new Font("Montserrat", Font.PLAIN, 15));
			listOpcoesDeVisualizacao.setSelectedIndex(0);
			listOpcoesDeVisualizacao.setBounds(508, 520, 153, 43);
		}
		return listOpcoesDeVisualizacao;
	}

	public boolean isViewingInSets() {
		return isViewingInSets;
	}

	public void setViewingInSets(boolean isViewingInSets) {
		this.isViewingInSets = isViewingInSets;
	}
	
	
}
