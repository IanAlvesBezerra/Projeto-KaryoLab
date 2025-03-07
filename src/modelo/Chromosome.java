package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Chromosome {
	
	private Position centriole;
	private ArrayList<Position> coordinatesLongerArm;
	private ArrayList<Position> coordinatesShorterArm;
	private int biggerArm;
	private double longerArmLength;
	private double shorterArmLength;
	private double totalLength;
	private double armRatio;
	private double centromericIndex;
	private String armRatioClassification;
	private String nature;
	private ArrayList<Band> bandsLongerArm;
	private ArrayList<Band> bandsShorterArm;
	
	public Chromosome(int centrioleX, int centrioleY, ArrayList<Position> coordinatesFirstArm, ArrayList<Position> coordinatesSecondArm) {
		centriole = new Position(centrioleX, centrioleY);
		
		double firstArmLength = calculateArmLength(coordinatesFirstArm);
		double secondArmLength = calculateArmLength(coordinatesSecondArm);
		
		if(firstArmLength > secondArmLength) {
			coordinatesLongerArm = coordinatesFirstArm;
			coordinatesShorterArm = coordinatesSecondArm;
			longerArmLength = firstArmLength;
			shorterArmLength = secondArmLength;
			biggerArm = 1;
		}
		else {
			coordinatesLongerArm = coordinatesSecondArm;
			coordinatesShorterArm = coordinatesFirstArm;
			longerArmLength = secondArmLength;
			shorterArmLength = firstArmLength;
			biggerArm = 2;
		}
		
		totalLength = longerArmLength + shorterArmLength;
		armRatio = longerArmLength / shorterArmLength;
		centromericIndex = (shorterArmLength / totalLength) * 100;
		
		nature = "Autossômico";
		armRatioClassification = setClassification();
		
		bandsLongerArm = new ArrayList<Band>();
		bandsShorterArm = new ArrayList<Band>();
	}
	
	public Chromosome() {
		
	}

	private double calculateArmLength(ArrayList<Position> armCoordinates) {
		double length = 0;
		
		Position previousPosition = new Position(centriole.getX(), centriole.getY());
		for(Position position : armCoordinates) {
			//fórmula: distancia = raíz((xB-xA)^2 + (yB-yA)^2) 
			double distance = Math.sqrt(Math.pow((previousPosition.getX() - position.getX()), 2) + Math.pow((previousPosition.getY() - position.getY()), 2));
			length += distance;
			previousPosition.setX(position.getX());
			previousPosition.setY(position.getY());
		}
		
		return length;
	}
	
	private String setClassification() {
		String armRatioClassification;
		if(armRatio <= 1.70) {
			armRatioClassification = "Metacêntrico";
		}
		else if(armRatio <= 3) {
			armRatioClassification = "Submetacêntrico";
		}
		else if(armRatio <= 7) {
			armRatioClassification = "Subtelocêntrico";
		}
		else {
			armRatioClassification = "Acrocêntrico";
		}
		return armRatioClassification;
	}
	
	public static ArrayList<ArrayList<Chromosome>> findHomologousSets(ArrayList<Chromosome> chromosomes, int ploidy) {
		
		ArrayList<ArrayList<Chromosome>> homologousSets = new ArrayList<ArrayList<Chromosome>>();
		
		//Adiciona os cromossomos Autossômicos
		ArrayList<Chromosome> currentSet = new ArrayList<>();
        for (Chromosome chromosome : chromosomes) {
            currentSet.add(chromosome);
            if (currentSet.size() == ploidy) {
                homologousSets.add(new ArrayList<>(currentSet));
                currentSet.clear(); // Inicia um novo conjunto
            }
        }
		
		separateSexualChromosomes(homologousSets);

        return homologousSets;
    }
	
	private static void separateSexualChromosomes(ArrayList<ArrayList<Chromosome>> homologousSets) {
        ArrayList<ArrayList<Chromosome>> sexualSets = new ArrayList<>();
        for (int i = 0; i < homologousSets.size(); i++) {
            ArrayList<Chromosome> set = homologousSets.get(i);
            for (Chromosome chromosome : set) {
                if (chromosome.getNature().equals("Sexual")) {
                    sexualSets.add(set);
                    homologousSets.remove(i);
                    i--;
                    break;
                }
            }
        }
        homologousSets.addAll(sexualSets);
    }
	
	public void addBand(Band band) {
		if(band.getArmIndex() == biggerArm) {
			bandsLongerArm.add(band);
		} else {
			bandsShorterArm.add(band);
		}
	}
	
	public double calculateDistanceToBand(Band band, int selectArm) {
	    if (band == null || band.getBandCoordinates() == null || band.getBandCoordinates().isEmpty()) {
	        return 0;
	    }

	    ArrayList<Position> arm = (selectArm == 1) ? coordinatesLongerArm : coordinatesShorterArm;

	    if (arm == null || arm.size() < 2) {
	        return 0;
	    }

	    double distance = 0;
	    Position previousPosition = centriole;

	    for (Position currentPosition : arm) {
	        double distanceToPoint = Math.hypot(
	            previousPosition.getX() - currentPosition.getX(),
	            previousPosition.getY() - currentPosition.getY()
	        );

	        distance += distanceToPoint;

	        previousPosition = currentPosition;

	        if (currentPosition.getX() == band.getBandCoordinates().get(0).getX() &&
	        	currentPosition.getY() == band.getBandCoordinates().get(0).getY()) {
	            break;
	        }
	        
	    }
	    return distance;
	}


	
	public Position getShorterArmStart() {
	    if (coordinatesShorterArm != null && !coordinatesShorterArm.isEmpty()) {
	        return coordinatesShorterArm.get(0);
	    }
	    return null;
	}

	public Position getLongerArmStart() {
	    if (coordinatesLongerArm != null && !coordinatesLongerArm.isEmpty()) {
	        return coordinatesLongerArm.get(0);
	    }
	    return null;
	}


	public Position getCentriole() {
		return centriole;
	}

	public ArrayList<Position> getCoordinatesLongerArm(){
		return coordinatesLongerArm;
	}
	
	public ArrayList<Position> getCoordinatesShorterArm(){
		return coordinatesShorterArm;
	}
	
	public ArrayList<Band> getBandsLongerArm() {
		return bandsLongerArm;
	}

	public void setBandsLongerArm(ArrayList<Band> bandsLongerArm) {
		this.bandsLongerArm = bandsLongerArm;
	}

	public ArrayList<Band> getBandsShorterArm() {
		return bandsShorterArm;
	}

	public void setBandsShorterArm(ArrayList<Band> bandsShorterArm) {
		this.bandsShorterArm = bandsShorterArm;
	}

	public double getLongerArmLength() {
		return longerArmLength;
	}

	public double getShorterArmLength() {
		return shorterArmLength;
	}

	public double getTotalLength() {
		return totalLength;
	}

	public double getArmRatio() {
		return armRatio;
	}

	public double getCentromericIndex() {
		return centromericIndex;
	}

	public String getClassification() {
		return armRatioClassification;
	}
	
	public String getNature() {
		return nature;
	}
	
	public void setNature(String nature) {
		this.nature = nature;
	}
	
	public void alternateNature() {
		if(nature == "Autossômico") {
			nature = "Sexual";
		} else {
			nature = "Autossômico";
		}
	}
}
