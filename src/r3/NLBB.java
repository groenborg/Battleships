package r3;

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
public class NLBB implements BattleshipAI {

    private final String aiName = "R1 - Nobel Laureate Black Bartholomew";
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
    private Stack<Placement> patternStack;
    private boolean patternUp;

    public NLBB() {
        this.patternStack = new Stack();
        this.support = new GameMap();
        this.shipDecrement = 100;
        this.shotDecrement = 100;
        this.shotStack = new Stack();
        this.lastHit = new Position(0, 0);
        this.rnd = new Random();
        this.map = constructMap();
        this.shotSpray = 2;
        this.shotIncrement = 0;
        this.patternUp = true;
    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        c = 0;
        trendSetter(true);
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        this.patternStack = this.support.createPatternStack(map);
        this.shotStack.clear();
        trendSetter(false);
        if (patternUp) {
            patternUp = false;
        } else {
            patternUp = true;
        }

        shotX = 42;
        if (patternUp == false) {
            shotY = 0;
        } else {
            shotY = sizeY - 1;
        }
        
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
                        if (checkRandomField(x, y, s, vertical)) {
                            pos = new Position(x, y);
                            prodShip(x, y, s, vertical);
                            finished = true;
                        }
                    }
                } else {
                    while (!finished) {
                        int x = rnd.nextInt(sizeX - (s.size() - 1));
                        int y = rnd.nextInt(sizeY);
                        if (checkRandomField(x, y, s, vertical)) {
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
            int cs = 0;
            for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
                s = fleet.getShip(i);
                if (i == 0) {
                    placeRandomly(s, board);
                } else {
                    boolean finished = false;
                    while (!finished) {
                        if (!placements.isEmpty()) {
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
                        } else {
                            boolean backUp = false;
                            Position poss;
                            boolean randVert = rnd.nextBoolean();
                            if (randVert) {
                                while (!backUp) {
                                    int x = rnd.nextInt(sizeX);
                                    int y = rnd.nextInt(sizeY - (s.size() - 1));
                                    if (checkRandomField(x, y, s, randVert)) {
                                        poss = new Position(x, y);
                                        prodShip(x, y, s, randVert);
                                        backUp = true;
                                    }
                                }
                            } else {
                                while (!backUp) {
                                    int x = rnd.nextInt(sizeX - (s.size() - 1));
                                    int y = rnd.nextInt(sizeY);
                                    if (checkRandomField(x, y, s, randVert)) {
                                        poss = new Position(x, y);
                                        prodShip(x, y, s, randVert);
                                        backUp = true;
                                    }
                                }
                            }
                            finished = true;
                        }

                    }
                    board.placeShip(p, s, vertical);
                }
            }
        }
    }

    @Override
    public void incoming(Position pstn) {
        map[pstn.x][pstn.y].incOppShotTrend(shotDecrement);
        shotDecrement--;
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {

        if (this.c < 20) {
            return normalShooter(fleet);
        } else {
            return trendShooter(fleet);
        }
    }

    private Position trendShooter(Fleet fleet) {

        Placement tmp;
        Position p = new Position(0, 0);
        boolean stop = false;

        if (!this.shotStack.empty()) {
            Position temp = (Position) this.shotStack.pop();
            this.map[temp.x][temp.y].setShot(true);
            this.lastHit = new Position(temp.x, temp.y);
            return temp;
        } else if (!this.patternStack.empty()) {
            while (stop == false) {
                if (!this.patternStack.empty()) {
                    tmp = patternStack.pop();
                    p = tmp.getPos();
                    if (map[p.x][p.y].getShot() == false) {
                        stop = true;
                    }
                } else {
                    return normalShooter(fleet);
                }
            }
            this.map[p.x][p.y].setShot(true);
            this.lastHit = new Position(p.x, p.y);
            return new Position(p.x, p.y);
        } else {
            return normalShooter(fleet);
        }
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
                if (x != 0) {
                    if (map[x - 1][y + b].getUsShip()) {
                        return false;
                    }
                    if (x != 1) {
                        if (map[x - 2][y + b].getUsShip()) {
                            return false;
                        }
                    }
                }
                if ((x < sizeX - 1)) {
                    if (map[x + 1][y + b].getUsShip()) {
                        return false;
                    }
                    if (x < sizeX - 2) {
                        if (map[x + 2][y + b].getUsShip()) {
                            return false;
                        }
                    }
                }
                if (y != 0) {
                    if (b == 0 && map[x][y - 1].getUsShip()) {
                        return false;
                    }
                    if (y != 1) {
                        if (b == 0 && map[x][y - 2].getUsShip()) {
                            return false;
                        }
                    }
                }
                if ((y + b) < sizeY - 1) {
                    if (b == s.size() - 1 && map[x][y + b + 1].getUsShip()) {
                        return false;
                    }
                    if ((y + b) < sizeY - 2) {
                        if (b == s.size() - 1 && map[x][y + b + 2].getUsShip()) {
                            return false;
                        }
                    }
                }
            } else {
                if ((x + b) < sizeX) {
                    if (this.map[x + b][y].getUsShip()) {
                        return false;
                    }
                } else {
                    return false;
                }
                if (y != 0) {
                    if (map[x + b][y - 1].getUsShip()) {
                        return false;
                    }
                    if (y != 1) {
                        if (map[x + b][y - 2].getUsShip()) {
                            return false;
                        }
                    }
                }
                if ((y < sizeY - 1)) {
                    if (map[x + b][y + 1].getUsShip()) {
                        return false;
                    }
                    if (y < sizeY - 2) {
                        if (map[x + b][y + 2].getUsShip()) {
                            return false;
                        }
                    }
                }
                if (x != 0) {
                    if (b == 0 && map[x - 1][y].getUsShip()) {
                        return false;
                    }
                    if (x != 1) {
                        if (b == 0 && map[x - 2][y].getUsShip()) {
                            return false;
                        }
                    }
                }
                if ((x + b) < sizeX - 1) {
                    if (b == s.size() - 1 && map[x + b + 1][y].getUsShip()) {
                        return false;
                    }
                    if ((x + b) < sizeX - 2) {
                        if (b == s.size() - 1 && map[x + b + 2][y].getUsShip()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private Position normalShooter(Fleet fleet) {
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
                    if(patternUp ==false){
                        if (this.shotY < this.sizeY - 1) {
                        this.shotY++;
                        }
                    }else{
                        if(this.shotY != 0){
                            shotY--;
                        }
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
                if ((shotX == 9 && shotY == 0) || (shotX == 8 && shotY == 0)) {
                    stop = true;
                }
            }
        }
        this.map[this.shotX][this.shotY].setShot(true);
        this.lastHit = new Position(this.shotX, this.shotY);
        return new Position(this.shotX, this.shotY);
    }

    private boolean checkRandomField(int x, int y, Ship s, boolean vertical) {
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
