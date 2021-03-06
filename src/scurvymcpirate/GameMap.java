/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scurvymcpirate;

import battleships.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 *
 * @author Simon
 */
public class GameMap {
    
    private int shipValue = 2; /// optimereing
    private ArrayList<Placement> shipDensity;
    private ArrayList<Placement> shootDensity;
    
    public GameMap() {
        this.shipDensity = new ArrayList();
        this.shootDensity = new ArrayList();
    }
    
    protected ArrayList<Placement> densityMapping(Field[][] map, int c) {
        shootDensity.clear();
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length - shipValue + 1; ++x) {
                int tmp = 0;
                for (int z = 0; z < shipValue; ++z) {
                    tmp = tmp + map[x + z][y].getOppShotTrend();
                }
                Placement temp = new Placement(new Position(x, y), false, tmp);
                shootDensity.add(temp);
            }
        }
        for (int y = 0; y < map.length - shipValue; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                int tmp = 0;
                for (int z = 0; z < shipValue; ++z) {
                    tmp = tmp + map[x][y + z].getOppShotTrend();
                }
                Placement temp = new Placement(new Position(x, y), true, tmp);
                shootDensity.add(temp);
            }
        }
        if (c > 5) {
            Collections.sort(shootDensity);
        } else {
            Collections.shuffle(shootDensity);
        }
        return shootDensity;
    }

    // Print methods
    public void showMap(Field[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                if (map[x][y].getShot()) {
                    if (map[x][y].getHit()) {
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
    
    public void showShip(Field[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                if (map[x][y].getUsShip()) {
                    System.out.print("S");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }
    
    public void shotDensity(Field[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                System.out.print(map[x][y].getOppShotTrend() + "  ");
            }
            System.out.println("");
        }
    }
    
    public void shipDensity(Field[][] map) {
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                System.out.print(map[x][y].getOppShipTrend() + "\t");
            }
            System.out.println("");
        }
    }
}
