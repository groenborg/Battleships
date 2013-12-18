/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nobellaureateblackbart;

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

    private int shipValue = 3; /// optimereing
    private Stack<Placement> shootStack;
    private ArrayList<Placement> shootDensity;

    public GameMap() {
        this.shootStack = new Stack();
        this.shootDensity = new ArrayList();
    }

    protected Stack<Placement> createShootStack(Field[][] map) {
        boolean indent = false;
        int y = 0;
        int x = 0;
        while (y < 10) {
            while (x < 10) {
                int tmp = map[x][y].getOppShipTrend();
                shootStack.push(new Placement(new Position(x, y), indent, tmp));
                x = x + 2;
            }
            if (indent) {
                indent = false;
                x = 0;
            } else {
                indent = true;
                x = 1;
            }
            y++;
        }
        Collections.sort(shootStack, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Placement p1 = (Placement) o1;
                Placement p2 = (Placement) o2;
                return p1.getDensity() - p2.getDensity();
            }
        });
        return this.shootStack;
    }

    protected ArrayList<Placement> densityMapping(Field[][] map, int c) {
        shootDensity.clear();
        for (int y = 0; y < map.length; ++y) {
            for (int x = 0; x < map[y].length - shipValue + 1; ++x) {
                int tmp = 0;
                for (int z = 0; z < shipValue; ++z) {
                    //tmp = tmp + map[x + z][y].getOppShotTrend();
                    int tmp2 = map[x + z][y].getOppShotTrend();
                    if (tmp2 > tmp) {
                        tmp = tmp2;
                    }
                }
                Placement temp = new Placement(new Position(x, y), false, tmp);
                shootDensity.add(temp);
            }
        }
        for (int y = 0; y < map.length - shipValue; ++y) {
            for (int x = 0; x < map[y].length; ++x) {
                int tmp = 0;
                for (int z = 0; z < shipValue; ++z) {
                    //tmp = tmp + map[x][y + z].getOppShotTrend();
                    int tmp2 = map[x][y + z].getOppShotTrend();
                    if (tmp2 > tmp) {
                        tmp = tmp2;
                    }
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
