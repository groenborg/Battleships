package captainobvious;

import battleships.BattleshipAI;
import battleships.Board;
import battleships.Fleet;
import battleships.Position;
import battleships.Ship;
import java.util.Random;

/**
 *
 * @author Simon og Kasper
 */
public class RAI implements BattleshipAI {

    private final String aiName = "Captain Obvious";
    
    private Position shotCurrent;
    private int shotIncrement;
    private int shotSpray;
    
    
    private Field[][] map;
    private Random rnd;
    private int matchNumber;
    private int sizeX = 10;
    private int sizeY = 10;
    private int nextX;
    private int nextY;

    public RAI() {
        this.shotCurrent = new Position(0,0);
        this.rnd = new Random();
        this.map = constructMap();
    }

    @Override
    public String getName() {
        return aiName;
    }

    @Override
    public void newMatch(int i) {
        resetMapFields(true);
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        resetMapFields(false);
        this.shotCurrent = new Position(0,0);
        
        nextX = 0;
        nextY = 0;
        sizeX = board.sizeX();
        sizeY = board.sizeY();

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
        }
    }

    @Override
    public void incoming(Position pstn) {
    }

    @Override
    public Position getFireCoordinates(Fleet fleet) {
        Position pos;
        int x = shotCurrent.x;
        int y = shotCurrent.y;
        
        this.shotIncrement = 0;
        this.shotSpray = 2;
        
        if(shotCurrent.x == 0 && shotCurrent.y == 0){
            
        }else{
            x = x + this.shotSpray;
            if(x >= 10){
                x = shotIncrement;
                y++;
            }
        }
        
        
        
        
        return new Position(x,y);
    }

    @Override
    public void hitFeedBack(boolean bln, Fleet fleet) {
        
        
        
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
            }
        }
        return mapField;
    }

    private void resetMapFields(boolean all) {
        for (int x = 0; x < this.sizeX;) {
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
