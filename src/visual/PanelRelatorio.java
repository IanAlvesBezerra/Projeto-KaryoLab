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
import javax.swing.JList;
import javax.swing.ImageIcon;

public class PanelRelatorio extends JPanel {
	
	ArrayList<ArrayList<Chromosome>> homologousSet;
	ArrayList<ArrayList<Chromosome>> copyHomologousSet;
	double proprotion;
	
	public String speciesName;
	private JLabel labelEspecie;
	private JButton buttonExportarPDF;
	private JList<String> listOpcoesDeVisualizacao;
	private JList <String> listOpcoesDeOrdenacao;
	private boolean isViewingInSets = true;
	
	public PanelRelatorio(ArrayList<ArrayList<Chromosome>> homologousSet, String speciesName) {
		setSize(874, 614);
		setLayout(null);
		this.homologousSet = homologousSet;
		copyHomologousSet = new ArrayList<ArrayList<Chromosome>>(homologousSet);
		this.speciesName = speciesName;
		add(getLabelEspecie());
		add(getButtonExportarPDF());
		add(getListOpcoesDeVisualizacao());
		add(getListOpcoesDeOrdenacao());
	}
	
	public ArrayList<ArrayList<Chromosome>> getHomologousSet() {
		return homologousSet;
	}

	public void setHomologousSet(ArrayList<ArrayList<Chromosome>> homologousSet) {
		this.homologousSet = homologousSet;
	}

	public ArrayList<ArrayList<Chromosome>> getCopyHomologousSet() {
		return copyHomologousSet;
	}

	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    proprotion = calculateHeightPorportion();
	    
	    int xPosition = 32;
	    int yPosition = 125;
	    Integer referenceCentromereY = null;
	    
	    for (ArrayList<Chromosome> set : homologousSet) {
	        if (isViewingInSets) {
	            for (Chromosome c : set) {
	                g.setColor(Color.BLACK);
	                int shorterHight = calculateArmHight(c.getShorterArmLength());
	                int longerHight = calculateArmHight(c.getLongerArmLength());
	                
	                int centromereY = yPosition + shorterHight + 1;
	                
	                if (referenceCentromereY == null) {
	                    referenceCentromereY = centromereY; 
	                }
	                
	                int adjustedYPosition = referenceCentromereY - shorterHight - 1;
	                
	                g.fillRect(xPosition, adjustedYPosition, 10, shorterHight);
	                g.fillOval(xPosition, referenceCentromereY, 10, 10);
	                g.fillRect(xPosition, referenceCentromereY + 11, 10, longerHight);
	                
	                for (Band band : c.getBandsShorterArm()) {
	                    g.setColor(band.getColor());
	                    int bandY = (int) (adjustedYPosition + c.calculateDistanceToBand(band, 2));
	                    g.fillRect(xPosition, bandY, 10, (int) band.calculateBendLength());
	                }
	                
	                for (Band band : c.getBandsLongerArm()) {
	                    g.setColor(band.getColor());
	                    int bandY = (int) (referenceCentromereY + 11 + c.calculateDistanceToBand(band, 1));
	                    g.fillRect(xPosition, bandY, 10, (int) band.calculateBendLength());
	                }
	                
	                xPosition += 15;
	            }
	        } else {
	            g.setColor(Color.BLACK);
	            int averageShorterArm = 0;
	            int averageLongerArm = 0;
	            for (Chromosome c : set) {
	                averageShorterArm += calculateArmHight(c.getShorterArmLength());
	                averageLongerArm += calculateArmHight(c.getLongerArmLength());
	            }
	            
	            averageShorterArm /= set.size();
	            averageLongerArm /= set.size();
	            
	            int centromereY = yPosition + averageShorterArm + 1;
	            
	            if (referenceCentromereY == null) {
	                referenceCentromereY = centromereY;
	            }
	            
	            int adjustedYPosition = referenceCentromereY - averageShorterArm - 1;
	            
	            g.fillRect(xPosition, adjustedYPosition, 10, averageShorterArm);
	            g.fillOval(xPosition, referenceCentromereY, 10, 10);
	            g.fillRect(xPosition, referenceCentromereY + 11, 10, averageLongerArm);
	            
	            xPosition += 15;
	        }
	        
	        xPosition += 15;
	        if (xPosition >= 827) {
	            xPosition = 32;
	            yPosition += 110;
	        }
	    }
	}

	
	private double calculateHeightPorportion() {
	    if (homologousSet.isEmpty() || homologousSet.getFirst().isEmpty()) {
	        return 1; // Valor padrão para evitar divisão por zero
	    }
	    
	    if(isViewingInSets) {
	    	Chromosome reference = homologousSet.getFirst().getFirst();
	    	return 100 / reference.getTotalLength();	    	
	    }
	    else {
	    	double averageSize = 0;
	    	for(Chromosome c : homologousSet.getFirst()) {
	    		averageSize += c.getTotalLength();
	    	}
	    	averageSize /= homologousSet.getFirst().size();
	    	return 100 / averageSize;
	    }
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
			listOpcoesDeVisualizacao.setBounds(343, 520, 153, 43);
		}
		return listOpcoesDeVisualizacao;
	}
	public JList<String> getListOpcoesDeOrdenacao() {
		if (listOpcoesDeOrdenacao == null) {
			String[] options = {"Ordem de Marcação", "Crescente", "Decrescente"};
			listOpcoesDeOrdenacao = new JList<String>(options);
			listOpcoesDeOrdenacao.setFont(new Font("Montserrat", Font.PLAIN, 15));
			listOpcoesDeOrdenacao.setSelectedIndex(0);
			listOpcoesDeOrdenacao.setBounds(506, 500, 155, 63);
		}
		return listOpcoesDeOrdenacao;
	}

	public boolean isViewingInSets() {
		return isViewingInSets;
	}

	public void setViewingInSets(boolean isViewingInSets) {
		this.isViewingInSets = isViewingInSets;
	}
}
