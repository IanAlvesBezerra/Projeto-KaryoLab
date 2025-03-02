package controle;

import java.awt.Color;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import modelo.Band;
import modelo.Chromosome;
import modelo.Position;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Persistence {
	
	public static void generateXML(ArrayList<Chromosome> arrayChromosomes, String filePath, String imagePath, int ploidy) {
	    try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.newDocument();

	        Element rootElement = doc.createElement("karyotyping");
	        doc.appendChild(rootElement);

	        Element imageElement = doc.createElement("imagePath");
	        imageElement.setTextContent(imagePath);
	        rootElement.appendChild(imageElement);
	        
            Element ploidyElement = doc.createElement("ploidy");
            ploidyElement.setTextContent(String.valueOf(ploidy));
            rootElement.appendChild(ploidyElement);

	        Element chromosomesElement = doc.createElement("chromosomes");
	        rootElement.appendChild(chromosomesElement);

	        for (Chromosome chromosome : arrayChromosomes) {
	            Element chromosomeElement = doc.createElement("chromosome");
	            chromosomesElement.appendChild(chromosomeElement);

	            // Centríolo
	            Element centrioleElement = doc.createElement("centriole");
	            Element xElement = doc.createElement("x");
	            xElement.setTextContent(String.valueOf(chromosome.getCentriole().getX()));
	            Element yElement = doc.createElement("y");
	            yElement.setTextContent(String.valueOf(chromosome.getCentriole().getY()));
	            centrioleElement.appendChild(xElement);
	            centrioleElement.appendChild(yElement);
	            chromosomeElement.appendChild(centrioleElement);

	            // Coordenadas do braço mais longo
	            Element longerArmElement = doc.createElement("longerArm");
	            for (Position position : chromosome.getCoordinatesLongerArm()) {
	                Element positionElement = doc.createElement("position");
	                Element xPos = doc.createElement("x");
	                xPos.setTextContent(String.valueOf(position.getX()));
	                Element yPos = doc.createElement("y");
	                yPos.setTextContent(String.valueOf(position.getY()));
	                positionElement.appendChild(xPos);
	                positionElement.appendChild(yPos);
	                longerArmElement.appendChild(positionElement);
	            }
	            chromosomeElement.appendChild(longerArmElement);

	            // Coordenadas do braço mais curto
	            Element shorterArmElement = doc.createElement("shorterArm");
	            for (Position position : chromosome.getCoordinatesShorterArm()) {
	                Element positionElement = doc.createElement("position");
	                Element xPos = doc.createElement("x");
	                xPos.setTextContent(String.valueOf(position.getX()));
	                Element yPos = doc.createElement("y");
	                yPos.setTextContent(String.valueOf(position.getY()));
	                positionElement.appendChild(xPos);
	                positionElement.appendChild(yPos);
	                shorterArmElement.appendChild(positionElement);
	            }
	            chromosomeElement.appendChild(shorterArmElement);

	            // Grava bandas do braço mais longo
	            Element bandsLongerArmElement = doc.createElement("bandsLongerArm");
	            for (Band band : chromosome.getBandsLongerArm()) {
	                Element bandElement = doc.createElement("band");

	                for (Position position : band.getBandCoordinates()) {
	                    Element positionElement = doc.createElement("position");
	                    Element xPos = doc.createElement("x");
	                    xPos.setTextContent(String.valueOf(position.getX()));
	                    Element yPos = doc.createElement("y");
	                    yPos.setTextContent(String.valueOf(position.getY()));
	                    positionElement.appendChild(xPos);
	                    positionElement.appendChild(yPos);
	                    bandElement.appendChild(positionElement);
	                }

	                // Cor da banda
	                Element colorElement = doc.createElement("color");
	                colorElement.setTextContent(String.valueOf(band.getColor().getRGB()));
	                bandElement.appendChild(colorElement);

	                // Índice do braço
	                Element armIndexElement = doc.createElement("armIndex");
	                armIndexElement.setTextContent(String.valueOf(band.getArmIndex()));
	                bandElement.appendChild(armIndexElement);

	                bandsLongerArmElement.appendChild(bandElement);
	            }
	            chromosomeElement.appendChild(bandsLongerArmElement);

	            // Grava bandas do braço mais curto
	            Element bandsShorterArmElement = doc.createElement("bandsShorterArm");
	            for (Band band : chromosome.getBandsShorterArm()) {
	                Element bandElement = doc.createElement("band");

	                for (Position position : band.getBandCoordinates()) {
	                    Element positionElement = doc.createElement("position");
	                    Element xPos = doc.createElement("x");
	                    Element yPos = doc.createElement("y");
	                    xPos.setTextContent(String.valueOf(position.getX()));
	                    yPos.setTextContent(String.valueOf(position.getY()));
	                    positionElement.appendChild(xPos);
	                    positionElement.appendChild(yPos);
	                    bandElement.appendChild(positionElement);
	                }

	                // Cor da banda
	                Element colorElement = doc.createElement("color");
	                colorElement.setTextContent(String.valueOf(band.getColor().getRGB()));
	                bandElement.appendChild(colorElement);

	                // Índice do braço
	                Element armIndexElement = doc.createElement("armIndex");
	                armIndexElement.setTextContent(String.valueOf(band.getArmIndex()));
	                bandElement.appendChild(armIndexElement);

	                bandsShorterArmElement.appendChild(bandElement);
	            }
	            chromosomeElement.appendChild(bandsShorterArmElement);

	            // Natureza
	            Element natureElement = doc.createElement("nature");
	            natureElement.setTextContent(chromosome.getNature());
	            chromosomeElement.appendChild(natureElement);
	        }

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new FileOutputStream(filePath));
	        transformer.transform(source, result);
	        
	        

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public static String readXML(String filePath, ArrayList<Chromosome> arrayChromosomes, ControladorPanelCariotipagem controladorPanelCariotipagem) {
	    try {
	        File file = new File(filePath);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(file);
	        doc.getDocumentElement().normalize();

	        NodeList imageNode = doc.getElementsByTagName("imagePath");
	        String imagePath = "";
	        if (imageNode.getLength() > 0) {
	            imagePath = imageNode.item(0).getTextContent();
	        }
	        
	        NodeList ploidyNode = doc.getElementsByTagName("ploidy");
            if (ploidyNode.getLength() > 0) {
                int ploidy = Integer.parseInt(ploidyNode.item(0).getTextContent());
                controladorPanelCariotipagem.setPloidy(ploidy);
            }

	        NodeList chromosomeNodes = doc.getElementsByTagName("chromosome");

	        for (int i = 0; i < chromosomeNodes.getLength(); i++) {
	            Element chromosomeElement = (Element) chromosomeNodes.item(i);

	            // Centríolo
	            Element centrioleElement = (Element) chromosomeElement.getElementsByTagName("centriole").item(0);
	            int centrioleX = Integer.parseInt(centrioleElement.getElementsByTagName("x").item(0).getTextContent());
	            int centrioleY = Integer.parseInt(centrioleElement.getElementsByTagName("y").item(0).getTextContent());

	            // Braço mais longo
	            ArrayList<Position> longerArm = new ArrayList<>();
	            Element longerArmElement = (Element) chromosomeElement.getElementsByTagName("longerArm").item(0);
	            NodeList positionNodesLonger = longerArmElement.getElementsByTagName("position");
	            for (int j = 0; j < positionNodesLonger.getLength(); j++) {
	                Element positionElement = (Element) positionNodesLonger.item(j);
	                int x = Integer.parseInt(positionElement.getElementsByTagName("x").item(0).getTextContent());
	                int y = Integer.parseInt(positionElement.getElementsByTagName("y").item(0).getTextContent());
	                longerArm.add(new Position(x, y));
	            }

	            // Braço mais curto
	            ArrayList<Position> shorterArm = new ArrayList<>();
	            Element shorterArmElement = (Element) chromosomeElement.getElementsByTagName("shorterArm").item(0);
	            NodeList positionNodesShorter = shorterArmElement.getElementsByTagName("position");
	            for (int j = 0; j < positionNodesShorter.getLength(); j++) {
	                Element positionElement = (Element) positionNodesShorter.item(j);
	                int x = Integer.parseInt(positionElement.getElementsByTagName("x").item(0).getTextContent());
	                int y = Integer.parseInt(positionElement.getElementsByTagName("y").item(0).getTextContent());
	                shorterArm.add(new Position(x, y));
	            }

	            // Criação do cromossomo
	            Chromosome chromosome = new Chromosome(centrioleX, centrioleY, longerArm, shorterArm);

	            // Ler bandas do braço mais longo
	            Element bandsLongerArmElement = (Element) chromosomeElement.getElementsByTagName("bandsLongerArm").item(0);
	            NodeList bandNodesLonger = bandsLongerArmElement.getElementsByTagName("band");
	            for (int j = 0; j < bandNodesLonger.getLength(); j++) {
	                Element bandElement = (Element) bandNodesLonger.item(j);

	                ArrayList<Position> bandCoordinates = new ArrayList<>();
	                NodeList positionNodes = bandElement.getElementsByTagName("position");
	                for (int k = 0; k < positionNodes.getLength(); k++) {
	                    Element positionElement = (Element) positionNodes.item(k);
	                    int x = Integer.parseInt(positionElement.getElementsByTagName("x").item(0).getTextContent());
	                    int y = Integer.parseInt(positionElement.getElementsByTagName("y").item(0).getTextContent());
	                    bandCoordinates.add(new Position(x, y));
	                }

	                Color color = new Color(Integer.parseInt(bandElement.getElementsByTagName("color").item(0).getTextContent()));
	                int armIndex = Integer.parseInt(bandElement.getElementsByTagName("armIndex").item(0).getTextContent());

	                chromosome.getBandsLongerArm().add(new Band(bandCoordinates, color, armIndex));
	            }

	            // Ler bandas do braço mais curto
	            Element bandsShorterArmElement = (Element) chromosomeElement.getElementsByTagName("bandsShorterArm").item(0);
	            NodeList bandNodesShorter = bandsShorterArmElement.getElementsByTagName("band");
	            for (int j = 0; j < bandNodesShorter.getLength(); j++) {
	                Element bandElement = (Element) bandNodesShorter.item(j);

	                ArrayList<Position> bandCoordinates = new ArrayList<>();
	                NodeList positionNodes = bandElement.getElementsByTagName("position");
	                for (int k = 0; k < positionNodes.getLength(); k++) {
	                    Element positionElement = (Element) positionNodes.item(k);
	                    int x = Integer.parseInt(positionElement.getElementsByTagName("x").item(0).getTextContent());
	                    int y = Integer.parseInt(positionElement.getElementsByTagName("y").item(0).getTextContent());
	                    bandCoordinates.add(new Position(x, y));
	                }

	                Color color = new Color(Integer.parseInt(bandElement.getElementsByTagName("color").item(0).getTextContent()));
	                int armIndex = Integer.parseInt(bandElement.getElementsByTagName("armIndex").item(0).getTextContent());

	                chromosome.getBandsShorterArm().add(new Band(bandCoordinates, color, armIndex));
	            }

	            // Natureza
	            String nature = chromosomeElement.getElementsByTagName("nature").item(0).getTextContent();
	            chromosome.setNature(nature);

	            arrayChromosomes.add(chromosome);
	        }

	        return imagePath;

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Houve um problema com o arquivo de salvamento");
	    }

	    return null;
	}
}
