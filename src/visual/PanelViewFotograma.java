package visual;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import modelo.Position;
import modelo.Band;
import modelo.Chromosome;

public class PanelViewFotograma extends JPanel {

    private JLabel labelFotograma;
    private Position centrioloPreview;
    private ArrayList<Position> arrayNodesFirstArm;
    private ArrayList<Position> arrayNodesSecondArm;
    private ArrayList<Position> arrayBandPositions;
    private ArrayList<Band> arrayBands;
    private ArrayList<Chromosome> arrayChromosomes;
    private Image image;  // Fotograma
    private boolean isPreviewFixed = false;
    int radius = 5; // Raios dos círculos
    Chromosome selectedChromosome;
    private Color bandPreviewColor;
    private boolean isShowingGridLines = false;
	private int gridScale = 48;
    
    public PanelViewFotograma() {
        arrayNodesFirstArm = new ArrayList<Position>();
        arrayNodesSecondArm = new ArrayList<Position>();
        arrayBandPositions = new ArrayList<Position>();
        arrayBands = new ArrayList<Band>();
        arrayChromosomes = new ArrayList<Chromosome>();
        add(getLabelFotograma());
    }
    
    public JLabel getLabelFotograma() {
        if (labelFotograma == null) {
            labelFotograma = new JLabel();
            labelFotograma.setBounds(10, 11, 636, 634);
        }
        return labelFotograma;
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        // Desenha a imagem, se disponível
        if (image != null) {
        	getLabelFotograma().setVisible(false);
            g.drawImage(image, 3, 3, getWidth()-6, getHeight()-6, this);
        }
        
        for(int j = 1; j <= 2; j++) {
        	ArrayList<Position> arrayNodes;
        	arrayNodes = (j == 1) ? arrayNodesFirstArm : arrayNodesSecondArm;
        	
        	if(arrayNodes != null && arrayNodes.size() > 0) {
            	drawConection(g, centrioloPreview, arrayNodes.get(0));
            	for(int i = 1; i < arrayNodes.size(); i++) {
            		drawConection(g, arrayNodes.get(i-1), arrayNodes.get(i));
            	}
            	for(int i = 0; i < arrayNodes.size(); i++) {
            		drawNode(g, arrayNodes.get(i), Color.BLUE);
            	}
            }
        }
        
        //Desenha as previews de Bandas
        for(Position p : arrayBandPositions) {
        	drawNode(g, p, bandPreviewColor);
        }
        
        for(Band b : arrayBands) {
    		for(Position p : b.getBandCoordinates()) {
    			drawNode(g, p, b.getColor());
    		}
    	}
        
        // DESENHAR OS CROMOSSOMOS DA CLASSE CHROMOSOME
        
        for(Chromosome c : arrayChromosomes) {
        	Color color;
        	if(c.getNature().equals("Sexual")) {
        		color = Color.MAGENTA;
        	} else {
        		color = Color.BLUE;
        	}
        	
        	for(int j = 1; j <= 2; j++) {
            	ArrayList<Position> arrayNodes;
            	arrayNodes = (j == 1) ? c.getCoordinatesLongerArm() : c.getCoordinatesShorterArm();
            	
            	if(arrayNodes != null && arrayNodes.size() > 0) {
                	drawConection(g, c.getCentriole(), arrayNodes.get(0));
                	for(int i = 1; i < arrayNodes.size(); i++) {
                		drawConection(g, arrayNodes.get(i-1), arrayNodes.get(i));
                	}
                	
                	for(int i = 0; i < arrayNodes.size(); i++) {
                		drawNode(g, arrayNodes.get(i), color);
                	}
                }
            }
        	
        	// Desenha as Bandas
        	for(int j = 1; j <= 2; j++) {
        		ArrayList<Band> bands;
            	bands = (j == 1) ? c.getBandsLongerArm() : c.getBandsShorterArm();
            	
            	for(Band b : bands) {
            		for(Position p : b.getBandCoordinates()) {
            			drawNode(g, p, b.getColor());
            		}
            	}
        	}
        	
        }
        
        // Desenha os centríolos
        g.setColor(Color.GREEN);
        for (Chromosome c : arrayChromosomes) {
            g.fillOval(c.getCentriole().getX() - radius, c.getCentriole().getY() - radius, radius * 2, radius * 2);
        }
        
        // Desenha a preview de um centríolo, se disponível
        if(centrioloPreview != null) {
        	g.setColor(isPreviewFixed ? Color.GREEN : Color.RED); //se o preview não estiver fixado a cor é vermelha
        	g.fillOval(centrioloPreview.getX() - radius, centrioloPreview.getY() - radius, radius * 2, radius * 2);
        }
        
        // Desenha o quadrado ao redor do cromossomo selecionado
        if (selectedChromosome != null) {
            Graphics2D g2d = (Graphics2D) g; // Cast para Graphics2D

            // Define a grossura do traço
            g2d.setStroke(new BasicStroke(3)); // Ajuste o valor 3 para a espessura desejada

            g.setColor(Color.RED); // Cor do quadrado (você pode mudar)
            int x = selectedChromosome.getCentriole().getX() - 10; // Ajuste a posição e o tamanho do quadrado
            int y = selectedChromosome.getCentriole().getY() - 10;
            int width = 20; // Largura do quadrado
            int height = 20; // Altura do quadrado

            // Encontra os extremos para englobar todo o cromossomo
            int minX = selectedChromosome.getCentriole().getX();
            int maxX = selectedChromosome.getCentriole().getX();
            int minY = selectedChromosome.getCentriole().getY();
            int maxY = selectedChromosome.getCentriole().getY();

            for(Position p : selectedChromosome.getCoordinatesLongerArm()){
                minX = Math.min(minX, p.getX());
                maxX = Math.max(maxX, p.getX());
                minY = Math.min(minY, p.getY());
                maxY = Math.max(maxY, p.getY());
            }

            for(Position p : selectedChromosome.getCoordinatesShorterArm()){
                minX = Math.min(minX, p.getX());
                maxX = Math.max(maxX, p.getX());
                minY = Math.min(minY, p.getY());
                maxY = Math.max(maxY, p.getY());
            }

            x = minX - 10;
            y = minY - 10;
            width = maxX - minX + 20;
            height = maxY - minY + 20;

            g2d.drawRect(x, y, width, height);
            g2d.setStroke(new BasicStroke(1));
        }
        
        if(isShowingGridLines) {
        	Graphics2D g2d = (Graphics2D) g;
        	g2d.setColor(Color.GRAY);
        	float opacity = 0.5f; // 50% transparente
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        	
        	for(int i = 6; i <= 630; i += gridScale) { //633 é o tamanho da imagem carregada
        		g.drawLine(i, 3, i, 633);
        		g.drawLine(3, i, 633, i);
        	}
        }
    }

