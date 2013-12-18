/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nobellaureateblackbart;


import battleships.Position;


/**
 *
 * @author Simon
 */
public class Placement implements Comparable {

    private Position pos;
    private boolean vertical;
    private int density;
    

    public Placement(Position pos, boolean vertical, int Density) {
        this.pos = pos;
        this.vertical = vertical;
        this.density = Density;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isVertical() {
        return vertical;
    }

    public int getDensity() {
        return density;
    }

    @Override
    public int compareTo(Object o) {
        Placement p = (Placement) o;
        return this.density - p.density;
    }

    @Override
    public String toString() {
        return "\n" + density;
    }

    
}
