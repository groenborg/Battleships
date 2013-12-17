package captainobvious;

import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;
import battleships.Ship;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author Simon og Kasper
 */
public class RAI implements BattleshipAI {

    private final String aiName = "Captain Obvious";
    private Position shotCurrent;
    private int shotIncrement;
    private int shotSpray;
    private int shotX;
    private int shotY;
    private Position lastHit;
    private Stack shotStack;
    private Field[][] map;
    private Random rnd;
    private int matchNumber;
    private int sizeX = 10;
    private int sizeY = 10;
    private int counter = 0;

    public RAI() {
        this.shotStack = new Stack();
        this.shotCurrent = new Position(0, 0);
        this.lastHit = new Position(0, 0);
        this.rnd = new Random();
        this.map = constructMap();
        this.shotSpray = 2;
        this.shotIncrement = 1;
        System.out.println("lol");
    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        resetMapFields(true);
        System.out.println("new match begun");
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        resetMapFields(false);
        this.shotCurrent = new Position(0, 0);

        shotX = 42;
        shotY = 0;
        sizeX = board.sizeX();
        sizeY = board.sizeY();

        System.out.println("hej");
        for (int i = 0; i < fleet.getNumberOfShips(); ++i) {

            Ship s = fleet.getShip(i);
            boolean vertical = rnd.nextBoolean();
            boolean finished = true;
            Position pos = new Position(1, 1);
            if (vertical) {
                while (!finished) {
                    int x = rnd.nextInt(sizeX);
                    int y = rnd.nextInt(sizeY - (s.size() - 1));
                    if (!checkField(x, y, s, vertical)) {
                        pos = new Position(x, y);
                        prodShip(x, y, s, vertical);
                        finished = true;
                    }
                }
            } else {
                while (!finished) {
                    int x = rnd.nextInt(sizeX - (s.size() - 1));
                    int y = rnd.nextInt(sizeY);
                    if (!checkField(x, y, s, vertical)) {
                        pos = new Position(x, y);
                        prodShip(x, y, s, vertical);
                        finished = true;
                    }
                }
            }
            board.placeShip(pos, s, vertical);
            System.out.println("ship places" + s.size());
        }
    }

    @Override
    public void incoming(Position pstn) {
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
        if (this.shotX == 42) {
            this.shotX = 0;
            return new Position(this.shotX, this.shotY);
        }

        if (this.shotX < this.sizeX - this.shotSpray) {
            this.shotX = this.shotX + this.shotSpray;
        } else {
            this.shotX = this.shotIncrement;
            if (this.shotY < this.sizeY - 1) {
                this.shotY++;
            }
            if (this.shotIncrement == 1) {
                this.shotIncrement = 0;
            } else {
                this.shotIncrement = 1;
            }
        }
        if (!this.shotStack.empty()) {
            Position tmp = (Position) this.shotStack.pop();
            this.map[tmp.x][tmp.y].setShot(true);
            this.lastHit = new Position(tmp.x, tmp.y);
            System.out.println("stack shot " + counter + " [" + tmp.x + " , " + tmp.y + "]");
            ++counter;
            return tmp;
        }

        System.out.println("pattern shot " + counter + " [" + shotX + "," + shotY + "]");
        ++counter;
        this.map[this.shotX][this.shotY].setShot(true);
        this.lastHit = new Position(this.shotX, this.shotY);
        return new Position(this.shotX, this.shotY);
    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
        int x = this.lastHit.x;
        int y = this.lastHit.y;
        if (bln) {
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
            System.out.println("calc");
        }
    }

    // Private methods here
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

    private Field[][] constructMap() {
        Field[][] mapField = new Field[sizeX][sizeY];
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                mapField[x][y] = new Field();
                System.out.print(y);
            }
            System.out.println("");
        }
        return mapField;
    }

    private void resetMapFields(boolean all) {
        for (int x = 0; x < this.sizeX; ++x) {
            for (int y = 0; y < this.sizeY; ++y) {
                if (all) {
                    this.map[x][y].resetMatch();
                } else {
                    this.map[x][y].resetRound();
                }
            }
        }
    }
}