	private void drawNode(Graphics g, Position destiny, Color color) {
    	g.setColor(color);
    	g.fillOval(destiny.getX() - radius, destiny.getY() - radius, radius * 2, radius * 2);
    }
    
    private void drawConection(Graphics g, Position origin, Position destiny) {
    	g.setColor(Color.GRAY);
    	g.drawLine(origin.getX(), origin.getY(), destiny.getX(), destiny.getY());
    	g.setColor(Color.WHITE);
    	g.drawLine(origin.getX()-1, origin.getY()-1, destiny.getX()-1, destiny.getY()-1);
    	g.drawLine(origin.getX()+1, origin.getY()+1, destiny.getX()+1, destiny.getY()+1);
    }
    
    public void fixCentriolo() {
    	isPreviewFixed = true;
    }
    
    public void viewCentriolo(Position centriolo) {
    	centrioloPreview = centriolo;
    	repaint();
    }
    
    public Position getCentrioloPreview() {
    	return centrioloPreview;
    }
    
    public void addNode(Position node, int select) {
    	if(select == 1) {
    		arrayNodesFirstArm.add(node);
    	} else {
    		arrayNodesSecondArm.add(node);
    	}
    	repaint();
    }
    
    public void resetArmsPreview() {
    	arrayNodesFirstArm.clear();
    	arrayNodesSecondArm.clear();
    }
    
    public void addChromosome() {
		ArrayList<Position> copyFirstArm = new ArrayList<Position>();
		for(Position p : arrayNodesFirstArm) {
			copyFirstArm.add(p);
		}
		
		ArrayList<Position> copySecondArm = new ArrayList<Position>();
		for(Position p : arrayNodesSecondArm) {
			copySecondArm.add(p);
		}
    	
		Chromosome chromosome = new Chromosome(centrioloPreview.getX(), centrioloPreview.getY(), copyFirstArm, copySecondArm);
    	
		for(Band b : arrayBands) {
			Band bandCopy = new Band(new ArrayList<>(b.getBandCoordinates()), b.getColor(), b.getArmIndex());
    		chromosome.addBand(bandCopy);
    	}
    	
    	arrayChromosomes.add(chromosome);
    	
    	centrioloPreview = null;
    	isPreviewFixed = false;
    	arrayBandPositions.clear();
    	arrayBands.clear();
    }
    
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }
    
    public ArrayList<Position> getArrayNodes(int select) {
    	if(select == 1) {
    		return arrayNodesFirstArm;
    	} else {
    		return arrayNodesSecondArm;
    	}
    }
    
    public ArrayList<Chromosome> getArrayChromosomes(){
    	return arrayChromosomes;
    }
    
    public void setArrayChromosomes(ArrayList<Chromosome> arrayChromosomes) {
    	this.arrayChromosomes = arrayChromosomes;
    	repaint();
    }
    
    public int getRadius() {
    	return radius;
    }
    
    public void expandMarkings() {
		radius++;
	}
	
	public void decreaseMarkings() {
		radius--;
	}
	
	public void clearMarkings(){
		arrayChromosomes.clear();
		arrayNodesFirstArm.clear();
		arrayNodesSecondArm.clear();
		centrioloPreview = null;
		isPreviewFixed = false;
	}
	
	public void setSelectedChromosome(Chromosome chromosome) {
        this.selectedChromosome = chromosome;
        repaint();
    }
	
	public void addNodeToBand(Position node) {
		arrayBandPositions.add(node);
	}
	
	public void clearBandPreview() {
	    arrayBandPositions.clear();
	    bandPreviewColor = null;
	}

	public void addBand(int index) {
	    Band band = new Band(new ArrayList<>(arrayBandPositions), bandPreviewColor, index);
	    arrayBands.add(band);
	    arrayBandPositions.clear(); 
	    bandPreviewColor = null;  
	    repaint();
	}
	
	public ArrayList<Band> getArrayBands(){
		return arrayBands;
	}
	
	public void setBandPreviewColor(Color color) {
		this.bandPreviewColor = color;
	}
	
	public ArrayList<Position> getArrayBandPositions(){
		return arrayBandPositions;
	}
	
	public boolean isShowingGridLines() {
		return isShowingGridLines;
	}

	public void setShowingGridLines(boolean isShowingGridLines) {
		this.isShowingGridLines = isShowingGridLines;
	}
	
	public int getGridScale() {
		return gridScale;
	}

	public void setGridScale(int gridScale) {
		this.gridScale = gridScale;
	}
}