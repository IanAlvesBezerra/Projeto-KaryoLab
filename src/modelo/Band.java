package modelo;

import java.awt.Color;
import java.util.ArrayList;

public class Band {

	ArrayList <Position> bandCoordinates;
	Color color;
	int armIndex;
	
	public Band(ArrayList<Position> bandCoordinates, Color color, int armIndex) {
		this.bandCoordinates = bandCoordinates;
		this.color = color;
		this.armIndex = armIndex;
	}
	
	public Band() {
		
	}
	
	public double calculateBendLength() {
	    double length = 0;

	    for (int i = 1; i < bandCoordinates.size(); i++) {
	        Position previousPosition = bandCoordinates.get(i - 1);
	        Position currentPosition = bandCoordinates.get(i);

	        //fórmula: distancia = raíz((xB-xA)^2 + (yB-yA)^2) 
	        length += Math.sqrt(
	            Math.pow(previousPosition.getX() - currentPosition.getX(), 2) +
	            Math.pow(previousPosition.getY() - currentPosition.getY(), 2)
	        );
	    }

	    return length;
	}

	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<Position> getBandCoordinates(){
		return bandCoordinates;
	}
	
	public void setBandCoordinates(ArrayList<Position> bandCoordinates) {
		this.bandCoordinates = bandCoordinates;
	}
	
	public void addNodeToBand(Position node) {
		bandCoordinates.add(node);
	}
	
	public int getArmIndex() {
		return armIndex;
	}
	
	public void setArmIndex(int armIndex) {
		this.armIndex = armIndex;
	}
	
	 public void printBandInfo() {
        System.out.println("Band Info:");
        System.out.println("  Color: " + color);
        System.out.println("  Arm Index: " + armIndex);
        System.out.println("  Coordinates:");
        if (bandCoordinates != null) {
            for (Position pos : bandCoordinates) {
                System.out.println("    x: " + pos.getX() + ", y: " + pos.getY());
            }
        } else {
            System.out.println("    No coordinates");
        }
        System.out.println("  Length: " + calculateBendLength()); // Adicionado o comprimento
    }
}
