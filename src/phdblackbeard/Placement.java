/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phdblackbeard;


import battleships.Position;
import java.util.Comparator;

/**
 *
 * @author Simon
 */
public class Placement implements Comparable {

    private Position pos;
    private boolean vertical;
    private int shotDensity;

    public Placement(Position pos, boolean vertical, int shotDensity) {
        this.pos = pos;
        this.vertical = vertical;
        this.shotDensity = shotDensity;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isVertical() {
        return vertical;
    }

    public int getShotDensity() {
        return shotDensity;
    }

    @Override
    public int compareTo(Object o) {
        Placement p = (Placement) o;
        return this.shotDensity - p.shotDensity;
    }

    @Override
    public String toString() {
        return "\n" + shotDensity;
    }
}
