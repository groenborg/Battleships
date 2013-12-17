/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scurvymcpirate;

import battleships.Position;
import battleships.Ship;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Simon
 */
public class Map {

    private Field[][] map;
    private List<Field> shipPlaces;
    private List<Field> shotPlaces;
    private int sizeX;
    private int sizeY;
    private int shipDec;
    private int shotDec;

    public Map() {
        createMap();
        this.shipPlaces = new ArrayList();
        this.shotPlaces = new ArrayList();
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

    protected void stratAnalyser() {
        Comparator<Field> comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Field tmp1 = (Field) o1;
                Field tmp2 = (Field) o2;
                return tmp1.getOppShotTrend() - tmp2.getOppShotTrend();
            }
        };
        
        for (int y = 0; y < this.map.length; ++y) {
            for (int x = 0; x < this.map[y].length; ++x) {
                this.shotPlaces.add(this.map[x][y]);
            }
        }
        Collections.sort(this.shotPlaces, comp);
         this.shotPlaces = this.shotPlaces.subList(0, 17);
    }

    protected void prodOurShot(Position p) {
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

    protected void prodOppShot(Position p) {
        this.map[p.x][p.y].incOppShotTrend(this.shotDec);
        shotDec--;
    }

    protected void prodOppShip(Position p) {
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

    public void showOppShotDensity() {
        for (int y = 0; y < this.map.length; ++y) {
            for (int x = 0; x < this.map[y].length; ++x) {
                System.out.print(this.map[x][y].getOppShipTrend());
            }
            System.out.println("");
        }
    }

    public void showOppShipDensity() {
        for (int y = 0; y < this.map.length; ++y) {
            for (int x = 0; x < this.map[y].length; ++x) {
                System.out.print(this.map[x][y].getOppShipTrend());
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
