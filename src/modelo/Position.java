package modelo;

import java.util.Objects;

public class Position {

	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Position() {
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public static boolean isSamePosition(Position p1, Position p2) {
	    return (int)(Math.abs(p1.getX()) - (int)(p2.getX())) < 0.01 &&
	           (int)(Math.abs(p1.getY()) - (int)(p2.getY())) < 0.01;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Position position = (Position) obj;
	    return x == position.x && y == position.y;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(x, y);
	}

}
