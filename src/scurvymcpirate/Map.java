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
    
    
     public void setShot(int x, int y, boolean yes) {
        this.map[x][y].setShot(yes);
    }

    public void setHit(int x, int y, boolean yes) {
        this.map[x][y].setHit(yes);
    }

    public void setResolved(int x, int y, boolean yes) {
        this.map[x][y].setResolved(yes);
    }

    public void setUsShip(int x, int y, boolean yes) {
        this.map[x][y].setUsShip(yes);
    }

    public void incOppShipTrend(int x, int y) {
        this.map[x][y].incOppShotTrend(this.shotDec);
        shotDec--;
    }

    public void incOppShotTrend(int x, int y) {
        this.map[x][y].incOppShipTrend(this.shotDec);
        shotDec--;
    }

    public boolean getShot(int x, int y) {
        return this.map[x][y].getShot();
    }

    public boolean getHit(int x, int y) {
        return this.map[x][y].getHit();
    }

    public boolean getResolved(int x, int y) {
        return this.map[x][y].getResolved();
    }

    public boolean getUsShip(int x, int y) {
        return this.map[x][y].getUsShip();
    }

    public int getOppShipTrend(int x, int y) {
        return this.map[x][y].getOppShipTrend();
    }

    public int getOppShotTrend(int x, int y) {
        return this.map[x][y].getOppShotTrend();
    }
    
    protected void setUsShip(int x, int y, Ship s, boolean vertical) {
        for (int a = 0; a < s.size(); ++a) {
            this.map[x][y].setUsShip(true);
            if (vertical) {
                y++;
            } else {
                x++;
            }
        }
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
    
    

//    protected void fieldAnalyser() {
//        Comparator<Field> comp = new Comparator() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                Field tmp1 = (Field) o1;
//                Field tmp2 = (Field) o2;
//                return tmp1.getOppShotTrend() - tmp2.getOppShotTrend();
//            }
//        };
//
//        for (int y = 0; y < this.map.length; ++y) {
//            for (int x = 0; x < this.map[y].length; ++x) {
//                this.shotPlaces.add(this.map[x][y]);
//            }
//        }
//        Collections.sort(this.shotPlaces, comp);
//
//    }
   

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
