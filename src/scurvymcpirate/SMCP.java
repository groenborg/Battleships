/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scurvymcpirate;

import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;
import battleships.Ship;
import java.util.ArrayList;

import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Simon
 */
public class SMCP implements BattleshipAI {

    private final String aiName = "Scurvy MC Pirate";
    private int shotIncrement;
    private int shotSpray;
    private int shotX;
    private int shotY;
    private GameMap support;
    private Position lastHit;
    private Stack shotStack;
    private Field[][] map;
    private Random rnd;
    private int sizeX = 10;
    private int sizeY = 10;
    private int c;
    private int shotDecrement;
    private int shipDecrement;

    public SMCP() {
        this.support = new GameMap();
        this.shipDecrement = 100;
        this.shotDecrement = 100;
        this.shotStack = new Stack();
        this.lastHit = new Position(0, 0);
        this.rnd = new Random();
        this.map = constructMap();
        this.shotSpray = 2;
        this.shotIncrement = 0;

    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        trendSetter(true);
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        trendSetter(false);
        shotX = 42;
        shotY = 0;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        c++;

        if (c == 1) {
            for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
                Ship s = fleet.getShip(i);
                boolean vertical = rnd.nextBoolean();
                boolean finished = false;
                Position pos = new Position(1, 1);
                if (vertical) {
                    while (!finished) {
                        int x = rnd.nextInt(sizeX);
                        int y = rnd.nextInt(sizeY - (s.size() - 1));
                        if (checkField(x, y, s, vertical)) {
                            pos = new Position(x, y);
                            prodShip(x, y, s, vertical);
                            finished = true;
                        }
                    }
                } else {
                    while (!finished) {
                        int x = rnd.nextInt(sizeX - (s.size() - 1));
                        int y = rnd.nextInt(sizeY);
                        if (checkField(x, y, s, vertical)) {
                            pos = new Position(x, y);
                            prodShip(x, y, s, vertical);
                            finished = true;
                        }
                    }
                }
                board.placeShip(pos, s, vertical);
            }
        } else {
            ArrayList<Placement> placements = support.densityMapping(map, c);
            Placement tmp;
            Position p = null;
            boolean vertical = false;
            Ship s;
            for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
                s = fleet.getShip(i);
                if (i == 0) {
                    placeRandomly(s, board);
                } else {
                    boolean finished = false;
                    while (!finished) {
                        tmp = placements.remove(0);
                        p = tmp.getPos();
                        vertical = tmp.isVertical();
                        if (vertical) {
                            if (checkField(p.x, p.y, s, vertical)) {
                                prodShip(p.x, p.y, s, vertical);
                                finished = true;
                            }
                        } else {
                            if (checkField(p.x, p.y, s, vertical)) {
                                prodShip(p.x, p.y, s, vertical);
                                finished = true;
                            }
                        }
                    }
                    board.placeShip(p, s, vertical);
                }
            }
        }

//        this.support.shotDensity(map);
        System.out.println("Scurvy mc");
        this.support.showShip(map);
        System.out.println("");
    }

    @Override
    public void incoming(Position pstn) {
        map[pstn.x][pstn.y].incOppShotTrend(shotDecrement);
        shotDecrement--;
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
//        if (shotSpray < fleet.getShip(0).size()) {
//            shotSpray = fleet.getShip(0).size();
//        }

        boolean stop = false;

        if (this.shotX == 42) {
            this.shotX = 0;
            this.map[this.shotX][this.shotY].setShot(true);
            return new Position(this.shotX, this.shotY);
        }

        if (!this.shotStack.empty()) {
            Position tmp = (Position) this.shotStack.pop();
            this.map[tmp.x][tmp.y].setShot(true);
            this.lastHit = new Position(tmp.x, tmp.y);
            return tmp;
        } else {
            while (!stop) {
                if (this.shotX < this.sizeX - this.shotSpray) {
                    this.shotX = this.shotX + this.shotSpray;
                    if (!map[shotX][shotY].getShot()) {
                        stop = true;
                    }
                } else {
                    if (this.shotY < this.sizeY - 1) {
                        this.shotY++;
                    }
                    incCalc();
                    this.shotX = this.shotIncrement;
                    if (!map[shotX][shotY].getShot()) {
                        stop = true;
                    }
                }
                if ((shotX == 9 && shotY == 9) || (shotX == 8 && shotY == 9)) {
                    stop = true;
                }
            }
        }

        this.map[this.shotX][this.shotY].setShot(true);
        this.lastHit = new Position(this.shotX, this.shotY);
        return new Position(this.shotX, this.shotY);
    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
        int x = this.lastHit.x;
        int y = this.lastHit.y;
        if (bln) {
            this.map[x][y].incOppShipTrend(shipDecrement);
            this.shipDecrement--;
            this.map[x][y].setHit(true);
            if (x - 1 >= 0) {
                if (!this.map[x - 1][y].getShot()) {
                    this.shotStack.push(new Position(x - 1, y));
                }
            }
            if (y - 1 >= 0) {
                if (!this.map[x][y - 1].getShot()) {
                    this.shotStack.push(new Position(x, y - 1));
                }
            }
            if (y + 1 < this.sizeY) {
                if (!this.map[x][y + 1].getShot()) {
                    this.shotStack.push(new Position(x, y + 1));
                }
            }
            if (x + 1 < this.sizeX) {
                if (!this.map[x + 1][y].getShot()) {
                    this.shotStack.push(new Position(x + 1, y));
                }
            }
        }
    }

    // Private methods here
    private void incCalc() {
        if (!(shotIncrement < shotSpray - 1)) {
            shotIncrement = 0;
        } else {
            shotIncrement++;
        }
    }

    private void placeRandomly(Ship s, Board b) {
        boolean vertical = rnd.nextBoolean();
        Position pos;
        if (vertical) {
            int x = rnd.nextInt(sizeX);
            int y = rnd.nextInt(sizeY - (s.size() - 1));
            pos = new Position(x, y);
            prodShip(x, y, s, vertical);
        } else {
            int x = rnd.nextInt(sizeX - (s.size() - 1));
            int y = rnd.nextInt(sizeY);
            pos = new Position(x, y);
            prodShip(x, y, s, vertical);
        }
        b.placeShip(pos, s, vertical);
    }

    private void prodShip(int x, int y, Ship s, boolean vertical) {
        for (int a = 0; a < s.size(); ++a) {
            this.map[x][y].setUsShip(true);
            if (vertical) {
                y++;
            } else {
                x++;
            }
        }
    }

    private boolean checkField(int x, int y, Ship s, boolean vertical) {
        for (int b = 0; b < s.size(); ++b) {
            if (vertical) {
                if ((y + b) < sizeY) {
                    if (this.map[x][y + b].getUsShip()) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if ((x + b) < sizeX) {
                    if (this.map[x + b][y].getUsShip()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private Field[][] constructMap() {
        Field[][] mapField = new Field[sizeX][sizeY];
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                mapField[x][y] = new Field();
            }
        }
        return mapField;
    }

    private void trendSetter(boolean all) {
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                if (all) {
                    this.map[x][y].resetMatch();
                } else {
                    this.map[x][y].resetRound();
                }
            }
        }
        this.shipDecrement = 100;
        this.shotDecrement = 100;
    }
}
