/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scurvymcpirate;

import battleships.Position;
import battleships.Ship;

/**
 *
 * @author Simon
 */
public class Map {

    private Field[][] map;
    private int sizeX;
    private int sizeY;
    
    private int shipDec;
    private int shotDec;
    
    public Map() {
        createMap();
        this.shipDec = 100;
        this.shotDec = 100;
    }

    private void createMap() {
       this.map = new Field[10][10];
        for (int x = 0; x < 10; ++x) {
            for (int y = 0; y < 10; ++y) {
                this.map[x][y] = new Field();
            }
        }
    }

    
    protected void prodOurShot(Position p){
        this.map[p.x][p.y].setShot(true);
    }
    
    
    protected void prodOurShip(Position p, Ship s, boolean vertical) {
        int x = p.x;
        int y = p.y;
        for (int a = 0; a < s.size(); ++a) {
            this.map[x][y].setUsShip(true);
            if (vertical) {
                y++;
            } else {
                x++;
            }
        }
    }
    
    protected void prodOppShot(Position p){
        this.map[p.x][p.y].incOppShotTrend(this.shotDec);
        shotDec--;
    }
    
    protected void prodOppShip(Position p){
        this.map[p.x][p.y].incOppShipTrend(this.shipDec);
        shipDec--;
    }
    
    protected boolean checkField(int x, int y, Ship s, boolean vertical) {
        for (int a = 0; a < s.size(); ++a) {
            if (this.map[x][y].getUsShip()) {
                return true;
            }
            if (vertical) {
                y++;
            } else {
                x++;
            }
        }
        return false;
    }
    
    
    // reset methods
    
    protected void trendSetter(boolean all) {
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                if (all) {
                    this.map[x][y].resetMatch();
                } else {
                    this.map[x][y].resetRound();
                }
            }
        }
        this.shipDec = 100;
        this.shotDec = 100;
    }
    
    
    // Print methods
    public void showMap() {
        for (int y = 0; y < this.map.length; ++y) {
            for (int x = 0; x < this.map[y].length; ++x) {
                if (this.map[x][y].getShot()) {
                    if (this.map[x][y].getHit()) {
                        System.out.print("O");
                    } else {
                        System.out.print("X");
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }

    public void showShip() {
        for (int y = 0; y < this.map.length; ++y) {
            for (int x = 0; x < this.map[y].length; ++x) {
                if (this.map[x][y].getUsShip()) {
                    System.out.print("S");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }
}
